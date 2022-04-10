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
package angelos.ioc

import co.touchlab.stately.collections.sharedMutableMapOf
import co.touchlab.stately.concurrency.AtomicReference

abstract class Container<N, M: Module> {
    private val config = AtomicReference<Config<N, M>>(mapOf())
    private val modules = sharedMutableMapOf<N, M>()

    fun config(block: () -> Config<N, M>) { config.set(block()) }

    fun reg(key: N, module: M): M {
        modules[key] = module
        return module
    }

    @Suppress("UNCHECKED_CAST")
    open operator fun <M2: M>get(key: N): M2 = when(key) {
        in modules -> modules[key]!!
        in config.get() -> config.get()[key]!!(key)
        else -> throw ContainerException("$key is not configured!")
    } as M2
}