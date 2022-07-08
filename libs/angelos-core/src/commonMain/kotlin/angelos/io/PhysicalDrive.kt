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

import angelos.interop.IO
import org.angproj.io.buf.ImmutableNativeBuffer
import org.angproj.io.buf.MutableNativeBuffer
import kotlin.jvm.JvmStatic

@Suppress("OVERRIDE_BY_INLINE")
class PhysicalDrive(drive: String) : FileSystem<ImmutableNativeBuffer, MutableNativeBuffer>(drive) {
    override fun readFile(number: Int, dst: ImmutableNativeBuffer, index: Int, count: Long): Long =
        IO.readFile(number, dst, index, count)

    override fun writeFile(number: Int, src: MutableNativeBuffer, index: Int, count: Long): Long =
        IO.writeFile(number, src, index, count)

    override fun tellFile(number: Int): Long = IO.tellFile(number)
    override fun seekFile(number: Int, position: Long, whence: FileSystem.Seek): Long =
        IO.seekFile(number, position, whence)

    override fun closeFile(number: Int): Boolean = IO.closeFile(number)

    override fun checkReadable(path: String): Boolean = IO.checkReadable(path)
    override fun checkWritable(path: String): Boolean = IO.checkWritable(path)
    override fun checkExecutable(path: String): Boolean = IO.checkExecutable(path)
    override fun checkExists(path: String): Boolean = IO.checkExists(path)
    override fun getFileType(path: String): Int = IO.getFileType(path)
    override fun getFileInfo(path: String): Info = IO.getFileInfo(path)
    override fun getLinkTarget(path: String): String = IO.getLinkTarget(path)
    override fun openDir(path: String): Long = IO.openDir(path)
    override fun readDir(dir: Long): FileEntry = IO.readDir(dir)
    override fun closeDir(dir: Long): Boolean = IO.closeDir(dir)
    override fun openFile(path: String, option: Int): Int = IO.openFile(path, option)

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