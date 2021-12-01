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
package angelos.io.net

import angelos.interop.IO
import angelos.io.IOException
import kotlinx.coroutines.sync.withLock

class StreamServerSocket(host: String, port: Short) : ServerSocket(host, port) {

    override fun open() {
        suspend {
            globalMutex.withLock {
                _sock = IO.serverOpen(Family.INET, Type.STREAM, 0)
                if (_sock == -1)
                    throw IOException("Failed to open a new socket and subscribe signals to process.")

                startup()
                open(this)

                val err = IO.serverListen(_sock, host, port, Family.INET, 10)
                if (err != 0)
                    throw IOException("Failed to bind and listen on socket with port $port.")
            }
        }
    }

    override fun listen() {
        TODO("Not yet implemented")
    }

    override fun handle() {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}