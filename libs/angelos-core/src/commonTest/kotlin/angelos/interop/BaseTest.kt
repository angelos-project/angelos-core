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
package angelos.interop

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BaseTest{

    @Test
    fun getEndian() {
        Base.getEndian()
    }

    @Test
    fun getPlatform() {
        Base.getPlatform()
    }

    @Test
    fun setInterrupt() {
        assertTrue { Base.setInterrupt(2) }
        assertFalse { Base.setInterrupt(500) }
    }
}