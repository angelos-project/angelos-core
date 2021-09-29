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
    actual override fun _getChar(): Char {
        TODO("Not yet implemented")
    }

    actual override fun _putChar(value: Char) {
        TODO("Not yet implemented")
    }

    actual override fun _getShort(): Short {
        TODO("Not yet implemented")
    }

    actual override fun _putShort(value: Short) {
        TODO("Not yet implemented")
    }

    actual override fun _getUShort(): UShort {
        TODO("Not yet implemented")
    }

    actual override fun _putUShort(value: UShort) {
        TODO("Not yet implemented")
    }

    actual override fun _getInt(): Int {
        TODO("Not yet implemented")
    }

    actual override fun _putInt(value: Int) {
        TODO("Not yet implemented")
    }

    actual override fun _getUInt(): UInt {
        TODO("Not yet implemented")
    }

    actual override fun _putUInt(value: UInt) {
        TODO("Not yet implemented")
    }

    actual override fun _getLong(): Long {
        TODO("Not yet implemented")
    }

    actual override fun _putLong(value: Long) {
        TODO("Not yet implemented")
    }

    actual override fun _getULong(): ULong {
        TODO("Not yet implemented")
    }

    actual override fun _putULong(value: ULong) {
        TODO("Not yet implemented")
    }

    actual override fun _getFloat(): Int {
        TODO("Not yet implemented")
    }

    actual override fun _putFloat(value: Int) {
        TODO("Not yet implemented")
    }

    actual override fun _getDouble(): Long {
        TODO("Not yet implemented")
    }

    actual override fun _putDouble(value: Long) {
        TODO("Not yet implemented")
    }
}