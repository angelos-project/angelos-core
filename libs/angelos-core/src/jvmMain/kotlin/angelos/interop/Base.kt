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

import angelos.io.signal.SigName
import sun.misc.Signal
import java.lang.System
import kotlin.properties.Delegates

actual class Base {
    actual companion object {
        init {
            System.loadLibrary("jni-base") // Load underlying library via JNI.
        }

        internal actual var interrupt: (sigNum: SigName) -> Unit by Delegates.notNull()

        actual fun getEndian(): Int = endian()

        actual fun getPlatform(): Int = platform()

        @JvmStatic
        private external fun endian(): Int

        @JvmStatic
        private external fun platform(): Int

        actual fun setInterrupt(sigName: SigName): Boolean {
            Signal.handle(Signal(sigName.sigName)) {
                incomingSignal(SigName.codeToName(it.number))
            }
            return true
        }

        internal actual fun incomingSignal(sigName: SigName) { interrupt(sigName) }

        actual fun sigAbbr(sigNum: Int): String = signal_abbreviation(sigNum).uppercase()

        @JvmStatic
        private external fun signal_abbreviation(sigNum: Int): String

        actual fun getError() {
            get_error()
        }

        @JvmStatic
        private external fun get_error()
    }
}