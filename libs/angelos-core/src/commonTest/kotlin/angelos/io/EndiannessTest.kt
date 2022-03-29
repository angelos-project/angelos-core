package angelos.io

import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EndiannessTest {

    @Test
    fun isBig() {
        val endianness = Endianness.BIG_ENDIAN
        assertTrue(endianness.isBig())
    }

    @Test
    fun isLittle() {
        val endianness = Endianness.LITTLE_ENDIAN
        assertTrue(endianness.isLittle())
    }

    @Test
    fun nativeOrder() {
        val endianness = Endianness.nativeOrder()
        assertNotNull(endianness)
    }

    @Test
    fun testToString() {
        val endianness = Endianness.nativeOrder()
        assertNotEquals(endianness.toString(), "UNKNOWN_ENDIAN")
    }
}