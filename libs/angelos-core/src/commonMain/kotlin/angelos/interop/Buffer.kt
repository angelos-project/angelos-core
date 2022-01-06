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

internal expect class Buffer {
   companion object{
        fun allocateMemory(size: Long): Long
        fun freeMemory(address: Long)
        fun getShort(address: Long): Short
        fun putShort(address: Long, x: Short)
        fun getChar(address: Long): Char
        fun putChar(address: Long, x: Char)
        fun getInt(address: Long): Int
        fun putInt(address: Long, x: Int)
        fun getLong(address: Long): Long
        fun putLong(address: Long, x: Long)
        fun getFloat(address: Long): Float
        fun putFloat(address: Long, x: Float)
        fun getDouble(address: Long): Double
        fun putDouble(address: Long, x: Double)
    }
}