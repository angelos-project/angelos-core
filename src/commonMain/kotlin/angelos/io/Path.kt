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

abstract class Path (path: String, separator: PathSeparator = PathSeparator.POSIX) {
    internal val path: MutableList<String> = path.split(separator.toChar()) as MutableList<String>
    internal val separator: PathSeparator = separator

    override fun toString(): String = path.joinToString(separator.toString())
}
