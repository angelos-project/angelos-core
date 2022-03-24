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

abstract class AbstractByteBuffer internal constructor(
    capacity: Int,
    limit: Int,
    mark: Int,
    endianness: Endianness
): ByteBuffer {

    protected var _capacity: Int
    override val capacity: Int
        get() = _capacity

    protected var _limit: Int
    override val limit: Int
        get() = _limit

    protected var _mark: Int
    override val mark: Int
        get() = _mark

    protected var _reverse: Boolean
    val reverse: Boolean
        get() = _reverse

    private var _endian: Endianness
    override var endian: Endianness
        get() = _endian
        set(value) {
            _endian = value
            _reverse = _endian != ByteBuffer.nativeEndianness
        }

    init {
        _capacity = capacity
        _limit = min(capacity.absoluteValue, limit.absoluteValue)
        _mark = min(limit.absoluteValue, mark.absoluteValue)
        _endian = endianness
        _reverse = _endian != ByteBuffer.nativeEndianness
    }

    override fun rewind() {
        _mark = 0
    }

    protected open fun enoughData(size: Int) {
        if (_limit - _mark < size)
            throw ByteBufferException("End of data.")
    }

    private inline fun forwardMark(length: Int) { _mark += length }

    override fun getChar(): Char {
        enoughData(2)
        val value = readChar()
        forwardMark(2)
        return value
    }

    override fun getShort(): Short {
        enoughData(2)
        val value = readShort()
        forwardMark(2)
        return value
    }

    override fun getUShort(): UShort {
        enoughData(2)
        val value = readUShort()
        forwardMark(2)
        return value
    }

    override fun getInt(): Int {
        enoughData(4)
        val value = readInt()
        forwardMark(4)
        return value
    }

    override fun getUInt(): UInt {
        enoughData(4)
        val value = readUInt()
        forwardMark(4)
        return value
    }

    override fun getLong(): Long {
        enoughData(8)
        val value = readLong()
        forwardMark(8)
        return value
    }

    override fun getULong(): ULong {
        enoughData(8)
        val value = readULong()
        forwardMark(8)
        return value
    }

    override fun getFloat(): Float {
        enoughData(4)
        val value = readFloat()
        forwardMark(4)
        return Float.fromBits(value)
    }

    override fun getDouble(): Double {
        enoughData(8)
        val value = readDouble()
        forwardMark(8)
        return Double.fromBits(value)
    }

    internal abstract fun load(offset: Int): UByte

    private inline fun readChar(): Char = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8)
        false -> load(1).toInt() or
                (load(0).toInt() shl 8)
    }.toChar()

    private inline fun readShort(): Short = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8)
        false -> load(1).toInt() or
                (load(0).toInt() shl 8)
    }.toShort()

    private inline fun readUShort(): UShort = when (_reverse) {
        true -> (load(0).toInt() or
                (load(1).toInt() shl 8))
        false -> (load(1).toInt() or
                (load(0).toInt() shl 8))
    }.toUShort()

    private inline fun readInt(): Int = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8) or
                (load(2).toInt() shl 16) or
                (load(3).toInt() shl 24)
        false -> load(3).toInt() or
                (load(2).toInt() shl 8) or
                (load(1).toInt() shl 16) or
                (load(0).toInt() shl 24)
    }

    private inline fun readUInt(): UInt = when (_reverse) {
        true -> load(0).toUInt() or
                (load(1).toUInt() shl 8) or
                (load(2).toUInt() shl 16) or
                (load(3).toUInt() shl 24)
        false -> load(3).toUInt() or
                (load(2).toUInt() shl 8) or
                (load(1).toUInt() shl 16) or
                (load(0).toUInt() shl 24)
    }

    private inline fun readLong(): Long = when (_reverse) {
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

    private inline fun readULong(): ULong = when (_reverse) {
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

    private inline fun readFloat(): Int = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8) or
                (load(2).toInt() shl 16) or
                (load(3).toInt() shl 24)
        false -> load(3).toInt() or
                (load(2).toInt() shl 8) or
                (load(1).toInt() shl 16) or
                (load(0).toInt() shl 24)
    }

    private inline fun readDouble(): Long = when (_reverse) {
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
}