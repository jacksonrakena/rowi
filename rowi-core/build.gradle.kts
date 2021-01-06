import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.dokka")
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

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            //includes.from("Module.md")
            platform.set(org.jetbrains.dokka.Platform.jvm)
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(
                    URL(
                        "https://github.com/abyssal/rowi/tree/master/rowi-core/" +
                                "src/main/kotlin"
                    )
                )
                remoteLineSuffix.set("#L")
            }
        }
    }
}