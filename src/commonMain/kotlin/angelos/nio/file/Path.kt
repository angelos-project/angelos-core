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
package angelos.nio.file

import angelos.io.File
import angelos.net.URI

class Path : Comparable<Path>, Iterable<Path>, Watchable {
    override fun compareTo(other: Path): Int {
        TODO("Not yet implemented")
    }

    fun endsWith(other: Path): Boolean {
        TODO("Not yet implemented")
    }

    fun endsWith(other: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }

    fun getFileName(): Path {
        TODO("Not yet implemented")
    }

    //fun getFileSystem(): FileSystem {
//
  //  }

    fun getName(index: Int): Path {
        TODO("Not yet implemented")
    }

    fun getNameCount(): Int {
        TODO("Not yet implemented")
    }

    fun getParent(): Path {
        TODO("Not yet implemented")
    }

    fun getRoot(): Path {
        TODO("Not yet implemented")
    }

    override fun hashCode(): Int {
        TODO("Not yet implemented")
    }

    fun isAbsolute(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<Path> {
        TODO("Not yet implemented")
    }

    fun normalize(): Path {
        TODO("Not yet implemented")
    }

    fun <T> register(watcher: WatchService, vararg events: WatchEvent.Kind<T>) {
        TODO("Not yet implemented")
    }

    fun <T> register(watcher: WatchService, events: List<WatchEvent.Kind<T>>, vararg modifiers: WatchEvent.Modifier) {
        TODO("Not yet implemented")
    }

    fun relativize(other: Path): Path {
        TODO("Not yet implemented")
    }

    fun resolve(other: Path): Path {
        TODO("Not yet implemented")
    }

    fun resolve(other: String): Path {
        TODO("Not yet implemented")
    }

    fun resolveSibling(other: Path): Path {
        TODO("Not yet implemented")
    }

    fun resolveSibling(other: String): Path {
        TODO("Not yet implemented")
    }

    fun startsWith(other: Path): Boolean {
        TODO("Not yet implemented")
    }

    fun startsWith(other: String): Boolean {
        TODO("Not yet implemented")
    }

    fun subpath(beginIndex: Int, endIndex: Int): Path {
        TODO("Not yet implemented")
    }

    fun toAbsolutePath(): Path {
        TODO("Not yet implemented")
    }

    fun toFile(): File {
        TODO("Not yet implemented")
    }

    //fun toRealPath(vararg options: LinkOption): Path {
//
  //  }

    override fun toString(): String {
        TODO("Not yet implemented")
    }

    fun toUri(): URI {
        TODO("Not yet implemented")
    }
}