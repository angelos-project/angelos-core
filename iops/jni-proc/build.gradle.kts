/*plugins {
    base
    `cpp-library`
}

library {
    //val shared = components.withType(CppSharedLibrary::class)
    binaries.configureEach{
        base{
            archivesName.set("jniProc")
            //distsDirectory.set(layout.buildDirectory.dir("lib/main/release/"))
        }
        val compileTask = compileTask.get()

        compileTask.source.setFrom(fileTree("src/main/cpp"))

        val javaHome = System.getenv("JAVA_HOME")
        compileTask.includes.from("$javaHome/include")

        val osFamily = targetPlatform.targetMachine.operatingSystemFamily
        when{
            osFamily.isMacOs-> {
                compileTask.includes.from("-I$javaHome/include/darwin")
                compileTask.compilerArgs.add("-I/Library/Developer/CommandLineTools/SDKs/MacOSX10.15.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers/")
            }
            osFamily.isLinux -> {
                compileTask.includes.from("$javaHome/include/linux")
            }
            osFamily.isWindows -> {
                compileTask.includes.from("$javaHome/include/win32")
            }
        }

        /*println(compileTask.asDynamicObject) */
        when(toolChain) {
            is VisualCpp -> compileTask.compilerArgs.addAll(listOf("/TC"))
            is Clang, is GccCompatibleToolChain -> compileTask.compilerArgs.addAll(listOf("-x", "c", "-std=c11"))
        }
    }
}

/*configurations {
    create("jniProc"){
        isCanBeResolved = true
        isCanBeConsumed = true
        isTransitive = true
        isVisible = true
        attributes {
            //attribute(Category.CATEGORY_ATTRIBUTE, named(Category.LIBRARY))
        }
    }

    create("jniProcDebug"){
        isCanBeResolved = true
        isCanBeConsumed = true
        isTransitive = true
        isVisible = true
    }
}

dependencies {
    //"jniProc"(fileTree(layout.buildDirectory.dir("lib/main/release/stripped")))
    //"jniProcDebug"(fileTree(layout.buildDirectory.dir("lib/main/debug")))

}

artifacts {
    add("jniProc", fileTree(layout.buildDirectory.dir("lib/main/release/stripped")).dir)
    add("jniProcDebug", fileTree(layout.buildDirectory.dir("lib/main/release/stripped")).dir)

}*/

// https://github.com/gradle/native-samples/blob/master/cpp/prebuilt-binaries/build.gradle
 */

plugins {
    id("angelos-jni-library")
    //`angelos-jni-library`
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