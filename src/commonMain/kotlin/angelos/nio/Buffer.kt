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

import kotlin.math.absoluteValue
import kotlin.math.min

abstract class Buffer(capacity: Long, limit: Long, position: Long, order: ByteOrder = nativeEndianness) {
    private var _capacity: Int
    val capacity: Int
        get() = _capacity

    protected var _limit: Int
    val limit: Int
        get() = _limit

    protected var _position: Int = 0
    var position: Int
        get() = _position
        set(value) {_position = min(value.absoluteValue, _limit)}

    private var _endian: ByteOrder = order
    protected var _reverse: Boolean = _endian != nativeEndianness

    init {
        _capacity = capacity.absoluteValue.toInt()
        _limit = min(limit.absoluteValue.toInt(), _capacity)
        this.position = min(position.absoluteValue.toInt(), _limit)
    }

    protected abstract fun _readChar(): Char
    protected abstract fun _writeChar(value: Char)
    protected abstract fun _readShort(): Short
    protected abstract fun _writeShort(value: Short)
    protected abstract fun _readUShort(): UShort
    protected abstract fun _writeUShort(value: UShort)
    protected abstract fun _readInt(): Int
    protected abstract fun _writeInt(value: Int)
    protected abstract fun _readUInt(): UInt
    protected abstract fun _writeUInt(value: UInt)
    protected abstract fun _readLong(): Long
    protected abstract fun _writeLong(value: Long)
    protected abstract fun _readULong(): ULong
    protected abstract fun _writeULong(value: ULong)
    protected abstract fun _readFloat(): Int
    protected abstract fun _writeFloat(value: Int)
    protected abstract fun _readDouble(): Long
    protected abstract fun _writeDouble(value: Long)

    protected abstract fun load(offset: Int): UByte
    protected abstract fun save(value: UByte, offset: Int)

    private inline fun enoughSpace(size: Int) {
        if (_limit - _position < size)
            throw BufferUnderflowException("End of buffer.")
    }

    private inline fun forwardPosition(length: Int) {_position += length}

    fun readChar(): Char {
        enoughSpace(2)
        val value = _readChar()
        forwardPosition(2)
        return value
    }

    fun writeChar(value: Char) {
        enoughSpace(2)
        _writeChar(value)
        forwardPosition(2)
    }

    fun readShort(): Short {
        enoughSpace(2)
        val value = _readShort()
        forwardPosition(2)
        return value
    }

    fun writeShort(value: Short) {
        enoughSpace(2)
        _writeShort(value)
        forwardPosition(2)
    }

    fun readUShort(): UShort {
        enoughSpace(2)
        val value = _readUShort()
        forwardPosition(2)
        return value
    }

    fun writeUShort(value: UShort) {
        enoughSpace(2)
        _writeUShort(value)
        forwardPosition(2)
    }

    fun readInt(): Int {
        enoughSpace(4)
        val value = _readInt()
        forwardPosition(4)
        return value
    }

    fun writeInt(value: Int) {
        enoughSpace(4)
        _writeInt(value)
        forwardPosition(4)
    }

    fun readUInt(): UInt {
        enoughSpace(4)
        val value = _readUInt()
        forwardPosition(4)
        return value
    }

    fun writeUInt(value: UInt) {
        enoughSpace(4)
        _writeUInt(value)
        forwardPosition(4)
    }

    fun readLong(): Long {
        enoughSpace(8)
        val value = _readLong()
        forwardPosition(8)
        return value
    }

    fun writeLong(value: Long) {
        enoughSpace(8)
        _writeLong(value)
        forwardPosition(8)
    }

    fun readULong(): ULong {
        enoughSpace(8)
        val value = _readULong()
        forwardPosition(8)
        return value
    }

    fun writeULong(value: ULong) {
        enoughSpace(8)
        _writeULong(value)
        forwardPosition(8)
    }

    fun readFloat(): Float {
        enoughSpace(4)
        val value = _readFloat()
        forwardPosition(4)
        return Float.fromBits(value)
    }

    fun writeFloat(value: Float) {
        enoughSpace(4)
        _writeFloat(value.toRawBits())
        forwardPosition(4)
    }

    fun readDouble(): Double {
        enoughSpace(8)
        val value = _readDouble()
        forwardPosition(8)
        return Double.fromBits(value)
    }

    fun writeDouble(value: Double) {
        enoughSpace(8)
        _writeDouble(value.toRawBits())
        forwardPosition(8)
    }

    abstract class ByteBufferOperations {
        abstract fun <T>get(o: Any, address: Long): T

        abstract fun <T>put(o: Any, address: Long, value: T)

        companion object{
            fun allocate(size: Int): ByteArray = ByteArray(size)
        }
    }

    companion object{
        private val nativeEndianness: ByteOrder = ByteOrder.nativeOrder()
    }

}