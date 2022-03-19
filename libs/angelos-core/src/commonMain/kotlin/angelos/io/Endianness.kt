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

enum class Endianness(val endian: Int) {
    UNKNOWN_ENDIAN(0),
    BIG_ENDIAN(1),
    LITTLE_ENDIAN(2);

    fun isUnknown(): Boolean = endian == UNKNOWN_ENDIAN.endian
    fun isBig(): Boolean = endian == BIG_ENDIAN.endian
    fun isLittle(): Boolean = endian == LITTLE_ENDIAN.endian

    companion object {
        fun nativeOrder(): Endianness = when(Base.getEndian()) {
            BIG_ENDIAN.endian -> BIG_ENDIAN
            LITTLE_ENDIAN.endian -> LITTLE_ENDIAN
            else -> UNKNOWN_ENDIAN
        }
    }

    override fun toString(): String = when(endian) {
        BIG_ENDIAN.endian -> "BIG_ENDIAN"
        LITTLE_ENDIAN.endian -> "LITTLE_ENDIAN"
        else -> "UNKNOWN_ENDIAN"
    }
}