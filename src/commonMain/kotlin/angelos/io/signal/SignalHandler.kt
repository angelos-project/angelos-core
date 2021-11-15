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
package angelos.io.signal

import kotlinx.coroutines.channels.Channel

class SignalHandler internal constructor(
    val signals: List<Int>,
    private val queue: Channel<Int> = Channel()
) {
    fun send(signum: Int) = suspend { queue.send(signum) }
}