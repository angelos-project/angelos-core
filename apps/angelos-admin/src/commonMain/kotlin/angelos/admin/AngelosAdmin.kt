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

open class AngelosAdmin(prepare: Application.() -> Unit) : Application(prepare) {
    override suspend fun initialize() {
        println("Initializer")
    }

    override suspend fun finalize() {
        println("Finalizer")
    }

    override suspend fun execute() {
        println("Main loop")
    }
}