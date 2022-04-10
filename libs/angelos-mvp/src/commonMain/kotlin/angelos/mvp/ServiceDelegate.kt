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

import co.touchlab.stately.concurrency.AtomicReference
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ServiceDelegate<S: Service> : ReadOnlyProperty<Application, S> {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Application, property: KProperty<*>): S = when(val name = property.name) {
        in thisRef.serviceInstances -> thisRef.serviceInstances[name]!!.get()
        in thisRef.serviceInitiators -> AtomicReference(thisRef.serviceInitiators[name]!!() as S).also {
            it as AtomicReference<Service>
            thisRef.serviceUseOrder.add(it)
            thisRef.serviceInstances[name] = it
            thisRef.serviceInitiators.remove(name)
            it.get().setup()
        }.get()
        else -> throw ApplicationException("Error due to misconfiguration of service '$name'")
    } as S
}