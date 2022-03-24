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

import kotlin.math.absoluteValue
import kotlin.math.ceil

fun byteBufferFrom(array: ByteArray, endianness: Endianness = ByteBuffer.nativeEndianness): ByteBuffer = ByteBufferImpl(array, array.size, array.size, 0, endianness)
fun mutableByteBufferWith(capacity: Int, endianness: Endianness = ByteBuffer.nativeEndianness): MutableByteBuffer = MutableByteBufferImpl(ByteArray(ceil((capacity.absoluteValue / 8).toFloat()).toInt() * 8), capacity, capacity, 0, 0, endianness)
fun mutableByteBufferFrom(array: ByteArray, endianness: Endianness = ByteBuffer.nativeEndianness): MutableByteBuffer = MutableByteBufferImpl(array, array.size, array.size, 0, 0, endianness)