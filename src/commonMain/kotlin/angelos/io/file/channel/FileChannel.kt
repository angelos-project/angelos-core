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
package angelos.io.file.channel

import angelos.io.FileSystem
import angelos.io.channel.GatheringByteChannel
import angelos.io.channel.ScatteringByteChannel
import angelos.io.channel.SeekableByteChannel
import angelos.nio.Buffer

class FileChannel: SeekableByteChannel, GatheringByteChannel, ScatteringByteChannel {
    fun open(file: FileSystem.File, options: FileSystem.OpenOption): FileChannel{
        TODO()
    }

    fun lock() {
        TODO()
    }

    /*fun read(dst: Buffer, position: Long) {

    }

    fun write(src: Buffer, position: Long) {

    }*/

    override fun write(srcs: List<Buffer>) {
        TODO("Not yet implemented")
    }

    override fun write(srcs: List<Buffer>, offset: Long, length: Long) {
        TODO("Not yet implemented")
    }

    override fun write(src: Buffer): Long {
        TODO("Not yet implemented")
    }

    override fun read(dsts: List<Buffer>): Long {
        TODO("Not yet implemented")
    }

    override fun read(dsts: List<Buffer>, offset: Long, length: Long): Long {
        TODO("Not yet implemented")
    }

    override fun position(): Long {
        TODO("Not yet implemented")
    }

    override fun position(newPosition: Long): SeekableByteChannel {
        TODO("Not yet implemented")
    }

    override fun size(): Long {
        TODO("Not yet implemented")
    }

    override fun truncate(size: Long): SeekableByteChannel {
        TODO("Not yet implemented")
    }

    override fun read(dst: Buffer): Long {
        TODO("Not yet implemented")
    }

    override fun isOpen(): Boolean {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

}