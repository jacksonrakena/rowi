plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.dokkaHtmlMultiModule {
    documentationFileName.set("README.md")
}


subprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/dokka/dev")
    }
}