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

import angelos.interop.Base
import angelos.io.file.Watcher
import angelos.io.signal.SigName
import angelos.io.signal.SignalError
import angelos.io.signal.SignalHandler

class ExtWatcher(private val signal: ExtSignal): Extension, Watcher {
    override val identifier: String
        get() = "watcher"

    init {
        signalReg()
    }

    override fun setup() {}
    override fun cleanup() { Base.pollFinalize() }

    private fun signalReg() {
        val handler: SignalHandler = { poll(it) }
        when {
            SigName.isImplemented(SigName.SIGIO) -> signal.registerHandler(SigName.SIGIO, handler)
            SigName.isImplemented(SigName.SIGPOLL) -> signal.registerHandler(SigName.SIGPOLL, handler)
            else -> throw SignalError("Neither ${SigName.SIGIO} nor ${SigName.SIGPOLL} are implemented")
        }
    }
}