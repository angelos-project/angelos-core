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
import kotlin.math.ceil
import kotlin.math.min

abstract class AbstractMutableByteBuffer internal constructor(
    capacity: Int,
    limit: Int,
    position: Int,
    mark: Int,
    endianness: Endianness
) : AbstractByteBuffer(capacity, limit, mark, endianness), MutableByteBuffer {
    protected var _position: Int
    override val position: Int
        get() = _position

    init {
        _position = min(limit.absoluteValue, position.absoluteValue)
        _mark = min(position.absoluteValue, mark.absoluteValue)
    }

    constructor(array: ByteArray, limit: Int, endianness: Endianness = ByteBuffer.nativeEndianness) : this(array.size, limit, 0, 0, endianness) {
        _array = array
    }

    constructor(capacity: Int, endianness: Endianness = ByteBuffer.nativeEndianness) : this(ByteArray(ceil((capacity.absoluteValue / 8).toFloat()).toInt() * 8), capacity, endianness)


    override fun rewind() {
        _position = 0
        _mark = 0
    }

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

    override fun putChar(value: Char) {
        enoughSpace(2)
        writeChar(value)
        forwardPosition(2)
    }

    override fun putShort(value: Short) {
        enoughSpace(2)
        writeShort(value)
        forwardPosition(2)
    }

    override fun putUShort(value: UShort) {
        enoughSpace(2)
        writeUShort(value)
        forwardPosition(2)
    }

    override fun putInt(value: Int) {
        enoughSpace(4)
        writeInt(value)
        forwardPosition(4)
    }

    override fun putUInt(value: UInt) {
        enoughSpace(4)
        writeUInt(value)
        forwardPosition(4)
    }

    override fun putLong(value: Long) {
        enoughSpace(8)
        writeLong(value)
        forwardPosition(8)
    }

    override fun putULong(value: ULong) {
        enoughSpace(8)
        writeULong(value)
        forwardPosition(8)
    }

    override fun putFloat(value: Float) {
        enoughSpace(4)
        writeFloat(value.toRawBits())
        forwardPosition(4)
    }

    override fun putDouble(value: Double) {
        enoughSpace(8)
        writeDouble(value.toRawBits())
        forwardPosition(8)
    }

    internal abstract fun save(value: UByte, offset: Int)

    private inline fun writeChar(value: Char) = when (_reverse) {
        true -> {
            save((value.code and 0xFF).toUByte(), 0)
            save(((value.code ushr 8) and 0xFF).toUByte(), 1)
        }
        false -> {
            save((value.code and 0xFF).toUByte(), 1)
            save(((value.code ushr 8) and 0xFF).toUByte(), 0)
        }
    }

    private inline fun writeShort(value: Short) = when (_reverse) {
        true -> {
            save((value.toInt() and 0xFF).toUByte(), 0)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
        }
        false -> {
            save((value.toInt() and 0xFF).toUByte(), 1)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 0)
        }
    }

    private inline fun writeUShort(value: UShort) = when (_reverse) {
        true -> {
            save((value.toInt() and 0xFF).toUByte(), 0)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
        }
        false -> {
            save((value.toInt() and 0xFF).toUByte(), 1)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 0)
        }
    }

    private inline fun writeInt(value: Int) = when (_reverse) {
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

    private inline fun writeUInt(value: UInt) = when (_reverse) {
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

    private inline fun writeLong(value: Long) = when (_reverse) {
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

    private inline fun writeULong(value: ULong) = when (_reverse) {
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

    private inline fun writeFloat(value: Int) = when (_reverse) {
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

    private inline fun writeDouble(value: Long) = when (_reverse) {
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