package com.aznos.packets.play.`in`

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.OrbEntity
import com.aznos.events.EventManager
import com.aznos.events.PlayerArmSwingEvent
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerAnimationPacket
import com.aznos.packets.play.out.ServerSoundEffectPacket
import com.aznos.packets.play.out.ServerSpawnExperienceOrb
import com.aznos.world.items.Item
import com.aznos.world.sounds.SoundCategories
import com.aznos.world.sounds.Sounds

/**
 * This packet is sent to the server when a player swings their arm
*/
class ClientAnimationPacket(data: ByteArray) : Packet(data) {
    val hand: Int = getIStream().readVarInt()

    override fun apply(client: ClientSession) {
        val event = PlayerArmSwingEvent(client.player)
        EventManager.fire(event)
        if(event.isCancelled) return

        val world = client.player.world ?: return

        if(client.player.getHeldItemID() == Item.EXPERIENCE_BOTTLE.id) {
            world.orbs.add(OrbEntity())
            val orb = world.orbs.last()
            orb.location = client.player.location.copy().add(0.0, 1.0, 0.0)
            orb.xp = (3..11).random()

            for(player in Bullet.players) {
                player.sendPacket(
                    ServerSpawnExperienceOrb(
                        orb.entityID,
                        client.player.location.toBlockPosition().add(0.0, 1.0, 0.0),
                        orb.xp
                    )
                )

                player.sendPacket(
                    ServerSoundEffectPacket(
                        Sounds.ENTITY_EXPERIENCE_BOTTLE_THROW,
                        SoundCategories.PLAYER,
                        client.player.location.x.toInt(),
                        client.player.location.y.toInt(),
                        client.player.location.z.toInt()
                    )
                )
            }
        }

        for(otherPlayer in Bullet.players) {
            if(otherPlayer != client.player) {
                otherPlayer.sendPacket(ServerAnimationPacket(client.player.entityID, 0))
            }
        }
    }
}