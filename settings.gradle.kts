rootProject.name = "angelos"

dependencyResolutionManagement {
    // https://docs.gradle.org/current/userguide/declaring_repositories.html#sub:centralized-repository-declaration
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

/**
 * Interoperability libraries for cinterop and JNI.
 */

include("base")
project(":base").projectDir = File("iops/base")

include("jni-io")
project(":jni-io").projectDir = File("iops/jni-io")

include("jni-platform")
project(":jni-platform").projectDir = File("iops/jni-platform")

include("jni-proc")
project(":jni-proc").projectDir = File("iops/jni-proc")

/**
 * Libraries mainly for commonMain.
 */

include("angelos-core")
project(":angelos-core").projectDir = File("libs/angelos-core")

include("angelos-nacl")
project(":angelos-nacl").projectDir = File("libs/angelos-nacl")

/**
 * Application projects using the libraries.
 */

include("rig-server")
project(":rig-server").projectDir = File("apps/rig-server")

include("rig-client")
project(":rig-client").projectDir = File("apps/rig-client")

include("logo-messenger-android")
project(":logo-messenger-android").projectDir = File("apps/logo_messenger/android")

include("logo-messenger-ios")
project(":logo-messenger-ios").projectDir = File("apps/logo_messenger/ios")

include("logo-messenger-linux")
project(":logo-messenger-linux").projectDir = File("apps/logo_messenger/linux")

include("logo-messenger-macos")
project(":logo-messenger-macos").projectDir = File("apps/logo_messenger/macos")

include("logo-messenger-windows")
project(":logo-messenger-windows").projectDir = File("apps/logo_messenger/windows")