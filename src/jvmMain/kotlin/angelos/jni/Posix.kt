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

import angelos.io.IOException

class Posix {
    companion object {
        external fun close(fd: Int): Int
        external fun read(fd: Int, buf: ByteArray?, n: Long): Long
        external fun write(fd: Int, buf: ByteArray?, n: Long): Long
        external fun lseek(fd: Int, offset: Long, whence: Int): Long
        external fun alloc()
        external fun access(path: String?, amode: Int): Int

        @Throws(IOException::class)
        external fun readlink(path: String?): String?
        external fun opendir(name: String?): Int?
        external fun readdir(dirp: Int?): ByteArray?
        external fun closedir(dirp: Int?): Int
        external fun open(path: String?, flags: Int, perm: Int): Int
        external fun isLittleEndian(): Boolean

        init {
            System.loadLibrary("libjni")
        }
    }
}