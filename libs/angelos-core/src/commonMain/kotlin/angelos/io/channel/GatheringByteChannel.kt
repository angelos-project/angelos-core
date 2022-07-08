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

import org.angproj.io.buf.MutableBuffer

interface GatheringByteChannel<W: MutableBuffer>: WritableByteChannel<W> {
    suspend fun write(srcs: List<W>): Long
    suspend fun write(srcs: List<W>, offset: Int, length: Int): Long
}