/**
 * Copyright (c) 2021-2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

interface Signal {
    fun registerHandler(sig: SigName, action: SignalHandler) {
        if (signals.contains(sig))
            signals[sig]!!.add(action)
        else
            signals[sig] = mutableListOf(action)
            Base.setInterrupt(sig)
    }

    companion object {
        init {
            Base.interrupt = { catchInterrupt(it) }
        }

        private val scope = CoroutineScope(EmptyCoroutineContext)
        private val signals = mutableMapOf<SigName, MutableList<SignalHandler>>()

        private inline fun catchInterrupt(sigName: SigName) = signals[sigName]?.forEach { scope.launch { it(sigName) } }
    }
}