plugins {
    kotlin("multiplatform")
}

group = "angelos-admin"
version = "1.0-SNAPSHOT"

/**
 * To run in the terminal without distractions, try using
 * "runReleaseExecutableNative" or "runDebugExecutableNative"
 * in replacement for <task>:
 *
 * > ./gradlew -q --console=plain <task>
 */

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "angelos.main"
                runTask!!.standardInput = System.`in`
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libs.coro) { version { strictly("${Versions.coro}-native-mt") } }
                implementation(Libs.collections)
                implementation(project(":angelos-core"))
                implementation(project(":angelos-mvp"))
            }
        }
        val nativeMain by getting
    }
}