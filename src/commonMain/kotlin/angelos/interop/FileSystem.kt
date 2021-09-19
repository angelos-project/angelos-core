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
import angelos.io.FileEntry
import angelos.io.FileObject

internal expect class FileSystem {
    companion object {
        @ExperimentalUnsignedTypes
        inline fun readFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong

        @ExperimentalUnsignedTypes
        inline fun writeFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong
        inline fun tellFile(number: Int): ULong
        inline fun seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): ULong
        inline fun closeFile(number: Int): Boolean

        inline fun checkReadable(path: String): Boolean
        inline fun checkWritable(path: String): Boolean
        inline fun checkExecutable(path: String): Boolean
        inline fun checkExists(path: String): Boolean
        inline fun getFileType(path: String): Int
        inline fun getFileInfo(path: String): FileObject.Info
        inline fun getLinkTarget(path: String): String
        inline fun openDir(path: String): Any
        inline fun readDir(dir: Any): FileEntry
        inline fun closeDir(dir: Any): Boolean
        inline fun openFile(path: String, option: Int): Int
    }
}