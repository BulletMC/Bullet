package com.aznos.packets.login.`in`

import com.aznos.Bullet
import com.aznos.Bullet.players
import com.aznos.ClientSession
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet
import com.aznos.packets.login.out.ServerLoginDisconnectPacket
import com.aznos.util.Hashes
import com.aznos.util.LoginUtils
import com.aznos.util.MojangNetworking
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * This packet is sent by the client in response to a server encryption request
 */
class ClientEncryptionResponsePacket(data: ByteArray) : Packet(data) {
    var secretKey: ByteArray
    var verifyToken: ByteArray

    init {
        val input = getIStream()

        val secretLen = input.readVarInt()
        secretKey = ByteArray(secretLen)
        input.readFully(secretKey)

        val verifyTokenLen = input.readVarInt()
        verifyToken = ByteArray(verifyTokenLen)
        input.readFully(verifyToken)
    }

    override fun apply(client: ClientSession) {
        val rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        rsa.init(Cipher.DECRYPT_MODE, Bullet.keyPair.private)

        val sharedSecret = rsa.doFinal(secretKey)
        val verifyToken = rsa.doFinal(verifyToken)

        verifyPlayerToken(client, verifyToken)

        val secretKey = SecretKeySpec(sharedSecret, "AES")
        val iv = IvParameterSpec(sharedSecret)

        val decrypt = Cipher.getInstance("AES/CFB8/NoPadding").apply {
            init(Cipher.DECRYPT_MODE, secretKey, iv)
        }
        val encrypt = Cipher.getInstance("AES/CFB8/NoPadding").apply {
            init(Cipher.ENCRYPT_MODE, secretKey, iv)
        }

        client.enableEncryption(decrypt, encrypt)

        val player = client.player

        val hash = Hashes.makeServerIDHash(sharedSecret, Bullet.publicKey)
        val prof = runBlocking { MojangNetworking.querySessionServer(player, hash) }

        if(prof == null) {
            client.sendPacket(
                ServerLoginDisconnectPacket(
                    Component.text(
                        "Failed to verify username with Mojang servers, please try again later",
                        NamedTextColor.RED
                    )
                )
            )

            client.close()
            return
        }

        val uuidWithDashes = prof.id.replaceFirst(
            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)".toRegex(),
            "$1-$2-$3-$4-$5"
        )

        client.player.uuid = UUID.fromString(uuidWithDashes)
        client.player.username = prof.name

        val dupes = players.filter { it.uuid == client.player.uuid || it.username == client.player.username }
        players.removeAll(dupes)

        for(old in dupes) {
            old.disconnect(
                Component.text()
                    .append(Component.text("You logged in from another location", NamedTextColor.RED))
                    .append(
                        Component.text(
                            "\n\nIf this wasn’t you, your account may have been compromised.",
                            NamedTextColor.GRAY
                        )
                    )
                    .build()
            )

            old.clientSession.close()
        }

        LoginUtils.loginPlayer(client)
    }

    private fun verifyPlayerToken(client: ClientSession, verifyToken: ByteArray) {
        if(!client.verifyToken.contentEquals(verifyToken)) {
            client.sendPacket(
                ServerLoginDisconnectPacket(
                    Component
                        .text("Invalid verification token", NamedTextColor.RED)
                )
            )

            client.close()
            return
        }
    }
}