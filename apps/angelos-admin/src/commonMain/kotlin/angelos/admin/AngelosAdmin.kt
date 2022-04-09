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
import angelos.mvp.Extension
import co.touchlab.stately.collections.IsoMutableList
import co.touchlab.stately.collections.IsoMutableMap
import co.touchlab.stately.collections.sharedMutableListOf
import co.touchlab.stately.collections.sharedMutableMapOf

object AngelosAdmin : Application {
    val modules: IsoMutableMap<String, Extension> = sharedMutableMapOf()
    val identifiers: IsoMutableList<String> = sharedMutableListOf()

    init {

    }

    override suspend fun execute() {
        println("Main loop")
    }
}