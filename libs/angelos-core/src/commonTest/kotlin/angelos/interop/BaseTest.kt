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
package angelos.angelos.interop

import angelos.interop.Base
import angelos.io.signal.SigName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BaseTest{

    @Test
    fun getEndian() {
        assertNotEquals(Base.getEndian(), 0)
    }

    @Test
    fun getPlatform() {
        assertNotEquals(Base.getPlatform(), 0)

    }

    @Test
    fun setInterrupt() {
        // SIGFPE, SIGILL, SIGKILL, SIGQUIT, SIGSEGV are reserved by the Jvm.

        assertTrue(Base.setInterrupt(SigName.SIGABRT))
        assertTrue(Base.setInterrupt(SigName.SIGALRM))
        // assertTrue(Base.setInterrupt(SigName.SIGFPE))
        assertTrue(Base.setInterrupt(SigName.SIGHUP))
        // assertTrue(Base.setInterrupt(SigName.SIGILL))
        assertTrue(Base.setInterrupt(SigName.SIGINT))
        // assertTrue(Base.setInterrupt(SigName.SIGKILL))
        assertTrue(Base.setInterrupt(SigName.SIGPIPE))
        // assertTrue(Base.setInterrupt(SigName.SIGQUIT))
        // assertTrue(Base.setInterrupt(SigName.SIGSEGV))
        assertTrue(Base.setInterrupt(SigName.SIGTERM))
        assertTrue(Base.setInterrupt(SigName.SIGTRAP))
    }

    @Test
    fun sigAbbr() {
        assertEquals(Base.sigAbbr(6), SigName.SIGABRT.sigName)
        assertEquals(Base.sigAbbr(14), SigName.SIGALRM.sigName)
        assertEquals(Base.sigAbbr(8), SigName.SIGFPE.sigName)
        assertEquals(Base.sigAbbr(1), SigName.SIGHUP.sigName)
        assertEquals(Base.sigAbbr(4), SigName.SIGILL.sigName)
        assertEquals(Base.sigAbbr(2), SigName.SIGINT.sigName)
        assertEquals(Base.sigAbbr(9), SigName.SIGKILL.sigName)
        assertEquals(Base.sigAbbr(13), SigName.SIGPIPE.sigName)
        assertEquals(Base.sigAbbr(3), SigName.SIGQUIT.sigName)
        assertEquals(Base.sigAbbr(11), SigName.SIGSEGV.sigName)
        assertEquals(Base.sigAbbr(15), SigName.SIGTERM.sigName)
        assertEquals(Base.sigAbbr(5), SigName.SIGTRAP.sigName)
    }
}