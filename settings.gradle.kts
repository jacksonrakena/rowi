pluginManagement {
    plugins {
        kotlin("jvm") version "1.4.0"
        id("org.jetbrains.dokka") version ("1.4.0")
    }
    repositories {
        gradlePluginPortal()
        jcenter()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/dokka/dev")
    }
}

include(":rowi-catnip")
include(":rowi-core")
include(":rowi-discord-interactions")
include(":rowi-jda")

rootProject.name = "rowi"