/**
 * Copyright (c) 2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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

expect class ByteBufferImpl internal constructor(
    array: ByteArray,
    capacity: Int,
    limit: Int,
    mark: Int,
    endianness: Endianness
): ByteBuffer {
    override fun getArray(): ByteArray
    override fun load(offset: Int): UByte

    fun toMutableNativeByteBuffer(): MutableNativeByteBufferImpl
    fun toNativeByteBuffer(): NativeByteBufferImpl
}

fun byteBufferFrom(array: ByteArray, endianness: Endianness = ByteBuffer.nativeEndianness): ByteBufferImpl = ByteBufferImpl(array, array.size, array.size, 0, endianness)

fun ByteBufferImpl.toMutableByteBuffer(): MutableByteBufferImpl {
    return MutableByteBufferImpl(getArray().copyOf(), capacity, limit, mark, mark, endian)
}