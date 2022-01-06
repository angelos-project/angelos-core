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

import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*

class StreamServerSocketTest {

    @Test
    fun getHost() {
    }

    @Test
    fun getPort() {
    }

    @Test
    fun open() = runBlockingTest {
        val socket = StreamServerSocket("127.0.0.1", 80)
        socket.open()
        println(socket.sock)
    }

    @Test
    fun listen() {
    }

    @Test
    fun handle() {
    }

    @Test
    fun close() {
    }
}