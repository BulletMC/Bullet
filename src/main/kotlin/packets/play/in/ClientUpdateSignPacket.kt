package com.aznos.packets.play.`in`

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.BlockPositionType.readBlockPos
import com.aznos.datatypes.StringType.readString
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerBlockEntityDataPacket
import net.querz.nbt.tag.CompoundTag
import kotlin.collections.set

/**
 * This message is sent from the client to the server when the "Done" button is
 * pushed after placing a sign and editing the text.
 */
class ClientUpdateSignPacket(data: ByteArray) : Packet(data) {
    val blockPos: BlockPositionType.BlockPosition
    val line1: String
    val line2: String
    val line3: String
    val line4: String

    init {
        val input = getIStream()

        blockPos = input.readBlockPos()
        line1 = input.readString()
        line2 = input.readString()
        line3 = input.readString()
        line4 = input.readString()
    }

    override fun apply(client: ClientSession) {
        val data = CompoundTag()
        data.putString("id", "minecraft:sign")
        data.putInt("x", blockPos.x.toInt())
        data.putInt("y", blockPos.y.toInt())
        data.putInt("z", blockPos.z.toInt())

        data.putString("Text1", "{\"text\":\"${line1}\"}")
        data.putString("Text2", "{\"text\":\"${line2}\"}")
        data.putString("Text3", "{\"text\":\"${line3}\"}")
        data.putString("Text4", "{\"text\":\"${line4}\"}")

        for(otherPlayer in Bullet.players) {
            if(otherPlayer != client.player) {
                otherPlayer.sendPacket(
                    ServerBlockEntityDataPacket(
                        blockPos,
                        9,
                        data
                    )
                )
            }
        }

        val world = client.player.world!!
        val prev = world.modifiedBlocks[blockPos]
        if(prev != null) {
            val lines = listOf(line1, line2, line3, line4)
            world.modifiedBlocks[blockPos] = prev.copy(textLines = lines)
        }
    }
}