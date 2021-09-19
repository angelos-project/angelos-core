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
package angelos.jni

class Posix {
    companion object {

        fun close(fd: Int): Int{
            return posix_close(fd)
        }

        fun read(fd: Int, buf: ByteArray?, n: Long): Long{
            return posix_read(fd, buf, n)
        }

        fun write(fd: Int, buf: ByteArray?, n: Long): Long{
            return posix_write(fd, buf, n)
        }

        fun lseek(fd: Int, offset: Long, whence: Int): Long{
            return posix_lseek(fd, offset, whence)
        }

        fun access(path: String?, amode: Int): Int{
            return posix_access(path, amode)
        }

        fun readlink(path: String?): String?{
            return posix_readlink(path)
        }

        fun opendir(name: String?): Int?{
            return posix_opendir(name)
        }

        fun readdir(dirp: Int?): ByteArray?{
            return posix_readdir(dirp)
        }

        fun closedir(dirp: Int?): Int{
            return posix_closedir(dirp)
        }

        fun open(path: String?, flags: Int, perm: Int): Int{
            return posix_open(path, flags, perm)
        }

        fun isLittleEndian(): Boolean{
            return posix_endian()
        }

        @JvmStatic
        private external fun posix_close(fd: Int): Int

        @JvmStatic
        private external fun posix_read(fd: Int, buf: ByteArray?, n: Long): Long

        @JvmStatic
        private external fun posix_write(fd: Int, buf: ByteArray?, n: Long): Long

        @JvmStatic
        private external fun posix_lseek(fd: Int, offset: Long, whence: Int): Long

        @JvmStatic
        private external fun posix_access(path: String?, amode: Int): Int

        @JvmStatic
        private external fun posix_readlink(path: String?): String?

        @JvmStatic
        private external fun posix_opendir(name: String?): Int?

        @JvmStatic
        private external fun posix_readdir(dirp: Int?): ByteArray?

        @JvmStatic
        private external fun posix_closedir(dirp: Int?): Int

        @JvmStatic
        private external fun posix_open(path: String?, flags: Int, perm: Int): Int

        @JvmStatic
        private external fun posix_endian(): Boolean

        init {
            System.loadLibrary("jniposix")
        }
    }
}