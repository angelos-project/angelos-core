val javaHome = System.getenv("JAVA_HOME")

plugins {
    `cpp-library`
}

library {
    binaries.configureEach {
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

        compileTask.includes.setFrom(fileTree("src/main/public"))
        compileTask.source.setFrom(fileTree("src/main/cpp"))

        when(toolChain) {
            is VisualCpp -> compileTask.compilerArgs.addAll(listOf("/TC"))
            is Clang, is GccCompatibleToolChain -> compileTask.compilerArgs.addAll(listOf("-x", "c", "-std=c11"))
        }
    }
}

// Generate C++ native header file.
// javac -h ./jni/src/main/public/ src/jvmMain/java/angelos/jni/Posix.java

// https://programmerclick.com/article/73771902404/