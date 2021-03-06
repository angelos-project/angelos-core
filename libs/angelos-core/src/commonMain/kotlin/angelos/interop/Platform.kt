/**
 * Copyright (c) 2021 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
 *      Kristoffer Paulsson - initial implementation
 */
package angelos.interop

enum class System(val platform: Int) {
    POSIX(1),
    NT(2),
    UNKNOWN(0)
}

internal expect class Platform {
    companion object {
        inline fun isLittleEndian(): Boolean
        inline fun getPlatform(): Int
    }
}