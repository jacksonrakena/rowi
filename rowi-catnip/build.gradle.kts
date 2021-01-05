plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    `java-library`
}

repositories {
    jcenter()
    maven("https://jitpack.io") // mewna/catnip
}

group = "com.abyssaldev"
version = "1.0.0_${getBuild()}"

fun getBuild(): String {
    return System.getenv("BUILD_NUMBER")
        ?: System.getProperty("BUILD_NUMBER")
        ?: System.getenv("GIT_COMMIT")?.substring(0, 7)
        ?: System.getProperty("GIT_COMMIT")?.substring(0, 7)
        ?: "DEV"
}

dependencies {
    implementation(project(":rowi-core"))
    implementation("com.mewna:catnip:feature~api-v8-SNAPSHOT")
}