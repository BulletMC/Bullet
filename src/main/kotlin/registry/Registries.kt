package com.aznos.registry

object Registries {

    private val registries = mutableListOf<Registry<*>>()

    val damage_type = DamageTypeRegistry().register()
    val dimension_type = DimensionTypeRegistry().register()
    val wolf_variant = WolfVariantRegistry().register()
    val painting_variant = PaintingVariantRegistry().register()

    val biomes = BiomeTypes().register()

    fun getRegistries(): List<Registry<*>> = registries.toList()

    private fun <T> Registry<T>.register(): Registry<T> {
        registries.add(this)
        return this
    }

}