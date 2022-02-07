plugins {
    kotlin("multiplatform")
    application
}

group = "rig-client"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies{
                implementation(project(":angelos-core"))
            }
        }
        val jvmMain by getting {
            dependencies{
                implementation(Libs.coro)
            }
        }
    }
}

application {
    mainClass.set("MainKt")
}