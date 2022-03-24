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
    fun setChar(value: Char)
    fun setShort(value: Short)
    fun setUShort(value: UShort)
    fun setInt(value: Int)
    fun setUInt(value: UInt)
    fun setLong(value: Long)
    fun setULong(value: ULong)
    fun setFloat(value: Float)
    fun setDouble(value: Double)
}