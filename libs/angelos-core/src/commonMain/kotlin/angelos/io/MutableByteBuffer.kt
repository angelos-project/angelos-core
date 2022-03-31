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
package angelos.io

import kotlin.math.absoluteValue
import kotlin.math.min

abstract class MutableByteBuffer internal constructor(
    capacity: Int,
    limit: Int,
    position: Int,
    mark: Int,
    endianness: Endianness
) : ByteBuffer(capacity, limit, mark, endianness) {
    protected var _position: Int
    val position: Int
        get() = _position

    init {
        _position = min(limit.absoluteValue, position.absoluteValue)
        _mark = min(position.absoluteValue, mark.absoluteValue)
    }

    override fun rewind() {
        _position = 0
        _mark = 0
    }

    override fun remaining(): Int = _capacity - _position

    private inline fun enoughSpace(size: Int) {
        if (_limit - _position < size)
            throw ByteBufferException("End of buffer.")
    }

    override fun enoughData(size: Int) {
        if (_position - _mark < size)
            throw ByteBufferException("End of data.")
    }

    private inline fun forwardPosition(length: Int) {
        _position += length
    }

    fun setByte(value: Byte) {
        enoughSpace(1)
        writeByte(value)
        forwardPosition(1)
    }

    fun setUByte(value: UByte) {
        enoughSpace(1)
        writeUByte(value)
        forwardPosition(1)
    }

    fun setChar(value: Char) {
        enoughSpace(2)
        writeChar(value)
        forwardPosition(2)
    }

    fun setShort(value: Short) {
        enoughSpace(2)
        writeShort(value)
        forwardPosition(2)
    }

    fun setUShort(value: UShort) {
        enoughSpace(2)
        writeUShort(value)
        forwardPosition(2)
    }

    fun setInt(value: Int) {
        enoughSpace(4)
        writeInt(value)
        forwardPosition(4)
    }

    fun setUInt(value: UInt) {
        enoughSpace(4)
        writeUInt(value)
        forwardPosition(4)
    }

    fun setLong(value: Long) {
        enoughSpace(8)
        writeLong(value)
        forwardPosition(8)
    }

    fun setULong(value: ULong) {
        enoughSpace(8)
        writeULong(value)
        forwardPosition(8)
    }

    fun setFloat(value: Float) {
        enoughSpace(4)
        writeFloat(value.toRawBits())
        forwardPosition(4)
    }

    fun setDouble(value: Double) {
        enoughSpace(8)
        writeDouble(value.toRawBits())
        forwardPosition(8)
    }

    internal abstract fun save(value: UByte, offset: Int)

    internal open fun writeByte(value: Byte) = save((value.toInt() and 0xFF).toUByte(), 0)
    internal open fun writeUByte(value: UByte) = save(value, 0)

    internal open fun writeChar(value: Char) = when (_reverse) {
        true -> {
            save((value.code and 0xFF).toUByte(), 0)
            save(((value.code ushr 8) and 0xFF).toUByte(), 1)
        }
        false -> {
            save((value.code and 0xFF).toUByte(), 1)
            save(((value.code ushr 8) and 0xFF).toUByte(), 0)
        }
    }

    internal open fun writeShort(value: Short) = when (_reverse) {
        true -> {
            save((value.toInt() and 0xFF).toUByte(), 0)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
        }
        false -> {
            save((value.toInt() and 0xFF).toUByte(), 1)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 0)
        }
    }

    internal open fun writeUShort(value: UShort) = when (_reverse) {
        true -> {
            save((value.toInt() and 0xFF).toUByte(), 0)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
        }
        false -> {
            save((value.toInt() and 0xFF).toUByte(), 1)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 0)
        }
    }

    internal open fun writeInt(value: Int) = when (_reverse) {
        true -> {
            save((value and 0xFF).toUByte(), 0)
            save(((value ushr 8) and 0xFF).toUByte(), 1)
            save(((value ushr 16) and 0xFF).toUByte(), 2)
            save(((value ushr 24) and 0xFF).toUByte(), 3)
        }
        false -> {
            save((value and 0xFF).toUByte(), 3)
            save(((value ushr 8) and 0xFF).toUByte(), 2)
            save(((value ushr 16) and 0xFF).toUByte(), 1)
            save(((value ushr 24) and 0xFF).toUByte(), 0)
        }
    }

    internal open fun writeUInt(value: UInt) = when (_reverse) {
        true -> {
            save((value.toInt() and 0xFF).toUByte(), 0)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
            save(((value.toInt() ushr 16) and 0xFF).toUByte(), 2)
            save(((value.toInt() ushr 24) and 0xFF).toUByte(), 3)
        }
        false -> {
            save((value.toInt() and 0xFF).toUByte(), 3)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 2)
            save(((value.toInt() ushr 16) and 0xFF).toUByte(), 1)
            save(((value.toInt() ushr 24) and 0xFF).toUByte(), 0)
        }
    }

    internal open fun writeLong(value: Long) = when (_reverse) {
        true -> {
            save((value and 0xFF).toUByte(), 0)
            save(((value ushr 8) and 0xFF).toUByte(), 1)
            save(((value ushr 16) and 0xFF).toUByte(), 2)
            save(((value ushr 24) and 0xFF).toUByte(), 3)
            save(((value ushr 32) and 0xFF).toUByte(), 4)
            save(((value ushr 40) and 0xFF).toUByte(), 5)
            save(((value ushr 48) and 0xFF).toUByte(), 6)
            save(((value ushr 56) and 0xFF).toUByte(), 7)
        }
        false -> {
            save((value and 0xFF).toUByte(), 7)
            save(((value ushr 8) and 0xFF).toUByte(), 6)
            save(((value ushr 16) and 0xFF).toUByte(), 5)
            save(((value ushr 24) and 0xFF).toUByte(), 4)
            save(((value ushr 32) and 0xFF).toUByte(), 3)
            save(((value ushr 40) and 0xFF).toUByte(), 2)
            save(((value ushr 48) and 0xFF).toUByte(), 1)
            save(((value ushr 56) and 0xFF).toUByte(), 0)
        }
    }

    internal open fun writeULong(value: ULong) = when (_reverse) {
        true -> {
            save((value.toLong() and 0xFF).toUByte(), 0)
            save(((value.toLong() ushr 8) and 0xFF).toUByte(), 1)
            save(((value.toLong() ushr 16) and 0xFF).toUByte(), 2)
            save(((value.toLong() ushr 24) and 0xFF).toUByte(), 3)
            save(((value.toLong() ushr 32) and 0xFF).toUByte(), 4)
            save(((value.toLong() ushr 40) and 0xFF).toUByte(), 5)
            save(((value.toLong() ushr 48) and 0xFF).toUByte(), 6)
            save(((value.toLong() ushr 56) and 0xFF).toUByte(), 7)
        }
        false -> {
            save((value.toLong() and 0xFF).toUByte(), 7)
            save(((value.toLong() ushr 8) and 0xFF).toUByte(), 6)
            save(((value.toLong() ushr 16) and 0xFF).toUByte(), 5)
            save(((value.toLong() ushr 24) and 0xFF).toUByte(), 4)
            save(((value.toLong() ushr 32) and 0xFF).toUByte(), 3)
            save(((value.toLong() ushr 40) and 0xFF).toUByte(), 2)
            save(((value.toLong() ushr 48) and 0xFF).toUByte(), 1)
            save(((value.toLong() ushr 56) and 0xFF).toUByte(), 0)
        }
    }

    internal open fun writeFloat(value: Int) = when (_reverse) {
        true -> {
            save((value and 0xFF).toUByte(), 0)
            save(((value ushr 8) and 0xFF).toUByte(), 1)
            save(((value ushr 16) and 0xFF).toUByte(), 2)
            save(((value ushr 24) and 0xFF).toUByte(), 3)
        }
        false -> {
            save((value and 0xFF).toUByte(), 3)
            save(((value ushr 8) and 0xFF).toUByte(), 2)
            save(((value ushr 16) and 0xFF).toUByte(), 1)
            save(((value ushr 24) and 0xFF).toUByte(), 0)
        }
    }

    internal open fun writeDouble(value: Long) = when (_reverse) {
        true -> {
            save((value and 0xFF).toUByte(), 0)
            save(((value ushr 8) and 0xFF).toUByte(), 1)
            save(((value ushr 16) and 0xFF).toUByte(), 2)
            save(((value ushr 24) and 0xFF).toUByte(), 3)
            save(((value ushr 32) and 0xFF).toUByte(), 4)
            save(((value ushr 40) and 0xFF).toUByte(), 5)
            save(((value ushr 48) and 0xFF).toUByte(), 6)
            save(((value ushr 56) and 0xFF).toUByte(), 7)
        }
        false -> {
            save((value and 0xFF).toUByte(), 7)
            save(((value ushr 8) and 0xFF).toUByte(), 6)
            save(((value ushr 16) and 0xFF).toUByte(), 5)
            save(((value ushr 24) and 0xFF).toUByte(), 4)
            save(((value ushr 32) and 0xFF).toUByte(), 3)
            save(((value ushr 40) and 0xFF).toUByte(), 2)
            save(((value ushr 48) and 0xFF).toUByte(), 1)
            save(((value ushr 56) and 0xFF).toUByte(), 0)
        }
    }
}