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
package angelos.mvp.services

import angelos.interop.Base
import angelos.mvp.Application
import angelos.mvp.Service
import org.angproj.io.sig.Signal

class SignalService : Service(), Signal {
    override fun setup(thisRef: Application) {
        Base.initializeSignalHandler()
        println("SignalService.setup($thisRef)")
    }
}