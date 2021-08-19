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

import java.io.File
import java.nio.file.Files


internal actual inline fun checkReadable(path: String): Boolean {
    val file = File(path)
    return Files.isReadable(file.toPath())
}

internal actual inline fun checkWritable(path: String): Boolean {
    val file = File(path)
    return Files.isWritable(file.toPath())
}

internal actual inline fun checkExecutable(path: String): Boolean {
    val file = File(path)
    return Files.isExecutable(file.toPath())
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