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
package angelos.interop

import angelos.io.FileSystem
import angelos.io.net.Socket
import org.angproj.io.buf.ImmutableNativeBuffer
import org.angproj.io.buf.MutableNativeBuffer

data class PollAction(val descriptor: Int, val action: Int)

internal expect class IO {
    companion object {
        fun readFile(number: Int, dst: ImmutableNativeBuffer, index: Int, count: Long): Long
        fun writeFile(number: Int, src: MutableNativeBuffer, index: Int, count: Long): Long
        fun tellFile(number: Int): Long
        fun seekFile(number: Int, position: Long, whence: FileSystem.Seek): Long
        fun closeFile(number: Int): Boolean

        fun checkReadable(path: String): Boolean
        fun checkWritable(path: String): Boolean
        fun checkExecutable(path: String): Boolean
        fun checkExists(path: String): Boolean
        fun getFileType(path: String): Int
        fun getFileInfo(path: String): FileSystem.Info
        fun getLinkTarget(path: String): String
        fun openDir(path: String): Long
        fun readDir(dir: Long): FileSystem.FileEntry
        fun closeDir(dir: Long): Boolean
        fun openFile(path: String, option: Int): Int

        fun serverOpen(domain: Socket.Family, type: Socket.Type, protocol: Int): Int
        fun serverListen(sock: Int, host: String, port: Short, domain: Socket.Family, conn: Int): Int
        fun serverHandle()
        fun serverClose()

        fun pollAction(): PollAction

        fun clientOpen(host: String, port: Short, domain: Socket.Family, type: Socket.Type, protocol: Int): Int
        fun clientClose()

        fun streamOpen(stream: Int): Int
        fun streamClose(stream: Int)
    }
}