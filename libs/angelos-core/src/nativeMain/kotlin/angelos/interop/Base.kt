/**
 * Copyright (c) 2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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

import angelos.io.signal.SigName
import angelos.sys.Error
import base.*
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.errno
import platform.posix.strerror
import kotlin.properties.Delegates


actual class Base {
    actual companion object {
        init {
            // Setting up the outbound signal action callback
            // and initializing external signal handler.
            init_signal_handler(staticCFunction<Int, Unit> {
                incomingSignal(SigName.codeToName(it))
            })
        }

        internal actual var interrupt: (sigNum: SigName) -> Unit by Delegates.notNull()

        actual fun getEndian(): Int = endian()

        actual fun getPlatform(): Int = platform()

        actual fun setInterrupt(sigName: SigName): Boolean = when(register_signal_handler(SigName.nameToCode(sigName))){
            0 -> true
            else -> false
        }

        internal actual inline fun incomingSignal(sigName: SigName) { interrupt(sigName) }

        actual fun sigAbbr(sigNum: Int): String = memScoped{
            val abbr = signal_abbreviation(sigNum)
            abbr?.toKString().toString().uppercase()
        }

        actual inline fun getError() {
            Error.errNum.usePinned { errno }
            Error.errMsg.usePinned { strerror(errno)?.toKString().toString() }
        }
    }
}