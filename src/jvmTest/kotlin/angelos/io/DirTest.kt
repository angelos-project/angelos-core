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
import kotlin.test.assertContains

class DirTest {
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
            java.nio.file.Paths.get(tmpDir.toString(), "link.tmp"), tmpFile)
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
    operator fun iterator() {
        // Method that returns a directory iterator class
        // This method is implicitly tested in walk().
    }

    @Test
    fun walk() {
        val files = mutableListOf<String>()
        drive.getDirectory(drive.getPath(VirtualPath(tmpDir.toString()))).walk().forEach {
            if(it !is Dir || (it is Dir && !it.skip))
                files.add(it.path.toString())
        }
        assertContains(files, tmpFile.toString())
        assertContains(files, tmpLink.toString())

        val files2 = mutableListOf<String>()
        drive.getDirectory(drive.getPath(VirtualPath(tmpDir.toString()))).walk(1).forEach {
            if(it !is Dir || (it is Dir && !it.skip))
                files2.add(it.path.toString())
        }
        assertContains(files2, tmpFile.toString())
        assertContains(files2, tmpLink.toString())
    }
}