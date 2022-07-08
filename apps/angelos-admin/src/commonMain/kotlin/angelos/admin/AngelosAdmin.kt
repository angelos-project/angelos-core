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
package angelos.admin

import angelos.mvp.Application
import angelos.mvp.lazyService
import angelos.mvp.services.ExitService
import angelos.mvp.services.SignalService
import angelos.mvp.services.StreamsService
import angelos.mvp.services.WatcherService

object AngelosAdmin : Application() {
    val signal by lazyService { SignalService() }
    val exit by lazyService { ExitService(signal) }
    val watcher by lazyService { WatcherService(signal) }
    val streams by lazyService { StreamsService(watcher, true) }

    operator fun invoke(action: suspend AngelosAdmin.() -> Unit) { execute { this@AngelosAdmin.action() } }

    override suspend fun initialize() {
        println("Runs initialize")
    }

    override suspend fun finalize() {
        println("Runs finalize")
    }
}