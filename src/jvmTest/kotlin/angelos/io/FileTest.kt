package angelos.io

import org.junit.Test

class FileTest {

    @Test
    fun getReadable() {
        kotlin.test.assertTrue(File(RealPath("/tmp")).readable)
    }

    @Test
    fun getWritable() {
        kotlin.test.assertTrue(File(RealPath("/tmp")).writable)
    }

    @Test
    fun getExecutable() {
        kotlin.test.assertTrue(File(RealPath("/tmp")).executable)
    }
}