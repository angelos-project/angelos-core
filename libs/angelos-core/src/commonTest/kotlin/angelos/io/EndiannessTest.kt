package angelos.io

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class EndiannessTest {

    @Test
    fun isUnknown() {
        val endianness = Endianness.UNKNOWN_ENDIAN
        assertTrue(endianness.isUnknown())
    }

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
        assertFalse(endianness.isUnknown())
    }

    @Test
    fun testToString() {
        val endianness = Endianness.nativeOrder()
        assertNotEquals(endianness.toString(), "UNKNOWN_ENDIAN")
    }
}