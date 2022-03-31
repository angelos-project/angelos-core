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

import angelos.io.MutableNativeByteBufferImpl
import angelos.io.channel.WritableByteChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Output : Stream(StdNum.STDOUT.fileNum), WritableByteChannel<MutableNativeByteBufferImpl> {
    private val mutex = Mutex()
    override suspend fun write(src: MutableNativeByteBufferImpl): Long  = mutex.withLock {
        TODO("Not yet implemented")
    }
}