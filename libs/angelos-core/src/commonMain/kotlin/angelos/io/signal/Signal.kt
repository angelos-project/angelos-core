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

import angelos.interop.Base

interface Signal {
    fun registerHandler(sig: SigName, action: suspend (it: SigName) -> (Unit)) {
        if (signals.contains(sig))
            signals[sig]!!.add(action)
        else
            signals[sig] = mutableListOf(action)
    }

    companion object {
        init {
            Base.interrupt = { catchInterrupt(it) }
        }

        private val signals = mutableMapOf<SigName, MutableList<suspend (it: SigName) -> (Unit)>>()

        private inline fun catchInterrupt(sigName: SigName) = signals[sigName]?.forEach { suspend { it(sigName) } }
    }
}