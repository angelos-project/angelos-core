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

import co.touchlab.stately.collections.sharedMutableListOf
import co.touchlab.stately.collections.sharedMutableMapOf
import co.touchlab.stately.concurrency.AtomicReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

abstract class Application {
    internal val serviceInitiators = sharedMutableMapOf<String, () -> Service>()
    internal val serviceInstances = sharedMutableMapOf<String, AtomicReference<Service>>()
    internal val serviceUseOrder = sharedMutableListOf<AtomicReference<Service>>()

    fun <S: Service>lazyService(initiator: () -> S) = ServiceInitializer(initiator)
    protected fun runCleanUp() = serviceUseOrder.asReversed().forEach { it.get().cleanup() }

    fun run(action: suspend () -> Unit) = runBlocking {
        launch {
            initialize()
            execute()
            action()
            finalize()
        }
    }

    abstract suspend fun execute()
    abstract suspend fun initialize()
    abstract suspend fun finalize()
}

