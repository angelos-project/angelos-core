package angelos.nio

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ByteDirectBufferTest{
    private val short: Short = 0B1010101_10101010
    private val ushort: UShort = 0B10101010_10101010u

    private val int: Int = 0B1010101_10101010_10101010_10101010
    private val uint: UInt = 0B10101010_10101010_10101010_10101010u

    private val long: Long = 0B1010101_10101010_10101010_10101010_10101010_10101010_10101010_10101010L
    private val ulong: ULong = 0B10101010_10101010_10101010_10101010_10101010_10101010_10101010_10101010u

    private val size: Long = 128
    private var buffer: ByteDirectBuffer = ByteDirectBuffer(size, size, 0L)

    fun reverseEndian(){
        buffer.order = if(buffer.order == ByteOrder.LITTLE_ENDIAN)
            ByteOrder.BIG_ENDIAN
        else
            ByteOrder.LITTLE_ENDIAN
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun readChar() {
        // readChar is tested in writeChar.
    }

    @Test
    fun writeChar() {
        val letter = 'Å'
        buffer.writeChar(letter)
        assertEquals(buffer.position, Char.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readChar(), letter)

        reverseEndian()
        buffer.rewind()

        buffer.writeChar(letter)
        assertEquals(buffer.position, Char.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readChar(), letter)
    }

    @Test
    fun readShort() {
        // readShort is tested in writeShort.
    }

    @Test
    fun writeShort() {
        buffer.writeShort(short)
        assertEquals(buffer.position, Short.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readShort(), short)

        reverseEndian()
        buffer.rewind()

        buffer.writeShort(short)
        assertEquals(buffer.position, Short.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readShort(), short)
    }

    @Test
    fun readUShort() {
        // readUShort is tested in writeUShort.
    }

    @Test
    fun writeUShort() {
        buffer.writeUShort(ushort)
        assertEquals(buffer.position, UShort.SIZE_BYTES)

        buffer.rewind()
        assertEquals(ushort, buffer.readUShort())

        reverseEndian()
        buffer.rewind()

        buffer.writeUShort(ushort)
        assertEquals(buffer.position, UShort.SIZE_BYTES)

        buffer.rewind()
        assertEquals(ushort, buffer.readUShort())
    }

    @Test
    fun readInt() {
        // readInt is tested in writeInt.
    }

    @Test
    fun writeInt() {
        buffer.writeInt(-int)
        assertEquals(buffer.position, Int.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readInt(), -int)

        reverseEndian()
        buffer.rewind()

        buffer.writeInt(-int)
        assertEquals(buffer.position, Int.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readInt(), -int)
    }

    @Test
    fun readUInt() {
        // readUInt is tested in writeUInt.
    }

    @Test
    fun writeUInt() {
        buffer.writeUInt(uint)
        kotlin.test.assertEquals(buffer.position, UInt.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readUInt(), uint)

        reverseEndian()
        buffer.rewind()

        buffer.writeUInt(uint)
        assertEquals(buffer.position, UInt.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readUInt(), uint)
    }

    @Test
    fun readLong() {
        // readLong is tested in writeLong.
    }

    @Test
    fun writeLong() {
        buffer.writeLong(long)
        assertEquals(buffer.position, Long.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readLong(), long)

        reverseEndian()
        buffer.rewind()

        buffer.writeLong(long)
        assertEquals(buffer.position, Long.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readLong(), long)
    }

    @Test
    fun readULong() {
        // readULong is tested in writeULong.
    }

    @Test
    fun writeULong() {
        buffer.writeULong(ulong)
        assertEquals(buffer.position, ULong.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readULong(), ulong)

        reverseEndian()
        buffer.rewind()

        buffer.writeULong(ulong)
        assertEquals(buffer.position, ULong.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readULong(), ulong)
    }

    @Test
    fun readFloat() {
        // readFloat is tested in writeFloat.
    }

    @Test
    fun writeFloat() {
        val value: Float = -123.565F
        buffer.writeFloat(value)
        assertEquals(buffer.position, Float.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readFloat(), value)

        reverseEndian()
        buffer.rewind()

        buffer.writeFloat(value)
        assertEquals(buffer.position, Float.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readFloat(), value)
    }

    @Test
    fun readDouble() {
        // readDouble is tested in writeDouble.
    }

    @Test
    fun writeDouble() {
        val value: Double = (-234958739.324893498573495834753947535234571209347F).toDouble()
        buffer.writeDouble(value)
        assertEquals(buffer.position, Double.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readDouble(), value, 0.0)

        reverseEndian()
        buffer.rewind()

        buffer.writeDouble(value)
        assertEquals(buffer.position, Double.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.readDouble(), value, 0.0)
    }

    @Test
    fun load() {
    }

    @Test
    fun save() {
    }
}