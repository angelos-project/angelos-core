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

import angelos.interop.NativeBuffer

expect class MutableNativeByteBufferImpl internal constructor(
    capacity: Int,
    limit: Int,
    position: Int,
    mark: Int,
    endianness: Endianness
) : MutableByteBuffer, NativeBuffer {
    override fun getArray(): ByteArray
    override fun load(offset: Int): UByte
    override fun save(value: UByte, offset: Int)

    fun toMutableByteBuffer(): MutableByteBufferImpl
}

fun mutableNativeByteBufferWith(capacity: Int, endianness: Endianness = ByteBuffer.nativeEndianness): MutableNativeByteBufferImpl = MutableNativeByteBufferImpl(capacity, capacity, 0, 0, endianness)
