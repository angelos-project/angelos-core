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
package angelos.interop

import angelos.io.FileNotFoundException
import angelos.io.NotLinkException
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class FileSystemTest {
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
    fun checkExists() {
        assertTrue(FileSystem.checkExists(tmpDir.toString()))
        assertTrue(FileSystem.checkExists(tmpFile.toString()))
        assertTrue(FileSystem.checkExists(tmpLink.toString()))
        assertFalse(FileSystem.checkExists(tmpMissing.toString()))
    }

    @Test
    fun checkReadable() {
        assertTrue(FileSystem.checkReadable(tmpDir.toString()))
        assertTrue(FileSystem.checkReadable(tmpFile.toString()))
        assertTrue(FileSystem.checkReadable(tmpLink.toString()))
        assertFalse(FileSystem.checkReadable(tmpMissing.toString()))
    }

    @Test
    fun checkWritable() {
        assertTrue(FileSystem.checkWritable(tmpDir.toString()))
        assertTrue(FileSystem.checkWritable(tmpFile.toString()))
        assertTrue(FileSystem.checkWritable(tmpLink.toString()))
        assertFalse(FileSystem.checkWritable(tmpMissing.toString()))
    }

    @Test
    fun checkExecutable() {
        assertTrue(FileSystem.checkExecutable(tmpDir.toString()))
        assertFalse(FileSystem.checkExecutable(tmpFile.toString()))
        assertFalse(FileSystem.checkExecutable(tmpLink.toString()))
        assertFalse(FileSystem.checkExecutable(tmpMissing.toString()))
    }

    @Test
    fun testDirectory(){
        val dir = FileSystem.openDir(tmpDir.toString())
        assertEquals(FileSystem.readDir(dir).name, ".")
        assertEquals(FileSystem.readDir(dir).name, "..")
        assertTrue(FileSystem.readDir(dir).name.endsWith(".tmp"))
        FileSystem.closeDir(dir)
    }

    @Test
    fun testWrite(){
        val message = "Hello, world!"
        val size = message.toByteArray().size.toLong()

        val file = FileSystem.openFile(tmpFile.toString(), 3)
        assertTrue(FileSystem.writeFile(file, message.toByteArray(), 0, size) == size)
        assertTrue(FileSystem.closeFile(file))

        assertEquals(java.nio.file.Files.newBufferedReader(tmpFile).readLine(), message)
    }

    @Test
    fun testRead(){
        val message = "Hello, world!"
        val size = message.toByteArray().size.toLong()
        var loaded = ByteArray(size.toInt())

        val writer = java.nio.file.Files.newBufferedWriter(tmpFile)
        writer.write(message)
        writer.close()

        val file = FileSystem.openFile(tmpFile.toString(), 3)
        assertTrue(FileSystem.readFile(file, loaded, 0, size) == size)
        assertTrue(FileSystem.closeFile(file))

        assertEquals(loaded.contentToString(), message.toByteArray().contentToString())
    }

    @Test
    fun testFile() {
        val message = "Hello, world!".toByteArray()
        val size = message.size.toLong()
        val file = FileSystem.openFile(tmpFile.toString(), 3)
        assertNotEquals(file, 0)

        assertTrue(FileSystem.tellFile(file) == 0L)
        assertTrue(FileSystem.writeFile(file, message, 0, size) == 13L)

        assertTrue(FileSystem.tellFile(file) == size)
        assertTrue(FileSystem.seekFile(file, 0, angelos.io.FileSystem.Seek.SET) == 0L)

        val loaded = ByteArray(size.toInt())
        assertTrue(FileSystem.readFile(file, loaded, 0, size) == 13L)
        assertEquals(message.contentToString(), loaded.contentToString())

        assertTrue(FileSystem.closeFile(file))
        assertFalse(FileSystem.closeFile(file))
    }

    @Test
    fun testReadLink(){
        assertEquals(FileSystem.getLinkTarget(tmpLink.toString()), tmpFile.toString())

        assertExceptionThrown<NotLinkException>(
            {FileSystem.getLinkTarget(tmpMissing.toString())},
            "Missing file should raise FileNotFoundException"
        )
    }

    @Test
    fun testFileType(){
        assertEquals(FileSystem.getFileType(tmpLink.toString()), 1)
        assertEquals(FileSystem.getFileType(tmpDir.toString()), 2)
        assertEquals(FileSystem.getFileType(tmpFile.toString()), 3)
        assertExceptionThrown<FileNotFoundException>(
            {FileSystem.getFileType(tmpMissing.toString())},
            "Missing file should raise FileNotFoundException"
        )
    }

    @Test
    fun testFileInfo(){
        val fileInfo = FileSystem.getFileInfo(tmpFile.toString())
        assertTrue(fileInfo.createdAt > 0)
        assertExceptionThrown<FileNotFoundException>(
            {FileSystem.getFileInfo(tmpMissing.toString())},
            "Missing file should raise FileNotFoundException"
        )
    }
}