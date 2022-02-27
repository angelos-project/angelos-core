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
 *      Kristoffer Paulsson - port from python
 */
package angelos.mvp

import angelos.io.signal.Signal
import angelos.io.signal.SignalHandler
import kotlinx.coroutines.channels.Channel

class ExtQuit(prepare: (it: ExtQuit) -> Unit) : Extension("quit", prepare as (Extension) -> Unit) {
    private lateinit var handler: SignalHandler
    lateinit var signal: ExtSignal

    fun signalReg() {
        handler = signal.build(Channel(),
            // Signal.Num.SIGKILL.signum,
            Signal.Num.SIGABRT.signum,
            Signal.Num.SIGINT.signum
        )
        signal.register(handler)
    }

    suspend fun await(): Int = handler.receive()
}
