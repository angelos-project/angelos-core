package angelos.io

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MutableNativeByteBufferTest {
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

    private val refWrite = byteArrayOf(
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
        10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
        20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
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

    fun reverseEndian(buffer: ByteBuffer) {
        buffer.endian = when(buffer.endian != Endianness.LITTLE_ENDIAN) {
            true -> Endianness.LITTLE_ENDIAN
            false -> Endianness.BIG_ENDIAN
        }
    }

    @Test
    fun readAny() {
        val buf = byteBufferFrom(refRead.copyOf(), Endianness.LITTLE_ENDIAN).toMutableNativeByteBuffer()
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
    fun writeAny() {
        val buf = byteBufferFrom(refWrite.copyOf(), Endianness.LITTLE_ENDIAN).toMutableNativeByteBuffer()
        buf.setByte(refByte)
        buf.setUByte(refUByte)
        buf.setShort(refShort)
        buf.setUShort(refUShort)
        buf.setInt(refInt)
        buf.setUInt(refUInt)
        buf.setLong(refLong)
        buf.setULong(refULong)
        assertContentEquals(buf.getArray(), refRead)
    }

    @Test
    fun writeByte() {
        val buf = mutableNativeByteBufferWith(size)
        buf.setByte(refByte)
        assertEquals(buf.position, Byte.SIZE_BYTES)
        assertEquals(buf.getByte(), refByte)

        reverseEndian(buf)
        buf.rewind()

        buf.setByte(refByte)
        assertEquals(buf.position, Byte.SIZE_BYTES)
        assertEquals(buf.getByte(), refByte)
    }

    @Test
    fun writeUByte() {
        val buf = mutableNativeByteBufferWith(size)
        buf.setUByte(refUByte)
        assertEquals(buf.position, UByte.SIZE_BYTES)
        assertEquals(buf.getUByte(), refUByte)

        reverseEndian(buf)
        buf.rewind()

        buf.setUByte(refUByte)
        assertEquals(buf.position, UByte.SIZE_BYTES)
        assertEquals(buf.getUByte(), refUByte)
    }

    @Test
    fun writeChar() {
        val buf = mutableNativeByteBufferWith(size)
        val letter = 'Å'

        buf.setChar(letter)
        assertEquals(buf.position, Char.SIZE_BYTES)
        assertEquals(buf.getChar(), letter)

        reverseEndian(buf)
        buf.rewind()

        buf.setChar(letter)
        assertEquals(buf.position, Char.SIZE_BYTES)
        assertEquals(buf.getChar(), letter)
    }

    @Test
    fun writeShort() {
        val buf = mutableNativeByteBufferWith(size)
        buf.setShort(refShort)
        assertEquals(buf.position, Short.SIZE_BYTES)
        assertEquals(buf.getShort(), refShort)

        reverseEndian(buf)
        buf.rewind()

        buf.setShort(refShort)
        assertEquals(buf.position, Short.SIZE_BYTES)
        assertEquals(buf.getShort(), refShort)
    }

    @Test
    fun writeUShort() {
        val buf = mutableNativeByteBufferWith(size)
        buf.setUShort(refUShort)
        assertEquals(buf.position, UShort.SIZE_BYTES)
        assertEquals(refUShort, buf.getUShort())

        reverseEndian(buf)
        buf.rewind()

        buf.setUShort(refUShort)
        assertEquals(buf.position, UShort.SIZE_BYTES)
        assertEquals(refUShort, buf.getUShort())
    }

    @Test
    fun writeInt() {
        val buf = mutableNativeByteBufferWith(size)
        buf.setInt(-refInt)
        assertEquals(buf.position, Int.SIZE_BYTES)
        assertEquals(buf.getInt(), -refInt)

        reverseEndian(buf)
        buf.rewind()

        buf.setInt(-refInt)
        assertEquals(buf.position, Int.SIZE_BYTES)
        assertEquals(buf.getInt(), -refInt)
    }

    @Test
    fun writeUInt() {
        val buf = mutableNativeByteBufferWith(size)
        buf.setUInt(refUInt)
        assertEquals(buf.position, UInt.SIZE_BYTES)
        assertEquals(buf.getUInt(), refUInt)

        reverseEndian(buf)
        buf.rewind()

        buf.setUInt(refUInt)
        assertEquals(buf.position, UInt.SIZE_BYTES)
        assertEquals(buf.getUInt(), refUInt)
    }

    @Test
    fun writeLong() {
        val buf = mutableNativeByteBufferWith(size)
        buf.setLong(refLong)
        assertEquals(buf.position, Long.SIZE_BYTES)
        assertEquals(buf.getLong(), refLong)

        reverseEndian(buf)
        buf.rewind()

        buf.setLong(refLong)
        assertEquals(buf.position, Long.SIZE_BYTES)
        assertEquals(buf.getLong(), refLong)
    }

    @Test
    fun writeULong() {
        val buf = mutableNativeByteBufferWith(size)
        buf.setULong(refULong)
        assertEquals(buf.position, ULong.SIZE_BYTES)
        assertEquals(buf.getULong(), refULong)

        reverseEndian(buf)
        buf.rewind()

        buf.setULong(refULong)
        assertEquals(buf.position, ULong.SIZE_BYTES)
        assertEquals(buf.getULong(), refULong)
    }

    @Test
    fun writeFloat() {
        val buf = mutableNativeByteBufferWith(size)
        val value: Float = -123.565F
        buf.setFloat(value)
        assertEquals(buf.position, Float.SIZE_BYTES)
        assertEquals(buf.getFloat(), value)

        reverseEndian(buf)
        buf.rewind()

        buf.setFloat(value)
        assertEquals(buf.position, Float.SIZE_BYTES)
        assertEquals(buf.getFloat(), value)
    }

    @Test
    fun writeDouble() {
        val buf = mutableNativeByteBufferWith(size)
        val value: Double = (-2.34958736E8F).toDouble()
        buf.setDouble(value)
        assertEquals(buf.position, Double.SIZE_BYTES)
        assertEquals(buf.getDouble(), value, 0.0)

        reverseEndian(buf)
        buf.rewind()

        buf.setDouble(value)
        assertEquals(buf.position, Double.SIZE_BYTES)
        assertEquals(buf.getDouble(), value, 0.0)
    }

    @Test
    fun mutableNativeByteBufferWith() {
        assertNotNull(mutableNativeByteBufferWith(4096))
    }

    @Test
    fun testToMutableByteBuffer() {
        assertContentEquals(byteBufferFrom(refRead.copyOf()).toMutableNativeByteBuffer().toMutableByteBuffer().getArray(), refRead)
    }

    @Test
    fun copyInto() {
        val buf = mutableByteBufferWith(128)
        byteBufferFrom(refRead.copyOf()).toMutableNativeByteBuffer().copyInto(buf, 0..128)
        assertContentEquals(buf.getArray(), refRead)
    }
}