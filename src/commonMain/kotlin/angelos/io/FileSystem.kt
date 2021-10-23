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

import angelos.nio.BufferOverflowException
import angelos.nio.BufferUnderflowException
import angelos.nio.ByteBuffer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class FileSystem(val drive: String) {
    private val mutex = Mutex()

    suspend fun getRoot(): Dir = getDirectory(getPath(VirtualPath(drive)))

    fun getPath(path: VirtualPath): RealPath {
        return RealPath(path.root, path.path, path.separator)
    }

    suspend fun getDirectory(path: RealPath): Dir = mutex.withLock {
        when {
            !path.isDir() -> path.baseDir().getItem() as Dir
            else -> path.getItem() as Dir
        }
    }

    suspend fun walkDirectory(dir: Dir): Flow<FileObject> = flow {
        mutex.withLock {
            for (item in dir.walk())
                emit(item)
        }
    }

    protected abstract fun readFile(number: Int, array: ByteArray, index: Int, count: Long): Long
    protected abstract fun writeFile(number: Int, array: ByteArray, index: Int, count: Long): Long
    protected abstract fun tellFile(number: Int): Long
    protected abstract fun seekFile(number: Int, position: Long, whence: Seek): Long
    protected abstract fun closeFile(number: Int): Boolean

    protected abstract fun checkReadable(path: String): Boolean
    protected abstract fun checkWritable(path: String): Boolean
    protected abstract fun checkExecutable(path: String): Boolean
    protected abstract fun checkExists(path: String): Boolean
    protected abstract fun getFileType(path: String): Int
    protected abstract fun getFileInfo(path: String): Info
    protected abstract fun getLinkTarget(path: String): String
    protected abstract fun openDir(path: String): Long
    protected abstract fun readDir(dir: Long): FileEntry
    protected abstract fun closeDir(dir: Long): Boolean
    protected abstract fun openFile(path: String, option: Int): Int

    enum class Type {
        UNKNOWN,
        FILE,
        LINK,
        DIR;
    }

    data class Info(
        val user: Int,
        val group: Int,
        val accessedAt: Long,
        val modifiedAt: Long,
        val changedAt: Long,
        val createdAt: Long,
        val size: Long,
    )

    data class FileEntry(
        val name: String,
        val number: Int,
    )

    enum class OpenOption(option: Int) {
        READ_ONLY(0),
        WRITE_ONLY(1),
        READ_WRITE(2),
    }

    enum class Seek{
        SET, CUR, END
    }

    open inner class FileObject (path: RealPath){
        protected val _path: RealPath = path
        protected val _info: Info by lazy { getFileInfo(_path.toString()) }
        private val name: String = _path.path.last()
        private val suffix: String = name.substringAfter('.')

        val path: RealPath
            get() = _path

        internal val user: Int
            get() = _info.user

        suspend fun getUser(): Int = mutex.withLock { user }

        internal val group: Int
            get() = _info.group

        suspend fun getGroup(): Int = mutex.withLock { group }

        internal val lastAccessed: Long
            get() = _info.accessedAt

        suspend fun getLastAccessed(): Long = mutex.withLock { lastAccessed }

        internal val lastModified: Long
            get() = _info.modifiedAt

        suspend fun getLastModified(): Long = mutex.withLock { lastModified }

        internal val changed: Long
            get() = _info.changedAt

        suspend fun getChanged(): Long = mutex.withLock { changed }

        internal val created: Long
            get() = _info.createdAt

        suspend fun getCreated(): Long = mutex.withLock { created }

        internal val readable: Boolean
            get() = checkReadable(_path.toString())

        suspend fun isReadable(): Boolean = mutex.withLock { readable }

        internal val writable: Boolean
            get() = checkWritable(_path.toString())

        suspend fun isWritable(): Boolean = mutex.withLock { writable }

        internal val executable: Boolean
            get() = checkExecutable(_path.toString())

        suspend fun isExecutable(): Boolean = mutex.withLock { executable }
    }

    inner class Dir(path: RealPath) : FileObject(path), Iterable<FileObject> {
        val skip = _path.path.last() == "." || _path.path.last() == ".."

        inner class DirIterator: Iterator<FileObject>{
            private var _dir = openDir(_path.toString())
            private var _entry = readDir(_dir)

            override fun hasNext(): Boolean = _entry.number != 0

            override fun next(): FileObject {
                val current = _entry
                _entry = readDir(_dir)

                if(_entry.number == 0){
                    closeDir(_dir)
                    _dir = 0
                }

                return _path.getItem(_path.join(current.name), _path.getType(current.number))
            }

            protected fun finalize(){
                if(_dir != 0L) {
                    closeDir(_dir)
                }
            }
        }

        override fun iterator(): DirIterator = DirIterator()

        fun walk(recursive: Boolean = true) = FileTreeWalk(this, Int.MAX_VALUE)
        fun walk(maxDepth: Int) = FileTreeWalk(this, maxDepth)
    }

    inner class FileTreeWalk(private val dir: Dir, val maxDepth: Int) : Sequence<FileObject> {
        inner class TreeIterator : Iterator<FileObject> {
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

        override fun iterator(): TreeIterator = TreeIterator()
    }

    inner class Link(path: RealPath) : FileObject(path) {
        private val _target: RealPath by lazy {
            _path.wrap(
                getLinkTarget(_path.toString()),
                _path.separator
            )
        }

        val target: String
            get() = _target.toString()

        fun goToTarget(): FileObject = _target.getItem()
    }

    inner class File(path: RealPath) : FileObject(path) {
        val size: Long
            get() = _info.size

        fun open(option: OpenOption): FileDescriptor = FileDescriptor(this, option, openFile(_path.toString(), option.ordinal))
    }

    @ExperimentalUnsignedTypes
    inner class FileDescriptor internal constructor(
        internal val file: File,
        val option: OpenOption,
        private var _number: Int,
    ) : Closable {
        private var _position: ULong = 0u

        val position: Long
            get() = _position.toLong()

        val number: Int
            get() = _number

        fun read(buffer: ByteBuffer, count: Long) {
            if (buffer.remaining() < count)
                throw BufferUnderflowException()
            if(readFile(_number, buffer.array(), buffer.position, count) != count)
                throw IOException("Couldn't read $count bytes from file.")
            _position += count.toULong()
        }

        fun write(buffer: ByteBuffer, count: Long) {
            if (buffer.remaining() < count)
                throw BufferOverflowException()
            if(writeFile(_number, buffer.array(), buffer.position, count) != count)
                throw IOException("Couldn't write $count bytes to file.")
            _position += count.toULong()
        }

        fun tell(): Long{
            val position = tellFile(_number)
            if (position.toULong() != _position)
                throw SyncFailedException("File descriptor out of sync with physical cursor.")
            return position
        }

        fun seek(position: Long, whence: Seek): Long {
            _position = seekFile(_number, position, whence).toULong()
            return _position.toLong()
        }

        override fun close(){
            closeFile(_number)
            _number = 0
        }
    }

    inner class RealPath constructor(root: String, path: PathList, separator: PathSeparator) :
        Path(root, path, separator) {

        fun wrap(path: String, separator: PathSeparator): RealPath {
            val elements = getElements(path, separator)
            return RealPath(elements.first, elements.second, elements.third)
        }

        internal inline fun getType(type: Int): Type = when (type) {
            1 -> Type.LINK
            2 -> Type.DIR
            3 -> Type.FILE
            else -> Type.UNKNOWN
        }

        internal inline fun getType(): Type = getType(getFileType(this.toString()))

        internal inline fun getItem(path: RealPath, type: Type): FileObject = when (type) {
            Type.DIR -> Dir(path)
            Type.LINK -> Link(path)
            Type.FILE -> File(path)
            else -> throw UnsupportedOperationException()
        }

        internal inline fun getItem(): FileObject = getItem(this, getType())

        suspend fun getDir(): Dir = mutex.withLock {
            if (getType() != Type.DIR)
                throw UnexpectedFileObject("Expected object of type 'Dir' at: $this")
            return getItem(this, Type.DIR) as Dir
        }
        suspend fun getLink(): Link = mutex.withLock {
            if (getType() != Type.LINK)
                throw UnexpectedFileObject("Expected object of type 'Link' at: $this")
            return getItem(this, Type.LINK) as Link
        }
        suspend fun getFile(): File = mutex.withLock{
            if (getType() != Type.FILE)
                throw UnexpectedFileObject("Expected object of type 'File' at: $this")
            return getItem(this, Type.DIR) as File
        }
        suspend fun exists(): Boolean = mutex.withLock { checkExists(this.toString()) }
        suspend fun isLink(): Boolean = mutex.withLock { getType() == Type.LINK }
        suspend fun isFile(): Boolean = mutex.withLock { getType() == Type.FILE }
        suspend fun isDir(): Boolean = mutex.withLock { getType() == Type.DIR }

        override fun join(vararg elements: String): RealPath =
            RealPath(root, (path + elements) as PathList, separator)
        override fun join(path: String): RealPath =
            RealPath(root, (this.path + splitString(path, separator)) as PathList, separator)

        fun baseDir(): RealPath = RealPath(root, parent(), separator)
    }
}