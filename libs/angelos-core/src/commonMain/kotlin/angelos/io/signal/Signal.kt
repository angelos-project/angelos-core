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
package angelos.io.signal

interface Signal {
    companion object {
        private val signals = mutableMapOf<SigName, MutableList<suspend (it: SigName) -> (Unit)>>()

        init {
            // Hook into Base class.
        }

        fun catchInterrupt(sigNum: Int) {
            val sig = SigName.codeToName(sigNum)
            signals[sig]?.forEach { suspend { it(sig) } }
        }
    }

    fun registerHandler(sig: SigName, action: suspend (it: SigName) -> (Unit)) {
        if (signals.contains(sig))
            signals[sig]!!.add(action)
        else
            signals[sig] = mutableListOf(action)
    }
}