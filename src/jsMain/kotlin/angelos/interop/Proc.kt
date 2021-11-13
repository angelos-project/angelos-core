package angelos.interop

/**
 * How to call Kotlin from outside.
 * https://www.iitk.ac.in/esc101/05Aug/tutorial/native1.1/implementing/method.html
 * https://kotlinlang.org/docs/mapping-function-pointers-from-c.html#c-function-pointers-in-kotlin
 */

actual class Proc {
    actual companion object {
        actual fun registerInterrupt(signum: Int) {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/JS")
        }

        actual fun interrupt(signum: Int) {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/JS")
        }

    }
}