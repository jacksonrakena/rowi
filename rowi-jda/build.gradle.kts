plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    `java-library`
}

repositories {
    jcenter()
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

val jdaVersion = "4.2.0_223"

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation(project(":rowi-core"))
}