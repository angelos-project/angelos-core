/**
 * Copyright (c) 2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
import angelos.io.stdio.Streams
import angelos.mvp.Application
import angelos.mvp.Service
import kotlinx.coroutines.channels.Channel
import org.angproj.io.buf.NativeByteBuffer
import org.angproj.io.buf.nativeByteBufferOf

class StreamsService(
    private val watcher: WatcherService,
    private val terminalMode: Boolean = false,
    private val bufSize: Int = 4096
): Service(), Streams {
    private var queue = Channel<NativeByteBuffer>(32)

    private fun watchableReg() = watcher.register(Streams.stdIn, {
        val buffer = nativeByteBufferOf(bufSize)
        Streams.stdIn.read(buffer)
        queue.send(buffer as NativeByteBuffer)
    })

    override fun setup(thisRef: Application) {
        watchableReg()
        if (terminalMode) Base.initializeTerminalMode() }
    override fun cleanup(thisRef: Application) { if (terminalMode) Base.finalizeTerminalMode() }

    suspend fun read(): NativeByteBuffer = queue.receive()
}