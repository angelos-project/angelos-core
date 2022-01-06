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

    protected abstract fun readFd(dst: Buffer, position: Int, count: Long): Long
    protected abstract fun writeFd(src: Buffer, position: Int, count: Long): Long
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

    override fun read(dst: Buffer): Long {
        val length = dst.allowance()
        if(readFd(dst, dst.position, length) != length)
            throw throw IOException("Couldn't read $length bytes from file.")
        pos += length
        return length
    }

    override fun read(dsts: List<Buffer>, offset: Int, length: Int): Long {
        var count: Long = 0
        dsts.subList(offset, length).asSequence().forEach { count += read(it) }
        return count
    }

    override fun read(dsts: List<Buffer>): Long = read(dsts, 0, dsts.size)

    override fun write(src: Buffer): Long {
        val length = src.allowance()
        if(writeFd(src, src.position, length) != length)
            throw IOException("Couldn't write $length bytes to file.")
        pos += length
        return length
    }

    override fun write(srcs: List<Buffer>, offset: Int, length: Int): Long {
        var count: Long = 0
        srcs.subList(offset, length).asSequence().forEach { count += write(it) }
        return count
    }

    override fun write(srcs: List<Buffer>): Long = write(srcs, 0, srcs.size)

    override fun truncate(size: Long): SeekableByteChannel {
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