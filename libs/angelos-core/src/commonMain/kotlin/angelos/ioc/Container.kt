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


interface Container<M: Module, I> {
    val modules: MutableMap<I, M>

    operator fun get(identifier: I): M {
        if (!modules.containsKey(identifier))
            throw ContainerException("Module for \"$identifier\" not found")
        return modules[identifier]!!
    }

    fun add(identifier: I, module: M){
        if (modules.containsKey(identifier))
            throw ContainerException("Module with \"$identifier\" already exists")
        modules[identifier] = module
    }
}