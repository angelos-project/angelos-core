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

import angelos.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class StreamClientSocketTest {

    @Test
    fun run() = runBlockingTest {
        val host = "localhost"
        val port = 8080.toShort()
        val server = StreamServerSocket(host, port)
        val client = StreamClientSocket(host, port)
        //server.open()
        //delay(1000)
        //client.connect()
    }

    @Test
    fun connect() = runBlockingTest {
        val socket = StreamClientSocket("localhost", 80)
        socket.connect()
    }

    @Test
    fun close() {
        println("Hello")
    }
}