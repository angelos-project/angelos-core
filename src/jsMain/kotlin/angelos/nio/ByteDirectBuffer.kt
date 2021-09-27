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

actual class ByteDirectBuffer actual constructor(capacity: Long, limit: Long, position: Long) :
    Buffer(capacity, limit, position) {
    actual override fun _readChar(): Char {
        TODO("Not yet implemented")
    }

    actual override fun _writeChar(value: Char) {
        TODO("Not yet implemented")
    }

    actual override fun _readShort(): Short {
        TODO("Not yet implemented")
    }

    actual override fun _writeShort(value: Short) {
        TODO("Not yet implemented")
    }

    actual override fun _readUShort(): UShort {
        TODO("Not yet implemented")
    }

    actual override fun _writeUShort(value: UShort) {
        TODO("Not yet implemented")
    }

    actual override fun _readInt(): Int {
        TODO("Not yet implemented")
    }

    actual override fun _writeInt(value: Int) {
        TODO("Not yet implemented")
    }

    actual override fun _readUInt(): UInt {
        TODO("Not yet implemented")
    }

    actual override fun _writeUInt(value: UInt) {
        TODO("Not yet implemented")
    }

    actual override fun _readLong(): Long {
        TODO("Not yet implemented")
    }

    actual override fun _writeLong(value: Long) {
        TODO("Not yet implemented")
    }

    actual override fun _readULong(): ULong {
        TODO("Not yet implemented")
    }

    actual override fun _writeULong(value: ULong) {
        TODO("Not yet implemented")
    }

    actual override fun _readFloat(): Int {
        TODO("Not yet implemented")
    }

    actual override fun _writeFloat(value: Int) {
        TODO("Not yet implemented")
    }

    actual override fun _readDouble(): Long {
        TODO("Not yet implemented")
    }

    actual override fun _writeDouble(value: Long) {
        TODO("Not yet implemented")
    }
}