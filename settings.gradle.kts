rootProject.name = "angelos"

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

/**
 * Application projects using the libraries.
 */

include("")