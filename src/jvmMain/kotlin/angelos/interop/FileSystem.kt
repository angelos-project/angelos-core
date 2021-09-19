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

enum class AccessFlag(val flag: Int){
    F_OK(0),
    X_OK(0x01),
    W_OK(0x02),
    R_OK(0x04)
}

internal actual class FileSystem {
    actual companion object {
        @ExperimentalUnsignedTypes
        actual inline fun readFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong {TODO("Not yet implemented")}

        @ExperimentalUnsignedTypes
        actual inline fun writeFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong {TODO("Not yet implemented")}
        actual inline fun tellFile(number: Int): ULong {TODO("Not yet implemented")}
        actual inline fun seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): ULong {TODO("Not yet implemented")}
        actual inline fun closeFile(number: Int): Boolean {TODO("Not yet implemented")}

        actual inline fun checkReadable(path: String): Boolean = fs_access(path, AccessFlag.R_OK.flag) == 0
        actual inline fun checkWritable(path: String): Boolean = fs_access(path, AccessFlag.W_OK.flag) == 0
        actual inline fun checkExecutable(path: String): Boolean = fs_access(path, AccessFlag.X_OK.flag) == 0
        actual inline fun checkExists(path: String): Boolean = fs_access(path, AccessFlag.F_OK.flag) == 0
        actual inline fun getFileType(path: String): Int {TODO("Not yet implemented")}
        actual inline fun getFileInfo(path: String): FileObject.Info {TODO("Not yet implemented")}
        actual inline fun getLinkTarget(path: String): String {TODO("Not yet implemented")}
        actual inline fun openDir(path: String): Any {TODO("Not yet implemented")}
        actual inline fun readDir(dir: Any): FileEntry {TODO("Not yet implemented")}
        actual inline fun closeDir(dir: Any): Boolean {TODO("Not yet implemented")}
        actual inline fun openFile(path: String, option: Int): Int {TODO("Not yet implemented")}

        @JvmStatic
        private external fun fs_access(path: String, amode: Int): Int

        init {
            System.loadLibrary("jnifilesystem")
        }
    }
}