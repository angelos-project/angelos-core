/**
 * Copyright (c) 2021 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
 *
 * This software is available under the terms of the MIT license. Parts are licensed
 * under different terms if stated. The legal terms are attached to the LICENSE file
 * and are made available on:
 *
 *      https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *
 * Contributors:
 *      Kristoffer Paulsson - initial implementation
 */
package angelos.interop

import kotlinx.cinterop.*
import platform.posix.*

/**
 * How to call Kotlin from outside.
 * https://kotlinlang.org/docs/mapping-function-pointers-from-c.html#c-function-pointers-in-kotlin
 */

@Suppress("VARIABLE_IN_SINGLETON_WITHOUT_THREAD_LOCAL")
actual class Proc: AbstractProc () {
    actual companion object {
        actual var errNum: Int = 0
        actual var errMsg: String = ""

        actual fun getError(): SystemError { TODO("Not yet implemented") }

        private val sigHandlerPtr = staticCFunction<Int, CPointer<__siginfo>?, COpaquePointer?, Unit> {
            signum, info, context -> memScoped {
                val sigs = alloc<sigset_tVar>()
                val sigsRef = sigs.ptr as CValuesRef<sigset_tVar>

                sigemptyset(sigsRef)
                pthread_sigmask(0, null, sigsRef)
                if (sigismember(sigsRef, SIGIO) > 0) {
                    interrupt(signum)
                    sigaddset(sigsRef, SIGIO);
                    pthread_sigmask(SIG_UNBLOCK, sigsRef, null);
                }
                free(sigs.ptr)
            }
        }
        private val sigActionData: sigaction

        private fun posixSigAction(): sigaction = memScoped{
            val data = alloc<sigaction>()
            data.sa_flags = SA_SIGINFO or SA_RESTART or SA_NODEFER
            data.__sigaction_u.__sa_sigaction = sigHandlerPtr
            sigfillset(data.sa_mask as CValuesRef<sigset_tVar>)
            sigdelset(data.sa_mask as CValuesRef<sigset_tVar>, SIGIO)
            return data
        }

        init {
            sigActionData = posixSigAction()
            if(sigaction(SIGIO, sigActionData.ptr as CValuesRef<sigaction>, null) > 0)
                throw UnsupportedOperationException("Failed to register signal handler.")
        }

        actual fun registerInterrupt(signum: Int): Boolean = when (sigaction(signum, sigActionData.ptr as CValuesRef<sigaction>, null)) {
            0 -> true
            else -> false
        }
    }
}