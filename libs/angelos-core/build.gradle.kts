plugins {
    kotlin("multiplatform") version "1.6.10"
}

// Implement JNI according to:
// https://github.com/eskatos/jni-library-sample

group = "angelos-core"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                //runtimeOnly(project(":jni-proc"))
                //runtimeOnly(project(":jni-platform"))
                runtimeOnly(project(":jni-io"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("junit:junit:4.13.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
            }
        }
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}