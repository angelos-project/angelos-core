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

import angelos.ioc.Container

interface Application: Container<Extension, String> {
    val identifiers: MutableList<String>

    suspend operator fun invoke(i: suspend Application.() -> Unit) = i(this)

    suspend fun config(c: suspend Application.() -> Unit) {c()}
    suspend fun run(e: suspend Application.() -> Unit) {
        initialize()
        execute()
        e()
        finalize()
    }

    fun add(module: Extension) {
        identifiers.add(module.identifier)
        super.add(module.identifier, module)
    }

    suspend fun initialize() = identifiers.forEach { modules[it]!!.setup() }
    suspend fun finalize() = identifiers.asReversed().forEach { modules[it]!!.cleanup() }

    suspend fun execute() {}
}