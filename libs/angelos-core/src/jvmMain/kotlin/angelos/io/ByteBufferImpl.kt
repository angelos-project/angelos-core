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

@OptIn(ExperimentalUnsignedTypes::class)
actual class ByteBufferImpl internal actual constructor(
    array: ByteArray,
    capacity: Int,
    limit: Int,
    mark: Int,
    endianness: Endianness
) : ByteBuffer(capacity, limit, mark, endianness) {

    private val _array = array
    private val _view = _array.asUByteArray()

    actual override fun getArray(): ByteArray = _array
    actual override fun load(offset: Int): UByte = _view[_mark + offset]

    actual fun toMutableNativeByteBuffer(): MutableNativeByteBufferImpl {
        val mnbb = MutableNativeByteBufferImpl(capacity, limit, mark, mark, endian)
        for (index in 0 until capacity)
            mnbb.theUnsafe.putByte(mnbb._array + index, _array[index])
        return mnbb
    }

    actual fun toNativeByteBuffer(): NativeByteBufferImpl {
        val mnbb = NativeByteBufferImpl(capacity, limit, mark, endian)
        for (index in 0 until capacity)
            mnbb.theUnsafe.putByte(mnbb._array + index, _array[index])
        return mnbb
    }
}

