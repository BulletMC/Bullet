package com.aznos.util

import java.math.BigInteger
import java.security.MessageDigest

object Hashes {
    /**
     * Produces the signed Minecraft SHA-1 hex digest
     *
     * @param sharedSecret The 16-byte AES key the client sends
     * @param publicKeyDER The DER-encoded public key of the server
     * @return serverID string
     */
    fun makeServerIDHash(sharedSecret: ByteArray, publicKeyDER: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-1")
        md.update(byteArrayOf())
        md.update(sharedSecret)
        md.update(publicKeyDER)

        return BigInteger(md.digest()).toString(16)
    }
}