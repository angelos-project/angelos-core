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

    override fun toArray(): ByteArray = _inner._array
    override fun toPtr(): Long = throw UnsupportedOperationException()

    override inline fun _getChar(): Char = _inner.getChar()
    override inline fun _putChar(value: Char) = _inner.putChar(value)
    override inline fun _getShort(): Short = _inner.getShort()
    override inline fun _putShort(value: Short) = _inner.putShort(value)
    override inline fun _getUShort(): UShort = _inner.getUShort()
    override inline fun _putUShort(value: UShort) = _inner.putUShort(value)
    override inline fun _getInt(): Int = _inner.getInt()
    override inline fun _putInt(value: Int) = _inner.putInt(value)
    override inline fun _getUInt(): UInt = _inner.getUInt()
    override inline fun _putUInt(value: UInt) = _inner.putUInt(value)
    override inline fun _getLong(): Long = _inner.getLong()
    override inline fun _putLong(value: Long) = _inner.putLong(value)
    override inline fun _getULong(): ULong = _inner.getULong()
    override inline fun _putULong(value: ULong) = _inner.putULong(value)
    override inline fun _getFloat(): Int = _inner.getFloat()
    override inline fun _putFloat(value: Int) = _inner.putFloat(value)
    override inline fun _getDouble(): Long = _inner.getDouble()
    override inline fun _putDouble(value: Long) = _inner.putDouble(value)
}