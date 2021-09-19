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

class Dir(path: RealPath) : FileObject(path), Iterable<FileObject> {

    class DirIterator(private val dir: Dir): Iterator<FileObject>{
        private var _dir: Any? = null
        private var _entry: FileEntry = FileEntry("", 0)

        init {
            _dir = FileSystem.openDir(dir.path.toString())
            _entry = FileSystem.readDir(_dir!!)
        }

        override fun hasNext(): Boolean = _entry.second != 0

        override fun next(): FileObject {
            val current = _entry
            _entry = FileSystem.readDir(_dir!!)

            if(_entry.second == 0){
                FileSystem.closeDir(_dir!!)
                _dir = null
            }

            return RealPath.getItem(dir.path.join(current.first), RealPath.getType(current.second))
        }

        protected fun finalize(){
            if(_dir != null)
                FileSystem.closeDir(_dir!!)
        }
    }

    override fun iterator(): DirIterator = DirIterator(this)

    fun walk(recursive: Boolean = true) = FileTreeWalk(this, Int.MAX_VALUE)
    fun walk(maxDepth: Int) = FileTreeWalk(this, maxDepth)
}

internal typealias FileEntry = Pair<String, Int>

