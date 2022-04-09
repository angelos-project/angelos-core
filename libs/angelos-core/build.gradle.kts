plugins {
    kotlin("multiplatform")
}

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
            dependsOn(":jni-base:assemble")

            from("${project(":jni-proc").buildDir}/lib/main/release/stripped")
            from("${project(":jni-platform").buildDir}/lib/main/release/stripped")
            from("${project(":jni-io").buildDir}/lib/main/release/stripped")
            from("${project(":jni-base").buildDir}/lib/main/release/stripped")
        }

        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
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

    nativeTarget.apply {
        val includePath = file("${project(":base").projectDir}/src/main/public/").absolutePath
        val libraryPathMain = file(project.file("${project(":base").buildDir}/lib/main/release/")).absolutePath
        val libraryPathTest = file(project.file("${project(":base").buildDir}/lib/main/debug/")).absolutePath

        val main by compilations.getting

        val base by main.cinterops.creating {
            defFile(project.file("src/nativeInterop/cinterop/base.def"))
            compilerOpts("-I$includePath")
            includeDirs.allHeaders(includePath)
            extraOpts("-libraryPath", libraryPathMain)
            extraOpts("-libraryPath", libraryPathTest)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libs.coro) { version { strictly("${Versions.coro}-native-mt") } }
                implementation(Libs.stately)
                implementation(Libs.concurrency)
                implementation(Libs.collections)
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
                implementation(TestLibs.coro)
            }
        }
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.CInteropProcess::class) {
    dependsOn(":base:assemble")
}
