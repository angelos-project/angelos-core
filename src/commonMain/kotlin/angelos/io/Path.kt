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

import kotlin.jvm.JvmStatic

typealias PathElements = Triple<String, List<String>, PathSeparator>

/**
 * Path abstract class.
 */
abstract class Path internal constructor(val root: String, val path: List<String>, val separator: PathSeparator) {
    val absolute: Boolean = root.isNotEmpty()

    constructor(elements: PathElements): this(elements.first, elements.second, elements.third)

    constructor(path: String, separator: PathSeparator = PathSeparator.POSIX) : this(getElements(path, separator)) {
        if (path.isEmpty())
            throw IllegalArgumentException()
    }

    companion object {
        @JvmStatic
        protected inline fun splitString(path: String, separator: PathSeparator): List<String> =
            path.split(separator.toChar())

        @JvmStatic
        protected inline fun getElements(
            path: String,
            separator: PathSeparator,
        ): PathElements = when {
            separator == PathSeparator.WINDOWS && path.contains(windowsRegex) -> PathElements(path.substring(0..2),
                path.substring(3..path.length).split(separator.toChar()),
                separator)
            separator == PathSeparator.POSIX && path.contains(posixRegex) -> PathElements("/",
                path.substring(1..path.length).split(separator.toChar()),
                separator)
            else -> PathElements("", path.split(separator.toChar()), separator)
        }

        val windowsRegex: Regex = Regex.fromLiteral("""^[a-zA-Z]:\\""")
        val posixRegex: Regex = Regex.fromLiteral("""^\/""")
    }

    abstract fun join(vararg elements: String): Path
    abstract fun join(path: String): Path

    /**
     * Builds a complete file path.
     */
    protected inline fun joinStrings(elements: List<String> = listOf()): String =
        root + (path + elements).joinToString(separator.toString())

    override fun toString(): String = joinStrings()
}
