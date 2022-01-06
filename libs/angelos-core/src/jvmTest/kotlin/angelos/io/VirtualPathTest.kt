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

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class VirtualPathTest {
    val winPath = """C:\foo\bar\baz.txt"""
    val posixPath = """/foo/bar/baz.txt"""
    lateinit var vpw: VirtualPath
    lateinit var vpp: VirtualPath

    @Before
    fun setUp() {
        vpw = VirtualPath(winPath, PathSeparator.WINDOWS)
        vpp = VirtualPath(posixPath)
    }

    @Test
    fun join() {
        assertEquals(
            vpw.join("""hello\world\here_i_am.tmp""").toString(),
            """C:\foo\bar\baz.txt\hello\world\here_i_am.tmp"""
        )
        assertEquals(
            vpp.join("hello", "world", "here_i_am.tmp").toString(),
            """/foo/bar/baz.txt/hello/world/here_i_am.tmp"""
        )
    }

    @Test
    fun getAbsolute() {
        assertTrue(vpw.absolute)
        assertTrue(vpp.absolute)
    }

    @Test
    fun joinStrings() {
        // Joins the existing path parts and rebuilds string literal representation.
        // This method is tested in testToString().
    }

    @Test
    fun testToString() {
        assertEquals(vpw.toString(), winPath)
        assertEquals(vpp.toString(), posixPath)
    }

    @Test
    fun getRoot() {
        assertEquals(vpw.root, "C:\\")
        assertEquals(vpp.root, "/")
    }

    @Test
    fun getPath() {
        assertEquals(vpw.path[0], "foo")
        assertEquals(vpw.path[1], "bar")
        assertEquals(vpw.path[2], "baz.txt")

        assertEquals(vpp.path[0], "foo")
        assertEquals(vpp.path[1], "bar")
        assertEquals(vpp.path[2], "baz.txt")
    }

    @Test
    fun getSeparator() {
        assertEquals(vpw.separator, PathSeparator.WINDOWS)
        assertEquals(vpp.separator, PathSeparator.POSIX)
    }

    @Test
    fun splitString() {
        // Static method that splits a string using a PathSeparator.
        // This method is tested in join().
    }

    @Test
    fun getElements() {
        // Static method that parses a string literal path into root, parts and separator.
        // This method is tested implicitly in setUp().
    }
}