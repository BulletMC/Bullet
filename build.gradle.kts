plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.10"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    application
}

group = "com.aznos"
version = "0.0.1"

repositories {
    mavenCentral()

    maven {
        url = uri("https://libraries.minecraft.net")
    }
}

dependencies {
    //Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    //Serialization
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("dev.dewy:nbt:1.5.1")

    //Kyori
    implementation("net.kyori:adventure-api:4.19.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.19.0")
    implementation("net.kyori:adventure-text-minimessage:4.19.0")

    //Logging / util
    implementation("org.apache.logging.log4j:log4j-core:2.24.3")

    //Mojang dependencies
    implementation("com.mojang:brigadier:1.0.18")
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$projectDir/config/detekt/detekt.yml")
}

application {
    mainClass = "com.aznos.MainKt"
}

tasks.register("runServer") {
    dependsOn("detekt", "run")
    group = "bullet"
}

kotlin {
    jvmToolchain(21)
}