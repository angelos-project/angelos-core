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

typealias Ioc = Container<Extension, String>

open class Application(val prepare: Application.() -> Unit): Ioc() {
    private val instances = mutableMapOf<String, Extension>()

    init {

    }

    private fun setup () = prepare(this)

    override operator fun get(identifier: String): Extension {
        if (!instances.containsKey(identifier)) {
            val ext = modules[identifier]!!
            ext()
            instances[identifier] = ext
        }
        return instances[identifier]!!
    }

    fun add(module: Extension) = add(module.identifier, module)

    protected open suspend fun initialize() {}

    protected open suspend fun finalize() {}

    protected open suspend fun execute() {}

    suspend fun run(main: suspend Ioc.() -> Unit) {
        setup()
        initialize()
        execute()
        main()
        finalize()
    }
}