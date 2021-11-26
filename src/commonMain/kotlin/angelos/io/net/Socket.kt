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
import angelos.io.signal.Signal
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlin.jvm.JvmStatic
import kotlin.properties.Delegates

open class Socket(val host: String, val port: Short) {
    protected var sock: Int = 0

    init {
    }

    protected fun isPortAvailable(port: Short) = port in ports

    enum class Family (family: Int) {
        UNIX(1),
        INET(2),
        INET6(10),
    }

    enum class Type(type: Int) {
        STREAM(1),
        DATAGRAM(2),
    }

    companion object{
        private var running: Boolean by Delegates.observable(false) { _, _, _ -> }
        private val queue = Channel<Int>()
        private val sigHandler = SocketSignalHandler(queue)
        protected val sockets: MutableMap<Int, Socket> = mutableMapOf()
        protected val ports: MutableMap<Short, Socket> = mutableMapOf()
        @JvmStatic
        protected val globalMutex = Mutex()

        /*init {
        }*/

        @JvmStatic
        protected fun startup() {
            if (!running){
                running = true
                Signal.register(sigHandler)
            }
        }

        @JvmStatic
        protected fun open(socket: Socket) {
            if (socket.port in ports)
                throw IOException("Port $socket.port is already reserved.")
            if (socket.sock in sockets)
                throw IOException("Socket $socket.sock is already in use.")

            ports[socket.port] = socket
            sockets[socket.sock] = socket
        }

        @JvmStatic
        protected fun listen(socket: Socket) {}

        protected fun close(socket: Socket) {}
    }
}