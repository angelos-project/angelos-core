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

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ServiceInitializer<S: Service>(val initiator: () -> S) {
    operator fun provideDelegate(thisRef: Application, prop: KProperty<*>): ReadOnlyProperty<Application, S> {
        thisRef.serviceInitiators[prop.name] = initiator
        return ServiceDelegate()
    }
}