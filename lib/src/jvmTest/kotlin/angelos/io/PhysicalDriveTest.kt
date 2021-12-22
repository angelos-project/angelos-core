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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.assertContains

@ExperimentalCoroutinesApi
class PhysicalDriveTest {
    lateinit var _tmpDir: java.nio.file.Path
    lateinit var _tmpFile: java.nio.file.Path
    lateinit var _tmpLink: java.nio.file.Path
    lateinit var _tmpMissing: java.nio.file.Path
    lateinit var drive: PhysicalDrive

    @Before
    fun setUp() {
        _tmpDir = java.nio.file.Files.createTempDirectory("temporary")
        _tmpFile = java.nio.file.Files.createTempFile(_tmpDir, "test", ".tmp")
        _tmpLink = java.nio.file.Files.createSymbolicLink(
            java.nio.file.Paths.get(_tmpDir.toString(), "link.tmp"), _tmpFile
        )
        _tmpMissing = java.nio.file.Paths.get(_tmpDir.toString(), "missing.tmp")
        drive = PhysicalDrive.createFileSystem(PhysicalDrive.Drive.UNIX)
    }

    @After
    fun tearDown() {
        java.nio.file.Files.deleteIfExists(_tmpFile)
        java.nio.file.Files.deleteIfExists(_tmpLink)
        java.nio.file.Files.deleteIfExists(_tmpMissing)
        java.nio.file.Files.deleteIfExists(_tmpDir)
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

    fun tmpDir() = drive.getPath(VirtualPath((_tmpDir.toString())))
    fun tmpFile() = tmpDir().join(_tmpFile.fileName.toString())
    fun tmpLink() = tmpDir().join("link.tmp")
    fun tmpMissing() = tmpDir().join("missing.tmp")

    @Test
    fun readFile() = runBlockingTest {
    }

    @Test
    fun writeFile() = runBlockingTest {
    }

    @Test
    fun tellFile() = runBlockingTest {
    }

    @Test
    fun seekFile() = runBlockingTest {
    }

    @Test
    fun closeFile() = runBlockingTest {
    }

    @Test
    fun getUser() = runBlockingTest {
        assertTrue(tmpDir().getDir().getUser() > 0)
        assertTrue(tmpFile().getFile().getUser() > 0)
        assertTrue(tmpLink().getLink().getUser() > 0)
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getFile().getUser()},
            "User property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getGroup() = runBlockingTest {
        assertTrue(tmpDir().getDir().getGroup() > 0)
        assertTrue(tmpFile().getFile().getGroup() > 0)
        assertTrue(tmpLink().getLink().getGroup() > 0)
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getFile().getGroup()},
            "Group property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getLastAccessed() = runBlockingTest {
        assertTrue(tmpDir().getDir().getLastAccessed() > 0)
        assertTrue(tmpFile().getFile().getLastAccessed() > 0)
        assertTrue(tmpLink().getLink().getLastAccessed() > 0)
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getFile().getLastAccessed()},
            "LastAccessed property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getLastModified() = runBlockingTest {
        assertTrue(tmpDir().getDir().getLastModified() > 0)
        assertTrue(tmpFile().getFile().getLastModified() > 0)
        assertTrue(tmpLink().getLink().getLastModified() > 0)
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getFile().getLastModified()},
            "LastModified property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getChanged() = runBlockingTest {
        assertTrue(tmpDir().getDir().getChanged() > 0)
        assertTrue(tmpFile().getFile().getChanged() > 0)
        assertTrue(tmpLink().getLink().getChanged() > 0)
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getFile().getChanged()},
            "Changed property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getCreated() = runBlockingTest {
        assertTrue(tmpDir().getDir().getCreated() > 0)
        assertTrue(tmpFile().getFile().getCreated() > 0)
        assertTrue(tmpLink().getLink().getCreated() > 0)
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getFile().getCreated()},
            "Created property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun isReadable() = runBlockingTest {
        assertTrue(tmpDir().getDir().isReadable())
        assertTrue(tmpFile().getFile().isReadable())
        assertTrue(tmpLink().getLink().isReadable())
    }

    @Test
    fun isWritable() = runBlockingTest {
        assertTrue(tmpDir().getDir().isWritable())
        assertTrue(tmpFile().getFile().isWritable())
        assertTrue(tmpLink().getLink().isWritable())
    }

    @Test
    fun isExecutable() = runBlockingTest {
        assertTrue(tmpDir().getDir().isExecutable())
        assertFalse(tmpFile().getFile().isExecutable())
        assertFalse(tmpLink().getLink().isExecutable())
    }

    @Test
    fun exists() = runBlockingTest {
        assertTrue(tmpDir().exists())
        assertTrue(tmpFile().exists())
        assertTrue(tmpLink().exists())
        //assertFalse(tmpLink().exists())
    }

    @Test
    fun isDir() = runBlockingTest {
        assertTrue(tmpDir().isDir())
        assertFalse(tmpFile().isDir())
        assertFalse(tmpLink().isDir())
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().isDir()},
            "isDir method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun isLink() = runBlockingTest {
        assertFalse(tmpDir().isLink())
        assertFalse(tmpFile().isLink())
        assertTrue(tmpLink().isLink())
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().isLink()},
            "isLink method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun isFile() = runBlockingTest {
        assertFalse(tmpDir().isFile())
        assertTrue(tmpFile().isFile())
        assertFalse(tmpLink().isFile())
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().isFile()},
            "isFile method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getFileInfo() = runBlockingTest {
    }

    @Test
    fun getTarget() = runBlockingTest {
        assertEquals(tmpLink().getLink().getTarget(), tmpFile().toString())
    }

    @Test
    fun goToDir() = runBlockingTest {
        assertExceptionThrown<UnexpectedFileObject>({
            tmpLink().getLink().goToDir()},
            "goToDir method to different type."
        )
    }

    @Test
    fun goToLink() = runBlockingTest {
        assertExceptionThrown<UnexpectedFileObject>({
            tmpLink().getLink().goToLink()},
            "goToLink method to different type."
        )
    }

    @Test
    fun goToFile() = runBlockingTest {
        assertEquals(
            tmpLink().getLink().goToFile().path.toString(),
            tmpFile().getFile().path.toString()
        )
    }

    @Test
    fun getSize() = runBlockingTest {
        assertTrue(tmpFile().getFile().getSize() == 0L)
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getFile().getSize()},
            "Size property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun open() = runBlockingTest {
        assertTrue(tmpFile().getFile().open(FileSystem.OpenOption.READ_WRITE) is FileSystem.FileDescriptor)
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getFile().open(FileSystem.OpenOption.READ_WRITE)},
            "Open method on non-file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getDir() = runBlockingTest {
        assertTrue(tmpDir().getDir() is FileSystem.Dir)
        assertExceptionThrown<UnexpectedFileObject>({
            tmpLink().getDir()},
            "getDir method on wrong filetype should trigger UnexpectedFileObject."
        )
        assertExceptionThrown<UnexpectedFileObject>({
            tmpFile().getDir()},
            "getDir method on wrong filetype should trigger UnexpectedFileObject."
        )
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getDir()},
            "getDir method to missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getLink() = runBlockingTest {
        assertExceptionThrown<UnexpectedFileObject>({
            tmpDir().getLink()},
            "getDir method on wrong filetype should trigger UnexpectedFileObject."
        )
        assertTrue(tmpLink().getLink() is FileSystem.Link)
        assertExceptionThrown<UnexpectedFileObject>({
            tmpFile().getLink()},
            "getDir method on wrong filetype should trigger UnexpectedFileObject."
        )
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getLink()},
            "getDir method to missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getFile() = runBlockingTest {
        assertExceptionThrown<UnexpectedFileObject>({
            tmpDir().getFile()},
            "getDir method on wrong filetype should trigger UnexpectedFileObject."
        )
        assertExceptionThrown<UnexpectedFileObject>({
            tmpLink().getFile()},
            "getDir method on wrong filetype should trigger UnexpectedFileObject."
        )
        assertTrue(tmpFile().getFile() is FileSystem.File)
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getFile()},
            "getDir method to missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun openDir() = runBlockingTest {
    }

    @Test
    fun readDir() = runBlockingTest {
    }

    @Test
    fun closeDir() = runBlockingTest {
    }

    @Test
    fun openFile() = runBlockingTest {
    }

    @Test
    fun walk() = runBlockingTest {
        val files = mutableListOf<String>()
        tmpDir().getDir().walk().forEach {
            if (it !is FileSystem.Dir || (it is FileSystem.Dir && !it.skip))
                files.add(it.path.toString())
        }
        assertContains(files, tmpFile().toString())
        assertContains(files, tmpLink().toString())

        val files2 = mutableListOf<String>()
        tmpDir().getDir().walk(1).forEach {
            if (it !is FileSystem.Dir || (it is FileSystem.Dir && !it.skip))
                files2.add(it.path.toString())
        }
        assertContains(files2, tmpFile().toString())
        assertContains(files2, tmpLink().toString())
    }

    @Test
    fun createFileSystem() = runBlockingTest {
        // Creates a file system instance that is a limited edition object.
        // Tested implicitly in setUp().
    }

}