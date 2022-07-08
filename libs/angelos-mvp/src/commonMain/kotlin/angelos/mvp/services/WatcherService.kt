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
package angelos.mvp.services

import angelos.interop.Base
import angelos.io.file.Watcher
import angelos.mvp.Application
import angelos.mvp.Service
import org.angproj.io.sig.SigName
import org.angproj.io.sig.SignalException
import org.angproj.io.sig.SignalHandler

class WatcherService(private val signal: SignalService): Service(), Watcher {
    override fun setup(thisRef: Application) {
        Base.initializePolling()
        signalReg()
    }
    override fun cleanup(thisRef: Application) { Base.finalizePolling() }

    private fun signalReg() {
        val handler: SignalHandler = { poll(it) }
        when {
            SigName.isImplemented(SigName.SIGIO) -> signal.registerHandler(SigName.SIGIO, handler)
            SigName.isImplemented(SigName.SIGPOLL) -> signal.registerHandler(SigName.SIGPOLL, handler)
            else -> throw SignalException("Neither ${SigName.SIGIO} nor ${SigName.SIGPOLL} are implemented")
        }
    }
}