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
package angelos.nio.channels

import angelos.nio.ByteBuffer
import angelos.nio.file.Path
import kotlin.jvm.JvmStatic

class AsynchronousFileChannel private constructor() : Any(), AsynchronousChannel {
    fun force(metaData: Boolean) {
        TODO("Not yet implemented")
    }

    //fun lock(): Future<FileLock> {
    //    TODO("Not yet implemented")
    //}

    //fun <A> lock(attachment: A, handler: CompletionHandler<FileLock, A>) {
    //    TODO("Not yet implemented")
    //}

    //fun lock(position: Long, size: Long, shared: Boolean): Future<FileLock> {
    //    TODO("Not yet implemented")
    //}

    //fun <A> lock(position: Long, size: Long, shared: Boolean, attachment: A, handler: CompletionHandler<FileLock, A>) {
    //    TODO("Not yet implemented")
    //}

    companion object {
        //@JvmStatic
        //fun open(file: Path, vararg options: OpenOption): AsynchronousFileChannel {
        //    TODO("Not yet implemented")
        //}

        //@JvmStatic
        //fun open(file: Path, options: Set<OpenOption>, executor: ExecutorService, vararg attrs: FileAttribute) {
        //    TODO("Not yet implemented")
        //}
    }

    //fun read(dst: ByteBuffer, position: Long): Future<Int> {
    //    TODO("Not yet implemented")
    //}

    //fun <A> read(dst: ByteBuffer, position: Long, attachment: A, handler: CompletionHandler<Int, A>) {
    //    TODO("Not yet implemented")
    //}

    fun size(): Long {
        TODO("Not yet implemented")
    }

    fun truncate(size: Long): AsynchronousFileChannel {
        TODO("Not yet implemented")
    }

    //fun tryLock(): FileLock {
    //    TODO("Not yet implemented")
    //}

    //fun tryLock(position: Long, size: Long, shared: Boolean): FileLock {
    //    TODO("Not yet implemented")
    //}

    //fun write(src: ByteBuffer, position: Long): Future<Int> {
    //    TODO("Not yet implemented")
    //}

    //fun <A> write(src: ByteBuffer, position: Long, attachment: A, handler: CompletionHandler<Int, A>) {
    //    TODO("Not yet implemented")
    //}

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun isOpen(): Boolean {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean{
        TODO("Not yet implemented")
    }

    override fun hashCode(): Int{
        TODO("Not yet implemented")
    }

    override fun toString(): String{
        TODO("Not yet implemented")
    }

}