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
package angelos.admin

import angelos.mvp.ExtQuit
import angelos.mvp.ExtSignal
import angelos.mvp.ExtStreams
import angelos.mvp.ExtWatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EntryPoint {
    companion object{
        fun start() {
            println("Hello, world!")
            runBlocking {
                launch {
                    AngelosAdmin {
                        config {
                            add(ExtSignal())
                            add(ExtQuit(this["signal"] as ExtSignal))
                            add(ExtWatcher(this["signal"] as ExtSignal))
                            add(ExtStreams(this["watcher"] as ExtWatcher, 128, false))
                        }
                        kotlin.run {
                            /*println(Streams.stdIn.isOpen())
                            println(Streams.stdOut.isOpen())
                            println(Streams.stdErr.isOpen())*/
                            //val sigName = (this["quit"] as ExtQuit).await()
                            //println("Quitting on $sigName")
                            delay(10000)
                        }
                    }
                }
            }
        }
    }
}