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

internal actual class Internals {
    actual companion object {
        actual fun launch(block: suspend CoroutineScope.() -> Unit) {
            error("Only for use in Kotlin: Jvm + Native.")
        }
    }
}