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

abstract class ByteBuffer internal constructor(
    capacity: Int,
    limit: Int,
    mark: Int,
    endianness: Endianness
) {

    internal var _capacity: Int
    val capacity: Int
        get() = _capacity

    internal var _limit: Int
    val limit: Int
        get() = _limit

    internal var _mark: Int
    val mark: Int
        get() = _mark

    internal var _reverse: Boolean
    val reverse: Boolean
        get() = _reverse

    internal var _endian: Endianness
    var endian: Endianness
    get() = _endian
        set(value) {
            _endian = value
            _reverse = _endian != nativeEndianness
        }

    init {
        _capacity = capacity
        _limit = min(capacity.absoluteValue, limit.absoluteValue)
        _mark = min(limit.absoluteValue, mark.absoluteValue)
        _endian = endianness
        _reverse = _endian != nativeEndianness
    }

    internal abstract fun getArray(): ByteArray

    open fun copyInto(buffer: MutableByteBuffer, range: IntRange) {
        getArray().copyInto(buffer.getArray(), buffer.position, range.first, range.last)
    }

    inline fun allowance(): Int = capacity - limit

    open fun remaining(): Int = capacity - mark

    open fun rewind() {
        _mark = 0
    }

    protected open fun enoughData(size: Int) {
        if (_limit - _mark < size)
            throw ByteBufferException("End of data.")
    }

    private inline fun forwardMark(length: Int) { _mark += length }

    fun getByte(): Byte {
        enoughData(1)
        val value = readByte()
        forwardMark(1)
        return value
    }

    fun getUByte(): UByte {
        enoughData(1)
        val value = readUByte()
        forwardMark(1)
        return value
    }

    fun getChar(): Char {
        enoughData(2)
        val value = readChar()
        forwardMark(2)
        return value
    }

    fun getShort(): Short {
        enoughData(2)
        val value = readShort()
        forwardMark(2)
        return value
    }

    fun getUShort(): UShort {
        enoughData(2)
        val value = readUShort()
        forwardMark(2)
        return value
    }

    fun getInt(): Int {
        enoughData(4)
        val value = readInt()
        forwardMark(4)
        return value
    }

    fun getUInt(): UInt {
        enoughData(4)
        val value = readUInt()
        forwardMark(4)
        return value
    }

    fun getLong(): Long {
        enoughData(8)
        val value = readLong()
        forwardMark(8)
        return value
    }

    fun getULong(): ULong {
        enoughData(8)
        val value = readULong()
        forwardMark(8)
        return value
    }

    fun getFloat(): Float {
        enoughData(4)
        val value = readFloat()
        forwardMark(4)
        return Float.fromBits(value)
    }

    fun getDouble(): Double {
        enoughData(8)
        val value = readDouble()
        forwardMark(8)
        return Double.fromBits(value)
    }

    internal abstract fun load(offset: Int): UByte

    internal open fun readByte(): Byte = load(0).toByte()
    internal open fun readUByte(): UByte = load(0)

    internal open fun readChar(): Char = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8)
        false -> load(1).toInt() or
                (load(0).toInt() shl 8)
    }.toChar()

    internal open fun readShort(): Short = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8)
        false -> load(1).toInt() or
                (load(0).toInt() shl 8)
    }.toShort()

    internal open fun readUShort(): UShort = when (_reverse) {
        true -> (load(0).toInt() or
                (load(1).toInt() shl 8))
        false -> (load(1).toInt() or
                (load(0).toInt() shl 8))
    }.toUShort()

    internal open fun readInt(): Int = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8) or
                (load(2).toInt() shl 16) or
                (load(3).toInt() shl 24)
        false -> load(3).toInt() or
                (load(2).toInt() shl 8) or
                (load(1).toInt() shl 16) or
                (load(0).toInt() shl 24)
    }

    internal open fun readUInt(): UInt = when (_reverse) {
        true -> load(0).toUInt() or
                (load(1).toUInt() shl 8) or
                (load(2).toUInt() shl 16) or
                (load(3).toUInt() shl 24)
        false -> load(3).toUInt() or
                (load(2).toUInt() shl 8) or
                (load(1).toUInt() shl 16) or
                (load(0).toUInt() shl 24)
    }

    internal open fun readLong(): Long = when (_reverse) {
        true -> load(0).toLong() or
                (load(1).toLong() shl 8) or
                (load(2).toLong() shl 16) or
                (load(3).toLong() shl 24) or
                (load(4).toLong() shl 32) or
                (load(5).toLong() shl 40) or
                (load(6).toLong() shl 48) or
                (load(7).toLong() shl 56)
        false -> load(7).toLong() or
                (load(6).toLong() shl 8) or
                (load(5).toLong() shl 16) or
                (load(4).toLong() shl 24) or
                (load(3).toLong() shl 32) or
                (load(2).toLong() shl 40) or
                (load(1).toLong() shl 48) or
                (load(0).toLong() shl 56)
    }

    internal open fun readULong(): ULong = when (_reverse) {
        true -> load(0).toULong() or
                (load(1).toULong() shl 8) or
                (load(2).toULong() shl 16) or
                (load(3).toULong() shl 24) or
                (load(4).toULong() shl 32) or
                (load(5).toULong() shl 40) or
                (load(6).toULong() shl 48) or
                (load(7).toULong() shl 56)
        false -> load(7).toULong() or
                (load(6).toULong() shl 8) or
                (load(5).toULong() shl 16) or
                (load(4).toULong() shl 24) or
                (load(3).toULong() shl 32) or
                (load(2).toULong() shl 40) or
                (load(1).toULong() shl 48) or
                (load(0).toULong() shl 56)
    }

    internal open fun readFloat(): Int = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8) or
                (load(2).toInt() shl 16) or
                (load(3).toInt() shl 24)
        false -> load(3).toInt() or
                (load(2).toInt() shl 8) or
                (load(1).toInt() shl 16) or
                (load(0).toInt() shl 24)
    }

    internal open fun readDouble(): Long = when (_reverse) {
        true -> load(0).toLong() or
                (load(1).toLong() shl 8) or
                (load(2).toLong() shl 16) or
                (load(3).toLong() shl 24) or
                (load(4).toLong() shl 32) or
                (load(5).toLong() shl 40) or
                (load(6).toLong() shl 48) or
                (load(7).toLong() shl 56)
        false -> load(7).toLong() or
                (load(6).toLong() shl 8) or
                (load(5).toLong() shl 16) or
                (load(4).toLong() shl 24) or
                (load(3).toLong() shl 32) or
                (load(2).toLong() shl 40) or
                (load(1).toLong() shl 48) or
                (load(0).toLong() shl 56)
    }

    companion object {
        val nativeEndianness = Endianness.nativeOrder()

        inline fun reverseShort(value: Short): Short = (
                (value.toInt() shl 8 and 0xFF00) or (value.toInt() shr 8 and 0xFF)).toShort()

        inline fun reverseInt(value: Int): Int = (value shl 24 and -0x1000000) or
                (value shl 8 and 0xFF0000) or
                (value shr 8 and 0xFF00) or
                (value shr 24 and 0xFF)

        inline fun reverseLong(value: Long): Long = (value shl 56 and -0x1000000_00000000) or
                (value shl 40 and 0xFF0000_00000000) or
                (value shl 24 and 0xFF00_00000000) or
                (value shl 8 and 0xFF_00000000) or
                (value shr 8 and 0xFF000000) or
                (value shr 24 and 0xFF0000) or
                (value shr 40 and 0xFF00) or
                (value shr 56 and 0xFF)
    }
}