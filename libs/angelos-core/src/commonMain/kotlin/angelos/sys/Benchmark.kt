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
package angelos.sys

import angelos.interop.Base

class Benchmark(val seconds: Long, val memory: Long, val writes: Long, val reads: Long) {

    override fun toString(): String = "µs: $seconds, kB: ${memory}, Ins: $writes, Outs: $reads"

    companion object {
        fun measure(block: () -> Unit): Benchmark {
            val start = Base.startUsage()
            block()
            return Base.endUsage(start)
        }

        suspend fun measureCoro(block: suspend () -> Unit): Benchmark {
            val start = Base.startUsage()
            block()
            return Base.endUsage(start)
        }
    }
}