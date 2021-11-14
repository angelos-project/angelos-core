package angelos.interop

import kotlinx.cinterop.staticCFunction
import platform.posix.signal

/**
 * How to call Kotlin from outside.
 * https://kotlinlang.org/docs/mapping-function-pointers-from-c.html#c-function-pointers-in-kotlin
 */

actual fun registerInterrupt(signum: Int) { signal(signum, staticCFunction<Int, Unit> { Proc.interrupt(it) }) }