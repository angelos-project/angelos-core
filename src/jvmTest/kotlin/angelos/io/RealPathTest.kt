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
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class RealPathTest {
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
    fun getItem() {
        assertTrue(VirtualPath(tmpDir.toString()).toRealPath().getItem() is Dir)
        assertTrue(VirtualPath(tmpFile.toString()).toRealPath().getItem() is File)
        assertTrue(VirtualPath(tmpLink.toString()).toRealPath().getItem() is Link)
        assertExceptionThrown<FileNotFoundException>({
            VirtualPath(tmpMissing.toString()).toRealPath().getItem()},
            "getItem method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun exists() {
        assertTrue(VirtualPath(tmpDir.toString()).toRealPath().exists())
        assertTrue(VirtualPath(tmpFile.toString()).toRealPath().exists())
        assertTrue(VirtualPath(tmpLink.toString()).toRealPath().exists())
        assertFalse(VirtualPath(tmpMissing.toString()).toRealPath().exists())
    }

    @Test
    fun isLink() {
        assertFalse(VirtualPath(tmpDir.toString()).toRealPath().isLink())
        assertFalse(VirtualPath(tmpFile.toString()).toRealPath().isLink())
        assertTrue(VirtualPath(tmpLink.toString()).toRealPath().isLink())
        assertExceptionThrown<FileNotFoundException>({
            VirtualPath(tmpMissing.toString()).toRealPath().isLink()},
            "isLink method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun isFile() {
        assertFalse(VirtualPath(tmpDir.toString()).toRealPath().isFile())
        assertTrue(VirtualPath(tmpFile.toString()).toRealPath().isFile())
        assertFalse(VirtualPath(tmpLink.toString()).toRealPath().isFile())
        assertExceptionThrown<FileNotFoundException>({
            VirtualPath(tmpMissing.toString()).toRealPath().isFile()},
            "isFile method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun isDir() {
        assertTrue(VirtualPath(tmpDir.toString()).toRealPath().isDir())
        assertFalse(VirtualPath(tmpFile.toString()).toRealPath().isDir())
        assertFalse(VirtualPath(tmpLink.toString()).toRealPath().isDir())
        assertExceptionThrown<FileNotFoundException>({
            VirtualPath(tmpMissing.toString()).toRealPath().isDir()},
            "isDir method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun join() {
        val winPath = """C:\foo\bar\baz.txt"""
        val posixPath = """/foo/bar/baz.txt"""
        val vpw = VirtualPath(winPath, PathSeparator.WINDOWS).toRealPath()
        val vpp = VirtualPath(posixPath).toRealPath()

        assertEquals(
            vpw.join("""hello\world\here_i_am.tmp""").toString(),
            """C:\foo\bar\baz.txt\hello\world\here_i_am.tmp"""
        )
        assertEquals(
            vpp.join("hello", "world", "here_i_am.tmp").toString(),
            """/foo/bar/baz.txt/hello/world/here_i_am.tmp"""
        )
    }
}