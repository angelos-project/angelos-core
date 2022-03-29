package angelos.io

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ByteBufferTest {
    private val refRead = byteArrayOf(
        0B1010101, 0B10101010u.toByte(),
        0B1010101, 0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(),
        0B1010101, 0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(),
        0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(),
        0B1010101, 0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(),
        0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(),
        0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(),
        0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(), 0B10101010.toByte(),
        30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
        40, 41, 42, 43, 44, 45, 46, 47, 48, 49,
        50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
        60, 61, 62, 63, 64, 65, 66, 67, 68, 69,
        70, 71, 72, 73, 74, 75, 76, 77, 78, 79,
        80, 81, 82, 83, 84, 85, 86, 87, 88, 89,
        90, 91, 92, 93, 94, 95, 96, 97, 98, 99,
        100, 101, 102, 103, 104, 105, 106, 107, 108, 109,
        110, 111, 112, 113, 114, 115, 116, 117, 118, 119,
        120, 121, 122, 123, 124, 125, 126, 127
    )

    private val refByte: Byte = 0B1010101
    private val refUByte: UByte = 0B10101010u

    private val refShort: Short = 0B1010101_10101010
    private val refUShort: UShort = 0B10101010_10101010u

    private val refInt: Int = 0B1010101_10101010_10101010_10101010
    private val refUInt: UInt = 0B10101010_10101010_10101010_10101010u

    private val refLong: Long = 0B1010101_10101010_10101010_10101010_10101010_10101010_10101010_10101010L
    private val refULong: ULong = 0B10101010_10101010_10101010_10101010_10101010_10101010_10101010_10101010u

    private val size: Int = 128

    @Test
    fun readAny() {
        val buf = byteBufferFrom(refRead.copyOf(), Endianness.LITTLE_ENDIAN)
        assertEquals(buf.getByte(), refByte)
        assertEquals(buf.getUByte(), refUByte)
        assertEquals(buf.getShort(), refShort)
        assertEquals(buf.getUShort(), refUShort)
        assertEquals(buf.getInt(), refInt)
        assertEquals(buf.getUInt(), refUInt)
        assertEquals(buf.getLong(), refLong)
        assertEquals(buf.getULong(), refULong)
        for(num in 30 until 128)
            assertEquals(buf.getByte(), num.toByte())
    }

    @Test
    fun byteBufferFrom() {
        assertContentEquals(byteBufferFrom(refRead.copyOf()).getArray(), refRead)
    }

    @Test
    fun toMutableByteBuffer() {
        assertContentEquals(byteBufferFrom(refRead.copyOf()).toMutableByteBuffer().getArray(), refRead)
    }

    @Test
    fun toMutableNativeByteBuffer() {
        assertContentEquals(byteBufferFrom(refRead.copyOf()).toMutableNativeByteBuffer().toMutableByteBuffer().getArray(), refRead)
    }

    @Test
    fun toNativeByteBuffer() {
        assertContentEquals(byteBufferFrom(refRead.copyOf()).toNativeByteBuffer().toByteBuffer().getArray(), refRead)
    }

    @Test
    fun copyInto() {
        val buf = mutableByteBufferWith(128)
        byteBufferFrom(refRead.copyOf()).copyInto(buf, 0..128)
        assertContentEquals(buf.getArray(), refRead)
    }
}