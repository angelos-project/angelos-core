/**
 * Copyright (c) 2021 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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

internal actual inline fun closeFile(number: Int): Boolean {
    TODO("Not yet implemented")
}

@ExperimentalUnsignedTypes
internal actual inline fun readFile(
    number: Int,
    array: UByteArray,
    index: Int,
    count: ULong,
): ULong {
    TODO("Not yet implemented")
}

internal actual inline fun writeFile(
    number: Int,
    array: UByteArray,
    index: Int,
    count: ULong,
): ULong {
    TODO("Not yet implemented")
}

internal actual inline fun tellFile(number: Int): ULong {
    TODO("Not yet implemented")
}

internal actual inline fun seekFile(
    number: Int,
    position: Long,
    whence: FileDescriptor.Seek,
): ULong {
    TODO("Not yet implemented")
}