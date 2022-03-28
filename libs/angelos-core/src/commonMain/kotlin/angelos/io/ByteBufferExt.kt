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

fun byteBufferFrom(array: ByteArray, endianness: Endianness = ByteBuffer.nativeEndianness): ByteBufferImpl = ByteBufferImpl(array, array.size, array.size, 0, endianness)
fun mutableByteBufferWith(capacity: Int, endianness: Endianness = ByteBuffer.nativeEndianness): MutableByteBufferImpl = MutableByteBufferImpl(ByteArray(capacity), capacity, capacity, 0, 0, endianness)
fun mutableByteBufferFrom(array: ByteArray, endianness: Endianness = ByteBuffer.nativeEndianness): MutableByteBufferImpl = MutableByteBufferImpl(array, array.size, array.size, 0, 0, endianness)
fun mutableNativeByteBufferWith(capacity: Int, endianness: Endianness = ByteBuffer.nativeEndianness): MutableNativeByteBufferImpl = MutableNativeByteBufferImpl(capacity, capacity, 0, 0, endianness)
fun nativeByteBufferWith(capacity: Int, endianness: Endianness = ByteBuffer.nativeEndianness): NativeByteBufferImpl = NativeByteBufferImpl(capacity, capacity, 0, endianness)

fun ByteBufferImpl.toMutableByteBuffer(): MutableByteBufferImpl {
    return MutableByteBufferImpl(getArray().copyOf(), capacity, limit, mark, mark, endian)
}

fun ByteBufferImpl.toMutableNativeByteBuffer(): MutableNativeByteBufferImpl {
    val mnbb = MutableNativeByteBufferImpl(capacity, limit, mark, mark, endian)
    copyInto(mnbb, 0..capacity)
    return mnbb
}

fun ByteBufferImpl.toNativeByteBuffer(): MutableByteBufferImpl {
    return MutableByteBufferImpl(getArray().copyOf(), capacity, limit, mark, mark, endian)
}

fun NativeByteBufferImpl.toByteBuffer(): ByteBufferImpl {
    return ByteBufferImpl(getArray().copyOf(), capacity, limit, mark, endian)
}

fun MutableByteBufferImpl.toMutableNativeByteBuffer(): MutableNativeByteBufferImpl {
    val mnbb = MutableNativeByteBufferImpl(capacity, limit, mark, mark, endian)
    copyInto(mnbb, 0..capacity)
    return mnbb
}

fun MutableNativeByteBufferImpl.toMutableByteBuffer(): MutableByteBufferImpl {
    return MutableByteBufferImpl(getArray().copyOf(), capacity, limit, mark, mark, endian)
}

fun NativeByteBufferImpl.toMutableNativeByteBuffer(): MutableByteBufferImpl {
    return MutableByteBufferImpl(getArray().copyOf(), capacity, limit, mark, mark, endian)
}