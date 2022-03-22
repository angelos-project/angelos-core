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

actual class MutableByteBufferImpl internal actual constructor(
    capacity: Int,
    limit: Int,
    position: Int,
    mark: Int,
    endianness: Endianness
) : AbstractMutableByteBuffer(capacity, limit, position, mark, endianness) {

    @OptIn(ExperimentalUnsignedTypes::class)
    private val _view: UByteArray = _array.asUByteArray()

    actual override fun save(value: UByte, offset: Int) {
        _view[_position + offset] = value
    }

    actual override fun load(offset: Int): UByte {
        return _view[_mark + offset]
    }
}