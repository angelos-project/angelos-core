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
import angelos.mvp.ExtQuit
import angelos.mvp.ExtSignal
import angelos.mvp.ExtStreams
import angelos.mvp.ExtWatcher
import java.lang.ProcessHandle.current

suspend fun main(args: Array<String>) = AngelosAdmin {
    config {
        add(ExtSignal())
        add(ExtQuit(this["signal"] as ExtSignal))
        add(ExtWatcher(this["signal"] as ExtSignal))
        add(ExtStreams(this["watcher"] as ExtWatcher))
    }
    run {
        println("Hello, world! ${current().pid()}")
        val sigName = (this["quit"] as ExtQuit).await()
        println("Quitting on $sigName")
    }
}