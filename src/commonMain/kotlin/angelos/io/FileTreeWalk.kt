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

/* class FileTreeWalk(private val dir: Dir, val maxDepth: Int) : Sequence<FileObject> {
    class TreeIterator(dir: Dir, val maxDepth: Int) : Iterator<FileObject> {
        private var _hierarchy: MutableList<Dir.DirIterator> = mutableListOf(dir.iterator())

        override fun hasNext(): Boolean {
            while(_hierarchy.size > 0 && !_hierarchy.last().hasNext())
                _hierarchy.removeLast()

            return _hierarchy.lastOrNull()?.hasNext() ?: false
        }

        override fun next(): FileObject {
            val current = _hierarchy.last().next()

            if (current is Dir && current.readable && !current!!.skip && _hierarchy.size < maxDepth)
                _hierarchy.add(current.iterator())

            return current
        }
    }

    override fun iterator(): TreeIterator {
        return TreeIterator(dir, maxDepth)
    }
}*/