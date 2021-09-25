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
package angelos.io

import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class LinkTest {
    lateinit var tmpDir: java.nio.file.Path
    lateinit var tmpFile: java.nio.file.Path
    lateinit var tmpLink: java.nio.file.Path
    lateinit var tmpMissing: java.nio.file.Path

    @Before
    fun setUp() {
        tmpDir = java.nio.file.Files.createTempDirectory("temporary")
        tmpFile = java.nio.file.Files.createTempFile(tmpDir, "test", ".tmp")
        tmpLink = java.nio.file.Files.createSymbolicLink(
            java.nio.file.Paths.get(tmpDir.toString(), "link.tmp"), tmpFile)
        tmpMissing = java.nio.file.Paths.get(tmpDir.toString(), "missing.tmp")
    }

    @After
    fun tearDown() {
        java.nio.file.Files.deleteIfExists(tmpFile)
        java.nio.file.Files.deleteIfExists(tmpLink)
        java.nio.file.Files.deleteIfExists(tmpDir)
    }

    private inline fun <reified E : Exception> assertExceptionThrown(test: () -> Unit, message: String) {
        var happened = false
        try {
            test()
        } catch (e: Exception) {
            if (e is E) {
                happened = true
            } else {
                throw e
            }
        } finally {
            kotlin.test.assertEquals(happened, true, message)
        }
    }

    @Test
    fun getTarget() {
        assertExceptionThrown<NotLinkException>({
            Link(VirtualPath(tmpDir.toString()).toRealPath()).target},
            "Target property on non-link should trigger NotLinkException."
        )
        assertExceptionThrown<NotLinkException>({
            Link(VirtualPath(tmpFile.toString()).toRealPath()).target},
            "getItem method on missing file should trigger NotLinkException."
        )
        assertEquals(Link(VirtualPath(tmpLink.toString()).toRealPath()).target, tmpFile.toString())
        assertExceptionThrown<NotLinkException>({
            Link(VirtualPath(tmpMissing.toString()).toRealPath()).target},
            "getItem method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun goToTarget() {
        assertExceptionThrown<NotLinkException>({
            Link(VirtualPath(tmpDir.toString()).toRealPath()).goToTarget()},
            "goToTarget method to non-link should trigger NotLinkException."
        )
        assertExceptionThrown<NotLinkException>({
            Link(VirtualPath(tmpFile.toString()).toRealPath()).goToTarget()},
            "goToTarget method to non-link file should trigger NotLinkException."
        )
        assertEquals(
            Link(VirtualPath(tmpLink.toString()).toRealPath()).goToTarget().path.toString(),
            File(VirtualPath(tmpFile.toString()).toRealPath()).path.toString()
        )
        assertExceptionThrown<NotLinkException>({
            Link(VirtualPath(tmpMissing.toString()).toRealPath()).goToTarget()},
            "goToTarget method to missing file should trigger FileNotFoundException."
        )
    }
}