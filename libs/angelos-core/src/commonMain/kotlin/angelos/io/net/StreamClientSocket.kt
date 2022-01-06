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
import kotlinx.coroutines.sync.withLock

class StreamClientSocket(host: String, port: Short) : ClientSocket(host, port) {
    suspend fun connect() = globalMutex.withLock {
        _sock = raise(IO.clientOpen(host, port, Family.INET, Type.STREAM, 0))
        startup()
        add()
    }

    fun close() {
        TODO("Not yet implemented")
    }
}