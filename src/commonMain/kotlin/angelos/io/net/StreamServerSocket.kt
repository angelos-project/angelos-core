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

    override suspend fun open(): Unit = globalMutex.withLock {
        _sock = raise(IO.serverOpen(Family.INET, Type.STREAM, 0))
        startup()
        add()
        raise(IO.serverListen(_sock, host, port, Family.INET, 10))
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