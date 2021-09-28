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

import angelos.interop.Buffer as BufferHelper

actual class ByteDirectBuffer actual constructor(capacity: Long, limit: Long, position: Long) :
    Buffer(capacity, limit, position) {
    private val _arrayAddress: Long = BufferHelper.allocateMemory(capacity)

    private fun calculateAddress(): Long = _arrayAddress + _position

    actual override fun _readChar(): Char = when (_reverse) {
        true -> reverseShort(BufferHelper.getShort(calculateAddress())).toInt().toChar()
        false -> BufferHelper.getChar(calculateAddress())
    }

    actual override fun _writeChar(value: Char) = when (_reverse) {
        true -> BufferHelper.putShort(calculateAddress(), reverseShort(value.code.toShort()))
        false -> BufferHelper.putChar(calculateAddress(), value)
    }

    actual override fun _readShort(): Short = when (_reverse) {
        true -> reverseShort(BufferHelper.getShort(calculateAddress()))
        false -> BufferHelper.getShort(calculateAddress())
    }

    actual override fun _writeShort(value: Short) = when (_reverse) {
        true -> BufferHelper.putShort(calculateAddress(), reverseShort(value))
        false -> BufferHelper.putShort(calculateAddress(), value)
    }

    actual override fun _readUShort(): UShort = when (_reverse) {
        true -> reverseShort(BufferHelper.getShort(calculateAddress()))
        false -> BufferHelper.getShort(calculateAddress())
    }.toUShort()

    actual override fun _writeUShort(value: UShort) = when (_reverse) {
        true -> BufferHelper.putShort(calculateAddress(), reverseShort(value.toShort()))
        false -> BufferHelper.putShort(calculateAddress(), value.toShort())
    }

    actual override fun _readInt(): Int = when (_reverse) {
        true -> reverseInt(BufferHelper.getInt(calculateAddress()))
        false -> BufferHelper.getInt(calculateAddress())
    }

    actual override fun _writeInt(value: Int) = when (_reverse) {
        true -> BufferHelper.putInt(calculateAddress(), reverseInt(value))
        false -> BufferHelper.putInt(calculateAddress(), value)
    }

    actual override fun _readUInt(): UInt = when (_reverse) {
        true -> reverseInt(BufferHelper.getInt(calculateAddress()))
        false -> BufferHelper.getInt(calculateAddress())
    }.toUInt()

    actual override fun _writeUInt(value: UInt) = when (_reverse) {
        true -> BufferHelper.putInt(calculateAddress(), reverseInt(value.toInt()))
        false -> BufferHelper.putInt(calculateAddress(), value.toInt())
    }

    actual override fun _readLong(): Long = when (_reverse) {
        true -> reverseLong(BufferHelper.getLong(calculateAddress()))
        false -> BufferHelper.getLong(calculateAddress())
    }

    actual override fun _writeLong(value: Long) = when (_reverse) {
        true -> BufferHelper.putLong(calculateAddress(), reverseLong(value))
        false -> BufferHelper.putLong(calculateAddress(), value)
    }

    actual override fun _readULong(): ULong = when (_reverse) {
        true -> reverseLong(BufferHelper.getLong(calculateAddress()))
        false -> BufferHelper.getLong(calculateAddress())
    }.toULong()

    actual override fun _writeULong(value: ULong) = when (_reverse) {
        true -> BufferHelper.putLong(calculateAddress(), reverseLong(value.toLong()))
        false -> BufferHelper.putLong(calculateAddress(), value.toLong())
    }

    actual override fun _readFloat(): Int = when (_reverse) {
        true -> reverseInt(BufferHelper.getInt(calculateAddress()))
        false -> BufferHelper.getInt(calculateAddress())
    }

    actual override fun _writeFloat(value: Int) = when (_reverse) {
        true -> BufferHelper.putInt(calculateAddress(), reverseInt(value))
        false -> BufferHelper.putInt(calculateAddress(), value)
    }

    actual override fun _readDouble(): Long = when (_reverse) {
        true -> reverseLong(BufferHelper.getLong(calculateAddress()))
        false -> BufferHelper.getLong(calculateAddress())
    }

    actual override fun _writeDouble(value: Long) = when (_reverse) {
        true -> BufferHelper.putLong(calculateAddress(), reverseLong(value))
        false -> BufferHelper.putLong(calculateAddress(), value)
    }

    protected fun finalize() {
        BufferHelper.freeMemory(_arrayAddress)
    }
}