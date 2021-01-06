import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

plugins {
    id("org.jetbrains.kotlin.jvm")
    `java-library`
    id("org.jetbrains.dokka")
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

    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            platform.set(org.jetbrains.dokka.Platform.jvm)
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(
                    URL(
                        "https://github.com/abyssal/rowi/tree/master/rowi-catnip/" +
                                "src/main/kotlin"
                    )
                )
                remoteLineSuffix.set("#L")
            }
        }
    }
}