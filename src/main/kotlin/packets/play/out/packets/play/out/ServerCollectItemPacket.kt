package com.aznos.packets.play.out.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Sent by the server when someone picks up an item lying on the ground,
 * its sole purpose appears to be the animation of the item flying towards the player
 * It doesn't destroy the entity in the client memory, and it doesn't add it to your inventory
 * The server only checks for items to be picked up after each Player Position packet sent by the client
 * The collector entity can be any entity; it does not have to be a player.
 * The collected entity also can be any entity
 *
 * @param collectedEntityID The entity ID for the item being collected
 * @param collectorEntityID The entity ID of the entity that collected the item
 * @param itemCount The number of items being collected, should be 1 for XP orbs, otherwise the number of items in the stack
 */
class ServerCollectItemPacket(
    collectedEntityID: Int,
    collectorEntityID: Int,
    itemCount: Int
) : Packet(0x55) {
    init {
        wrapper.writeVarInt(collectedEntityID)
        wrapper.writeVarInt(collectorEntityID)
        wrapper.writeVarInt(itemCount)
    }
}