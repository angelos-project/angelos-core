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
package angelos.io

open class FileObject (path: RealPath){
    private val name: String = path.path.last()
    private val suffix: String = name.substringAfter('.')
    private val _path: RealPath = path
    protected val _info: Info by lazy { getFileInfo(_path.toString()) }

    val path: RealPath
        get() = _path

    val user: UInt
        get() = _info.user

    val group: UInt
        get() = _info.group

    val lastAccessed: Long
        get() = _info.accessedAt

    val lastModified: Long
        get() = _info.modifiedAt

    val changed: Long
        get() = _info.changedAt

    val created: Long
        get() = _info.createdAt

    val readable: Boolean
        get() = checkReadable(_path.toString())

    val writable: Boolean
        get() = checkWritable(_path.toString())

    val executable: Boolean
        get() = checkExecutable(_path.toString())

    enum class Type {
        UNKNOWN,
        FILE,
        LINK,
        DIR;
    }

    data class Info(
        val user: UInt,
        val group: UInt,
        val accessedAt: Long,
        val modifiedAt: Long,
        val changedAt: Long,
        val createdAt: Long,
        val size: Long,
    )
}

internal expect inline fun checkReadable(path: String): Boolean
internal expect inline fun checkWritable(path: String): Boolean
internal expect inline fun checkExecutable(path: String): Boolean
internal expect inline fun checkExists(path: String): Boolean
internal expect inline fun getFileType(path: String): Int
internal expect inline fun getFileInfo(path: String): FileObject.Info
internal expect inline fun getLinkTarget(path: String): String
internal expect inline fun openDir(path: String): Any
internal expect inline fun readDir(dir: Any): FileEntry
internal expect inline fun closeDir(dir: Any): Boolean
internal expect inline fun openFile(path: String, option: Int): Int

/**
 * FILESYSTEM LINKS
 * Kotlin/Native - posix -> 05_posix.knm
 * Kotlin/Native - posix -> 06_posix.knm
 * Kotlin/Native - posix -> 23_posix.knm
 * Kotlin/Native - posix -> 24_posix.knm
 */