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

import angelos.interop.Base
import angelos.io.NativeByteBufferImpl
import angelos.io.channel.ReadableByteChannel

class Input : Stream(StdNum.STDIN.fileNum), ReadableByteChannel<NativeByteBufferImpl>{

    init {
        Base.attachStream(descriptor)
    }

    override fun read(dst: NativeByteBufferImpl): Long {
        println("Hello, world!")
        TODO("Not yet implemented")
    }
}