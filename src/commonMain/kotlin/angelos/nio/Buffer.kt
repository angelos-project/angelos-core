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
        set(value) {
            _position = min(value.absoluteValue, _limit)
        }

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
        this.position = min(position.absoluteValue.toInt(), _limit)
    }

    fun rewind() = 0.also { position = it }

    internal abstract fun _readChar(): Char
    internal abstract fun _writeChar(value: Char)
    internal abstract fun _readShort(): Short
    internal abstract fun _writeShort(value: Short)
    internal abstract fun _readUShort(): UShort
    internal abstract fun _writeUShort(value: UShort)
    internal abstract fun _readInt(): Int
    internal abstract fun _writeInt(value: Int)
    internal abstract fun _readUInt(): UInt
    internal abstract fun _writeUInt(value: UInt)
    internal abstract fun _readLong(): Long
    internal abstract fun _writeLong(value: Long)
    internal abstract fun _readULong(): ULong
    internal abstract fun _writeULong(value: ULong)
    internal abstract fun _readFloat(): Int
    internal abstract fun _writeFloat(value: Int)
    internal abstract fun _readDouble(): Long
    internal abstract fun _writeDouble(value: Long)

    private inline fun enoughSpace(size: Int) {
        if (_limit - _position < size)
            throw BufferUnderflowException("End of buffer.")
    }

    private inline fun forwardPosition(length: Int) {
        _position += length
    }

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

    @ExperimentalUnsignedTypes
    protected inner class ByteBufferOperations {

        private val _array: ByteArray = ByteArray(capacity)
        private val _view: UByteArray = _array.asUByteArray()

        internal inline fun readChar(): Char = when (_reverse) {
            true -> load(0).toInt() or
                    (load(1).toInt() shl 8)
            false -> load(1).toInt() or
                    (load(0).toInt() shl 8)
        }.toChar()

        internal inline fun writeChar(value: Char) = when (_reverse) {
            true -> {
                save((value.code and 0xFF).toUByte(), 0)
                save(((value.code ushr 8) and 0xFF).toUByte(), 1)
            }
            false -> {
                save((value.code and 0xFF).toUByte(), 1)
                save(((value.code ushr 8) and 0xFF).toUByte(), 0)
            }
        }

        internal inline fun readShort(): Short = when (_reverse) {
            true -> load(0).toInt() or
                    (load(1).toInt() shl 8)
            false -> load(1).toInt() or
                    (load(0).toInt() shl 8)
        }.toShort()

        internal inline fun writeShort(value: Short) = when (_reverse) {
            true -> {
                save((value.toInt() and 0xFF).toUByte(), 0)
                save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
            }
            false -> {
                save((value.toInt() and 0xFF).toUByte(), 1)
                save(((value.toInt() ushr 8) and 0xFF).toUByte(), 0)
            }
        }

        internal inline fun readUShort(): UShort = when (_reverse) {
            true -> (load(0).toInt() or
                    (load(1).toInt() shl 8))
            false -> (load(1).toInt() or
                    (load(0).toInt() shl 8))
        }.toUShort()

        internal inline fun writeUShort(value: UShort) = when (_reverse) {
            true -> {
                save((value.toInt() and 0xFF).toUByte(), 0)
                save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
            }
            false -> {
                save((value.toInt() and 0xFF).toUByte(), 1)
                save(((value.toInt() ushr 8) and 0xFF).toUByte(), 0)
            }
        }

        internal inline fun readInt(): Int = when (_reverse) {
            true -> load(0).toInt() or
                    (load(1).toInt() shl 8) or
                    (load(2).toInt() shl 16) or
                    (load(3).toInt() shl 24)
            false -> load(3).toInt() or
                    (load(2).toInt() shl 8) or
                    (load(1).toInt() shl 16) or
                    (load(0).toInt() shl 24)
        }

        internal inline fun writeInt(value: Int) = when (_reverse) {
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

        internal inline fun readUInt(): UInt = when (_reverse) {
            true -> load(0).toUInt() or
                    (load(1).toUInt() shl 8) or
                    (load(2).toUInt() shl 16) or
                    (load(3).toUInt() shl 24)
            false -> load(3).toUInt() or
                    (load(2).toUInt() shl 8) or
                    (load(1).toUInt() shl 16) or
                    (load(0).toUInt() shl 24)
        }

        internal inline fun writeUInt(value: UInt) = when (_reverse) {
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

        internal inline fun readLong(): Long = when (_reverse) {
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

        internal inline fun writeLong(value: Long) = when (_reverse) {
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

        internal inline fun readULong(): ULong = when (_reverse) {
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

        internal inline fun writeULong(value: ULong) = when (_reverse) {
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

        internal inline fun readFloat(): Int = when (_reverse) {
            true -> load(0).toInt() or
                    (load(1).toInt() shl 8) or
                    (load(2).toInt() shl 16) or
                    (load(3).toInt() shl 24)
            false -> load(3).toInt() or
                    (load(2).toInt() shl 8) or
                    (load(1).toInt() shl 16) or
                    (load(0).toInt() shl 24)
        }

        internal inline fun writeFloat(value: Int) = when (_reverse) {
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

        internal inline fun readDouble(): Long = when (_reverse) {
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

        internal inline fun writeDouble(value: Long) = when (_reverse) {
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
                (value.toInt() shl 8 and 0xff00) or (value.toInt() shr 8 and 0xff)).toShort()

        @JvmStatic
        inline fun reverseInt(value: Int): Int = (value shl 24 and -0x1000000) or
                (value shl 8 and 0xff0000) or
                (value shr 8 and 0xff00) or
                (value shr 24 and 0xff)

        @JvmStatic
        inline fun reverseLong(value: Long): Long = (value shl 56 and -0x1000000_00000000) or
                (value shl 40 and 0xff0000_00000000) or
                (value shl 24 and 0xff00_00000000) or
                (value shl 8 and 0xff_00000000) or
                (value shr 8 and 0xff000000) or
                (value shr 24 and 0xff0000) or
                (value shr 40 and 0xff00) or
                (value shr 56 and 0xff)
    }
}