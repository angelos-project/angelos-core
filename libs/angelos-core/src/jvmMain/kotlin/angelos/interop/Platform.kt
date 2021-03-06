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

import java.lang.System

internal actual class Platform {
    actual companion object {
        actual inline fun isLittleEndian(): Boolean = endian()
        actual inline fun getPlatform(): Int = platform()

        @JvmStatic
        private external fun endian(): Boolean

        @JvmStatic
        private external fun platform(): Int

        init {
            System.loadLibrary("jni-platform")
        }


    }
}