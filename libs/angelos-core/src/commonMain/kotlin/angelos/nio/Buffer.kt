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
import kotlin.math.absoluteValue
import kotlin.math.min

abstract class Buffer(capacity: Long, limit: Long, order: ByteOrder = nativeEndianness) {
    private var _capacity: Int
    val capacity: Int
        get() = _capacity

    protected var _limit: Int
    val limit: Int
        get() = _limit

    protected var _mark: Int
    val mark: Int
        get() = _mark

    protected var _position: Int = 0
    val position: Int
        get() = _position

    private var _endian: ByteOrder = order
    protected var _reverse: Boolean = _endian != nativeEndianness

    var order: ByteOrder
        get() = _endian
        set(value) {
            _endian = value
            _reverse = _endian != nativeEndianness
        }

    init {
        _capacity = capacity.absoluteValue.toInt()
        _limit = min(limit.absoluteValue.toInt(), _capacity)
        _mark = 0
        _position = 0
    }

    abstract fun toArray(): ByteArray
    abstract fun toPtr(): Long

    fun rewind() {
        _position = 0
        _mark = 0
    }
    fun allowance(): Long = (_capacity - _limit).toLong()

    internal abstract fun _getChar(): Char
    internal abstract fun _putChar(value: Char)
    internal abstract fun _getShort(): Short
    internal abstract fun _putShort(value: Short)
    internal abstract fun _getUShort(): UShort
    internal abstract fun _putUShort(value: UShort)
    internal abstract fun _getInt(): Int
    internal abstract fun _putInt(value: Int)
    internal abstract fun _getUInt(): UInt
    internal abstract fun _putUInt(value: UInt)
    internal abstract fun _getLong(): Long
    internal abstract fun _putLong(value: Long)
    internal abstract fun _getULong(): ULong
    internal abstract fun _putULong(value: ULong)
    internal abstract fun _getFloat(): Int
    internal abstract fun _putFloat(value: Int)
    internal abstract fun _getDouble(): Long
    internal abstract fun _putDouble(value: Long)

    private inline fun enoughSpace(size: Int) {
        if (_limit - _position < size)
            throw BufferUnderflowException("End of buffer.")
    }

    private inline fun enoughData(size: Int) {
        if (_position - _mark < size)
            throw BufferUnderflowException("End of data.")
    }

    private inline fun forwardPosition(length: Int) { _position += length }
    private inline fun forwardMark(length: Int) { _mark += length }

    fun getChar(): Char {
        enoughData(2)
        val value = _getChar()
        forwardMark(2)
        return value
    }

    fun putChar(value: Char) {
        enoughSpace(2)
        _putChar(value)
        forwardPosition(2)
    }

    fun getShort(): Short {
        enoughData(2)
        val value = _getShort()
        forwardMark(2)
        return value
    }

    fun putShort(value: Short) {
        enoughSpace(2)
        _putShort(value)
        forwardPosition(2)
    }

    fun getUShort(): UShort {
        enoughData(2)
        val value = _getUShort()
        forwardMark(2)
        return value
    }

    fun putUShort(value: UShort) {
        enoughSpace(2)
        _putUShort(value)
        forwardPosition(2)
    }

    fun getInt(): Int {
        enoughData(4)
        val value = _getInt()
        forwardMark(4)
        return value
    }

    fun putInt(value: Int) {
        enoughSpace(4)
        _putInt(value)
        forwardPosition(4)
    }

    fun getUInt(): UInt {
        enoughData(4)
        val value = _getUInt()
        forwardMark(4)
        return value
    }

    fun putUInt(value: UInt) {
        enoughSpace(4)
        _putUInt(value)
        forwardPosition(4)
    }

    fun getLong(): Long {
        enoughData(8)
        val value = _getLong()
        forwardMark(8)
        return value
    }

    fun putLong(value: Long) {
        enoughSpace(8)
        _putLong(value)
        forwardPosition(8)
    }

    fun getULong(): ULong {
        enoughData(8)
        val value = _getULong()
        forwardMark(8)
        return value
    }

    fun putULong(value: ULong) {
        enoughSpace(8)
        _putULong(value)
        forwardPosition(8)
    }

    fun getFloat(): Float {
        enoughData(4)
        val value = _getFloat()
        forwardMark(4)
        return Float.fromBits(value)
    }

    fun putFloat(value: Float) {
        enoughSpace(4)
        _putFloat(value.toRawBits())
        forwardPosition(4)
    }

    fun getDouble(): Double {
        enoughData(8)
        val value = _getDouble()
        forwardMark(8)
        return Double.fromBits(value)
    }

    fun putDouble(value: Double) {
        enoughSpace(8)
        _putDouble(value.toRawBits())
        forwardPosition(8)
    }

