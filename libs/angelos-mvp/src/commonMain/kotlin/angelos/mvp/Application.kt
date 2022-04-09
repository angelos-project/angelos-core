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

import angelos.ioc.Config
import angelos.ioc.Container


abstract class Application(setup: Container<String, Service>.() -> Config<String, Service>) :
    Container<String, Service>(setup) {

    suspend fun run() {
        initialize()
        execute()
        finalize()
    }

    abstract suspend fun execute()
    abstract suspend fun initialize()
    abstract suspend fun finalize()
}