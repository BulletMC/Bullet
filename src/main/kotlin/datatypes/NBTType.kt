package com.aznos.datatypes

import dev.dewy.nbt.api.registry.TagTypeRegistry
import dev.dewy.nbt.tags.TagType
import dev.dewy.nbt.tags.collection.CompoundTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.format.TextDecoration
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

object NBTType {

    val tagTypeRegistry = TagTypeRegistry()

    @Throws(IOException::class)
    fun DataInputStream.readNbtCompound(): CompoundTag {
        if (readByte() != TagType.COMPOUND.id)
            throw IOException("Root tag in NBT structure must be a compound tag.")

        val result = CompoundTag()
        result.name = readUTF()
        result.read(this, 0, tagTypeRegistry)

        return result;
    }

    @Throws(IOException::class)
    fun DataOutputStream.writeNbtCompound(tag: CompoundTag) {
        writeByte(TagType.COMPOUND.id.toInt())

        if (tag.name != null) {
            writeUTF(tag.name)
        }

        tag.write(this, 0, tagTypeRegistry)
    }

    /*
     * Converts a Component
     */
    fun componentToNbtComponent(component: Component): CompoundTag {
        val root = CompoundTag()

        // https://minecraft.wiki/w/Text_component_format
        when (component) {
            is TextComponent -> {
                if (component.content().isNotEmpty()) {
                    root.putString("text", component.content())
                }
            }
            is TranslatableComponent -> {
                root.putString("type", "translatable")
                root.putString("translate", component.key())

                if (component.fallback() != null) {
                    root.putString("fallback", component.fallback()!!)
                }

                if (component.children().isNotEmpty()) {
                    root.putList("with", component.children().map { componentToNbtComponent(it) })
                }
            }
            else -> TODO()
        }

        if (component.color() != null) {
            root.putString("color", component.color()!!.asHexString())
        }

        if (component.font() != null) {
            root.putString("font", component.font()!!.asString())
        }

        for (entry in component.decorations()) {
            if (entry.value == TextDecoration.State.TRUE) {
                root.putBoolean(entry.key.name, true)
            }
        }

        // TODO: Interactivity like click_events, open_url, run_command, suggest_command, etc.
        return root
    }

}

fun CompoundTag.putBoolean(name: String, value: Boolean) = this.putByte(name, if (value) 1 else 0)