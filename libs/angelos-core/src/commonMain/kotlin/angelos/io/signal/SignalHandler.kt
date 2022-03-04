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
package angelos.io.signal

import kotlinx.coroutines.channels.Channel

typealias SignalQueue = Channel<Int>

open class SignalHandler(
    val signals: List<Int>,
    private val queue: SignalQueue = Channel()
) {
    fun send(signum: Int) = suspend { queue.send(signum) }
    suspend fun receive(): Int = queue.receive()
}

// http://www.qnx.com/developers/docs/qnx_4.25_docs/tcpip50/prog_guide/sock_ipc_tut.html
// http://www.cs.tau.ac.il/~eddiea/samples/Signal-Driven/udp-signal-driven-server.c
// https://www.softprayog.in/programming/network-socket-programming-using-tcp-in-c
// https://gist.github.com/richiejp/1590344


// https://www.freebsd.org/cgi/man.cgi?query=kqueue&sektion=2
// https://habr.com/en/post/600123/
// https://github.com/stsaz/kernel-queue-the-complete-guide