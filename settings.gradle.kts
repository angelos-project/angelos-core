include(":jni:base")
include(":jni:platform") // Shared library returning platform specific information.
include(":jni:proc") // Shared library for process interrupts.
include(":jni:io") // Shared library for IO file systems and sockets.

include(":lib")

include(":test:net") // Test rig

rootProject.name = "angelos-core"
