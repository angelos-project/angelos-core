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

@Suppress("OVERRIDE_BY_INLINE")
actual class ByteDirectBuffer actual constructor(capacity: Long, limit: Long, position: Long) :
    Buffer(capacity, limit, position) {
    actual override inline fun _readChar(): Char {
        TODO("Not yet implemented")
    }

    actual override inline fun _writeChar(value: Char) {
        TODO("Not yet implemented")
    }

    actual override inline fun _readShort(): Short {
        TODO("Not yet implemented")
    }

    actual override inline fun _writeShort(value: Short) {
        TODO("Not yet implemented")
    }

    actual override inline fun _readUShort(): UShort {
        TODO("Not yet implemented")
    }

    actual override inline fun _writeUShort(value: UShort) {
        TODO("Not yet implemented")
    }

    actual override inline fun _readInt(): Int {
        TODO("Not yet implemented")
    }

    actual override inline fun _writeInt(value: Int) {
        TODO("Not yet implemented")
    }

    actual override inline fun _readUInt(): UInt {
        TODO("Not yet implemented")
    }

    actual override inline fun _writeUInt(value: UInt) {
        TODO("Not yet implemented")
    }

    actual override inline fun _readLong(): Long {
        TODO("Not yet implemented")
    }

    actual override inline fun _writeLong(value: Long) {
        TODO("Not yet implemented")
    }

    actual override inline fun _readULong(): ULong {
        TODO("Not yet implemented")
    }

    actual override inline fun _writeULong(value: ULong) {
        TODO("Not yet implemented")
    }

    actual override inline fun _readFloat(): Int {
        TODO("Not yet implemented")
    }

    actual override inline fun _writeFloat(value: Int) {
        TODO("Not yet implemented")
    }

    actual override inline fun _readDouble(): Long {
        TODO("Not yet implemented")
    }

    actual override inline fun _writeDouble(value: Long) {
        TODO("Not yet implemented")
    }
}