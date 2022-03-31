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
import kotlinx.cinterop.Pinned
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned

@OptIn(ExperimentalUnsignedTypes::class)
@Suppress("OVERRIDE_BY_INLINE")
actual class NativeByteBufferImpl internal actual constructor(
    capacity: Int,
    limit: Int,
    mark: Int,
    endianness: Endianness
) : ByteBuffer(capacity, limit, mark, endianness), NativeBuffer {

    private val _array = ByteArray(capacity)
    private val _view = _array.asUByteArray()

    actual override fun getArray(): ByteArray = _array
    actual override inline fun load(offset: Int): UByte = _view[_mark + offset]

    actual fun toByteBuffer(): ByteBufferImpl {
        return ByteBufferImpl(getArray().copyOf(), capacity, limit, mark, endian)
    }

    actual fun toMutableNativeByteBuffer(): MutableNativeByteBufferImpl {
        val mnbb = MutableNativeByteBufferImpl(capacity, limit, mark,  mark, endian)
        _array.copyInto(mnbb.getArray(), 0)
        return mnbb
    }

    override fun operation(block: (it: Pinned<ByteArray>) -> Long) = memScoped { _array.usePinned { block(it) } }
}