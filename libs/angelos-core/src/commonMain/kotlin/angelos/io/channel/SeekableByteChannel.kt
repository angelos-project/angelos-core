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
package angelos.io.channel

import org.angproj.io.buf.Buffer
import org.angproj.io.buf.MutableBuffer

interface SeekableByteChannel<R: Buffer, W: MutableBuffer>: ByteChannel<R, W> {
    val size: Long
    var position: Long

    fun truncate(size: Long): SeekableByteChannel<R, W>
}