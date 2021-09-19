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

import angelos.interop.Endian


enum class ByteOrder(val order: Boolean) {
    BIG_ENDIAN(true),
    LITTLE_ENDIAN(false);

    companion object {
        fun nativeOrder(): ByteOrder = if (Endian.checkNativeOrder()) LITTLE_ENDIAN else BIG_ENDIAN
    }

    override fun toString(): String {
        return if (this == BIG_ENDIAN) "BIG_ENDIAN" else "LITTLE_ENDIAN"
    }
}