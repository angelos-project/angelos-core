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

import angelos.nio.BufferOverflowException
import angelos.nio.BufferUnderflowException
import angelos.nio.ByteBuffer

class FileDescriptor internal constructor(
    internal val file: File,
    val option: File.OpenOption,
    private var _number: Int,
) : Closable {
    private var _position: ULong = 0u

    val position: ULong
        get() = _position

    val number: Int
        get() = _number

    fun read(buffer: ByteBuffer, count: ULong) {
        if (buffer.remaining().toULong() < count)
            throw BufferUnderflowException()
        if(readFile(_number, buffer.array(), buffer.position, count) != count)
            throw IOException("Couldn't read $count bytes from file.")
        _position += count
    }

    fun write(buffer: ByteBuffer, count: ULong) {
        if (buffer.remaining().toULong() < count)
            throw BufferOverflowException()
        if(writeFile(_number, buffer.array(), buffer.position, count) != count)
            throw IOException("Couldn't write $count bytes to file.")
        _position += count
    }

    fun tell(): ULong{
        val position = tellFile(_number)
        if (position != _position)
            throw SyncFailedException("File descriptor out of sync with physical cursor.")
        return position
    }

    fun seek(position: Long, whence: Seek): ULong{
        _position = seekFile(_number, position, whence)
        return _position
    }

    override fun close(){
        closeFile(_number)
        _number = 0
    }

    enum class Seek{
        SET, CUR, END
    }

}


@ExperimentalUnsignedTypes
internal expect inline fun readFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong
@ExperimentalUnsignedTypes
internal expect inline fun writeFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong
internal expect inline fun tellFile(number: Int): ULong
internal expect inline fun seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): ULong
internal expect inline fun closeFile(number: Int): Boolean