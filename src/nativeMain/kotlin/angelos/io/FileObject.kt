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
    val buffer = alloc<stat64>()
    if (stat64(path, buffer.ptr) != 0)
        throw FileNotFoundException("File not found.\n$path")
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
    memScoped {
        val buffer = ByteArray(4096)
        buffer.usePinned {
            val size = readlink(path, it.addressOf(0), buffer.size.convert()).toInt()
            return buffer.copyOf(size).toString()
        }
    }
}

internal actual inline fun openDir(path: String): Any =
    opendir(path) ?: throw FileNotFoundException("File not found.\n$path")

@Suppress("UNCHECKED_CAST")
internal actual inline fun readDir(dir: Any): FileEntry {
    memScoped {
        val dpPtr: CPointer<dirent>? = readdir(dir as CValuesRef<DIR>)
        return if (dpPtr == null)
            FileEntry("", 0)
        else {
            val dp = dpPtr.asStableRef<dirent>().get()
            FileEntry(dp.d_name.toString(), when (dp.d_type.toInt()) {
                DT_LNK -> 1
                DT_DIR -> 2
                DT_REG -> 3
                else -> 0
            })
        }
    }
}

@Suppress("UNCHECKED_CAST")
internal actual inline fun closeDir(dir: Any): Boolean = closedir(dir as CValuesRef<DIR>) == 0

internal actual inline fun getFileInfo(path: String): FileObject.Info {
    val data = posixStat(path)
    return FileObject.Info(
        user = data.st_uid,
        group = data.st_gid,
        accessedAt = data.st_atimespec.tv_sec,
        modifiedAt = data.st_mtimespec.tv_sec,
        changedAt = data.st_ctimespec.tv_sec,
        createdAt = data.st_birthtimespec.tv_sec,
        size = data.st_size,
    )
}

internal actual inline fun openFile(path: String, option: Int): Int = open(path, when (option) {
    1 -> O_WRONLY
    2 -> O_RDWR
    else -> O_RDONLY
})
