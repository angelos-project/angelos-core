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
 *      Kristoffer Paulsson - initial implementation
 */
package angelos.sys

import angelos.interop.Base

enum class Platform(private val api: Int) {
    UNKNOWN(0),
    POSIX(1),
    WINDOWS(2);

    fun isUnknown(): Boolean = api == UNKNOWN.api
    fun isPosix(): Boolean = api == POSIX.api
    fun isWindows(): Boolean = api == WINDOWS.api

    companion object {
        fun nativeApi(): Platform = when(Base.getPlatform()) {
            POSIX.api -> POSIX
            WINDOWS.api -> WINDOWS
            else -> UNKNOWN
        }
    }

    override fun toString(): String = when(api) {
        POSIX.api -> "POSIX"
        WINDOWS.api -> "WINDOWS"
        else -> "UNKNOWN"
    }
}
