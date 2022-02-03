import org.gradle.internal.jvm.Jvm

plugins {
    base
    `cpp-library`
}

repositories {
    mavenCentral()
    mavenLocal()
}

library {
    binaries.configureEach{
        val compileTask = compileTask.get()
        val javaHome = "${Jvm.current().javaHome.canonicalPath}"
        compileTask.compilerArgs.addAll(compileTask.targetPlatform.map {
            listOf("-I", "$javaHome/include") + when {
                it.operatingSystem.isMacOsX -> listOf("-I", "$javaHome/include/darwin")
                it.operatingSystem.isLinux -> listOf("-I", "$javaHome/include/linux")
                it.operatingSystem.isWindows -> listOf("-I", "$javaHome/include/win32")
                else -> emptyList()
            }
        })

        when(toolChain) {
            is VisualCpp -> compileTask.compilerArgs.addAll(listOf("/TC"))
            is Clang, is GccCompatibleToolChain -> compileTask.compilerArgs.addAll(listOf("-x", "c", "-std=c11"))
        }
    }
}

dependencies {
    implementation(project(":base"))
}