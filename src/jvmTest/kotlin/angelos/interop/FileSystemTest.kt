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

import angelos.io.FileDescriptor
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.io.path.deleteIfExists


class FileSystemTest {
    lateinit var tmpDir: java.nio.file.Path
    lateinit var tmpFile: java.nio.file.Path

    @Before
    fun setUp() {
        tmpDir = java.nio.file.Files.createTempDirectory("temporary")
        tmpFile = java.nio.file.Files.createTempFile(tmpDir, "test", ".tmp")
    }

    @After
    fun tearDown() {
        java.nio.file.Files.deleteIfExists(tmpFile)
        java.nio.file.Files.deleteIfExists(tmpDir)
    }

    @Test
    fun checkExists() {
        assertTrue(FileSystem.checkExists(tmpDir.toString()))
        assertTrue(FileSystem.checkExists(tmpFile.toString()))
    }

    @Test
    fun checkReadable() {
        assertTrue(FileSystem.checkReadable(tmpDir.toString()))
        assertTrue(FileSystem.checkReadable(tmpFile.toString()))
    }

    @Test
    fun checkWritable() {
        assertTrue(FileSystem.checkWritable(tmpDir.toString()))
        assertTrue(FileSystem.checkWritable(tmpFile.toString()))
    }

    @Test
    fun checkExecutable() {
        assertTrue(FileSystem.checkExecutable(tmpDir.toString()))
        assertFalse(FileSystem.checkExecutable(tmpFile.toString()))
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
        val size = message.toByteArray().size

        val file = FileSystem.openFile(tmpFile.toString(), 3)
        assertTrue(FileSystem.writeFile(file, message.toByteArray(), 0, size.toULong()) == size.toULong())
        assertTrue(FileSystem.closeFile(file))

        assertEquals(java.nio.file.Files.newBufferedReader(tmpFile).readLine(), message)
    }

    @Test
    fun testRead(){
        val message = "Hello, world!"
        val size = message.toByteArray().size
        var loaded = ByteArray(size)

        val writer = java.nio.file.Files.newBufferedWriter(tmpFile)
        writer.write(message)
        writer.close()

        val file = FileSystem.openFile(tmpFile.toString(), 3)
        assertTrue(FileSystem.readFile(file, loaded, 0, size.toULong()) == size.toULong())
        assertTrue(FileSystem.closeFile(file))


        assertEquals(loaded.contentToString(), message.toByteArray().contentToString())
    }

    @Test
    fun testFile() {
        val message = "Hello, world!".toByteArray()
        val size = message.size
        val file = FileSystem.openFile(tmpFile.toString(), 3)
        assertNotEquals(file, 0)

        assertTrue(FileSystem.tellFile(file) == 0.toULong())
        assertTrue(FileSystem.writeFile(file, message, 0, size.toULong()) == 13.toULong())

        assertTrue(FileSystem.tellFile(file) == size.toULong())
        assertTrue(FileSystem.seekFile(file, 0, FileDescriptor.Seek.SET) == 0.toULong())

        var loaded = ByteArray(size)
        assertTrue(FileSystem.readFile(file, loaded, 0, size.toULong()) == 13.toULong())
        assertEquals(message.contentToString(), loaded.contentToString())

        assertTrue(FileSystem.closeFile(file))
        assertFalse(FileSystem.closeFile(file))
    }

    @Test
    fun testReadLink(){
        val tmpLink: java.nio.file.Path = java.nio.file.Files.createLink(java.nio.file.Paths.get(tmpDir.toString(), "link.tmp"), tmpFile)
        //println(tmpLink)
        println(FileSystem.getLinkTarget(tmpLink.toString()))

        tmpLink.deleteIfExists()
    }
}