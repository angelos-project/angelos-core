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

open class Container<N, M: Module> internal constructor() {
    private lateinit var config: Config<N, M>
    private val modules = sharedMutableMapOf<N, M>()

    constructor(config: Config<N, M>): this() {
        this.config = config
    }

    constructor(setup: Container<N, M>.() -> Config<N, M>): this() {
        this.config = setup()
    }

    fun reg(key: N, module: M): M {
        modules[key] = module
        return module
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <M: Module>get(key: N): M = when(key) {
        in modules -> modules[key]!!
        in config -> config[key]!!(key)
        else -> throw ContainerException("$key is not configured!")
    } as M
}