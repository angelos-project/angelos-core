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
import angelos.admin.AngelosAdmin
import angelos.mvp.ExtSignal
import angelos.mvp.ExtQuit
import kotlinx.coroutines.delay

suspend fun main(args: Array<String>) = AngelosAdmin {
    add(ExtSignal { })
    add(ExtQuit {
        it.signal = AngelosAdmin@ this["signal"] as ExtSignal
        it.signalReg()
    })
}.run {
    delay(3000)
    println("Hello, world!")
    (this["quit"] as ExtQuit).await()
}