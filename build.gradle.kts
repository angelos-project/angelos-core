plugins {
    kotlin("multiplatform") version "1.5.30"
}

group = "angelos.nio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
            systemProperty(
                "java.library.path",
                listOf(
                    file("${project(":jniendian").buildDir}/lib/main/debug").absolutePath,
                    file("${project(":jnifilesystem").buildDir}/lib/main/debug").absolutePath,
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
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation("junit:junit:4.13.1")
            }
        }
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}

tasks.withType(AbstractCompile::class) {
    dependsOn(":jniendian:assemble")
    dependsOn(":jnifilesystem:assemble")
}

// Generate C++ native header file.
// javac -h ./jni/src/main/public/ src/jvmMain/java/angelos/jni/Posix.java

// https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/jniTOC.html
// https://programmerclick.com/article/73771902404/