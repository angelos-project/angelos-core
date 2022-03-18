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

import base.*
import kotlinx.cinterop.staticCFunction


actual class Base {
    actual companion object {
        init {
            // Setting up the outbound signal action callback
            // and initializing external signal handler.
            init_signal_handler(staticCFunction<Int, Unit> { incomingSignal(it) })
        }

        actual fun getEndian(): Int = endian()

        actual fun getPlatform(): Int = platform()

        actual fun setInterrupt(sigNum: Int): Boolean = when(register_signal_handler(sigNum)){
            0 -> true
            else -> false
        }

        private inline fun incomingSignal(sigNum: Int) {
            TODO("$sigNum. Time to implement signal handler on native.")
        }

        actual fun sigAbbr(sigNum: Int): String = signal_abbreviation(sigNum).toString()
    }
}