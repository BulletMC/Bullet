plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.10"
    application
}

group = "com.aznos"
version = "0.1.0"

repositories {
    mavenCentral()

    maven {
        url = uri("https://libraries.minecraft.net")
    }

    maven {
        url = uri("https://jitpack.io/")
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    implementation("io.ktor:ktor-network:2.3.7")
    implementation("io.ktor:ktor-utils:2.3.7")
    implementation("io.ktor:ktor-io:3.1.1")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("org.apache.logging.log4j:log4j-core:2.25.4")
    implementation("net.kyori:adventure-nbt:4.17.0")
}

application {
    mainClass = "com.maddoxh.bullet.MainKt"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.maddoxh.bullet.MainKt"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { zipTree(it) })
}

kotlin {
    jvmToolchain(21)
}