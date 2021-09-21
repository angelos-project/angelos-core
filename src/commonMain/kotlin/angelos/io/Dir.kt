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
        private var _dir: Long = 0
        private var _entry: FileEntry = FileEntry("", 0)

        init {
            _dir = FileSystem.openDir(dir.path.toString())
            _entry = FileSystem.readDir(_dir)
        }

        override fun hasNext(): Boolean = _entry.number != 0

        override fun next(): FileObject {
            val current = _entry
            _entry = FileSystem.readDir(_dir)

            if(_entry.number == 0){
                FileSystem.closeDir(_dir)
                _dir = 0
            }

            return RealPath.getItem(dir.path.join(current.name), RealPath.getType(current.number))
        }

        protected fun finalize(){
            if(_dir != 0L) {
                FileSystem.closeDir(_dir)
            }
        }
    }

    class FileEntry(val name: String, val number: Int)

    override fun iterator(): DirIterator = DirIterator(this)

    fun walk(recursive: Boolean = true) = FileTreeWalk(this, Int.MAX_VALUE)
    fun walk(maxDepth: Int) = FileTreeWalk(this, maxDepth)
}

