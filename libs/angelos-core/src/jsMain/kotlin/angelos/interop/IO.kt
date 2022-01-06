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

import angelos.io.net.Socket
import angelos.nio.Buffer
import angelos.io.FileSystem as RealFS

internal actual class IO {
    actual companion object {
        @ExperimentalUnsignedTypes
        actual inline fun readFile(number: Int, dst: Buffer, index: Int, count: Long): Long {TODO("Not yet implemented")}

        @ExperimentalUnsignedTypes
        actual inline fun writeFile(number: Int, src: Buffer, index: Int, count: Long): Long {TODO("Not yet implemented")}
        actual inline fun tellFile(number: Int): Long {TODO("Not yet implemented")}
        actual inline fun seekFile(number: Int, position: Long, whence: RealFS.Seek): Long {TODO("Not yet implemented")}
        actual inline fun closeFile(number: Int): Boolean {TODO("Not yet implemented")}

        actual inline fun checkReadable(path: String): Boolean {TODO("Not yet implemented")}
        actual inline fun checkWritable(path: String): Boolean {TODO("Not yet implemented")}
        actual inline fun checkExecutable(path: String): Boolean {TODO("Not yet implemented")}
        actual inline fun checkExists(path: String): Boolean {TODO("Not yet implemented")}
        actual inline fun getFileType(path: String): Int {TODO("Not yet implemented")}
        actual inline fun getFileInfo(path: String): RealFS.Info {TODO("Not yet implemented")}
        actual inline fun getLinkTarget(path: String): String {TODO("Not yet implemented")}
        actual inline fun openDir(path: String): Long {TODO("Not yet implemented")}
        actual inline fun readDir(dir: Long): RealFS.FileEntry {TODO("Not yet implemented")}
        actual inline fun closeDir(dir: Long): Boolean {TODO("Not yet implemented")}
        actual inline fun openFile(path: String, option: Int): Int {TODO("Not yet implemented")}

        actual inline fun serverOpen(domain: Socket.Family, type: Socket.Type, protocol: Int): Int {TODO("Not yet implemented")}
        actual inline fun serverListen(sock: Int, host: String, port: Short, domain: Socket.Family, conn: Int): Int {TODO("Not yet implemented")}
        actual inline fun serverHandle() {TODO("Not yet implemented")}
        actual inline fun serverClose() {TODO("Not yet implemented")}
        actual inline fun clientOpen(host: String, port: Short, domain: Socket.Family, type: Socket.Type, protocol: Int): Int {TODO("Not yet implemented")}
        actual inline fun clientClose() {TODO("Not yet implemented")}

    }
}