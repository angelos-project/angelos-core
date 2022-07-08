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
                implementation("org.angproj.io.buf:angelos-project-buffer:1.0.0-beta.2")
                implementation("org.angproj.io.sig:angelos-project-sig:1.0.0-beta.1")
                implementation(Libs.coro) { version { strictly("${Versions.coro}-native-mt") } }
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

// https://en.wikipedia.org/wiki/Signal_(IPC)

// http://www.qnx.com/developers/docs/qnx_4.25_docs/tcpip50/prog_guide/sock_ipc_tut.html
// http://www.cs.tau.ac.il/~eddiea/samples/Signal-Driven/udp-signal-driven-server.c
// https://www.softprayog.in/programming/network-socket-programming-using-tcp-in-c
// https://gist.github.com/richiejp/1590344


// https://www.freebsd.org/cgi/man.cgi?query=kqueue&sektion=2
// https://habr.com/en/post/600123/
// https://github.com/stsaz/kernel-queue-the-complete-guide


// https://stackoverflow.com/questions/29451133/whats-the-proper-way-to-safely-discard-stdin-characters-of-variable-length-in-c
// https://viewsourcecode.org/snaptoken/kilo/index.html