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
    lateinit var drive: PhysicalDrive

    @Before
    fun setUp() {
        tmpDir = java.nio.file.Files.createTempDirectory("temporary")
        tmpFile = java.nio.file.Files.createTempFile(tmpDir, "test", ".tmp")
        tmpLink = java.nio.file.Files.createSymbolicLink(
            java.nio.file.Paths.get(tmpDir.toString(), "link.tmp"), tmpFile
        )
        tmpMissing = java.nio.file.Paths.get(tmpDir.toString(), "missing.tmp")
        drive = PhysicalDrive.createFileSystem(PhysicalDrive.Drive.UNIX)
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

    /*@Test // Now internal don't reimplement
    fun getItem() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).getItem() is FileSystem.Dir)
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).getItem() is FileSystem.File)
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).getItem() is FileSystem.Link)
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).getItem()},
            "getItem method on missing file should trigger FileNotFoundException."
        )
    } */

    /*@Test // Reimplemented in PhysicalDriveTest
    fun exists() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).exists())
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).exists())
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).exists())
        assertFalse(drive.getPath(VirtualPath(tmpMissing.toString())).exists())
    }*/


    /*@Test // Reimplemented in PhysicalDriveTest
    fun isLink() {
        assertFalse(drive.getPath(VirtualPath(tmpDir.toString())).isLink())
        assertFalse(drive.getPath(VirtualPath(tmpFile.toString())).isLink())
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).isLink())
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).isLink()},
            "isLink method on missing file should trigger FileNotFoundException."
        )
    }

    @Test // Reimplemented in PhysicalDriveTest
    fun isFile() {
        assertFalse(drive.getPath(VirtualPath(tmpDir.toString())).isFile())
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).isFile())
        assertFalse(drive.getPath(VirtualPath(tmpLink.toString())).isFile())
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).isFile()},
            "isFile method on missing file should trigger FileNotFoundException."
        )
    }

    @Test // Reimplemented in PhysicalDriveTest
    fun isDir() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).isDir())
        assertFalse(drive.getPath(VirtualPath(tmpFile.toString())).isDir())
        assertFalse(drive.getPath(VirtualPath(tmpLink.toString())).isDir())
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).isDir()},
            "isDir method on missing file should trigger FileNotFoundException."
        )
    }*/

    /*@Test // Skip over this one
    fun join() {
        val winPath = """C:\foo\bar\baz.txt"""
        val posixPath = """/foo/bar/baz.txt"""
        val vpw = VirtualPath(winPath, PathSeparator.WINDOWS).toRealPath(drive)
        val vpp = VirtualPath(posixPath).toRealPath(drive)

        assertEquals(
            vpw.join("""hello\world\here_i_am.tmp""").toString(),
            """C:\foo\bar\baz.txt\hello\world\here_i_am.tmp"""
        )
        assertEquals(
            vpp.join("hello", "world", "here_i_am.tmp").toString(),
            """/foo/bar/baz.txt/hello/world/here_i_am.tmp"""
        )
    }*/
}