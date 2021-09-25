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

class FileObjectTest {
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
    fun get_info() {
        // Loads the information about a file object.
        // This method is tested implicitly.
    }

    @Test
    fun getPath() {
        val rpDir = VirtualPath(tmpDir.toString()).toRealPath()
        val rpFile = VirtualPath(tmpFile.toString()).toRealPath()
        val rpLink = VirtualPath(tmpLink.toString()).toRealPath()
        val rpMissing = VirtualPath(tmpMissing.toString()).toRealPath()
        assertEquals(FileObject(rpDir).path, rpDir)
        assertEquals(FileObject(rpFile).path, rpFile)
        assertEquals(FileObject(rpLink).path, rpLink)
        assertEquals(FileObject(rpMissing).path, rpMissing)
    }

    @Test
    fun getUser() {
        assertTrue(FileObject(VirtualPath(tmpDir.toString()).toRealPath()).user > 0)
        assertTrue(FileObject(VirtualPath(tmpFile.toString()).toRealPath()).user > 0)
        assertTrue(FileObject(VirtualPath(tmpLink.toString()).toRealPath()).user > 0)
        assertExceptionThrown<FileNotFoundException>({
            FileObject(VirtualPath(tmpMissing.toString()).toRealPath()).user},
            "User property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getGroup() {
        assertTrue(FileObject(VirtualPath(tmpDir.toString()).toRealPath()).group > 0)
        assertTrue(FileObject(VirtualPath(tmpFile.toString()).toRealPath()).group > 0)
        assertTrue(FileObject(VirtualPath(tmpLink.toString()).toRealPath()).group > 0)
        assertExceptionThrown<FileNotFoundException>({
            FileObject(VirtualPath(tmpMissing.toString()).toRealPath()).group},
            "Group property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getLastAccessed() {
        assertTrue(FileObject(VirtualPath(tmpDir.toString()).toRealPath()).lastAccessed > 0)
        assertTrue(FileObject(VirtualPath(tmpFile.toString()).toRealPath()).lastAccessed > 0)
        assertTrue(FileObject(VirtualPath(tmpLink.toString()).toRealPath()).lastAccessed > 0)
        assertExceptionThrown<FileNotFoundException>({
            FileObject(VirtualPath(tmpMissing.toString()).toRealPath()).lastAccessed},
            "LastAccessed property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getLastModified() {
        assertTrue(FileObject(VirtualPath(tmpDir.toString()).toRealPath()).lastModified > 0)
        assertTrue(FileObject(VirtualPath(tmpFile.toString()).toRealPath()).lastModified > 0)
        assertTrue(FileObject(VirtualPath(tmpLink.toString()).toRealPath()).lastModified > 0)
        assertExceptionThrown<FileNotFoundException>({
            FileObject(VirtualPath(tmpMissing.toString()).toRealPath()).lastModified},
            "LastModified property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getChanged() {
        assertTrue(FileObject(VirtualPath(tmpDir.toString()).toRealPath()).changed > 0)
        assertTrue(FileObject(VirtualPath(tmpFile.toString()).toRealPath()).changed > 0)
        assertTrue(FileObject(VirtualPath(tmpLink.toString()).toRealPath()).changed > 0)
        assertExceptionThrown<FileNotFoundException>({
            FileObject(VirtualPath(tmpMissing.toString()).toRealPath()).changed},
            "Changed property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getCreated() {
        assertTrue(FileObject(VirtualPath(tmpDir.toString()).toRealPath()).created > 0)
        assertTrue(FileObject(VirtualPath(tmpFile.toString()).toRealPath()).created > 0)
        assertTrue(FileObject(VirtualPath(tmpLink.toString()).toRealPath()).created > 0)
        assertExceptionThrown<FileNotFoundException>({
            FileObject(VirtualPath(tmpMissing.toString()).toRealPath()).created},
            "Created property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getReadable() {
        assertTrue(FileObject(VirtualPath(tmpDir.toString()).toRealPath()).readable)
        assertTrue(FileObject(VirtualPath(tmpFile.toString()).toRealPath()).readable)
        assertTrue(FileObject(VirtualPath(tmpLink.toString()).toRealPath()).readable)
        assertFalse(FileObject(VirtualPath(tmpMissing.toString()).toRealPath()).readable)
    }

    @Test
    fun getWritable() {
        assertTrue(FileObject(VirtualPath(tmpDir.toString()).toRealPath()).writable)
        assertTrue(FileObject(VirtualPath(tmpFile.toString()).toRealPath()).writable)
        assertTrue(FileObject(VirtualPath(tmpLink.toString()).toRealPath()).writable)
        assertFalse(FileObject(VirtualPath(tmpMissing.toString()).toRealPath()).writable)
    }

    @Test
    fun getExecutable() {
        assertTrue(FileObject(VirtualPath(tmpDir.toString()).toRealPath()).executable)
        assertFalse(FileObject(VirtualPath(tmpFile.toString()).toRealPath()).executable)
        assertFalse(FileObject(VirtualPath(tmpLink.toString()).toRealPath()).executable)
        assertFalse(FileObject(VirtualPath(tmpMissing.toString()).toRealPath()).executable)
    }
}