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

import angelos.io.FileDescriptor
import angelos.io.Dir.FileEntry
import angelos.io.FileNotFoundException
import angelos.io.FileObject

internal actual class FileSystem {
    actual companion object {
        @ExperimentalUnsignedTypes
        actual inline fun readFile(number: Int, array: ByteArray, index: Int, count: ULong): ULong =
            fs_read(number, array, index, count.toLong()).toULong()

        @ExperimentalUnsignedTypes
        actual inline fun writeFile(number: Int, array: ByteArray, index: Int, count: ULong): ULong =
            fs_write(number, array, index, count.toLong()).toULong()

        actual inline fun tellFile(number: Int): ULong = fs_lseek(number, 0, SeekDirective.CUR.whence).toULong()

        actual inline fun seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): ULong =
            fs_lseek(number, position, when (whence) {
                FileDescriptor.Seek.SET -> SeekDirective.SET.whence
                FileDescriptor.Seek.CUR -> SeekDirective.CUR.whence
                FileDescriptor.Seek.END -> SeekDirective.END.whence
            }).toULong()

        actual inline fun closeFile(number: Int): Boolean = fs_close(number) == 0

        actual inline fun checkReadable(path: String): Boolean = fs_access(path, AccessFlag.R_OK.flag) == 0

        actual inline fun checkWritable(path: String): Boolean = fs_access(path, AccessFlag.W_OK.flag) == 0

        actual inline fun checkExecutable(path: String): Boolean = fs_access(path, AccessFlag.X_OK.flag) == 0

        actual inline fun checkExists(path: String): Boolean = fs_access(path, AccessFlag.F_OK.flag) == 0

        actual inline fun getFileType(path: String): Int {
            val type: Int = fs_filetype(path)
            if (type == -1)
                throw FileNotFoundException("File not found.\n$path")
            return type
        }

        actual inline fun getFileInfo(path: String): FileObject.Info = fs_fileinfo(path) ?: throw FileNotFoundException("File not found.\n$path")

        actual inline fun getLinkTarget(path: String): String = fs_readlink(path)

        actual inline fun openDir(path: String): Long {
            val number = fs_opendir(path)
            if (number == 0L)
                throw FileNotFoundException("File not found.\n$path")
            return number
        }

        actual inline fun readDir(dir: Long): FileEntry = fs_readdir(dir)

        actual inline fun closeDir(dir: Long): Boolean = fs_closedir(dir) == 0

        actual inline fun openFile(path: String, option: Int): Int = fs_open(path, when (option) {
            1 -> AccessMode.READ_ONLY.mode
            2 -> AccessMode.WRITE_ONLY.mode
            else -> AccessMode.READ_WRITE.mode
        })

        enum class SeekDirective(val whence: Int) {
            SET(0),
            CUR(1),
            END(2),
        }

        enum class AccessFlag(val flag: Int) {
            F_OK(0),
            X_OK(0x01),
            W_OK(0x02),
            R_OK(0x04)
        }

        enum class AccessMode(val mode: Int) {
            READ_ONLY(0),
            WRITE_ONLY(0x01),
            READ_WRITE(0x02),
        }

        @JvmStatic
        private external fun fs_read(number: Int, array: ByteArray, index: Int, count: Long): Long

        @JvmStatic
        private external fun fs_write(number: Int, array: ByteArray, index: Int, count: Long): Long

        @JvmStatic
        private external fun fs_lseek(number: Int, offset: Long, whence: Int): Long

        @JvmStatic
        private external fun fs_close(number: Int): Int

        @JvmStatic
        private external fun fs_access(path: String, amode: Int): Int

        @JvmStatic
        private external fun fs_filetype(path: String): Int

        @JvmStatic
        private external fun fs_fileinfo(path: String): FileObject.Info?

        @JvmStatic
        private external fun fs_readlink(path: String): String

        @JvmStatic
        private external fun fs_open(path: String, option: Int): Int

        @JvmStatic
        private external fun fs_opendir(path: String): Long

        @JvmStatic
        private external fun fs_readdir(dir: Long): FileEntry

        @JvmStatic
        private external fun fs_closedir(dir: Long): Int

        init {
            System.loadLibrary("jnifilesystem")
        }
    }
}