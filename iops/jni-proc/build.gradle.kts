import org.gradle.internal.jvm.Jvm

plugins {
    base
    `cpp-library`
}

repositories {
    mavenCentral()
    mavenLocal()
}

// https://docs.gradle.org/current/userguide/variant_attributes.html
// https://docs.gradle.org/current/userguide/component_metadata_rules.html
// https://github.com/gradle/gradle/blob/master/subprojects/docs/src/docs/design/gradle-module-metadata-latest-specification.md
/* val mppAttribute = Attribute.of("org.jetbrains.kotlin.platform.type", String::class.java)
val jvmVersion = Attribute.of("org.gradle.jvm.version", String::class.java)
val jvmEnv = Attribute.of("org.gradle.jvm.environment", String::class.java)
val gradleCategory = Attribute.of("org.gradle.category", String::class.java)

dependencies.attributesSchema {
    attribute(mppAttribute)
    attribute(jvmVersion)
    attribute(jvmEnv)
    attribute(gradleCategory)
}

configurations {
    create("mppConfiguration") {
        attributes {
            attribute(mppAttribute, "jvm")
            attribute(jvmVersion, "8")
            attribute(jvmEnv, "standard-jvm")
            attribute(gradleCategory, "library")
        }
    }
} */


library {
   binaries.configureEach{
       val compileTask = compileTask.get()
       val javaHome = Jvm.current().javaHome.canonicalPath
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