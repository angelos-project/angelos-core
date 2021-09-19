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

import angelos.interop.FileSystem

open class FileObject (path: RealPath){
    private val name: String = path.path.last()
    private val suffix: String = name.substringAfter('.')
    private val _path: RealPath = path
    protected val _info: Info by lazy { FileSystem.getFileInfo(_path.toString()) }

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
        get() = FileSystem.checkReadable(_path.toString())

    val writable: Boolean
        get() = FileSystem.checkWritable(_path.toString())

    val executable: Boolean
        get() = FileSystem.checkExecutable(_path.toString())

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

