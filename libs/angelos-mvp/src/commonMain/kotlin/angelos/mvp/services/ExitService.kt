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

import angelos.mvp.Application
import angelos.mvp.Service
import kotlinx.coroutines.channels.Channel
import org.angproj.io.sig.SigName
import org.angproj.io.sig.SignalHandler

class ExitService(private val signal: SignalService): Service() {
    private val queue = Channel<SigName>()

    override fun setup(thisRef: Application) { signalReg() }

    private fun signalReg() {
        val handler: SignalHandler = {
            println("SignalHandler($it)")
            queue.send(it) }
        signal.registerHandler(SigName.SIGINT, handler)
        signal.registerHandler(SigName.SIGABRT, handler)
    }

    suspend fun await(): SigName = queue.receive()
    suspend fun stop() = queue.send(SigName.UNKNOWN)
}