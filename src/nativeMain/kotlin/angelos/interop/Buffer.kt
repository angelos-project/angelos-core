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
package angelos.interop

internal actual class Buffer {
    actual companion object {
        actual fun allocateMemory(size: Long): Long {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun freeMemory(address: Long) {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun getShort(address: Long): Short {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun putShort(address: Long, x: Short) {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun getChar(address: Long): Char {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun putChar(address: Long, x: Char) {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun getInt(address: Long): Int {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun putInt(address: Long, x: Int) {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun getLong(address: Long): Long {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun putLong(address: Long, x: Long) {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun getFloat(address: Long): Float {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun putFloat(address: Long, x: Float) {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun getDouble(address: Long): Double {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

        actual fun putDouble(address: Long, x: Double) {
            throw UnsupportedOperationException("Will not be implemented on Kotlin/Native")
        }

    }
}