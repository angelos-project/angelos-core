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

import kotlinx.cinterop.*
import platform.posix.*


/**
 * Get the necessary information from the physical filesystem on POSIX.
 * Information at https://man7.org/linux/man-pages/man2/stat.2.html
 * https://kotlinlang.org/docs/mapping-struct-union-types-from-c.html#conversion-between-cvalue-lt-t-gt-and-cvaluesref-lt-t-gt
 */
internal inline fun posixStat(path: String): stat64 = memScoped {
    val buffer: stat64 = alloc()
    if (stat64(path, buffer.rawPtr as CValuesRef<stat64>) != 0) {
        throw FileNotFoundException("File not found.\n$path")
    }
    return buffer
}

internal actual inline fun checkReadable(path: String): Boolean = access(path, R_OK) == 0
internal actual inline fun checkWritable(path: String): Boolean = access(path, W_OK) == 0
internal actual inline fun checkExecutable(path: String): Boolean = access(path, X_OK) == 0
internal actual inline fun checkExists(path: String): Boolean = access(path, F_OK) == 0

internal actual inline fun getFileType(path: String): Int = when (posixStat(path).st_mode and S_IFMT.toUShort()) {
    S_IFLNK.toUShort() -> 1
    S_IFDIR.toUShort() -> 2
    S_IFREG.toUShort() -> 3
    else -> 0
}

internal actual inline fun getLinkTarget(path: String): String {
    memScoped{
        val buffer = ByteArray(4096)
        buffer.usePinned{
            val size = readlink(path, it.addressOf(0), buffer.size.convert()).toInt()
            return buffer.copyOf(size).toString()
        }
    }
}