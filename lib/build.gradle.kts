plugins {
    kotlin("multiplatform") version "1.6.0"
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        val jniProcPath = "${project(":jni:proc").buildDir}/lib/main/release/stripped"
        val jniPlatformPath = "${project(":jni:platform").buildDir}/lib/main/release/stripped"
        val jniIoPath = "${project(":jni:io").buildDir}/lib/main/release/stripped"

        val processResources = compilations["main"].processResourcesTaskName
        (tasks[processResources] as ProcessResources).apply {
            dependsOn(":jni:platform:assemble")
            dependsOn(":jni:proc:assemble")
            dependsOn(":jni:io:assemble")

            from(jniProcPath)
            from(jniPlatformPath)
            from(jniIoPath)
            into("$buildDir/classes/kotlin/jvm/main")
        }
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
            systemProperty(
                "java.library.path",
                listOf(
                    rootDir.resolve(jniProcPath).absolutePath,
                    rootDir.resolve(jniPlatformPath).absolutePath,
                    rootDir.resolve(jniIoPath).absolutePath,
                ).joinToString(":") + ":" + System.getProperty("java.library.path")
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
        val jvmMain by getting {
            dependencies {
                /*project(":jni:platform")
                project(":jni:proc")
                project(":jni:io")*/
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

    /*val angelosCoreJar by configurations.creating {
        isCanBeConsumed = true
        isCanBeResolved = false
        extendsFrom(configurations["implementation"], configurations["runtimeOnly"])
        //extendsFrom(configurations["jvmMain"])
    }

    artifacts {
        add("angelosCoreJar", angelosCoreJar)
    }*/

    /* configurations {
        kotlinNativeCompilerPluginClasspath {
            this.
        }
    }*/
}

