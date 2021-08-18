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
import angelos.nio.ByteBuffer
import angelos.nio.ByteOrder
import angelos.nio.InvalidMarkException
import org.junit.After
import org.junit.Test
import kotlin.test.*


@ExperimentalUnsignedTypes
class ByteBufferTest {
    private val short: Short = 0B1010101_10101010
    private val ushort: UShort = 0B10101010_10101010u

    private val int: Int = 0B1010101_10101010_10101010_10101010
    private val uint: UInt = 0B10101010_10101010_10101010_10101010u

    private val long: Long = 0B1010101_10101010_10101010_10101010_10101010_10101010_10101010_10101010L
    private val ulong: ULong = 0B10101010_10101010_10101010_10101010_10101010_10101010_10101010_10101010u

    private val size: Int = 128
    private var buffer: ByteBuffer = ByteBuffer(size)

    @After
    fun tearDown() {
        buffer = ByteBuffer(size)
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
            assertEquals(happened, true, message)
        }
    }

    @Test
    fun getReadOnly() {
        assertEquals(buffer.readOnly, false, "Value 'readOnly' should implicitly be 'true'.")
    }

    @Test
    fun getDirect() {
        assertEquals(buffer.direct, true, "Value 'direct' should implicitly be 'true'.")
    }

    @Test
    fun getCapacity() {
        assertEquals(
            buffer.capacity,
            size,
            "Value 'capacity' should implicitly be the length of the underlying array."
        )
    }

    @Test
    fun getLimit() {
        assertEquals(buffer.limit, size, "Property 'limit' should implicitly be set to 'capacity'.")
    }

    @Test
    fun setLimit() {
        assertExceptionThrown<IllegalArgumentException>(
            { buffer.limit = -1 }, "Property 'limit' can not accept a negative value."
        )
        assertExceptionThrown<IllegalArgumentException>(
            { buffer.limit = 1 + buffer.capacity },
            "Property 'limit' can not be higher than property 'capacity'."
        )

        val limit: Int = (size * .67).toInt()
        buffer.position = size
        buffer.limit = limit
        assertEquals(buffer.limit, limit, "Property 'limit' should accept value between 0 and 'capacity'.")
        assertEquals(buffer.position, limit, "Property 'position' should be decreased to new 'limit'.")
    }

    @Test
    fun getPosition() {
        assertEquals(buffer.position, 0, "Property 'position' should implicitly be set to 0.")
    }

    @Test
    fun setPosition() {
        assertExceptionThrown<IllegalArgumentException>(
            { buffer.position = -1 }, "Property 'position' can not accept a negative value."
        )
        assertExceptionThrown<IllegalArgumentException>(
            { buffer.position = 1 + buffer.limit },
            "Property 'position' can not be higher than property 'limit'."
        )

        val position: Int = (buffer.limit * .67).toInt()
        buffer.position = position
        assertEquals(buffer.position, position, "Property 'position' should accept value between 0 and 'limit'.")
    }

    @Test
    fun getOrder() {
        assertEquals(buffer.order, ByteOrder.BIG_ENDIAN, "Property 'order' should implicitly be 'BIG_ENDIAN'.")
    }

    @Test
    fun setOrder() {
        buffer.order = ByteOrder.LITTLE_ENDIAN
        assertEquals(buffer.order, ByteOrder.LITTLE_ENDIAN, "Property 'order' should be settable.")
    }

    @Test
    fun clear() {
        buffer.limit = (size * .67).toInt()
        buffer.position = (buffer.limit * .67).toInt()

        buffer.clear()

        assertEquals(buffer.limit, size, "Property 'limit' should be cleared to 'capacity'.")
        assertEquals(buffer.position, 0, "Property 'position' should be cleared to 0.")
    }

    @Test
    fun flip() {
        val position: Int = (buffer.limit * .67).toInt()
        buffer.position = position

        buffer.flip()

        assertEquals(buffer.limit, position, "Property 'limit' should be flipped to 'position'.")
    }

    @Test
    fun hasRemaining() {
        assertEquals(
            buffer.hasRemaining(),
            true,
            "Method 'hasRemaining' should return true if 'position' has not reached the value of 'limit'."
        )
        buffer.position = size
        assertEquals(
            buffer.hasRemaining(),
            false,
            "When 'position' has reached value 'limit' method 'hasRemaining' should return false."
        )
    }

    @Test
    fun mark() {
        val position: Int = (buffer.limit * .33).toInt()
        buffer.position = position

        buffer.mark()
        buffer.reset()
    }

    @Test
    fun remaining() {
        assertEquals(
            buffer.remaining(),
            buffer.limit - buffer.position,
            "Method 'remaining' should return 'limit' minus 'position'."
        )
    }

    @Test
    fun reset() {
        assertExceptionThrown<InvalidMarkException>(
            { buffer.reset() }, "Unmarked buffer shouldn't be resettable."
        )
    }

    @Test
    fun rewind() {
        val position: Int = (buffer.limit * .67).toInt()
        buffer.position = position
        buffer.rewind()
        assertEquals(buffer.position, 0, "Method 'rewind' should rewind 'position' to 0.")
    }

    @Test
    fun read() { // Implement asserts
        val arr = UByteArray(64)
        buffer.read(arr)
    }

    @Test
    fun write() { // Implement asserts
        val arr = UByteArray(64)
        buffer.write(arr)
    }

    @Test
    fun testWrite() { // Implement asserts
        val arr = ByteBuffer(64)
        buffer.write(arr)
    }

    @Test
    fun hasArray() {
        assertEquals(buffer.hasArray(), true, "Method 'hasArray' should return true implicitly.")
    }

    @Test
    fun array() {
        assertEquals(buffer.array() is UByteArray, true, "Method 'array' should return a UByteArray.")
    }

    @Test
    fun arrayOffset() {
        buffer.arrayOffset()
    }

    @Test
    fun testHashCode() {
        val copy: ByteBuffer = buffer.duplicate()
        assertEquals(copy.hashCode(), buffer.hashCode(), "Copies should have the same hash code.")
    }

    @Test
    fun testEquals() {
        val copy: ByteBuffer = buffer.duplicate()
        assertEquals(copy.equals(buffer), true, "Copies should equal each other.")
    }

    @Test
    fun compareTo() {
        val copy: ByteBuffer = buffer.duplicate()
        assertEquals(copy.compareTo(buffer), 0, "Comparing copies should return 0.")
    }

    @Test
    fun testRead() {
        // Method 'read(): Byte' is tested in 'testWrite1'.
    }

    @Test
    fun testWrite1() {
        val byte: UByte = 0x80u
        buffer.write(byte)

        assertEquals(buffer.position, UByte.SIZE_BYTES,
            "Method 'set' should advance the 'position' with 'UByte.SIZE_BYTES'.")
        buffer.rewind()
        assertEquals(buffer.read(), byte, "Method 'get' should return the byte.")
        assertEquals(buffer.position, UByte.SIZE_BYTES,
            "Method 'get' should advance the 'position' with 'UByte.SIZE_BYTES'.")
    }

    @Test
    fun testRead1() {
        // Method 'read(Int): Byte' is tested in 'testWrite2'.
    }

    @Test
    fun testWrite2() {
        val index = 122
        val byte: UByte = 0x80u
        buffer.write(index, byte)

        assertEquals(buffer.position, 0,
            "Method 'set' should NOT advance the 'position'.")
        assertEquals(buffer.read(index), byte, "Method 'get' should return the byte.")
        assertEquals(buffer.position, 0,
            "Method 'get' should NOT advance the 'position'.")
    }

    @Test
    fun slice() {
        buffer.position = 10
        buffer.mark()
        buffer.limit = size - 10
        val copy: ByteBuffer = buffer.slice()
        assertEquals(copy.capacity, size - 20, "The slice buffer should have value 'capacity' shorter.")
    }

    @Test
    fun duplicate() {
        val copy: ByteBuffer = buffer.duplicate()
        assertFalse(copy === buffer, "The duplicate should not be the same class instance")
        assertEquals(copy, buffer, "Both buffers data should equal.")
    }

    @Test
    fun asReadOnly() {
        val copy: ByteBuffer = buffer.asReadOnly()
        assertTrue(copy.readOnly, "Value 'readOnly' should be set.")
    }

    @Test
    fun readChar() {
        // readChar is tested in writeChar.
    }

    @Test
    fun writeChar() {
        val letter = 'Å'
        buffer.writeChar(letter)
        assertEquals(
            buffer.position,
            Char.SIZE_BYTES,
            "Method 'writeChar' should advance the 'position' with 'Char.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readChar(), letter, "Method 'readChar' should be able to read what was written.")

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeChar(letter)
        assertEquals(
            buffer.position,
            Char.SIZE_BYTES,
            "Method 'writeChar' should advance the 'position' with 'Char.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readChar(), letter, "Method 'readChar' should be able to read what was written.")
    }

    @Test
    fun readShort() {
        // readShort is tested in writeShort.
    }

    @Test
    fun writeShort() {
        buffer.writeShort(short)
        assertEquals(
            buffer.position,
            Short.SIZE_BYTES,
            "Method 'writeShort' should advance the 'position' with 'Short.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readShort(), short, "Method 'readShort' should be able to read what was written.")

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeShort(short)
        assertEquals(
            buffer.position,
            Short.SIZE_BYTES,
            "Method 'writeShort' should advance the 'position' with 'Short.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readShort(), short, "Method 'readShort' should be able to read what was written.")
    }

    @Test
    fun readUShort() {
        // readUShort is tested in writeUShort.
    }

    @Test
    fun writeUShort() {
        buffer.writeUShort(ushort)
        assertEquals(
            buffer.position,
            UShort.SIZE_BYTES,
            "Method 'writeUShort' should advance the 'position' with 'UShort.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(ushort, buffer.readUShort(), "Method 'readUShort' should be able to read what was written.")

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeUShort(ushort)
        assertEquals(
            buffer.position,
            UShort.SIZE_BYTES,
            "Method 'writeUShort' should advance the 'position' with 'UShort.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(ushort, buffer.readUShort(), "Method 'readUShort' should be able to read what was written.")
    }

    @Test
    fun readInt() {
        // readInt is tested in writeInt.
    }

    @Test
    fun writeInt() {
        buffer.writeInt(-int)
        assertEquals(
            buffer.position,
            Int.SIZE_BYTES,
            "Method 'writeInt' should advance the 'position' with 'Int.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readInt(), -int, "Method 'ReadInt' should be able to read what was written.")

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeInt(-int)
        assertEquals(
            buffer.position,
            Int.SIZE_BYTES,
            "Method 'writeInt' should advance the 'position' with 'Int.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readInt(), -int, "Method 'ReadInt' should be able to read what was written.")
    }

    @Test
    fun readUInt() {
        // readUInt is tested in writeUInt.
    }

    @Test
    fun writeUInt() {
        buffer.writeUInt(uint)
        assertEquals(
            buffer.position,
            UInt.SIZE_BYTES,
            "Method 'writeUInt' should advance the 'position' with 'UInt.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readUInt(), uint, "Method 'ReadUInt' should be able to read what was written.")

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeUInt(uint)
        assertEquals(
            buffer.position,
            UInt.SIZE_BYTES,
            "Method 'writeUInt' should advance the 'position' with 'UInt.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readUInt(), uint, "Method 'ReadUInt' should be able to read what was written.")
    }

    @Test
    fun readLong() {
        // readLong is tested in writeLong.
    }

    @Test
    fun writeLong() {
        buffer.writeLong(long)
        assertEquals(
            buffer.position,
            Long.SIZE_BYTES,
            "Method 'writeLong' should advance the 'position' with 'Long.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readLong(), long, "Method 'readLong' should be able to read what was written.")

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeLong(long)
        assertEquals(
            buffer.position,
            Long.SIZE_BYTES,
            "Method 'writeLong' should advance the 'position' with 'Long.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readLong(), long, "Method 'readLong' should be able to read what was written.")
    }

    @Test
    fun readULong() {
        // readULong is tested in writeULong.
    }

    @Test
    fun writeULong() {
        buffer.writeULong(ulong)
        assertEquals(
            buffer.position,
            ULong.SIZE_BYTES,
            "Method 'writeLong' should advance the 'position' with 'ULong.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readULong(), ulong, "Method 'readULong' should be able to read what was written.")

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeULong(ulong)
        assertEquals(
            buffer.position,
            ULong.SIZE_BYTES,
            "Method 'writeLong' should advance the 'position' with 'ULong.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readULong(), ulong, "Method 'readULong' should be able to read what was written.")
    }

    @Test
    fun readFloat() {
        // readFloat is tested in writeFloat.
    }

    @Test
    fun writeFloat() {
        val value: Float = -123.565F
        buffer.writeFloat(value)
        assertEquals(
            buffer.position,
            Float.SIZE_BYTES,
            "Method 'writeFloat' should advance the 'position' with 'Float.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readFloat(), value, "Method 'readFloat' should be able to read what was written.")

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeFloat(value)
        assertEquals(
            buffer.position,
            Float.SIZE_BYTES,
            "Method 'writeFloat' should advance the 'position' with 'Float.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readFloat(), value, "Method 'readFloat' should be able to read what was written.")
    }

    @Test
    fun readDouble() {
        // readDouble is tested in writeDouble.
    }

    @Test
    fun writeDouble() {
        val value: Double = (-234958739.324893498573495834753947535234571209347F).toDouble()
        buffer.writeDouble(value)
        assertEquals(
            buffer.position,
            Double.SIZE_BYTES,
            "Method 'writeDouble' should advance the 'position' with 'Double.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readDouble(), value, "Method 'readDouble' should be able to read what was written.")

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeDouble(value)
        assertEquals(
            buffer.position,
            Double.SIZE_BYTES,
            "Method 'writeDouble' should advance the 'position' with 'Double.SIZE_BYTES'."
        )
        buffer.rewind()
        assertEquals(buffer.readDouble(), value, "Method 'readDouble' should be able to read what was written.")
    }

    @Test
    fun testToString() {
    }

    @Test
    fun allocateDirect() {
        buffer = ByteBuffer.allocateDirect(size)
        assertEquals(buffer.capacity, size, "Value 'capacity' should always be the same as the given size.")
        assertEquals(buffer.limit, buffer.capacity, "Property 'limit' should implicitly be set to capacity.")
        assertEquals(buffer.position, 0, "Property 'position' should implicitly be set to 0.")
        assertEquals(buffer.readOnly, false, "Value 'readOnly' should implicitly be set to 'false'.")
        assertEquals(buffer.direct, true, "Value 'direct' should implicitly be set to 'true'.")
    }

    @Test
    fun allocate() {
        buffer = ByteBuffer.allocate(size)
        assertEquals(buffer.capacity, size, "Value 'capacity' should always be the same as the given size.")
        assertEquals(buffer.limit, buffer.capacity, "Property 'limit' should implicitly be set to capacity.")
        assertEquals(buffer.position, 0, "Property 'position' should implicitly be set to 0.")
        assertEquals(buffer.readOnly, false, "Value 'readOnly' should implicitly be set to 'false'.")
        assertEquals(buffer.direct, false, "Value 'direct' should implicitly be set to 'false'.")
    }

    @Test
    fun wrap() {
        buffer = ByteBuffer.wrap(UByteArray(size))
        assertEquals(buffer.capacity, size, "Value 'capacity' should always be the same as the given size.")
        assertEquals(buffer.limit, buffer.capacity, "Property 'limit' should implicitly be set to capacity.")
        assertEquals(buffer.position, 0, "Property 'position' should implicitly be set to 0.")
        assertEquals(buffer.readOnly, false, "Value 'readOnly' should implicitly be set to 'false'.")
        assertEquals(buffer.direct, false, "Value 'direct' should implicitly be set to 'false'.")
    }
}