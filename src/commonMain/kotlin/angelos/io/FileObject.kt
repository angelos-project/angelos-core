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

open class FileObject (path: Path){
    private val name: String = path.path.last()
    private val suffix: String = name.substringAfter('.')
    private val path: Path = path

    val readable: Boolean
        get() = checkReadable(path.toString())

    val writable: Boolean
        get() = checkWritable(path.toString())

    val executable: Boolean
        get() = checkExecutable(path.toString())
}


internal expect inline fun checkReadable(path: String): Boolean
internal expect inline fun checkWritable(path: String): Boolean
internal expect inline fun checkExecutable(path: String): Boolean