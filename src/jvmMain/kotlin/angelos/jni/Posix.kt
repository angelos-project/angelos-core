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
        @JvmStatic
        external fun close(fd: Int): Int

        @JvmStatic
        external fun read(fd: Int, buf: ByteArray?, n: Long): Long

        @JvmStatic
        external fun write(fd: Int, buf: ByteArray?, n: Long): Long

        @JvmStatic
        external fun lseek(fd: Int, offset: Long, whence: Int): Long

        @JvmStatic
        external fun alloc()

        @JvmStatic
        external fun access(path: String?, amode: Int): Int

        @JvmStatic
        external fun readlink(path: String?): String?

        @JvmStatic
        external fun opendir(name: String?): Int?

        @JvmStatic
        external fun readdir(dirp: Int?): ByteArray?

        @JvmStatic
        external fun closedir(dirp: Int?): Int

        @JvmStatic
        external fun open(path: String?, flags: Int, perm: Int): Int

        @JvmStatic
        external fun isLittleEndian(): Boolean

        init {
            System.loadLibrary("libjni")
        }
    }
}