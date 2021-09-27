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

expect class ByteDirectBuffer(capacity: Long, limit: Long, position: Long) : Buffer {
    override fun _readChar(): Char
    override fun _writeChar(value: Char)
    override fun _readShort(): Short
    override fun _writeShort(value: Short)
    override fun _readUShort(): UShort
    override fun _writeUShort(value: UShort)
    override fun _readInt(): Int
    override fun _writeInt(value: Int)
    override fun _readUInt(): UInt
    override fun _writeUInt(value: UInt)
    override fun _readLong(): Long
    override fun _writeLong(value: Long)
    override fun _readULong(): ULong
    override fun _writeULong(value: ULong)
    override fun _readFloat(): Int
    override fun _writeFloat(value: Int)
    override fun _readDouble(): Long
    override fun _writeDouble(value: Long)
}