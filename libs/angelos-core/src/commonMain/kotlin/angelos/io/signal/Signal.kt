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
 *      Kristoffer Paulsson - initial implementation
 */
package angelos.io.signal

import angelos.interop.Base
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

interface Signal {
    fun registerHandler(sig: SigName, action: SignalHandler) {
        if (signals.contains(sig))
            signals[sig]!!.add(action)
        else
            signals[sig] = mutableListOf(action)
            Base.setInterrupt(sig)
    }

    companion object {
        init {
            Base.interrupt = { catchInterrupt(it) }
        }

        private val scope = CoroutineScope(EmptyCoroutineContext)
        private val signals = mutableMapOf<SigName, MutableList<SignalHandler>>()

        private inline fun catchInterrupt(sigName: SigName) = signals[sigName]?.forEach { it -> scope.launch { it(sigName) } }
    }
}


// http://www.qnx.com/developers/docs/qnx_4.25_docs/tcpip50/prog_guide/sock_ipc_tut.html
// http://www.cs.tau.ac.il/~eddiea/samples/Signal-Driven/udp-signal-driven-server.c
// https://www.softprayog.in/programming/network-socket-programming-using-tcp-in-c
// https://gist.github.com/richiejp/1590344


// https://www.freebsd.org/cgi/man.cgi?query=kqueue&sektion=2
// https://habr.com/en/post/600123/
// https://github.com/stsaz/kernel-queue-the-complete-guide


// https://stackoverflow.com/questions/29451133/whats-the-proper-way-to-safely-discard-stdin-characters-of-variable-length-in-c
// https://viewsourcecode.org/snaptoken/kilo/index.html