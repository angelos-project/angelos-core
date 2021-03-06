/**
 * Copyright (c) 2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
package angelos.io.stdio

import angelos.io.channel.WritableByteChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.angproj.io.buf.MutableNativeBuffer

class Error : Stream(StdNum.STDERR.fileNum), WritableByteChannel<MutableNativeBuffer> {
    private val mutex = Mutex()
    override suspend fun write(src: MutableNativeBuffer): Long  = mutex.withLock {
        TODO("Not yet implemented")
    }
}