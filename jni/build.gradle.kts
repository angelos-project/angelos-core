plugins {
    `cpp-library`
    `cpp-unit-test`
}

library {
    targetMachines.add(machines.macOS.x86_64)
    //targetMachines.add(machines.linux.x86_64)
    //targetMachines.add(machines.windows.x86_64)
    linkage.set(listOf(Linkage.SHARED))
}

val javaHome = System.getenv("JAVA_HOME")


tasks.withType(CppCompile::class.java).configureEach {

    compilerArgs.addAll(toolChain.map { toolChain ->
        when (toolChain) {
            is Gcc, is Clang -> listOf(
                "-c",
                "-v",
                "-fpic",
                "-I/usr/java/include",
                "-I/Library/Developer/CommandLineTools/SDKs/MacOSX10.15.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers/",
                "-I$javaHome/include",
                "-I$javaHome/include/darwin",
                "-I$javaHome/include/win32"
            )
            else -> listOf()
        }
    })
}


tasks.withType(LinkSharedLibrary::class.java).configureEach {
    linkerArgs.addAll(toolChain.map { toolChain ->
        when (toolChain) {
            is Gcc, is Clang -> listOf(
                "-v",
                //"-Wl,--out-implib=${project.name}.dll.a",
                //"-Wl,--export-all-symbols",
                // "-Wl,--enable-auto-import",
                //"-Wl,--output-def=${project.name}.def"
            )
            else -> listOf()
        }
    })
    doLast {
        copy {
            from("${project.rootDir}/${project.name}/build/lib/main/debug")
            into("${project.rootDir}/build/libs")
            //into("${project.rootDir}/build/classes/kotlin/jvm/main")
            exclude("*.java")
            exclude("*.i")
            exclude("*.obj")
        }

        copy {
            from("${project.rootDir}/${project.name}/src/main/swig")
            into("${project.rootDir}/src/main/java")
            exclude("*.i")
            exclude("*.cpp")
        }

        copy {
            from("${project.rootDir}/${project.name}/${project.name}.dll.a")
            into("${project.rootDir}")
        }

        copy {
            from("${project.rootDir}/${project.name}/${project.name}.def")
            into("${project.rootDir}")
        }
    }
}

// Generate C++ native header file.
// javac -h ./jni/src/main/public/ src/jvmMain/java/angelos/jni/Posix.java