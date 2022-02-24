import java.nio.file.Files
import java.util.*

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


/*
// https://gist.github.com/DRSchlaubi/f42be0da6fbd8864565b043b3da3b8b2
val localProperties = Properties()
val localPropertiesFile = File(rootProject.projectDir, "local.properties")
if (Files.exists(localPropertiesFile.toPath())) {
    Files.newBufferedReader(localPropertiesFile.toPath()).use { localProperties.load(it) }
}

val flutterSdkPath = localProperties.getProperty("flutter.sdk")
apply{
    from("$flutterSdkPath/packages/flutter_tools/gradle/app_plugin_loader.gradle")
}*/

include("logo-messenger")
project(":logo-messenger").projectDir = File("apps/logo_messenger")

// https://github.com/firebase/quickstart-android
include("app")
project(":app").projectDir = File("apps/logo_messenger-android")
//apply { from("flutter_settings.gradle") }

include("logo-messenger-ios")
project(":logo-messenger-ios").projectDir = File("apps/logo_messenger-ios")

//include("logo-messenger-linux")
//project(":logo-messenger-linux").projectDir = File("apps/logo_messenger/linux")

//include("logo-messenger-macos")
//project(":logo-messenger-macos").projectDir = File("apps/logo_messenger/macos")

//include("logo-messenger-windows")
//project(":logo-messenger-windows").projectDir = File("apps/logo_messenger/windows")

// https://github.com/littleGnAl/accounting-multiplatform/tree/littlegnal/blog-kmpp-flutter
