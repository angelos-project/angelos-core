/**
 * Copyright (c) 2021-2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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

import angelos.io.signal.Signal
import angelos.io.signal.SignalHandler
import angelos.io.signal.SignalQueue

class ExtSignal : Extension, Signal {
    override val identifier: String
        get() = "signal"

    override fun setup() { }
    override fun cleanup() { }

    override val handlers: MutableList<SignalHandler> = mutableListOf()
    override val signals: MutableList<Int> = mutableListOf()
    override val map: HashMap<Int, SignalHandler> = hashMapOf()

    fun build(queue: SignalQueue, vararg signums: Int): SignalHandler = SignalHandler(signums.toList(), queue)
}