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
import kotlin.math.min

@ExperimentalUnsignedTypes
class ByteBuffer internal constructor(
    array: UByteArray,
    capacity: Int = 0,
    limit: Int = 0,
    position: Int = 0,
    mark: Int = -1,
    readOnly: Boolean = false,
    direct: Boolean = false,
) : Any(), Comparable<ByteBuffer> {
    val readOnly: Boolean = readOnly
    val direct: Boolean = direct

    private var _capacity: Int
    private var _limit: Int = 0
    private var _position: Int = 0
    private var _mark: Int = -1

    private var order: ByteOrder = ByteOrder.BIG_ENDIAN

    private val _array: UByteArray = array

    val capacity: Int
        get() = _capacity

    var limit: Int
        get() = _limit
        set(value) {
            if ((value < 0) || (value > _capacity))
                throw IllegalArgumentException()

            if (value < _mark)
                _mark = -1

            if (_position > value)
                _position = value

            _limit = value
        }

    var position: Int
        get() = _position
        set(value) {
            if ((value < 0) || (value > _limit))
                throw IllegalArgumentException()

            if (value <= _mark)
                _mark = -1

            _position = value
        }

    init {
        if (capacity < 0)
            throw IllegalArgumentException()

        _capacity = capacity
        this.limit = limit
        this.position = position

        if (mark >= 0) {
            if (mark > _position)
                throw IllegalArgumentException()

            _mark = mark
        }
    }

    constructor(capacity: Int) : this(UByteArray(size = capacity), capacity, capacity, direct=true)

    companion object {
        @JvmStatic
        private inline fun checkArraySize(limit: Int, offset: Int, size: Int) {
            if ((offset < 0) || (size < 0) || (limit < size + offset))
                throw IndexOutOfBoundsException()
        }

        @JvmStatic
        fun allocateDirect(capacity: Int): ByteBuffer {
            return ByteBuffer.allocate(capacity)
        }

        @JvmStatic
        fun allocate(capacity: Int): ByteBuffer {
            return wrap(UByteArray(size = capacity), 0, capacity)
        }

        @JvmStatic
        fun wrap(array: UByteArray, offset: Int = 0, length: Int = array.size): ByteBuffer {
            return ByteBuffer(array, array.size, offset + length, offset, -1, direct = false)
        }
    }

    fun clear() {
        _limit = _capacity
        _position = 0
        _mark = -1
    }

    fun flip() {
        _limit = _position
        _position = 0
        _mark = -1
    }

    fun hasRemaining(): Boolean {
        return remaining() > 0
    }

    fun mark() {
        _mark = _position
    }

    fun remaining(): Int {
        return _limit - _position
    }

    fun reset() {
        if (_mark == -1)
            throw InvalidMarkException()

        _position = _mark
    }

    fun rewind() {
        _position = 0
        _mark = -1
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
        if (index < 0 || index >= _limit)
            throw IndexOutOfBoundsException()
    }

    private inline fun checkIfReadOnly() { // TODO(Mitigate and remove)
        if (readOnly)
            throw ReadOnlyBufferException()
    }

    private var _arrayOffset: Int = 0 // TODO(Remove or what?)

    fun get(dst: UByteArray, offset: Int = 0, length: Int = dst.size) {
        checkArraySize(dst.size, offset, length)
        checkForUnderflow(length)

        for (i: Int in offset..length)
            dst[i] = get()
    }

    fun set(src: ByteBuffer) {
        if (src == this)
            throw IllegalArgumentException()

        checkForOverflow(src.remaining())

        if (src.remaining() > 0) {
            val toSet: UByteArray = UByteArray(src.remaining())
            src.get(toSet)
            set(toSet)
        }
    }

    fun set(src: UByteArray, offset: Int = 0, length: Int = src.size) {
        checkArraySize(src.size, offset, length)
        checkForOverflow(length)

        for (i: Int in offset..length)
            set(src[i])
    }

    fun hasArray(): Boolean {
        return (_array != null && !readOnly)
    }

    fun array(): UByteArray {
        if (_array == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _array
    }

    fun arrayOffset(): Int {
        if (_array == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _position
    }

    override fun hashCode(): Int {
        var hashCode: Int = get(_position).toInt() + 31
        var multiplier: Int = 1

        for (i: Int in (_position + 1).._limit) {
            multiplier *= 31
            hashCode += (get(i).toInt() + 30) * multiplier
        }
        return hashCode
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is ByteBuffer)
            return compareTo(obj as ByteBuffer) == 0

        return false
    }

    override fun compareTo(other: ByteBuffer): Int {
        val num: Int = min(remaining(), other.remaining())
        var posThis: Int = position
        var posOther: Int = other.position

        for (count: Int in 0..num) {
            val a: UByte = get(posThis++)
            val b: UByte = get(posOther++)

            if (a == b)
                continue

            if (a < b)
                return -1

            return 1
        }

        return remaining() - other.remaining()
    }

    fun get(): UByte {
        checkArraySize(_limit, _position, 1)
        val b: UByte = _array[_position]
        _position++
        return b
    }

    fun set(b: UByte) {
        checkArraySize(_limit, _position, 1)
        _array[_position] = b
        _position++
    }

    fun get(index: Int): UByte {
        checkArraySize(_limit, index, 1)
        return _array[index]
    }

    fun set(index: Int, b: UByte) {
        checkArraySize(_limit, index, 1)
        _array[index] = b
    }

    fun shiftDown(dstOffset: Int, srcOffset: Int, count: Int) {
        for (i: Int in 0..count)
            set(dstOffset + i, get(srcOffset + i))
    }

    fun slice(): ByteBuffer {
        return this // TODO(Implement)
    }

    fun duplicate(): ByteBuffer {
        return this // TODO(Implement)
    }

    fun asReadOnly(): ByteBuffer {
        return this // TODO(Implement)
    }

    private inline fun scanBytes(size: Int): Long {
        checkArraySize(_limit, _position, size)
        var value: Long = 0
        for (i in 0..size)
            value = value or (_array[_position + i].toLong() shl 8 * i)
        _position += size
        return value
    }

    private inline fun printBytes(size: Int, value: Long) {
        checkArraySize(_limit, _position, size)
        val b: Long = value as Long
        for (i in 0..size)
            _array[_position + (size - i)] = ((b ushr 8 * i) and 0xFFFFFFFF).toUByte()
        _position += size
    }

    fun readChar(): Char {
        checkArraySize(_limit, _position, 2)
        var value: Int = _array[_position + 1].toInt()
        value += (_array[_position + 0].toInt() shl 8)
        _position += 2
        return value.toChar()
    }

    fun writeChar(value: Char) {
        checkArraySize(_limit, _position, 2)
        _array[_position + 1] = (value.toInt() and 0xFF).toUByte()
        _array[_position + 0] = ((value.toInt() ushr 8) and 0xFF).toUByte()
        _position += 2
    }

    fun readShort(): Short {
        checkArraySize(_limit, _position, 2)
        var value: Int = _array[_position + 1].toInt()
        value += (_array[_position + 0].toInt() shl 8)
        _position += 2
        return value.toShort()
    }

    fun writeShort(value: Short) {
        checkArraySize(_limit, _position, 2)
        _array[_position + 1] = (value.toInt() and 0xFF).toUByte()
        _array[_position + 0] = ((value.toInt() ushr 8) and 0xFF).toUByte()
        _position += 2
    }

    fun readUShort(): UShort {
        checkArraySize(_limit, _position, 2)
        var value: Int = _array[_position + 1].toInt()
        value += (_array[_position + 0].toInt() shl 8)
        _position += 2
        return value.toUShort()
    }

    fun writeUShort(value: UShort) {
        checkArraySize(_limit, _position, 2)
        _array[_position + 1] = (value.toInt() and 0xFF).toUByte()
        _array[_position + 0] = ((value.toInt() ushr 8) and 0xFF).toUByte()
        _position += 2
    }

    fun readInt(): Int {
        checkArraySize(_limit, _position, 4)
        var value: Int = _array[_position + 3].toInt()
        value += (_array[_position + 2].toInt() shl 8)
        value += (_array[_position + 1].toInt() shl 16)
        value += (_array[_position + 0].toInt() shl 24)
        _position += 4
        return value
    }

    fun writeInt(value: Int) {
        checkArraySize(_limit, _position, 4)
        _array[_position + 3] = (value and 0xFF).toUByte()
        _array[_position + 2] = ((value ushr 8) and 0xFF).toUByte()
        _array[_position + 1] = ((value ushr 16) and 0xFF).toUByte()
        _array[_position + 0] = ((value ushr 24) and 0xFF).toUByte()
        _position += 4
    }

    fun readUInt(): UInt {
        checkArraySize(_limit, _position, 4)
        var value: UInt = _array[_position + 3].toUInt()
        value += (_array[_position + 2].toUInt() shl 8)
        value += (_array[_position + 1].toUInt() shl 16)
        value += (_array[_position + 0].toUInt() shl 24)
        _position += 4
        return value
    }

    fun writeUInt(value: UInt) {
        checkArraySize(_limit, _position, 4)
        val data: Int = value.toInt()
        _array[_position + 3] = (data and 0xFF).toUByte()
        _array[_position + 2] = ((data ushr 8) and 0xFF).toUByte()
        _array[_position + 1] = ((data ushr 16) and 0xFF).toUByte()
        _array[_position + 0] = ((data ushr 24) and 0xFF).toUByte()
        _position += 4
    }

    fun readLong(): Long {
        checkArraySize(_limit, _position, 8)
        var value: Long = _array[_position + 7].toLong()
        value += (_array[_position + 6].toLong() shl 8)
        value += (_array[_position + 5].toLong() shl 16)
        value += (_array[_position + 4].toLong() shl 24)
        value += (_array[_position + 3].toLong() shl 32)
        value += (_array[_position + 2].toLong() shl 40)
        value += (_array[_position + 1].toLong() shl 48)
        value += (_array[_position + 0].toLong() shl 56)
        _position += 8
        return value
    }

    fun writeLong(value: Long) {
        checkArraySize(_limit, _position, 8)
        _array[_position + 7] = (value and 0xFF).toUByte()
        _array[_position + 6] = ((value ushr 8) and 0xFF).toUByte()
        _array[_position + 5] = ((value ushr 16) and 0xFF).toUByte()
        _array[_position + 4] = ((value ushr 24) and 0xFF).toUByte()
        _array[_position + 3] = ((value ushr 32) and 0xFF).toUByte()
        _array[_position + 2] = ((value ushr 40) and 0xFF).toUByte()
        _array[_position + 1] = ((value ushr 48) and 0xFF).toUByte()
        _array[_position + 0] = ((value ushr 56) and 0xFF).toUByte()
        _position += 8
    }

    fun readULong(): ULong {
        checkArraySize(_limit, _position, 8)
        var value: ULong = _array[_position + 7].toULong()
        value += (_array[_position + 6].toULong() shl 8)
        value += (_array[_position + 5].toULong() shl 16)
        value += (_array[_position + 4].toULong() shl 24)
        value += (_array[_position + 3].toULong() shl 32)
        value += (_array[_position + 2].toULong() shl 40)
        value += (_array[_position + 1].toULong() shl 48)
        value += (_array[_position + 0].toULong() shl 56)
        _position += 8
        return value
    }

    fun writeULong(value: ULong) {
        checkArraySize(_limit, _position, 8)
        val data: Long = value.toLong()
        _array[_position + 7] = (data and 0xFF).toUByte()
        _array[_position + 6] = ((data ushr 8) and 0xFF).toUByte()
        _array[_position + 5] = ((data ushr 16) and 0xFF).toUByte()
        _array[_position + 4] = ((data ushr 24) and 0xFF).toUByte()
        _array[_position + 3] = ((data ushr 32) and 0xFF).toUByte()
        _array[_position + 2] = ((data ushr 40) and 0xFF).toUByte()
        _array[_position + 1] = ((data ushr 48) and 0xFF).toUByte()
        _array[_position + 0] = ((data ushr 56) and 0xFF).toUByte()
        _position += 8
    }

    fun readFloat(): Float {
        checkArraySize(_limit, _position, 4)
        var value: Int = _array[_position + 3].toInt()
        value += (_array[_position + 2].toInt() shl 8)
        value += (_array[_position + 1].toInt() shl 16)
        value += (_array[_position + 0].toInt() shl 24)
        _position += 4
        return Float.fromBits(value)
    }

    fun writeFloat(value: Float) {
        val data: Int = value.toRawBits()
        checkArraySize(_limit, _position, 4)
        _array[_position + 3] = (data and 0xFF).toUByte()
        _array[_position + 2] = ((data ushr 8) and 0xFF).toUByte()
        _array[_position + 1] = ((data ushr 16) and 0xFF).toUByte()
        _array[_position + 0] = ((data ushr 24) and 0xFF).toUByte()
        _position += 4
    }

    fun readDouble(): Double {
        checkArraySize(_limit, _position, 8)
        var value: Long = _array[_position + 7].toLong()
        value += (_array[_position + 6].toLong() shl 8)
        value += (_array[_position + 5].toLong() shl 16)
        value += (_array[_position + 4].toLong() shl 24)
        value += (_array[_position + 3].toLong() shl 32)
        value += (_array[_position + 2].toLong() shl 40)
        value += (_array[_position + 1].toLong() shl 48)
        value += (_array[_position + 0].toLong() shl 56)
        _position += 8
        return Double.fromBits(value)
    }

    fun writeDouble(value: Double) {
        val data: Long = value.toRawBits()
        checkArraySize(_limit, _position, 8)
        _array[_position + 7] = (data and 0xFF).toUByte()
        _array[_position + 6] = ((data ushr 8) and 0xFF).toUByte()
        _array[_position + 5] = ((data ushr 16) and 0xFF).toUByte()
        _array[_position + 4] = ((data ushr 24) and 0xFF).toUByte()
        _array[_position + 3] = ((data ushr 32) and 0xFF).toUByte()
        _array[_position + 2] = ((data ushr 40) and 0xFF).toUByte()
        _array[_position + 1] = ((data ushr 48) and 0xFF).toUByte()
        _array[_position + 0] = ((data ushr 56) and 0xFF).toUByte()
        _position += 8
    }

    override fun toString(): String {
        return "ByteBuffer" + "pos[=" + position + " lim=" + limit + "cap=" + capacity + "]"
    }
}