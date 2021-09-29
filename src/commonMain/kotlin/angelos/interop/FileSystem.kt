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
import angelos.io.FileObject

internal expect class FileSystem {
    companion object {
        internal inline fun readFile(number: Int, array: ByteArray, index: Int, count: Long): Long
        inline fun writeFile(number: Int, array: ByteArray, index: Int, count: Long): Long
        inline fun tellFile(number: Int): Long
        inline fun seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): Long
        inline fun closeFile(number: Int): Boolean

        inline fun checkReadable(path: String): Boolean
        inline fun checkWritable(path: String): Boolean
        inline fun checkExecutable(path: String): Boolean
        inline fun checkExists(path: String): Boolean
        inline fun getFileType(path: String): Int
        inline fun getFileInfo(path: String): FileObject.Info
        inline fun getLinkTarget(path: String): String
        inline fun openDir(path: String): Long
        inline fun readDir(dir: Long): FileEntry
        inline fun closeDir(dir: Long): Boolean
        inline fun openFile(path: String, option: Int): Int
    }
}