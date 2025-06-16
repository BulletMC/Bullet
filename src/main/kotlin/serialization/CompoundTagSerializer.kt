package com.aznos.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.querz.nbt.io.SNBTUtil
import net.querz.nbt.tag.CompoundTag

class CompoundTagSerializer: KSerializer<CompoundTag> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("CompoundTag", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: CompoundTag) {
        encoder.encodeString(SNBTUtil.toSNBT(value))
    }

    override fun deserialize(decoder: Decoder): CompoundTag {
        return SNBTUtil.fromSNBT(decoder.decodeString()) as CompoundTag
    }
}