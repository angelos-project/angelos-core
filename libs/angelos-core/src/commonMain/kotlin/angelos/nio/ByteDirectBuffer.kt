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

expect class ByteDirectBuffer(capacity: Long, limit: Long, order: ByteOrder = nativeEndianness) : Buffer {
    override fun toArray(): ByteArray
    override fun toPtr(): Long

    override fun _getChar(): Char
    override fun _putChar(value: Char)
    override fun _getShort(): Short
    override fun _putShort(value: Short)
    override fun _getUShort(): UShort
    override fun _putUShort(value: UShort)
    override fun _getInt(): Int
    override fun _putInt(value: Int)
    override fun _getUInt(): UInt
    override fun _putUInt(value: UInt)
    override fun _getLong(): Long
    override fun _putLong(value: Long)
    override fun _getULong(): ULong
    override fun _putULong(value: ULong)
    override fun _getFloat(): Int
    override fun _putFloat(value: Int)
    override fun _getDouble(): Long
    override fun _putDouble(value: Long)
}