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

class RealPath internal constructor(root: String, path: List<String>, separator: PathSeparator) :
    Path(root, path, separator) {

    companion object{
        internal fun wrap(path: String, separator: PathSeparator): RealPath{
            val elements = getElements(path, separator)
            return RealPath(elements.first, elements.second, elements.third)
        }
    }

    fun getItem(): FileObject {
        return when (getType()) {
            FileType.DIR -> Dir(this)
            FileType.LINK -> Link(this)
            FileType.FILE -> File(this)
            else -> throw UnsupportedOperationException()
        }
    }

    fun exists(): Boolean {
        return checkExists(this.toString())
    }

    fun isLink(): Boolean = getType() == FileType.LINK
    fun isFile(): Boolean  = getType() == FileType.FILE
    fun isDir(): Boolean  = getType() == FileType.DIR

    fun getType(): FileType = when (getFileType(this.toString())) {
        1 -> FileType.LINK
        2 -> FileType.DIR
        3 -> FileType.FILE
        else -> FileType.UNKNOWN
    }

    override fun join(vararg elements: String): RealPath = RealPath(root, path + elements, separator)
    override fun join(path: String): RealPath = RealPath(root, this.path + splitString(path, separator), separator)
}