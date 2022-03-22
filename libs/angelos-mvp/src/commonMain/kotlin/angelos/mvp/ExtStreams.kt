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
package angelos.mvp

import angelos.io.stdio.Streams
import angelos.nio.ByteDirectBuffer
import kotlinx.coroutines.channels.Channel

class ExtStreams(private val watcher: ExtWatcher, val bufSize: Long = 4096) : Extension, Streams {
    override val identifier: String
        get() = "stdio"

    private var queue = Channel<ByteDirectBuffer>(32)

    init {
        watchableReg()
    }

    private fun watchableReg() {
        watcher.register(Streams.stdIn, {
            val buffer = ByteDirectBuffer(bufSize, bufSize)
            Streams.stdIn.read(buffer)
            queue.send(buffer)
        })
    }

    override fun setup() { }
    override fun cleanup() { }

    suspend fun read(): ByteDirectBuffer = queue.receive()
}