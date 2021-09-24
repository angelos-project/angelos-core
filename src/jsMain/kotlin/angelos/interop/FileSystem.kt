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

internal actual class FileSystem {
    actual companion object {
        @ExperimentalUnsignedTypes
        actual inline fun readFile(number: Int, array: ByteArray, index: Int, count: Long): Long {TODO("Not yet implemented")}

        @ExperimentalUnsignedTypes
        actual inline fun writeFile(number: Int, array: ByteArray, index: Int, count: Long): Long {TODO("Not yet implemented")}
        actual inline fun tellFile(number: Int): Long {TODO("Not yet implemented")}
        actual inline fun seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): Long {TODO("Not yet implemented")}
        actual inline fun closeFile(number: Int): Boolean {TODO("Not yet implemented")}

        actual inline fun checkReadable(path: String): Boolean {TODO("Not yet implemented")}
        actual inline fun checkWritable(path: String): Boolean {TODO("Not yet implemented")}
        actual inline fun checkExecutable(path: String): Boolean {TODO("Not yet implemented")}
        actual inline fun checkExists(path: String): Boolean {TODO("Not yet implemented")}
        actual inline fun getFileType(path: String): Int {TODO("Not yet implemented")}
        actual inline fun getFileInfo(path: String): FileObject.Info {TODO("Not yet implemented")}
        actual inline fun getLinkTarget(path: String): String {TODO("Not yet implemented")}
        actual inline fun openDir(path: String): Long {TODO("Not yet implemented")}
        actual inline fun readDir(dir: Long): FileEntry {TODO("Not yet implemented")}
        actual inline fun closeDir(dir: Long): Boolean {TODO("Not yet implemented")}
        actual inline fun openFile(path: String, option: Int): Int {TODO("Not yet implemented")}

    }
}