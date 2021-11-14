package angelos.interop

import angelos.io.signal.Signal

/**
 * How to call Kotlin from outside.
 * https://www.iitk.ac.in/esc101/05Aug/tutorial/native1.1/implementing/method.html
 */

actual fun registerInterrupt(signum: Int) {
    Proc.pr_signal(signum)
}

class Proc {
     companion object {
        @JvmStatic
        external fun pr_signal(signum: Int): Boolean

        init {
            System.loadLibrary("jniproc")
        }
    }
}