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
import angelos.io.IOException
import angelos.io.SyncFailedException
import angelos.io.channel.GatheringByteChannel
import angelos.io.channel.ScatteringByteChannel
import angelos.io.channel.SeekableByteChannel
import angelos.nio.Buffer

abstract class FileChannel(val option: FileSystem.OpenOption): SeekableByteChannel, GatheringByteChannel, ScatteringByteChannel {
    private var _position: ULong = 0u
    private var _open: Boolean = true

    protected abstract fun read(dst: ByteArray, position: Int, count: Long): Long
    protected abstract fun write(src: ByteArray, position: Int, count: Long): Long
    protected abstract fun tell(): Long
    protected abstract fun seek(newPosition: Long): Long

    fun lock() {
        TODO()
    }

    /*fun read(dst: Buffer, position: Long) {

    }

    fun write(src: Buffer, position: Long) {

    }*/

    override fun read(dst: Buffer): Long {
        val pos = dst.position
        val count = dst.limit - pos
        if(read(dst.array(), pos, count) != count)
            throw IOException("Couldn't read $count bytes from file.")
        _position += count.toULong()
    }

    override fun read(dsts: List<Buffer>): Long {
        TODO("Not yet implemented")
    }

    override fun read(dsts: List<Buffer>, offset: Long, length: Long): Long {
        TODO("Not yet implemented")
    }

    override fun write(srcs: List<Buffer>) {
        TODO("Not yet implemented")
    }

    override fun write(srcs: List<Buffer>, offset: Long, length: Long) {
        TODO("Not yet implemented")
    }

    override fun write(src: Buffer): Long {
    }

    override fun position(): Long {
        val position = tell()
        if (position.toULong() != _position)
            throw SyncFailedException("File descriptor out of sync with physical cursor.")
        return position
    }

    override fun position(newPosition: Long): SeekableByteChannel {
        _position = seek(newPosition).toULong()
        return this
    }

    override fun size(): Long {
        TODO("Not yet implemented")
    }

    override fun truncate(size: Long): SeekableByteChannel {
        TODO("Not yet implemented")
    }

    override fun isOpen(): Boolean {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

}