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

import angelos.io.FileSystem as RealFS
import angelos.nio.Buffer

internal expect class FileSystem {
    companion object {
        inline fun readFile(number: Int, dst: Buffer, index: Int, count: Long): Long
        inline fun writeFile(number: Int, src: Buffer, index: Int, count: Long): Long
        inline fun tellFile(number: Int): Long
        inline fun seekFile(number: Int, position: Long, whence: RealFS.Seek): Long
        inline fun closeFile(number: Int): Boolean

        inline fun checkReadable(path: String): Boolean
        inline fun checkWritable(path: String): Boolean
        inline fun checkExecutable(path: String): Boolean
        inline fun checkExists(path: String): Boolean
        inline fun getFileType(path: String): Int
        inline fun getFileInfo(path: String): RealFS.Info
        inline fun getLinkTarget(path: String): String
        inline fun openDir(path: String): Long
        inline fun readDir(dir: Long): RealFS.FileEntry
        inline fun closeDir(dir: Long): Boolean
        inline fun openFile(path: String, option: Int): Int
    }
}