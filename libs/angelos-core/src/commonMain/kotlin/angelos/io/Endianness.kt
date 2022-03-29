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

enum class Endianness(val endian: Boolean) {
    BIG_ENDIAN(false),
    LITTLE_ENDIAN(true);

    fun isBig(): Boolean = endian == BIG_ENDIAN.endian
    fun isLittle(): Boolean = endian == LITTLE_ENDIAN.endian

    companion object {
        fun nativeOrder(): Endianness = when(Base.getEndian()) {
            1 -> BIG_ENDIAN
            2 -> LITTLE_ENDIAN
            else -> throw RuntimeException("Unknown type of endian.")
        }
    }

    override fun toString(): String = when(endian) {
        false -> "BIG_ENDIAN"
        true -> "LITTLE_ENDIAN"
    }
}