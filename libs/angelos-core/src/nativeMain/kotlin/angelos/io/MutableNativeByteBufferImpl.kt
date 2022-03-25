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

actual class MutableNativeByteBufferImpl internal actual constructor(
    capacity: Int,
    limit: Int,
    position: Int,
    mark: Int,
    endianness: Endianness
) : AbstractMutableByteBuffer(capacity, limit, position, mark, endianness), NativeBuffer {
    actual override fun save(value: UByte, offset: Int) {
        TODO("Not yet implemented")
    }

    actual override fun load(offset: Int): UByte {
        TODO("Not yet implemented")
    }

    actual override fun copyInto(buffer: MutableByteBuffer, range: IntRange) {
        TODO("Not yet implemented")
    }
}