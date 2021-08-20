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

class Dir(path: RealPath) : FileObject(path), Iterable<FileObject> {

    class DirIterator(private val dir: Dir): Iterator<FileObject>{
        private var _dir: Any? = null
        private var _entry: FileEntry = FileEntry("", 0)

        init {
            _dir = openDir(dir.path.toString())
            _entry = readDir(_dir!!)
        }

        override fun hasNext(): Boolean = _entry.second != 0

        override fun next(): FileObject {
            val current = _entry
            _entry = readDir(_dir!!)

            if(_entry.second == 0){
                closeDir(_dir!!)
                _dir = null
            }

            return RealPath.getItem(dir.path.join(current.first), RealPath.getType(current.second))
        }

        protected fun finalize(){
            if(_dir != null)
                closeDir(_dir!!)
        }
    }

    override fun iterator(): DirIterator = DirIterator(this)
}

internal typealias FileEntry = Pair<String, Int>

