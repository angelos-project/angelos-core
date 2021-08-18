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

enum class PathSeparator(val separator: Char) {
    POSIX('/'),
    WINDOWS('\\');

    fun toChar(): Char {
        return separator
    }

    override fun toString(): String {
        return separator.toString()
    }
}