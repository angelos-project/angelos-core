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

internal actual inline fun checkReadable(path: String): Boolean {
    TODO("Not yet implemented")
}

internal actual inline fun checkWritable(path: String): Boolean {
    TODO("Not yet implemented")
}

internal actual inline fun checkExecutable(path: String): Boolean {
    TODO("Not yet implemented")
}

internal actual inline fun checkExists(path: String): Boolean {
    TODO("Not yet implemented")
}

internal actual inline fun getFileType(path: String): Int {
    TODO("Not yet implemented")
}

internal actual inline fun getLinkTarget(path: String): String {
    TODO("Not yet implemented")
}

internal actual inline fun openDir(path: String): Any {
    TODO("Not yet implemented")
}

internal actual inline fun readDir(dir: Any): FileEntry {
    TODO("Not yet implemented")
}

/**
 * FILESYSTEM LINKS
 * Kotlin/Native - posix -> 05_posix.knm
 * Kotlin/Native - posix -> 06_posix.knm
 * Kotlin/Native - posix -> 23_posix.knm
 * Kotlin/Native - posix -> 24_posix.knm
 */
internal actual inline fun closeDir(dir: Any): Boolean {
    TODO("Not yet implemented")
}

internal actual inline fun getFileInfo(path: String): FileObject.Info {
    TODO("Not yet implemented")
}

internal actual inline fun openFile(path: String, option: Int): Int {
    TODO("Not yet implemented")
}