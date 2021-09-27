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

@ExperimentalUnsignedTypes
@Suppress("OVERRIDE_BY_INLINE")
class ByteHeapBuffer(capacity: Long, limit: Long, position: Long) : Buffer(capacity, limit, position) {
    protected val _array: ByteArray = ByteBufferOperations.allocate(capacity.absoluteValue.toInt())
    protected val _view: UByteArray = _array.asUByteArray()

    override inline fun _readChar(): Char = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8)
        false -> load(1).toInt() or
                (load(0).toInt() shl 8)
    }.toChar()

    override inline fun _writeChar(value: Char) = when (_reverse) {
        true -> {
            save((value.code and 0xFF).toUByte(), 0)
            save(((value.code ushr 8) and 0xFF).toUByte(), 1)
        }
        false -> {
            save((value.code and 0xFF).toUByte(), 1)
            save(((value.code ushr 8) and 0xFF).toUByte(), 0)
        }
    }

    override inline fun _readShort(): Short = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8)
        false -> load(1).toInt() or
                (load(0).toInt() shl 8)
    }.toShort()

    override inline fun _writeShort(value: Short) = when (_reverse) {
        true -> {
            save((value.toInt() and 0xFF).toUByte(), 0)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
        }
        false -> {
            save((value.toInt() and 0xFF).toUByte(), 1)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 0)
        }
    }

    override inline fun _readUShort(): UShort = when (_reverse) {
        true -> (load(0).toInt() or
                (load(1).toInt() shl 8))
        false -> (load(1).toInt() or
                (load(0).toInt() shl 8))
    }.toUShort()

    override inline fun _writeUShort(value: UShort) = when (_reverse) {
        true -> {
            save((value.toInt() and 0xFF).toUByte(), 0)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 1)
        }
        false -> {
            save((value.toInt() and 0xFF).toUByte(), 1)
            save(((value.toInt() ushr 8) and 0xFF).toUByte(), 0)
        }
    }

    override inline fun _readInt(): Int = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8) or
                (load(2).toInt() shl 16) or
                (load(3).toInt() shl 24)
        false -> load(3).toInt() or
                (load(2).toInt() shl 8) or
                (load(1).toInt() shl 16) or
                (load(0).toInt() shl 24)
    }

    override inline fun _writeInt(value: Int) = when (_reverse) {
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

    override inline fun _readUInt(): UInt = when (_reverse) {
        true -> load(0).toUInt() or
                (load(1).toUInt() shl 8) or
                (load(2).toUInt() shl 16) or
                (load(3).toUInt() shl 24)
        false -> load(3).toUInt() or
                (load(2).toUInt() shl 8) or
                (load(1).toUInt() shl 16) or
                (load(0).toUInt() shl 24)
    }

    override inline fun _writeUInt(value: UInt) = when (_reverse) {
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

    override inline fun _readLong(): Long = when (_reverse) {
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

    override inline fun _writeLong(value: Long) = when (_reverse) {
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

    override inline fun _readULong(): ULong = when (_reverse) {
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

    override inline fun _writeULong(value: ULong) = when (_reverse) {
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

    override inline fun _readFloat(): Int = when (_reverse) {
        true -> load(0).toInt() or
                (load(1).toInt() shl 8) or
                (load(2).toInt() shl 16) or
                (load(3).toInt() shl 24)
        false -> load(3).toInt() or
                (load(2).toInt() shl 8) or
                (load(1).toInt() shl 16) or
                (load(0).toInt() shl 24)
    }

    override inline fun _writeFloat(value: Int) = when (_reverse) {
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

    override inline fun _readDouble(): Long = when (_reverse) {
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

    override inline fun _writeDouble(value: Long) = when (_reverse) {
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

    protected inline fun load(offset: Int): UByte {
        return _view[_position + offset]
    }

    protected inline fun save(value: UByte, offset: Int) {
        _view[_position + offset] = value
    }
}