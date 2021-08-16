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
package angelos.nio


enum class ByteOrder(val order: Boolean){
    BIG_ENDIAN(false),
    LITTLE_ENDIAN(true);
    companion object{
        fun nativeOrder(): ByteOrder{
            val value: UShort = 1u
            return if (value and 0x00FFu > 0u) LITTLE_ENDIAN else BIG_ENDIAN
        }
    }

    override fun toString(): String {
        return if (this == BIG_ENDIAN) "BIG_ENDIAN" else "LITTLE_ENDIAN"
    }
}