/**
 * Copyright (c) 2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


abstract class Application: CoroutineScope {
    internal val serviceInitiators = mutableMapOf<String, () -> Service>()
    internal val serviceInstances = mutableMapOf<String, Service>()
    internal val serviceUseOrder = mutableListOf<Service>()

    override val coroutineContext: CoroutineContext
        get() = CoroutineScope(EmptyCoroutineContext).coroutineContext

    protected abstract suspend fun initialize()
    protected abstract suspend fun finalize()

    fun execute(action: suspend () -> Unit) {
        Internals.launch{
            begin()
            action()
            end()
        }
    }

    private suspend fun begin() {
        initialize()
    }

    private suspend fun end() {
        finalize()
        runCleanUp()
    }

    private fun runCleanUp() = serviceUseOrder.asReversed().forEach { it.cleanup(this) }
}