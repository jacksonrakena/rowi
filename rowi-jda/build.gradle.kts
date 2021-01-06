import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

plugins {
    id("org.jetbrains.kotlin.jvm")
    `java-library`
    id("org.jetbrains.dokka")
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

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            //includes.from("Module.md")
            platform.set(org.jetbrains.dokka.Platform.jvm)
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(
                    URL(
                        "https://github.com/abyssal/rowi/tree/master/rowi-jda/" +
                                "src/main/kotlin"
                    )
                )
                remoteLineSuffix.set("#L")
            }
        }
    }
}