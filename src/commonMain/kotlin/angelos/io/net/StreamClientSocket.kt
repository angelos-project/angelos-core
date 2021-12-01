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

class StreamClientSocket(host: String, port: Short) : ClientSocket(host, port) {
    fun connect() {
        //suspend {
        //    globalMutex.withLock {
                _sock = IO.clientOpen(host, port, Family.INET, Type.STREAM, 0)
                if (_sock == -1)
                    throw IOException("Failed to open a new socket and connect to server.")
       //     }
       // }
    }

    fun close() {
        TODO("Not yet implemented")
    }
}