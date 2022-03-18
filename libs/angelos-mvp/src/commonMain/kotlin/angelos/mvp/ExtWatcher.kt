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

import angelos.io.file.Watcher
import angelos.io.signal.SignalHandler
import angelos.io.signal.SigName
import kotlinx.coroutines.channels.Channel

class ExtWatcher(private val signal: ExtSignal): Extension, Watcher {
    override val identifier: String
        get() = "watcher"

    private lateinit var handler: SignalHandler

    init {
        signalReg()
    }

    override fun setup() {}
    override fun cleanup() {}

    fun signalReg() {
        handler = signal.build(
            Channel() {
                poll()
            },
            SigName.SIGIO.sigNum,
        )
        signal.register(handler)
    }
}