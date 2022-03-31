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
        assertTrue(IO.checkExists(tmpDir.toString()))
        assertTrue(IO.checkExists(tmpFile.toString()))
        assertTrue(IO.checkExists(tmpLink.toString()))
        assertFalse(IO.checkExists(tmpMissing.toString()))
    }

    @Test
    fun checkReadable() {
        assertTrue(IO.checkReadable(tmpDir.toString()))
        assertTrue(IO.checkReadable(tmpFile.toString()))
        assertTrue(IO.checkReadable(tmpLink.toString()))
        assertFalse(IO.checkReadable(tmpMissing.toString()))
    }

    @Test
    fun checkWritable() {
        assertTrue(IO.checkWritable(tmpDir.toString()))
        assertTrue(IO.checkWritable(tmpFile.toString()))
        assertTrue(IO.checkWritable(tmpLink.toString()))
        assertFalse(IO.checkWritable(tmpMissing.toString()))
    }

    @Test
    fun checkExecutable() {
        assertTrue(IO.checkExecutable(tmpDir.toString()))
        assertFalse(IO.checkExecutable(tmpFile.toString()))
        assertFalse(IO.checkExecutable(tmpLink.toString()))
        assertFalse(IO.checkExecutable(tmpMissing.toString()))
    }

    @Test
    fun testDirectory(){
        val dir = IO.openDir(tmpDir.toString())
        assertEquals(IO.readDir(dir).name, ".")
        assertEquals(IO.readDir(dir).name, "..")
        assertTrue(IO.readDir(dir).name.endsWith(".tmp"))
        IO.closeDir(dir)
    }

    @Test
    fun testWrite() { /*
        val message = ByteHeapBuffer(13, 13);
        "Hello, world!".toByteArray().copyInto(message.toArray())
        val size = "Hello, world!".toByteArray().size.toLong()

        val file = IO.openFile(tmpFile.toString(), 3)
        assertTrue(IO.writeFile(file, message, 0, size) == size)
        assertTrue(IO.closeFile(file))

        assertEquals(java.nio.file.Files.newBufferedReader(tmpFile).readLine().toByteArray().contentToString(), message.toArray().contentToString())
    */ }

    @Test
    fun testRead() { /*
        val message = "Hello, world!"
        val size = message.toByteArray().size.toLong()
        var loaded = ByteHeapBuffer(size.toLong(), size.toLong())

        val writer = java.nio.file.Files.newBufferedWriter(tmpFile)
        writer.write(message)
        writer.close()

        val file = IO.openFile(tmpFile.toString(), 3)
        assertTrue(IO.readFile(file, loaded, 0, size) == size)
        assertTrue(IO.closeFile(file))

        assertEquals(loaded.toArray().contentToString(), message.toByteArray().contentToString())
    */ }

    @Test
    fun testFile() { /*
        val message = ByteHeapBuffer(13, 13);
        "Hello, world!".toByteArray().copyInto(message.toArray())
        val size = "Hello, world!".toByteArray().size.toLong()
        val file = IO.openFile(tmpFile.toString(), 3)
        assertNotEquals(file, 0)

        assertTrue(IO.tellFile(file) == 0L)
        assertTrue(IO.writeFile(file, message, 0, size) == 13L)

        assertTrue(IO.tellFile(file) == size)
        assertTrue(IO.seekFile(file, 0, angelos.io.FileSystem.Seek.SET) == 0L)

        val loaded = ByteHeapBuffer(size.toLong(), size.toLong())
        assertTrue(IO.readFile(file, loaded, 0, size) == 13L)
        assertEquals(message.toArray().contentToString(), loaded.toArray().contentToString())

        assertTrue(IO.closeFile(file))
        assertFalse(IO.closeFile(file))
    */ }

    @Test
    fun testReadLink(){
        assertEquals(IO.getLinkTarget(tmpLink.toString()), tmpFile.toString())

        assertExceptionThrown<NotLinkException>(
            {IO.getLinkTarget(tmpMissing.toString())},
            "Missing file should raise FileNotFoundException"
        )
    }

    @Test
    fun testFileType(){
        assertEquals(IO.getFileType(tmpLink.toString()), 1)
        assertEquals(IO.getFileType(tmpDir.toString()), 2)
        assertEquals(IO.getFileType(tmpFile.toString()), 3)
        assertExceptionThrown<FileNotFoundException>(
            {IO.getFileType(tmpMissing.toString())},
            "Missing file should raise FileNotFoundException"
        )
    }

    @Test
    fun testFileInfo(){
        val fileInfo = IO.getFileInfo(tmpFile.toString())
        assertTrue(fileInfo.createdAt > 0)
        assertExceptionThrown<FileNotFoundException>(
            {IO.getFileInfo(tmpMissing.toString())},
            "Missing file should raise FileNotFoundException"
        )
    }
}