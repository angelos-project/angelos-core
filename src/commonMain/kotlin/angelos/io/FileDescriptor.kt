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

import angelos.interop.FileSystem
import angelos.nio.BufferOverflowException
import angelos.nio.BufferUnderflowException
import angelos.nio.ByteBuffer

/* class FileDescriptor internal constructor(
    internal val file: File,
    val option: File.OpenOption,
    private var _number: Int,
) : Closable {
    private var _position: ULong = 0u

    val position: Long
        get() = _position.toLong()

    val number: Int
        get() = _number

    fun read(buffer: ByteBuffer, count: Long) {
        if (buffer.remaining() < count)
            throw BufferUnderflowException()
        if(file.path.store.readFile(_number, buffer.array(), buffer.position, count) != count)
            throw IOException("Couldn't read $count bytes from file.")
        _position += count.toULong()
    }

    fun write(buffer: ByteBuffer, count: Long) {
        if (buffer.remaining() < count)
            throw BufferOverflowException()
        if(file.path.store.writeFile(_number, buffer.array(), buffer.position, count) != count)
            throw IOException("Couldn't write $count bytes to file.")
        _position += count.toULong()
    }

    fun tell(): Long{
        val position = file.path.store.tellFile(_number)
        if (position.toULong() != _position)
            throw SyncFailedException("File descriptor out of sync with physical cursor.")
        return position
    }

    fun seek(position: Long, whence: Seek): Long {
        _position = file.path.store.seekFile(_number, position, whence).toULong()
        return _position.toLong()
    }

    override fun close(){
        file.path.store.closeFile(_number)
        _number = 0
    }

    enum class Seek{
        SET, CUR, END
    }

}*/