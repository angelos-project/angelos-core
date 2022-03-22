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

interface ByteBuffer {
    val capacity: Int
        get() = TODO()

    val limit: Int
        get() = TODO()

    val position: Int
        get() = TODO()

    val mark: Int
        get() = TODO()

    var endian: Endianness

    fun rewind()

    fun getChar(): Char
    fun getShort(): Short
    fun getUShort(): UShort
    fun getInt(): Int
    fun getUInt(): UInt
    fun getLong(): Long
    fun getULong(): ULong
    fun getFloat(): Float
    fun getDouble(): Double

    companion object {
        val nativeEndianness = Endianness.nativeOrder()

        inline fun reverseShort(value: Short): Short = (
                (value.toInt() shl 8 and 0xFF00) or (value.toInt() shr 8 and 0xFF)).toShort()

        inline fun reverseInt(value: Int): Int = (value shl 24 and -0x1000000) or
                (value shl 8 and 0xFF0000) or
                (value shr 8 and 0xFF00) or
                (value shr 24 and 0xFF)

        inline fun reverseLong(value: Long): Long = (value shl 56 and -0x1000000_00000000) or
                (value shl 40 and 0xFF0000_00000000) or
                (value shl 24 and 0xFF00_00000000) or
                (value shl 8 and 0xFF_00000000) or
                (value shr 8 and 0xFF000000) or
                (value shr 24 and 0xFF0000) or
                (value shr 40 and 0xFF00) or
                (value shr 56 and 0xFF)
    }
}