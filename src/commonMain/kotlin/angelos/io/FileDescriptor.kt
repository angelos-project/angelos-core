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
package angelos.io

class FileDescriptor(private val descriptor: ULong): Closable {

    //fun read()

    //fun write()

    //fun tell()

    //fun seek()

    //fun close()
    override fun close() {
        TODO("Not yet implemented")
    }

}

//internal expect inline fun doRead(path: String): UInt
//internal expect inline fun doWrite(path: String): UInt
//internal expect inline fun doTell(path: String): UInt
//internal expect inline fun doSeek(path: String): UInt
//internal expect inline fun doClose(path: String): UInt