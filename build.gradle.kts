plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    `java-library`
}

repositories {
    jcenter()
}

val versionObject = Version(major = "1", minor = "0", revision = "0")
group = "com.abyssaldev"
version = "$versionObject"
val archivesBaseName  = "commands"

val jdaVersion = "4.2.0_223"
val ktorVersion = "1.4.2"
val bouncyCastleVersion = "1.67"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("org.bouncycastle:bcprov-jdk15on:$bouncyCastleVersion")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")
    implementation("org.slf4j:slf4j-api:1.7.25")
}

fun getBuild(): String {
    return System.getenv("BUILD_NUMBER")
        ?: System.getProperty("BUILD_NUMBER")
        ?: System.getenv("GIT_COMMIT")?.substring(0, 7)
        ?: System.getProperty("GIT_COMMIT")?.substring(0, 7)
        ?: "DEV"
}

class Version(
    val major: String,
    val minor: String,
    val revision: String) {
    override fun toString() = "$major.$minor.${revision}_${getBuild()}"
}