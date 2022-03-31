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

import angelos.io.*
import angelos.io.channel.GatheringByteChannel
import angelos.io.channel.ScatteringByteChannel
import angelos.io.channel.SeekableByteChannel

abstract class FileChannel<R: ByteBuffer, W: MutableByteBuffer>(val option: FileSystem.OpenOption): SeekableByteChannel<R, W>, ScatteringByteChannel<R>, GatheringByteChannel<W> {

    private var _open: Boolean = true
    private var _size: ULong = 0u

    private var _pos: ULong = 0u
    private var pos: Long
        get() = _pos.toLong()
        set(value) {
            _pos = value.toULong()
            if (_pos > _size)
                _size = _pos
        }

    protected abstract fun readFd(dst: R, position: Int, count: Long): Long
    protected abstract fun writeFd(src: W, position: Int, count: Long): Long
    protected abstract fun tellFd(): Long
    protected abstract fun seekFd(newPosition: Long): Long
    protected abstract fun closeFd(): Boolean

    override val size: Long
        get() = _size.toLong()

    override var position: Long
        get() {
            val position = tellFd()
            if (position.toULong() != _pos)
                throw SyncFailedException("File descriptor out of sync with physical cursor.")
            return position
        }
        set(value) {
            _pos = seekFd(value).toULong()
        }

    fun lock() {
        TODO()
    }

    /*fun read(dst: Buffer, position: Long) {

    }

    fun write(src: Buffer, position: Long) {

    }*/

    override fun read(dst: R): Long {
        val length = dst.allowance()
        if(readFd(dst, dst.mark, length.toLong()) != length.toLong())
            throw throw IOException("Couldn't read $length bytes from file.")
        pos += length
        return length.toLong()
    }

    override fun read(dsts: List<R>, offset: Int, length: Int): Long {
        var count: Long = 0
        dsts.subList(offset, length).asSequence().forEach { count += read(it) }
        return count
    }

    override fun read(dsts: List<R>): Long = read(dsts, 0, dsts.size)

    override suspend fun write(src: W): Long {
        val length = src.allowance()
        if(writeFd(src, src.position, length.toLong()) != length.toLong())
            throw IOException("Couldn't write $length bytes to file.")
        pos += length
        return length.toLong()
    }

    override suspend fun write(srcs: List<W>, offset: Int, length: Int): Long {
        var count: Long = 0
        srcs.subList(offset, length).asSequence().forEach { count += write(it) }
        return count
    }

    override suspend fun write(srcs: List<W>): Long = write(srcs, 0, srcs.size)

    override fun truncate(size: Long): SeekableByteChannel<R, W> {
        TODO("Not yet implemented")
    }

    override fun isOpen(): Boolean {
        TODO("Not yet implemented")
    }

    override fun close() {
        if(_open)
            if(closeFd())
                _open = false
    }

    protected fun finalize() {
        close()
    }

}