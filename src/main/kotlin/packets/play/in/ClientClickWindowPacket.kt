package com.aznos.packets.play.`in`

import com.aznos.ClientSession
import com.aznos.datatypes.Slot
import com.aznos.datatypes.Slot.readSlot
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerSetSlotPacket
import com.aznos.packets.play.out.ServerWindowConfirmationPacket
import com.aznos.util.ItemUtils

/**
 * This packet is sent by the player when they click a slot in a window
 *
 * @property windowID The ID of the window which was clicked, 0 for player inventory
 * @property slot The clicked slot number, see below
 * @property button The button used in the click, see below
 * @property actionNumber A unique number for the action, implemented by the vanilla server as a counter
 * starting at 1 (different counter for every window ID). Used by the server to send back a Window Confirmation packet
 * @property mode Inventory operation mode, see below
 * @property clickedItem The clicked slot. Has to be empty (item ID = -1) for drop mode
 *
 * **Modes, Buttons, and Slots**:
 * - Mode 0, Button 0, normal slot: Left mouse click
 * - Mode 0, Button 1, normal slot: Right mouse click
 *
 * - Mode 1, Button 0, normal slot: Shift left mouse click
 * - Mode 1, Button 1, normal slot: Shift right mouse click
 *
 * - Mode 2, Button 0, normal slot: Number key 1
 * - Mode 2, Button 1, normal slot: Number key 2
 * - Mode 2, Button 2, normal slot: Number key 3
 *  :::
 * - Mode 2, Button 8, normal slot: Number key 9
 *
 * - Mode 3, Button 2, normal slot: Middle mouse click
 *
 * - Mode 4, Button 0, normal slot*: Drop key (Q)
 * - Mode 4, Button 1, normal slot*: Ctrl + Drop Key (Ctrl-Q)
 * - Mode 4, Button 0, -999: Left click outside inventory holding nothing
 * - Mode 4, Button 1, -999: Right click outside inventory holding nothing
 *
 * - Mode 5, Button 0, -999: Starting left mouse drag
 * - Mode 5, Button 4, -999: Starting right mouse drag
 * - Mode 5, Button 8, -999: Starting middle mouse drag
 * - Mode 5, Button 1, normal slot: Add slot for left mouse drag
 * - Mode 5, Button 5, normal slot: Add slot for right mouse drag
 * - Mode 5, Button 9, normal slot: Add slot for middle mouse drag
 * - Mode 5, Button 2, -999: End left mouse drag
 * - Mode 5, Button 6, -999: End right mouse drag
 * - Mode 5, Button 10, -999: End middle mouse drag
 *
 * - Mode 6, Button 0, normal slot: Double click left mouse button
 *
 * The server should send back a Window Confirmation packet, if the click was not accepted,
 * the client must send a matching Server-bound Window Confirmation packet before sending more Click Window packets.
 */
class ClientClickWindowPacket(data: ByteArray) : Packet(data) {
    val windowID: Byte
    val slot: Short
    val button: Byte
    val actionNumber: Short
    val mode: Int
    val clickedItem: Slot.SlotData

    init {
        val input = getIStream()

        windowID = input.readByte()
        slot = input.readShort()
        button = input.readByte()
        actionNumber = input.readShort()
        mode = input.readVarInt()
        clickedItem = input.readSlot()
    }

    override fun apply(client: ClientSession) {
        if(windowID.toInt() == 0 && mode == 4) {
            val player = client.player
            val held = player.inventory.heldStack(player.selectedSlot)
            if(held.isAir) return

            val dropAll = (button.toInt() == 1)
            val toDrop = if (dropAll) held else held.copy(count = 1)

            if(dropAll) {
                player.inventory.setHeldSlot(player.selectedSlot, null)
            } else {
                val newCount = held.count - 1
                if (newCount <= 0) player.inventory.setHeldSlot(player.selectedSlot, null)
                else player.inventory.setHeldSlot(player.selectedSlot, held.copy(count = newCount))
            }

            client.sendPacket(
                ServerSetSlotPacket(
                    0, player.selectedSlot + 36,
                    player.inventory.heldStack(player.selectedSlot).toSlotData()
                )
            )

            client.sendPacket(
                ServerWindowConfirmationPacket(
                    0, actionNumber, true
                )
            )

            ItemUtils.dropItem(player.world!!, player.location.toBlockPosition(), toDrop.id)
        }
    }
}