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
    val skip = path.path.last() == "." || path.path.last() == ".."

    class DirIterator(private val dir: Dir): Iterator<FileObject>{
        private var _dir = dir.path.store.openDir(dir.path.toString())
        private var _entry = dir.path.store.readDir(_dir)

        override fun hasNext(): Boolean = _entry.number != 0

        override fun next(): FileObject {
            val current = _entry
            _entry = dir.path.store.readDir(_dir)

            if(_entry.number == 0){
                dir.path.store.closeDir(_dir)
                _dir = 0
            }

            return RealPath.getItem(dir.path.join(current.name), RealPath.getType(current.number))
        }

        protected fun finalize(){
            if(_dir != 0L) {
                dir.path.store.closeDir(_dir)
            }
        }
    }

    class FileEntry(val name: String, val number: Int)

    override fun iterator(): DirIterator = DirIterator(this)

    fun walk(recursive: Boolean = true) = FileTreeWalk(this, Int.MAX_VALUE)
    fun walk(maxDepth: Int) = FileTreeWalk(this, maxDepth)
}

