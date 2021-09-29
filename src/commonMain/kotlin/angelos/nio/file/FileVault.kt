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
package angelos.nio.file

import angelos.io.*

abstract class FileVault (val drive: String) {
    abstract fun getRoot(): Dir
    fun getPath(path: VirtualPath): RealPath {
        return path.toRealPath(this)
    }

    fun getDirectory(path: RealPath): Dir = if(!path.isDir())
        path.baseDir().getItem() as Dir
    else
        path.getItem() as Dir

    internal abstract fun readFile(number: Int, array: ByteArray, index: Int, count: Long): Long
    internal abstract fun writeFile(number: Int, array: ByteArray, index: Int, count: Long): Long
    internal abstract fun tellFile(number: Int): Long
    internal abstract fun seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): Long
    internal abstract fun closeFile(number: Int): Boolean

    internal abstract fun checkReadable(path: String): Boolean
    internal abstract fun checkWritable(path: String): Boolean
    internal abstract fun checkExecutable(path: String): Boolean
    internal abstract fun checkExists(path: String): Boolean
    internal abstract fun getFileType(path: String): Int
    internal abstract fun getFileInfo(path: String): FileObject.Info
    internal abstract fun getLinkTarget(path: String): String
    internal abstract fun openDir(path: String): Long
    internal abstract fun readDir(dir: Long): Dir.FileEntry
    internal abstract fun closeDir(dir: Long): Boolean
    internal abstract fun openFile(path: String, option: Int): Int
}