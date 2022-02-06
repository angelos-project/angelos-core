plugins {
    kotlin("multiplatform") // version Versions.kotlin
}

// Implement JNI according to:
// https://github.com/eskatos/jni-library-sample

// https://github.com/dickensas/kotlin-gradle-templates/tree/master/swig-jni-inherit-callback

group = "angelos-core"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm {
        val processResources = compilations["main"].processResourcesTaskName
        (tasks[processResources] as ProcessResources).apply {
            dependsOn(":jni-platform:assemble")
            dependsOn(":jni-proc:assemble")
            dependsOn(":jni-io:assemble")

            from("${project(":jni-proc").buildDir}/lib/main/release/stripped")
            from("${project(":jni-platform").buildDir}/lib/main/release/stripped")
            from("${project(":jni-io").buildDir}/lib/main/release/stripped")
        }

        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnit()
            systemProperty(
                "java.library.path",
                file("${buildDir}/processedResources/jvm/main").absolutePath
            )
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
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(TestLibs.junit)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
            }
        }
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}