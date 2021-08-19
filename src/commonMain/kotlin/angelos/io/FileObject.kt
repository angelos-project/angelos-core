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

    val path: RealPath
        get() = _path

    val readable: Boolean
        get() = checkReadable(_path.toString())

    val writable: Boolean
        get() = checkWritable(_path.toString())

    val executable: Boolean
        get() = checkExecutable(_path.toString())
}

internal expect inline fun checkReadable(path: String): Boolean
internal expect inline fun checkWritable(path: String): Boolean
internal expect inline fun checkExecutable(path: String): Boolean
internal expect inline fun checkExists(path: String): Boolean
internal expect inline fun getFileType(path: String): Int
internal expect inline fun getLinkTarget(path: String): String

/**
 * FILESYSTEM LINKS
 * Kotlin/Native - posix -> 05_posix.knm
 * Kotlin/Native - posix -> 06_posix.knm
 * Kotlin/Native - posix -> 23_posix.knm
 * Kotlin/Native - posix -> 24_posix.knm
 */