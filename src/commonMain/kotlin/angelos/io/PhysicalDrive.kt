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

import angelos.nio.file.FileVault
import angelos.interop.FileSystem
import kotlin.jvm.JvmStatic

@Suppress("OVERRIDE_BY_INLINE")
class PhysicalDrive(drive: String) : FileVault(drive) {
    override fun getRoot(): Dir = getDirectory(getPath(VirtualPath(drive)))

    override inline fun _readFile(number: Int, array: ByteArray, index: Int, count: Long): Long =
        FileSystem.readFile(number, array, index, count)

    override inline fun _writeFile(number: Int, array: ByteArray, index: Int, count: Long): Long =
        FileSystem.writeFile(number, array, index, count)

    override inline fun _tellFile(number: Int): Long = FileSystem.tellFile(number)
    override inline fun _seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): Long =
        FileSystem.seekFile(number, position, whence)

    override inline fun _closeFile(number: Int): Boolean = FileSystem.closeFile(number)

    override inline fun _checkReadable(path: String): Boolean = FileSystem.checkReadable(path)
    override inline fun _checkWritable(path: String): Boolean = FileSystem.checkWritable(path)
    override inline fun _checkExecutable(path: String): Boolean = FileSystem.checkExecutable(path)
    override inline fun _checkExists(path: String): Boolean = FileSystem.checkExists(path)
    override inline fun _getFileType(path: String): Int = FileSystem.getFileType(path)
    override inline fun _getFileInfo(path: String): FileObject.Info = FileSystem.getFileInfo(path)
    override inline fun _getLinkTarget(path: String): String = FileSystem.getLinkTarget(path)
    override inline fun _openDir(path: String): Long = FileSystem.openDir(path)
    override inline fun _readDir(dir: Long): Dir.FileEntry = FileSystem.readDir(dir)
    override inline fun _closeDir(dir: Long): Boolean = FileSystem.closeDir(dir)
    override inline fun _openFile(path: String, option: Int): Int = FileSystem.openFile(path, option)

    enum class Drive(val root: String){
        UNIX("/"),
        DOS_A("A:\\"),
        DOS_B("B:\\"),
        DOS_C("C:\\"),
        DOS_D("D:\\"),
        DOS_E("E:\\"),
        DOS_F("F:\\"),
        DOS_G("G:\\"),
        DOS_H("H:\\"),
        DOS_I("I:\\"),
        DOS_J("J:\\"),
        DOS_K("K:\\"),
        DOS_L("L:\\"),
        DOS_M("M:\\"),
        DOS_N("N:\\"),
        DOS_O("O:\\"),
        DOS_P("P:\\"),
        DOS_Q("Q:\\"),
        DOS_R("R:\\"),
        DOS_S("S:\\"),
        DOS_T("T:\\"),
        DOS_V("V:\\"),
        DOS_W("W:\\"),
        DOS_X("X:\\"),
        DOS_Y("Y:\\"),
        DOS_Z("Z:\\")
    }

    companion object {
        private val fileStores: FileStores = FileStores()

        @JvmStatic
        fun createFileSystem(drive: Drive): PhysicalDrive {
            if (!fileStores.contains(drive.root))
                fileStores[drive.root] = PhysicalDrive(drive.root)
            return fileStores[drive.root]!!
        }
    }
}

typealias FileStores = HashMap<String, PhysicalDrive>