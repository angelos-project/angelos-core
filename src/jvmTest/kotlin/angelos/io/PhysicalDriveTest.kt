package angelos.io

import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

import kotlinx.coroutines.runBlocking

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
    fun tmpFile() = tmpDir().join("test.tmp")
    fun tmpLink() = tmpDir().join("link.tmp")
    fun tmpMissing() = tmpDir().join("missing.tmp")

    @Test
    fun readFile() = runBlocking {
    }

    @Test
    fun writeFile() = runBlocking {
    }

    @Test
    fun tellFile() = runBlocking {
    }

    @Test
    fun seekFile() = runBlocking {
    }

    @Test
    fun closeFile() = runBlocking {
    }

    @Test
    fun getUser() = runBlocking {
        assertTrue(tmpDir().getDir().getUser() > 0)
        assertTrue(tmpFile().getFile().getUser() > 0)
        assertTrue(tmpLink().getLink().getUser() > 0)
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().getFile().getUser()},
            "User property on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun isReadable() = runBlocking {
        assertTrue(tmpDir().getDir().isReadable())
        assertTrue(tmpFile().getFile().isReadable())
        assertTrue(tmpLink().getLink().isReadable())
    }

    @Test
    fun isWritable() = runBlocking {
        assertTrue(tmpDir().getDir().isWritable())
        assertTrue(tmpFile().getFile().isWritable())
        assertTrue(tmpLink().getLink().isWritable())
    }

    @Test
    fun isExecutable() = runBlocking {
        assertTrue(tmpDir().getDir().isExecutable())
        assertFalse(tmpFile().getFile().isExecutable())
        assertFalse(tmpLink().getLink().isExecutable())
    }

    @Test
    fun exists() = runBlocking {
        assertTrue(tmpDir().exists())
        assertTrue(tmpFile().exists())
        assertTrue(tmpLink().exists())
        assertFalse(tmpLink().exists())
    }

    @Test
    fun isDir() = runBlocking {
        assertTrue(tmpDir().isDir())
        assertFalse(tmpFile().isDir())
        assertFalse(tmpLink().isDir())
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().isDir()},
            "isDir method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun isLink() = runBlocking {
        assertFalse(tmpDir().isLink())
        assertFalse(tmpFile().isLink())
        assertTrue(tmpLink().isLink())
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().isLink()},
            "isLink method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun isFile() = runBlocking {
        assertFalse(tmpDir().isFile())
        assertTrue(tmpFile().isFile())
        assertFalse(tmpLink().isFile())
        assertExceptionThrown<FileNotFoundException>({
            tmpMissing().isFile()},
            "isFile method on missing file should trigger FileNotFoundException."
        )
    }

    @Test
    fun getFileInfo() = runBlocking {
    }

    @Test
    fun getLinkTarget() = runBlocking {
    }

    @Test
    fun openDir() = runBlocking {
    }

    @Test
    fun readDir() = runBlocking {
    }

    @Test
    fun closeDir() = runBlocking {
    }

    @Test
    fun openFile() = runBlocking {
    }

    @Test
    fun createFileSystem() = runBlocking {
        // Creates a file system instance that is a limited edition object.
        // Tested implicitly in setUp().
    }

}