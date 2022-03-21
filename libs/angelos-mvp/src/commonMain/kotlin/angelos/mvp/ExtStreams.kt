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
 *      Kristoffer Paulsson - port from python
 */
package angelos.mvp

import angelos.io.stdio.Streams

class ExtStreams(private val watcher: ExtWatcher) : Extension, Streams {
    override val identifier: String
        get() = "stdio"

    init {
        watchableReg()
    }

    private fun watchableReg() {
        watcher.register(Streams.stdIn, {
            // Read from stream and broadcast to queue
        })
    }

    override fun setup() { }
    override fun cleanup() { }
}