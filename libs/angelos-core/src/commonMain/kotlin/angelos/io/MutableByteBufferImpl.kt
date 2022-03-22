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

expect class MutableByteBufferImpl internal constructor(
    capacity: Int,
    limit: Int,
    position: Int,
    mark: Int,
    endianness: Endianness
) : AbstractMutableByteBuffer {
    override fun load(offset: Int): UByte
    override fun save(value: UByte, offset: Int)
}

fun mutableByteBufferOf(capacity: Int, endianness: Endianness = ByteBuffer.nativeEndianness) = MutableByteBufferImpl(capacity, capacity, 0, 0, endianness)