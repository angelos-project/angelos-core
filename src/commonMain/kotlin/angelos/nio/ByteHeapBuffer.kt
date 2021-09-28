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

@ExperimentalUnsignedTypes
@Suppress("OVERRIDE_BY_INLINE")
class ByteHeapBuffer(capacity: Long, limit: Long, position: Long) : Buffer(capacity, limit, position) {
    private val _inner = ByteBufferOperations()

    override inline fun _readChar(): Char = _inner.readChar()
    override inline fun _writeChar(value: Char) = _inner.writeChar(value)
    override inline fun _readShort(): Short = _inner.readShort()
    override inline fun _writeShort(value: Short) = _inner.writeShort(value)
    override inline fun _readUShort(): UShort = _inner.readUShort()
    override inline fun _writeUShort(value: UShort) = _inner.writeUShort(value)
    override inline fun _readInt(): Int = _inner.readInt()
    override inline fun _writeInt(value: Int) = _inner.writeInt(value)
    override inline fun _readUInt(): UInt = _inner.readUInt()
    override inline fun _writeUInt(value: UInt) = _inner.writeUInt(value)
    override inline fun _readLong(): Long = _inner.readLong()
    override inline fun _writeLong(value: Long) = _inner.writeLong(value)
    override inline fun _readULong(): ULong = _inner.readULong()
    override inline fun _writeULong(value: ULong) = _inner.writeULong(value)
    override inline fun _readFloat(): Int = _inner.readFloat()
    override inline fun _writeFloat(value: Int) = _inner.writeFloat(value)
    override inline fun _readDouble(): Long = _inner.readDouble()
    override inline fun _writeDouble(value: Long) = _inner.writeDouble(value)
}