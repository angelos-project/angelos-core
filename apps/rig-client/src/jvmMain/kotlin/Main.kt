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
 *      Kristoffer Paulsson - initial implementation
 */
import angelos.io.net.StreamClientSocket
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    println("Client: Hello World!")
    println("Program arguments: ${args.joinToString()}")

    val socket = StreamClientSocket("localhost", 8080)
    socket.connect()
}