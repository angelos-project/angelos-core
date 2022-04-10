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
import angelos.mvp.services.SignalService

object AngelosAdmin : Application() {
    val signal by lazyService { SignalService() }

    operator fun invoke(block: AngelosAdmin.() -> Unit) = run(block)

    override suspend fun execute() {
        println("To be implemented")
    }

    override suspend fun initialize() {
        println("To be implemented")
    }

    override suspend fun finalize() {
        runCleanUp()
    }

}