    @ExperimentalUnsignedTypes
    protected inner class ByteBufferOperations {

        internal val _array: ByteArray = ByteArray(capacity)
        private val _view: UByteArray = _array.asUByteArray()

        internal inline fun getChar(): Char = when (_reverse) {
            true -> load(0).toInt() or
                    (load(1).toInt() shl 8)
            false -> load(1).toInt() or
                    (load(0).toInt() shl 8)
        }.toChar()

        internal inline fun putChar(value: Char) = when (_reverse) {
            true -> {
                save((value.code and 0xFF).toUByte(), 0)
                save(((value.code ushr 8) and 0xFF).toUByte(), 1)
            }
            false -> {
                save((value.code and 0xFF).toUByte(), 1)
                save(((value.code ushr 8) and 0xFF).toUByte(), 0)
            }
        }

        internal inline fun getShort(): Short = when (_reverse) {
            true -> load(0).toInt() or
                    (load(1).toInt() shl 8)
            false -> load(1).toInt() or
                    (load(0).toInt() shl 8)
        }.toShort()

        internal inline fun putShort(value: Short) = when (_reverse) {
            true -> {
                save((value.toInt() and 0xFF).toUByte(), 0)
                save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
            }
            false -> {
                save((value.toInt() and 0xFF).toUByte(), 1)
                save(((value.toInt() ushr 8) and 0xFF).toUByte(), 0)
            }
        }

        internal inline fun getUShort(): UShort = when (_reverse) {
            true -> (load(0).toInt() or
                    (load(1).toInt() shl 8))
            false -> (load(1).toInt() or
                    (load(0).toInt() shl 8))
        }.toUShort()

        internal inline fun putUShort(value: UShort) = when (_reverse) {
            true -> {
                save((value.toInt() and 0xFF).toUByte(), 0)
                save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
            }
            false -> {
                save((value.toInt() and 0xFF).toUByte(), 1)
                save(((value.toInt() ushr 8) and 0xFF).toUByte(), 0)
            }
        }

        internal inline fun getInt(): Int = when (_reverse) {
            true -> load(0).toInt() or
                    (load(1).toInt() shl 8) or
                    (load(2).toInt() shl 16) or
                    (load(3).toInt() shl 24)
            false -> load(3).toInt() or
                    (load(2).toInt() shl 8) or
                    (load(1).toInt() shl 16) or
                    (load(0).toInt() shl 24)
        }

        internal inline fun putInt(value: Int) = when (_reverse) {
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

        internal inline fun getUInt(): UInt = when (_reverse) {
            true -> load(0).toUInt() or
                    (load(1).toUInt() shl 8) or
                    (load(2).toUInt() shl 16) or
                    (load(3).toUInt() shl 24)
            false -> load(3).toUInt() or
                    (load(2).toUInt() shl 8) or
                    (load(1).toUInt() shl 16) or
                    (load(0).toUInt() shl 24)
        }

        internal inline fun putUInt(value: UInt) = when (_reverse) {
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

        internal inline fun getLong(): Long = when (_reverse) {
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

        internal inline fun putLong(value: Long) = when (_reverse) {
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

        internal inline fun getULong(): ULong = when (_reverse) {
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

        internal inline fun putULong(value: ULong) = when (_reverse) {
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

        internal inline fun getFloat(): Int = when (_reverse) {
            true -> load(0).toInt() or
                    (load(1).toInt() shl 8) or
                    (load(2).toInt() shl 16) or
                    (load(3).toInt() shl 24)
            false -> load(3).toInt() or
                    (load(2).toInt() shl 8) or
                    (load(1).toInt() shl 16) or
                    (load(0).toInt() shl 24)
        }

        internal inline fun putFloat(value: Int) = when (_reverse) {
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

        internal inline fun getDouble(): Long = when (_reverse) {
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

        internal inline fun putDouble(value: Long) = when (_reverse) {
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

        internal inline fun load(offset: Int): UByte {
            return _view[_position + offset]
        }

        internal inline fun save(value: UByte, offset: Int) {
            _view[_position + offset] = value
        }
    }

    companion object {

        @JvmStatic
        private val nativeEndianness: ByteOrder = ByteOrder.nativeOrder()

        @JvmStatic
        inline fun reverseShort(value: Short): Short = (
                (value.toInt() shl 8 and 0xFF00) or (value.toInt() shr 8 and 0xFF)).toShort()

        @JvmStatic
        inline fun reverseInt(value: Int): Int = (value shl 24 and -0x1000000) or
                (value shl 8 and 0xFF0000) or
                (value shr 8 and 0xFF00) or
                (value shr 24 and 0xFF)

        @JvmStatic
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