plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    `java-library`
}

repositories {
    jcenter()
}

group = "com.abyssaldev"
version = "1.0.0_${getBuild()}"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
    implementation("org.slf4j:slf4j-api:1.7.25")
}

fun getBuild(): String {
    return System.getenv("BUILD_NUMBER")
        ?: System.getProperty("BUILD_NUMBER")
        ?: System.getenv("GIT_COMMIT")?.substring(0, 7)
        ?: System.getProperty("GIT_COMMIT")?.substring(0, 7)
        ?: "DEV"
}