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
package angelos.io

import angelos.interop.Base

enum class Endianness(private val order: Int) {
    UNKNOWN_ENDIAN(0),
    BIG_ENDIAN(1),
    LITTLE_ENDIAN(2);

    fun isUnknown(): Boolean = order == UNKNOWN_ENDIAN.order
    fun isBig(): Boolean = order == BIG_ENDIAN.order
    fun isLittle(): Boolean = order == LITTLE_ENDIAN.order

    companion object {
        fun nativeOrder(): Endianness = when(Base.getEndian()) {
            BIG_ENDIAN.order -> BIG_ENDIAN
            LITTLE_ENDIAN.order -> LITTLE_ENDIAN
            else -> UNKNOWN_ENDIAN
        }
    }

    override fun toString(): String = when(order) {
        BIG_ENDIAN.order -> "BIG_ENDIAN"
        LITTLE_ENDIAN.order -> "LITTLE_ENDIAN"
        else -> "UNKNOWN_ENDIAN"
    }
}