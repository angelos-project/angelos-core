plugins {
    kotlin("multiplatform")
    application
}

group = "angelos-admin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libs.coro)
                implementation(project(":angelos-core"))
                implementation(project(":angelos-mvp"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(Libs.coro)
            }
        }
    }
}

application {
    mainClass.set("MainKt")
}