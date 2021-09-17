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
package angelos.nio

import kotlin.jvm.JvmStatic

@ExperimentalUnsignedTypes
class ByteBuffer internal constructor(
    array: UByteArray,
    capacity: Int = 0,
    position: Int = 0,
    direct: Boolean = false,
) : Any(), Comparable<ByteBuffer> {
    val direct: Boolean = direct

    private var _capacity: Int
    private var _position: Int = 0

    private var _endian: ByteOrder = ByteOrder.BIG_ENDIAN
    private var _reverse: Boolean = ByteOrder.BIG_ENDIAN != nativeEndianness

    private val _array: UByteArray = array

    val capacity: Int
        get() = _capacity

    var position: Int
        get() = _position
        set(value) {
            if ((value < 0) || (value > _capacity))
                throw IllegalArgumentException()

            _position = value
        }

    var order: ByteOrder
        get() = _endian
        set(value) {
            _endian = value
            _reverse = _endian != nativeEndianness
        }

    init {
        if (capacity < 0)
            throw IllegalArgumentException()

        _capacity = capacity
        this.position = position
    }

    constructor(capacity: Int) : this(UByteArray(size = capacity), capacity, 0, direct = true)

    companion object {
        @JvmStatic
        private inline fun checkArraySize(limit: Int, offset: Int, size: Int) {
            if (limit < (size + offset))
                throw IndexOutOfBoundsException()
        }

        @JvmStatic
        fun allocateDirect(capacity: Int): ByteBuffer {
            return ByteBuffer(capacity)
        }

        @JvmStatic
        fun allocate(capacity: Int): ByteBuffer {
            return wrap(UByteArray(size = capacity), 0, capacity)
        }

        @JvmStatic
        fun wrap(array: UByteArray, offset: Int = 0, length: Int = array.size): ByteBuffer {
            return ByteBuffer(array, array.size, offset)
        }

        private val nativeEndianness: ByteOrder = ByteOrder.nativeOrder()
    }

    fun hasRemaining(): Boolean {
        return remaining() > 0
    }

    fun remaining(): Int {
        return _capacity - _position
    }

    fun rewind() {
        _position = 0
    }

    private inline fun checkForUnderflow(length: Int = 0) {
        if (length == 0 && (!hasRemaining()))
            throw BufferUnderflowException()
        if (remaining() < length)
            throw BufferUnderflowException()
    }

    private inline fun checkForOverflow(length: Int = 0) {
        if (length == 0 && (!hasRemaining()))
            throw BufferOverflowException()
        if (remaining() < length)
            throw BufferOverflowException()
    }

    private inline fun checkIndex(index: Int) {
        if (index < 0 || index >= _capacity)
            throw IndexOutOfBoundsException()
    }

    fun read(dst: UByteArray, offset: Int = 0, length: Int = dst.size) {
        checkArraySize(dst.size, offset, length)
        checkForUnderflow(length)

        _array.copyInto(dst, offset, _position, _position + length)
    }

    fun write(src: ByteBuffer) {
        if (src == this)
            throw IllegalArgumentException()

        checkForOverflow(src.remaining())

        if (src.remaining() > 0) {
            src._array.copyInto(_array, _position, src._position, _position + src.remaining())
        }
    }

    fun write(src: UByteArray, offset: Int = 0, length: Int = src.size) {
        checkArraySize(src.size, offset, length)
        checkForOverflow(length)

        src.copyInto(_array, _position, offset, offset + length)
    }

    fun hasArray(): Boolean = true

    fun array(): UByteArray = _array

    fun arrayOffset(): Int = _position

    override fun hashCode(): Int = _array.contentHashCode()

    override fun equals(other: Any?): Boolean = other is ByteBuffer && compareTo(other) == 0 && order == other.order

    override fun compareTo(other: ByteBuffer): Int = hashCode() - other.hashCode()

    fun read(): UByte {
        checkArraySize(_capacity, _position, 1)
        val b: UByte = _array[_position]
        _position++
        return b
    }

    fun write(b: UByte) {
        checkArraySize(_capacity, _position, 1)
        _array[_position] = b
        _position++
    }

    fun read(index: Int): UByte {
        checkArraySize(_capacity, index, 1)
        return _array[index]
    }

    fun write(index: Int, b: UByte) {
        checkArraySize(_capacity, index, 1)
        _array[index] = b
    }

    fun slice(range: IntRange): ByteBuffer {
        val array = _array.copyOfRange(range.first, range.last)
        return ByteBuffer(array, array.size, direct = direct)
    }

    fun duplicate(): ByteBuffer = ByteBuffer(_array.copyOf())

    fun readChar(): Char {
        checkArraySize(_capacity, _position, 2)
        var value: Int
        if (_reverse) {
            value = _array[_position + 0].toInt()
            value += (_array[_position + 1].toInt() shl 8)
        } else {
            value = _array[_position + 1].toInt()
            value += (_array[_position + 0].toInt() shl 8)
        }
        _position += 2
        return value.toChar()
    }

    fun writeChar(value: Char) {
        checkArraySize(_capacity, _position, 2)
        if (_reverse) {
            _array[_position + 0] = (value.code and 0xFF).toUByte()
            _array[_position + 1] = ((value.code ushr 8) and 0xFF).toUByte()
        } else {
            _array[_position + 1] = (value.code and 0xFF).toUByte()
            _array[_position + 0] = ((value.code ushr 8) and 0xFF).toUByte()
        }

        _position += 2
    }

    fun readShort(): Short {
        checkArraySize(_capacity, _position, 2)
        var value: Int
        if (_reverse) {
            value = _array[_position + 0].toInt()
            value += (_array[_position + 1].toInt() shl 8)
        } else {
            value = _array[_position + 1].toInt()
            value += (_array[_position + 0].toInt() shl 8)
        }
        _position += 2
        return value.toShort()
    }

    fun writeShort(value: Short) {
        checkArraySize(_capacity, _position, 2)
        if (_reverse) {
            _array[_position + 0] = (value.toInt() and 0xFF).toUByte()
            _array[_position + 1] = ((value.toInt() ushr 8) and 0xFF).toUByte()
        } else {
            _array[_position + 1] = (value.toInt() and 0xFF).toUByte()
            _array[_position + 0] = ((value.toInt() ushr 8) and 0xFF).toUByte()
        }
        _position += 2
    }

    fun readUShort(): UShort {
        checkArraySize(_capacity, _position, 2)
        var value: Int
        if (_reverse) {
            value = _array[_position + 0].toInt()
            value += (_array[_position + 1].toInt() shl 8)
        } else {
            value = _array[_position + 1].toInt()
            value += (_array[_position + 0].toInt() shl 8)
        }
        _position += 2
        return value.toUShort()
    }

    fun writeUShort(value: UShort) {
        checkArraySize(_capacity, _position, 2)
        if (_reverse) {
            _array[_position + 0] = (value.toInt() and 0xFF).toUByte()
            _array[_position + 1] = ((value.toInt() ushr 8) and 0xFF).toUByte()
        } else {
            _array[_position + 1] = (value.toInt() and 0xFF).toUByte()
            _array[_position + 0] = ((value.toInt() ushr 8) and 0xFF).toUByte()
        }
        _position += 2
    }

    fun readInt(): Int {
        checkArraySize(_capacity, _position, 4)
        var value: Int
        if (_reverse) {
            value = _array[_position + 0].toInt()
            value += (_array[_position + 1].toInt() shl 8)
            value += (_array[_position + 2].toInt() shl 16)
            value += (_array[_position + 3].toInt() shl 24)
        } else {
            value = _array[_position + 3].toInt()
            value += (_array[_position + 2].toInt() shl 8)
            value += (_array[_position + 1].toInt() shl 16)
            value += (_array[_position + 0].toInt() shl 24)
        }
        _position += 4
        return value
    }

    fun writeInt(value: Int) {
        checkArraySize(_capacity, _position, 4)
        if (_reverse) {
            _array[_position + 0] = (value and 0xFF).toUByte()
            _array[_position + 1] = ((value ushr 8) and 0xFF).toUByte()
            _array[_position + 2] = ((value ushr 16) and 0xFF).toUByte()
            _array[_position + 3] = ((value ushr 24) and 0xFF).toUByte()
        } else {
            _array[_position + 3] = (value and 0xFF).toUByte()
            _array[_position + 2] = ((value ushr 8) and 0xFF).toUByte()
            _array[_position + 1] = ((value ushr 16) and 0xFF).toUByte()
            _array[_position + 0] = ((value ushr 24) and 0xFF).toUByte()
        }
        _position += 4
    }

    fun readUInt(): UInt {
        checkArraySize(_capacity, _position, 4)
        var value: UInt
        if (_reverse) {
            value = _array[_position + 3].toUInt()
            value += (_array[_position + 2].toUInt() shl 8)
            value += (_array[_position + 1].toUInt() shl 16)
            value += (_array[_position + 0].toUInt() shl 24)
        } else {
            value = _array[_position + 3].toUInt()
            value += (_array[_position + 2].toUInt() shl 8)
            value += (_array[_position + 1].toUInt() shl 16)
            value += (_array[_position + 0].toUInt() shl 24)
        }
        _position += 4
        return value
    }

    fun writeUInt(value: UInt) {
        checkArraySize(_capacity, _position, 4)
        val data: Int = value.toInt()
        if (_reverse) {
            _array[_position + 0] = (data and 0xFF).toUByte()
            _array[_position + 1] = ((data ushr 8) and 0xFF).toUByte()
            _array[_position + 2] = ((data ushr 16) and 0xFF).toUByte()
            _array[_position + 3] = ((data ushr 24) and 0xFF).toUByte()
        } else {
            _array[_position + 3] = (data and 0xFF).toUByte()
            _array[_position + 2] = ((data ushr 8) and 0xFF).toUByte()
            _array[_position + 1] = ((data ushr 16) and 0xFF).toUByte()
            _array[_position + 0] = ((data ushr 24) and 0xFF).toUByte()
        }
        _position += 4
    }

    fun readLong(): Long {
        checkArraySize(_capacity, _position, 8)
        var value: Long
        if (_reverse) {
            value = _array[_position + 0].toLong()
            value += (_array[_position + 1].toLong() shl 8)
            value += (_array[_position + 2].toLong() shl 16)
            value += (_array[_position + 3].toLong() shl 24)
            value += (_array[_position + 4].toLong() shl 32)
            value += (_array[_position + 5].toLong() shl 40)
            value += (_array[_position + 6].toLong() shl 48)
            value += (_array[_position + 7].toLong() shl 56)
        } else {
            value = _array[_position + 7].toLong()
            value += (_array[_position + 6].toLong() shl 8)
            value += (_array[_position + 5].toLong() shl 16)
            value += (_array[_position + 4].toLong() shl 24)
            value += (_array[_position + 3].toLong() shl 32)
            value += (_array[_position + 2].toLong() shl 40)
            value += (_array[_position + 1].toLong() shl 48)
            value += (_array[_position + 0].toLong() shl 56)
        }
        _position += 8
        return value
    }

    fun writeLong(value: Long) {
        checkArraySize(_capacity, _position, 8)
        if (_reverse) {
            _array[_position + 0] = (value and 0xFF).toUByte()
            _array[_position + 1] = ((value ushr 8) and 0xFF).toUByte()
            _array[_position + 2] = ((value ushr 16) and 0xFF).toUByte()
            _array[_position + 3] = ((value ushr 24) and 0xFF).toUByte()
            _array[_position + 4] = ((value ushr 32) and 0xFF).toUByte()
            _array[_position + 5] = ((value ushr 40) and 0xFF).toUByte()
            _array[_position + 6] = ((value ushr 48) and 0xFF).toUByte()
            _array[_position + 7] = ((value ushr 56) and 0xFF).toUByte()
        } else {
            _array[_position + 7] = (value and 0xFF).toUByte()
            _array[_position + 6] = ((value ushr 8) and 0xFF).toUByte()
            _array[_position + 5] = ((value ushr 16) and 0xFF).toUByte()
            _array[_position + 4] = ((value ushr 24) and 0xFF).toUByte()
            _array[_position + 3] = ((value ushr 32) and 0xFF).toUByte()
            _array[_position + 2] = ((value ushr 40) and 0xFF).toUByte()
            _array[_position + 1] = ((value ushr 48) and 0xFF).toUByte()
            _array[_position + 0] = ((value ushr 56) and 0xFF).toUByte()
        }
        _position += 8
    }

    fun readULong(): ULong {
        checkArraySize(_capacity, _position, 8)
        var value: ULong
        if (_reverse) {
            value = _array[_position + 0].toULong()
            value += (_array[_position + 1].toULong() shl 8)
            value += (_array[_position + 2].toULong() shl 16)
            value += (_array[_position + 3].toULong() shl 24)
            value += (_array[_position + 4].toULong() shl 32)
            value += (_array[_position + 5].toULong() shl 40)
            value += (_array[_position + 6].toULong() shl 48)
            value += (_array[_position + 7].toULong() shl 56)
        } else {
            value = _array[_position + 7].toULong()
            value += (_array[_position + 6].toULong() shl 8)
            value += (_array[_position + 5].toULong() shl 16)
            value += (_array[_position + 4].toULong() shl 24)
            value += (_array[_position + 3].toULong() shl 32)
            value += (_array[_position + 2].toULong() shl 40)
            value += (_array[_position + 1].toULong() shl 48)
            value += (_array[_position + 0].toULong() shl 56)
        }
        _position += 8
        return value
    }

    fun writeULong(value: ULong) {
        checkArraySize(_capacity, _position, 8)
        val data: Long = value.toLong()
        if (_reverse) {
            _array[_position + 0] = (data and 0xFF).toUByte()
            _array[_position + 1] = ((data ushr 8) and 0xFF).toUByte()
            _array[_position + 2] = ((data ushr 16) and 0xFF).toUByte()
            _array[_position + 3] = ((data ushr 24) and 0xFF).toUByte()
            _array[_position + 4] = ((data ushr 32) and 0xFF).toUByte()
            _array[_position + 5] = ((data ushr 40) and 0xFF).toUByte()
            _array[_position + 6] = ((data ushr 48) and 0xFF).toUByte()
            _array[_position + 7] = ((data ushr 56) and 0xFF).toUByte()
        } else {
            _array[_position + 7] = (data and 0xFF).toUByte()
            _array[_position + 6] = ((data ushr 8) and 0xFF).toUByte()
            _array[_position + 5] = ((data ushr 16) and 0xFF).toUByte()
            _array[_position + 4] = ((data ushr 24) and 0xFF).toUByte()
            _array[_position + 3] = ((data ushr 32) and 0xFF).toUByte()
            _array[_position + 2] = ((data ushr 40) and 0xFF).toUByte()
            _array[_position + 1] = ((data ushr 48) and 0xFF).toUByte()
            _array[_position + 0] = ((data ushr 56) and 0xFF).toUByte()
        }
        _position += 8
    }

    fun readFloat(): Float {
        checkArraySize(_capacity, _position, 4)
        var value: Int
        if (_reverse) {
            value = _array[_position + 0].toInt()
            value += (_array[_position + 1].toInt() shl 8)
            value += (_array[_position + 2].toInt() shl 16)
            value += (_array[_position + 3].toInt() shl 24)
        } else {
            value = _array[_position + 3].toInt()
            value += (_array[_position + 2].toInt() shl 8)
            value += (_array[_position + 1].toInt() shl 16)
            value += (_array[_position + 0].toInt() shl 24)
        }
        _position += 4
        return Float.fromBits(value)
    }

    fun writeFloat(value: Float) {
        checkArraySize(_capacity, _position, 4)
        val data: Int = value.toRawBits()
        if (_reverse) {
            _array[_position + 0] = (data and 0xFF).toUByte()
            _array[_position + 1] = ((data ushr 8) and 0xFF).toUByte()
            _array[_position + 2] = ((data ushr 16) and 0xFF).toUByte()
            _array[_position + 3] = ((data ushr 24) and 0xFF).toUByte()
        } else {
            _array[_position + 3] = (data and 0xFF).toUByte()
            _array[_position + 2] = ((data ushr 8) and 0xFF).toUByte()
            _array[_position + 1] = ((data ushr 16) and 0xFF).toUByte()
            _array[_position + 0] = ((data ushr 24) and 0xFF).toUByte()
        }
        _position += 4
    }

    fun readDouble(): Double {
        checkArraySize(_capacity, _position, 8)
        var value: Long
        if (_reverse) {
            value = _array[_position + 0].toLong()
            value += (_array[_position + 1].toLong() shl 8)
            value += (_array[_position + 2].toLong() shl 16)
            value += (_array[_position + 3].toLong() shl 24)
            value += (_array[_position + 4].toLong() shl 32)
            value += (_array[_position + 5].toLong() shl 40)
            value += (_array[_position + 6].toLong() shl 48)
            value += (_array[_position + 7].toLong() shl 56)
        } else {
            value = _array[_position + 7].toLong()
            value += (_array[_position + 6].toLong() shl 8)
            value += (_array[_position + 5].toLong() shl 16)
            value += (_array[_position + 4].toLong() shl 24)
            value += (_array[_position + 3].toLong() shl 32)
            value += (_array[_position + 2].toLong() shl 40)
            value += (_array[_position + 1].toLong() shl 48)
            value += (_array[_position + 0].toLong() shl 56)
        }
        _position += 8
        return Double.fromBits(value)
    }

    fun writeDouble(value: Double) {
        checkArraySize(_capacity, _position, 8)
        val data: Long = value.toRawBits()
        if (_reverse) {
            _array[_position + 0] = (data and 0xFF).toUByte()
            _array[_position + 1] = ((data ushr 8) and 0xFF).toUByte()
            _array[_position + 2] = ((data ushr 16) and 0xFF).toUByte()
            _array[_position + 3] = ((data ushr 24) and 0xFF).toUByte()
            _array[_position + 4] = ((data ushr 32) and 0xFF).toUByte()
            _array[_position + 5] = ((data ushr 40) and 0xFF).toUByte()
            _array[_position + 6] = ((data ushr 48) and 0xFF).toUByte()
            _array[_position + 7] = ((data ushr 56) and 0xFF).toUByte()
        } else {
            _array[_position + 7] = (data and 0xFF).toUByte()
            _array[_position + 6] = ((data ushr 8) and 0xFF).toUByte()
            _array[_position + 5] = ((data ushr 16) and 0xFF).toUByte()
            _array[_position + 4] = ((data ushr 24) and 0xFF).toUByte()
            _array[_position + 3] = ((data ushr 32) and 0xFF).toUByte()
            _array[_position + 2] = ((data ushr 40) and 0xFF).toUByte()
            _array[_position + 1] = ((data ushr 48) and 0xFF).toUByte()
            _array[_position + 0] = ((data ushr 56) and 0xFF).toUByte()
        }
        _position += 8
    }

    override fun toString(): String {
        return "ByteBuffer" + "pos[=" + position + "cap=" + capacity + "]"
    }
}
