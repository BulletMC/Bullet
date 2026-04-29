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