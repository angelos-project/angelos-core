/* val javaHome = System.getenv("JAVA_HOME")

plugins {
    base
    `cpp-library`
}

library {
    binaries.configureEach {
        base{
            archivesName.set("jniIo")
            distsDirectory.set(layout.buildDirectory.dir("lib/main/release/"))
        }
        val compileTask = compileTask.get()
        compileTask.includes.from("$javaHome/include")

        val osFamily = targetPlatform.targetMachine.operatingSystemFamily
        when{
            osFamily.isMacOs-> {
                compileTask.includes.from("-I$javaHome/include/darwin")
                compileTask.compilerArgs.add("-I/Library/Developer/CommandLineTools/SDKs/MacOSX10.15.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers/")
            }
            osFamily.isLinux -> compileTask.includes.from("$javaHome/include/linux")
            osFamily.isWindows -> compileTask.includes.from("$javaHome/include/win32")
        }

        compileTask.source.setFrom(fileTree("src/main/cpp"))

        when(toolChain) {
            is VisualCpp -> compileTask.compilerArgs.addAll(listOf("/TC"))
            is Clang, is GccCompatibleToolChain -> compileTask.compilerArgs.addAll(listOf("-x", "c", "-std=c11"))
        }
    }
}

dependencies {
    implementation(project(":base"))
}*/

plugins {
    //`angelos-jni-library`
    id("angelos-jni-library")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    jniImplementation(project(":base"))
    testImplementation("junit:junit:4.12")
}

library {
    binaries.configureEach {
        val compileTask = compileTask.get()
        when(toolChain) {
            is VisualCpp -> compileTask.compilerArgs.addAll(listOf("/TC"))
            is Clang, is GccCompatibleToolChain -> compileTask.compilerArgs.addAll(listOf("-x", "c"))
        }
    }
}

tasks.register("prepareKotlinBuildScriptModel", DefaultTask::class) {
}