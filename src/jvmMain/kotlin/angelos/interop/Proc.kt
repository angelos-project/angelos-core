package angelos.interop

/**
 * How to call Kotlin from outside.
 * https://www.iitk.ac.in/esc101/05Aug/tutorial/native1.1/implementing/method.html
 */

actual class Proc {
    actual companion object {
        actual fun registerInterrupt(signum: Int) {
            pr_signal(signum)
        }

        actual fun interrupt(signum: Int) {
        }

        @JvmStatic
        private external fun pr_signal(signum: Int): Boolean

        init {
            System.loadLibrary("jniproc")
        }

    }
}