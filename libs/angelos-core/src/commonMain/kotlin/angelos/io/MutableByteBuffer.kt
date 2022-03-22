/**
 * Copyright (c) 2021-2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
package angelos.io

interface MutableByteBuffer: ByteBuffer {
    fun putChar(value: Char)
    fun putShort(value: Short)
    fun putUShort(value: UShort)
    fun putInt(value: Int)
    fun putUInt(value: UInt)
    fun putLong(value: Long)
    fun putULong(value: ULong)
    fun putFloat(value: Float)
    fun putDouble(value: Double)
}