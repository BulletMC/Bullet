package com.aznos.serialization

import com.google.gson.JsonElement
import net.querz.nbt.io.NBTOutputStream
import net.querz.nbt.tag.*
import java.io.ByteArrayOutputStream

object NBTJson {

    fun toTag(el: JsonElement): Tag<*> = when {
        el.isJsonObject -> CompoundTag().apply {
            el.asJsonObject.entrySet().forEach { (k, v) -> put(k, toTag(v)) }
        }

        el.isJsonArray -> {
            val arr = el.asJsonArray
            if(arr.size() == 0) {
                ListTag(EndTag::class.java)
            } else {
                val firstNBT = toTag(arr[0])

                @Suppress("UNCHECKED_CAST")
                val list = ListTag(firstNBT.javaClass as Class<Tag<*>>)

                list.add(firstNBT)
                for(i in 1 until arr.size()) {
                    list.add(toTag(arr[i]))
                }

                list
            }
        }

        el.isJsonPrimitive -> {
            val p = el.asJsonPrimitive
            when {
                p.isNumber -> IntTag(p.asInt)
                else -> StringTag(p.asString)
            }
        }

        else -> EndTag.INSTANCE
    }

    fun toNBTBytes(tag: CompoundTag): ByteArray =
        ByteArrayOutputStream().use { baos ->
            NBTOutputStream(baos).use { it.writeTag(tag, Tag.DEFAULT_MAX_DEPTH) }
            baos.toByteArray()
        }
}