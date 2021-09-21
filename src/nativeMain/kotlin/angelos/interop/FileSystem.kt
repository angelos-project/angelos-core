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
package angelos.interop

import angelos.io.*
import kotlinx.cinterop.*
import platform.posix.*

internal actual class FileSystem {
    actual companion object {

        @ExperimentalUnsignedTypes
        actual inline fun readFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong {
            array.usePinned {
                return read(number, it.addressOf(index), count).toULong()
            }
        }

        @ExperimentalUnsignedTypes
        actual inline fun writeFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong {
            array.usePinned {
                return write(number, it.addressOf(index), count).toULong()
            }
        }

        actual inline fun tellFile(number: Int): ULong = lseek(number, 0, SEEK_CUR).toULong()

        actual inline fun seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): ULong {
            val newPos: Long = lseek(number, position, when (whence) {
                FileDescriptor.Seek.SET -> SEEK_SET
                FileDescriptor.Seek.CUR -> SEEK_CUR
                FileDescriptor.Seek.END -> SEEK_END
            })
            if (newPos < 0)
                throw IOException("Failed seeking in file.")
            return newPos.toULong()
        }

        actual inline fun closeFile(number: Int): Boolean = close(number) == 0

        actual inline fun checkReadable(path: String): Boolean = access(path, R_OK) == 0
        actual inline fun checkWritable(path: String): Boolean = access(path, W_OK) == 0
        actual inline fun checkExecutable(path: String): Boolean = access(path, X_OK) == 0
        actual inline fun checkExists(path: String): Boolean = access(path, F_OK) == 0

        actual inline fun getFileType(path: String): Int = when (posixStat(path).st_mode and S_IFMT.toUShort()) {
            S_IFLNK.toUShort() -> 1
            S_IFDIR.toUShort() -> 2
            S_IFREG.toUShort() -> 3
            else -> 0
        }

        actual inline fun getFileInfo(path: String): FileObject.Info {
            val data = posixStat(path)
            return FileObject.Info(
                user = data.st_uid.toInt(),
                group = data.st_gid.toInt(),
                accessedAt = data.st_atimespec.tv_sec,
                modifiedAt = data.st_mtimespec.tv_sec,
                changedAt = data.st_ctimespec.tv_sec,
                createdAt = data.st_birthtimespec.tv_sec,
                size = data.st_size,
            )
        }

        actual inline fun getLinkTarget(path: String): String {
            memScoped {
                val buffer = ByteArray(4096)
                buffer.usePinned {
                    val size = readlink(path, it.addressOf(0), buffer.size.convert()).toInt()
                    return buffer.copyOf(size).toString()
                }
            }
        }

        actual inline fun openDir(path: String): Long =
            (opendir(path) ?: throw FileNotFoundException("File not found.\n$path")).toLong()

        actual inline fun readDir(dir: Long): Dir.FileEntry {
            memScoped {
                val dpPtr: CPointer<dirent>? = readdir(dir.toCPointer())
                return if (dpPtr == null)
                    Dir.FileEntry("", 0)
                else {
                    val dp = dpPtr.asStableRef<dirent>().get()
                    Dir.FileEntry(dp.d_name.toString(), when (dp.d_type.toInt()) {
                        DT_LNK -> 1
                        DT_DIR -> 2
                        DT_REG -> 3
                        else -> 0
                    })
                }
            }
        }

        actual inline fun closeDir(dir: Long): Boolean = closedir(dir.toCPointer()) == 0

        actual inline fun openFile(path: String, option: Int): Int = open(path, when (option) {
            1 -> O_WRONLY
            2 -> O_RDWR
            else -> O_RDONLY
        })

        private inline fun posixStat(path: String): stat64 = memScoped {
            val buffer = alloc<stat64>()
            if (stat64(path, buffer.ptr) != 0)
                throw FileNotFoundException("File not found.\n$path")
            return buffer
        }
    }
}

/**
 * FILESYSTEM LINKS
 * Kotlin/Native - posix -> 05_posix.knm
 * Kotlin/Native - posix -> 06_posix.knm
 * Kotlin/Native - posix -> 23_posix.knm
 * Kotlin/Native - posix -> 24_posix.knm
 */