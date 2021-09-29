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

class FileTest {
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
        java.nio.file.Files.deleteIfExists(tmpMissing)
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
    fun getSize() {
        assertTrue(File(drive.getPath(VirtualPath(tmpDir.toString()))).size > 0L)
        assertTrue(File(drive.getPath(VirtualPath(tmpFile.toString()))).size == 0L)
        assertTrue(File(drive.getPath(VirtualPath(tmpLink.toString()))).size == 0L)
        assertExceptionThrown<FileNotFoundException>({
            File(drive.getPath(VirtualPath(tmpMissing.toString()))).size},
            "Size property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun open() {
        assertExceptionThrown<FileNotFoundException>({
            File(drive.getPath(VirtualPath(tmpDir.toString()))).open(File.OpenOption.READ_WRITE)},
            "Open method on non-file should trigger FileNotFoundException."
        )
        assertTrue(File(drive.getPath(VirtualPath(tmpFile.toString()))).open(File.OpenOption.READ_WRITE) is FileDescriptor)
        assertTrue(File(drive.getPath(VirtualPath(tmpLink.toString()))).open(File.OpenOption.READ_WRITE) is FileDescriptor)
        assertTrue(File(drive.getPath(VirtualPath(tmpMissing.toString()))).open(File.OpenOption.READ_WRITE) is FileDescriptor)
    }
}