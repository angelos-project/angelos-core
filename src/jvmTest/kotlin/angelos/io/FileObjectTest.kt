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

import angelos.io.FileSystem.FileObject
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class FileObjectTest {
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

    @Test
    fun get_info() {
        // Loads the information about a file object.
        // This method is tested implicitly.
    }

    /* @Test
    fun getPath() {
        val rpDir = drive.getPath(VirtualPath(tmpDir.toString()))
        val rpFile = drive.getPath(VirtualPath(tmpFile.toString()))
        val rpLink = drive.getPath(VirtualPath(tmpLink.toString()))
        val rpMissing = drive.getPath(VirtualPath(tmpMissing.toString()))
        assertEquals(FileObject(rpDir).path, rpDir)
        assertEquals(FileObject(rpFile).path, rpFile)
        assertEquals(FileObject(rpLink).path, rpLink)
        assertEquals(FileObject(rpMissing).path, rpMissing)
    } */

    @Test
    fun getUser() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).getItem().user > 0)
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).getItem().user > 0)
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).getItem().user > 0)
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).getItem().user},
            "User property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getGroup() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).getItem().group > 0)
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).getItem().group > 0)
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).getItem().group > 0)
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).getItem().group},
            "Group property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getLastAccessed() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).getItem().lastAccessed > 0)
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).getItem().lastAccessed > 0)
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).getItem().lastAccessed > 0)
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).getItem().lastAccessed},
            "LastAccessed property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getLastModified() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).getItem().lastModified > 0)
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).getItem().lastModified > 0)
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).getItem().lastModified > 0)
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).getItem().lastModified},
            "LastModified property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getChanged() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).getItem().changed > 0)
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).getItem().changed > 0)
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).getItem().changed > 0)
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).getItem().changed},
            "Changed property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getCreated() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).getItem().created > 0)
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).getItem().created > 0)
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).getItem().created > 0)
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).getItem().created},
            "Created property on missing file should trigger FileNotFoundException."
        )
    }

    /*@Test // Reimplemented in PhysicalDriveTest
    fun getReadable() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).getItem().readable)
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).getItem().readable)
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).getItem().readable)
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).getItem().readable},
            "Created property on missing file should trigger FileNotFoundException."
        )
    }

    @Test // Reimplemented in PhysicalDriveTest
    fun getWritable() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).getItem().writable)
        assertTrue(drive.getPath(VirtualPath(tmpFile.toString())).getItem().writable)
        assertTrue(drive.getPath(VirtualPath(tmpLink.toString())).getItem().writable)
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).getItem().writable},
            "Created property on missing file should trigger FileNotFoundException."
        )
    }

    @Test // Reimplemented in PhysicalDriveTest
    fun getExecutable() {
        assertTrue(drive.getPath(VirtualPath(tmpDir.toString())).getItem().executable)
        assertFalse(drive.getPath(VirtualPath(tmpFile.toString())).getItem().executable)
        assertFalse(drive.getPath(VirtualPath(tmpLink.toString())).getItem().executable)
        assertExceptionThrown<FileNotFoundException>({
            drive.getPath(VirtualPath(tmpMissing.toString())).getItem().executable},
            "Created property on missing file should trigger FileNotFoundException."
        )
    }*/
}