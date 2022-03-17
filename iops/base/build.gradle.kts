plugins {
    `cpp-library`
    xcode
    `visual-studio`
}

repositories {
    mavenCentral()
}

library {
    linkage.set(listOf(Linkage.STATIC))
    binaries.configureEach {
        val compileTask = compileTask.get()
        compileTask.source.from(fileTree("src/main/c") {
            include("**/*.c")
        })
        when(toolChain) {
            is VisualCpp -> compileTask.compilerArgs.addAll(listOf("/TC"))
            is Clang, is GccCompatibleToolChain -> compileTask.compilerArgs.addAll(listOf("-x", "c"))
        }
    }
}