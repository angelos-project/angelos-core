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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class FileVault (val drive: String) {
    private val mutex = Mutex()

    abstract fun getRoot(): Dir
    fun getPath(path: VirtualPath): RealPath {
        return path.toRealPath(this)
    }

    fun getDirectory(path: RealPath): Dir = if(!path.isDir())
        path.baseDir().getItem() as Dir
    else
        path.getItem() as Dir

    internal abstract fun _readFile(number: Int, array: ByteArray, index: Int, count: Long): Long
    internal abstract fun _writeFile(number: Int, array: ByteArray, index: Int, count: Long): Long
    internal abstract fun _tellFile(number: Int): Long
    internal abstract fun _seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): Long
    internal abstract fun _closeFile(number: Int): Boolean

    internal abstract fun _checkReadable(path: String): Boolean
    internal abstract fun _checkWritable(path: String): Boolean
    internal abstract fun _checkExecutable(path: String): Boolean
    internal abstract fun _checkExists(path: String): Boolean
    internal abstract fun _getFileType(path: String): Int
    internal abstract fun _getFileInfo(path: String): FileObject.Info
    internal abstract fun _getLinkTarget(path: String): String
    internal abstract fun _openDir(path: String): Long
    internal abstract fun _readDir(dir: Long): Dir.FileEntry
    internal abstract fun _closeDir(dir: Long): Boolean
    internal abstract fun _openFile(path: String, option: Int): Int

    fun readFile(number: Int, array: ByteArray, index: Int, count: Long): Long = _readFile(number, array, index, count)
    fun writeFile(number: Int, array: ByteArray, index: Int, count: Long): Long = _writeFile(number, array, index, count)
    fun tellFile(number: Int): Long = _tellFile(number)
    fun seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): Long = _seekFile(number, position, whence)
    fun closeFile(number: Int): Boolean = _closeFile(number)
    fun checkReadable(path: String): Boolean = _checkReadable(path)
    fun checkWritable(path: String): Boolean = _checkWritable(path)
    fun checkExecutable(path: String): Boolean = _checkExecutable(path)
    fun checkExists(path: String): Boolean = _checkExists(path)
    fun getFileType(path: String): Int = _getFileType(path)
    fun getFileInfo(path: String): FileObject.Info = _getFileInfo(path)
    fun getLinkTarget(path: String): String = _getLinkTarget(path)
    fun openDir(path: String): Long = _openDir(path)
    fun readDir(dir: Long): Dir.FileEntry = _readDir(dir)
    fun closeDir(dir: Long): Boolean = _closeDir(dir)
    fun openFile(path: String, option: Int): Int = _openFile(path, option)
}