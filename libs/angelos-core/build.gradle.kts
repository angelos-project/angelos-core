plugins {
    kotlin("multiplatform") version "1.6.10"
}

group = "org.angelos-core"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm {
        //apply(plugin = "java")
        /**
         * JNI libraries subprojects are defined for the Kotlin/JVM.
         */
        val jniProcPath = "${project(":jni-proc").buildDir}/lib/main/release/stripped"
        val jniPlatformPath = "${project(":jni-platform").buildDir}/lib/main/release/stripped"
        val jniIoPath = "${project(":jni-io").buildDir}/lib/main/release/stripped"

        val copyJni by tasks.creating(Sync::class) {
            dependsOn(":jni-platform:assemble")
            dependsOn(":jni-proc:assemble")
            dependsOn(":jni-io:assemble")

            from(jniProcPath)
            from(jniPlatformPath)
            from(jniIoPath)
            into("$buildDir/classes/kotlin/jvm/main")

            preserve{
                include("angelos/**")
                include("META-INF/**")
            }
        }

        /** Libraries subprojects are added as resources. */
        val processResources = compilations["main"].processResourcesTaskName
        (tasks[processResources] as ProcessResources).apply {
            //into("$buildDir/classes/kotlin/jvm/main")
            dependsOn(copyJni)
            //outputs.dirs(file("$buildDir/classes/kotlin/jvm/main"))
        }
        /* configure<SourceSetContainer>{
            named("main"){
                //listOf("$buildDir/classes/kotlin/jvm/main")
                runtimeClasspath.forEach { println(it) }
            }
        }*/
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
            /** Libraries are added to the classpath for testing purposes. */
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