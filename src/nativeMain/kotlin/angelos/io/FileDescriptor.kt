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

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.*

internal actual inline fun closeFile(number: Int): Boolean = close(number) == 0
internal actual inline fun readFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong {
    array.usePinned {
        return read(number, it.addressOf(index), count).toULong()
    }
}

internal actual inline fun writeFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong {
    array.usePinned {
        return write(number, it.addressOf(index), count).toULong()
    }
}

internal actual inline fun tellFile(number: Int): ULong = lseek(number, 0, SEEK_CUR).toULong()
internal actual inline fun seekFile(
    number: Int,
    position: Long,
    whence: FileDescriptor.Seek,
): ULong {
    val newPos: Long = lseek(number, position, when(whence){
        FileDescriptor.Seek.SET -> SEEK_SET
        FileDescriptor.Seek.CUR -> SEEK_CUR
        FileDescriptor.Seek.END -> SEEK_END
    })
    if(newPos < 0)
        throw IOException("Failed seeking in file.")
    return newPos.toULong()
}

