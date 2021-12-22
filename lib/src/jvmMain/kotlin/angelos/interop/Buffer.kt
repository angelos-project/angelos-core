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

import sun.misc.Unsafe
import java.lang.reflect.Field

internal actual class Buffer {
    actual companion object {
        private val theUnsafe: Unsafe

        init{
            val f: Field = Unsafe::class.java.getDeclaredField("theUnsafe")
            f.isAccessible = true
            theUnsafe = f.get(null) as Unsafe
        }

        actual fun allocateMemory(size: Long): Long {
            return theUnsafe.allocateMemory(size)
        }

        actual fun freeMemory(address: Long) {
            return theUnsafe.freeMemory(address)
        }

        actual fun getShort(address: Long): Short {
            return theUnsafe.getShort(address)
        }

        actual fun putShort(address: Long, x: Short) {
            theUnsafe.putShort(address, x)
        }

        actual fun getChar(address: Long): Char {
            return theUnsafe.getChar(address)
        }

        actual fun putChar(address: Long, x: Char) {
            theUnsafe.putChar(address, x)
        }

        actual fun getInt(address: Long): Int {
            return theUnsafe.getInt(address)
        }

        actual fun putInt(address: Long, x: Int) {
            theUnsafe.putInt(address, x)
        }

        actual fun getLong(address: Long): Long {
            return theUnsafe.getLong(address)
        }

        actual fun putLong(address: Long, x: Long) {
            theUnsafe.putLong(address, x)
        }

        actual fun getFloat(address: Long): Float {
            return theUnsafe.getFloat(address)
        }

        actual fun putFloat(address: Long, x: Float) {
            theUnsafe.putFloat(address, x)
        }

        actual fun getDouble(address: Long): Double {
            return theUnsafe.getDouble(address)
        }

        actual fun putDouble(address: Long, x: Double) {
            theUnsafe.putDouble(address, x)
        }
    }
}