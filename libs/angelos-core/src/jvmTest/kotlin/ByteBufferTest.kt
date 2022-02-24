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
import org.junit.After
import org.junit.Test

import org.junit.Assert.*

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
            assertEquals(message, happened, true)
        }
    }

    @Test
    fun getDirect() {
        assertEquals("Value 'direct' should implicitly be 'true'.", buffer.direct, true)
    }

    @Test
    fun getCapacity() {
        assertEquals(
            "Value 'capacity' should implicitly be the length of the underlying array.",
            buffer.capacity,
            size
        )
    }

    @Test
    fun getPosition() {
        assertEquals("Property 'position' should implicitly be set to 0.", buffer.position, 0)
    }

    @Test
    fun setPosition() {
        assertExceptionThrown<IllegalArgumentException>(
            { buffer.position = -1 }, "Property 'position' can not accept a negative value."
        )
        assertExceptionThrown<IllegalArgumentException>(
            { buffer.position = 1 + buffer.capacity },
            "Property 'position' can not be higher than property 'limit'."
        )

        val position: Int = (buffer.capacity * .67).toInt()
        buffer.position = position
        assertEquals("Property 'position' should accept value between 0 and 'limit'.", buffer.position, position)
    }

    @Test
    fun getOrder() {
        assertEquals("Property 'order' should implicitly be 'BIG_ENDIAN'.", buffer.order, ByteOrder.BIG_ENDIAN)
    }

    @Test
    fun setOrder() {
        buffer.order = ByteOrder.LITTLE_ENDIAN
        assertEquals("Property 'order' should be settable.", buffer.order, ByteOrder.LITTLE_ENDIAN)
    }

    @Test
    fun hasRemaining() {
        assertEquals(
            "Method 'hasRemaining' should return true if 'position' has not reached the value of 'limit'.",
            buffer.hasRemaining(),
            true
        )
        buffer.position = size
        assertEquals(
            "When 'position' has reached value 'limit' method 'hasRemaining' should return false.",
            buffer.hasRemaining(),
            false
        )
    }

    @Test
    fun remaining() {
        assertEquals(
            "Method 'remaining' should return 'limit' minus 'position'.",
            buffer.remaining(),
            buffer.capacity - buffer.position
        )
    }

    @Test
    fun rewind() {
        val position: Int = (buffer.capacity * .67).toInt()
        buffer.position = position
        buffer.rewind()
        assertEquals("Method 'rewind' should rewind 'position' to 0.", buffer.position, 0)
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
        assertEquals("Method 'hasArray' should return true implicitly.", buffer.hasArray(), true)
    }

    @Test
    fun array() {
        assertEquals("Method 'array' should return a UByteArray.", buffer.array() is ByteArray, true)
    }

    @Test
    fun arrayOffset() {
        buffer.arrayOffset()
    }

    @Test
    fun testHashCode() {
        val copy: ByteBuffer = buffer.duplicate()
        assertEquals("Copies should have the same hash code.", copy.hashCode(), buffer.hashCode())
    }

    @Test
    fun testEquals() {
        val copy: ByteBuffer = buffer.duplicate()
        assertEquals("Copies should equal each other.", copy.equals(buffer), true)
    }

    @Test
    fun compareTo() {
        val copy: ByteBuffer = buffer.duplicate()
        assertEquals("Comparing copies should return 0.", copy.compareTo(buffer), 0)
    }

    @Test
    fun testRead() {
        // Method 'read(): Byte' is tested in 'testWrite1'.
    }

    @Test
    fun testWrite1() {
        val byte: UByte = 0x80u
        buffer.write(byte)

        assertEquals("Method 'set' should advance the 'position' with 'UByte.SIZE_BYTES'.",
            buffer.position, UByte.SIZE_BYTES)
        buffer.rewind()
        assertEquals("Method 'get' should return the byte.", buffer.read(), byte)
        assertEquals("Method 'get' should advance the 'position' with 'UByte.SIZE_BYTES'.",
            buffer.position, UByte.SIZE_BYTES)
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

        assertEquals("Method 'set' should NOT advance the 'position'.",
            buffer.position, 0)
        assertEquals("Method 'get' should return the byte.", buffer.read(index), byte)
        assertEquals("Method 'get' should NOT advance the 'position'.", buffer.position, 0)
    }

    @Test
    fun slice() {
        val copy: ByteBuffer = buffer.slice(buffer.position + 10..buffer.capacity - 10)
        assertEquals("The slice buffer should have value 'capacity' shorter.", copy.capacity, size - 20)
    }

    @Test
    fun duplicate() {
        val copy: ByteBuffer = buffer.duplicate()
        assertFalse("The duplicate should not be the same class instance", copy === buffer)
        assertEquals("Both buffers data should equal.", copy, buffer)
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
            "Method 'writeChar' should advance the 'position' with 'Char.SIZE_BYTES'.",
            buffer.position,
            Char.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readChar' should be able to read what was written.", buffer.readChar(), letter)

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeChar(letter)
        assertEquals(
            "Method 'writeChar' should advance the 'position' with 'Char.SIZE_BYTES'.",
            buffer.position,
            Char.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readChar' should be able to read what was written.", buffer.readChar(), letter)
    }

    @Test
    fun readShort() {
        // readShort is tested in writeShort.
    }

    @Test
    fun writeShort() {
        buffer.writeShort(short)
        assertEquals(
            "Method 'writeShort' should advance the 'position' with 'Short.SIZE_BYTES'.",
            buffer.position,
            Short.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readShort' should be able to read what was written.", buffer.readShort(), short)

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeShort(short)
        assertEquals(
            "Method 'writeShort' should advance the 'position' with 'Short.SIZE_BYTES'.",
            buffer.position,
            Short.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readShort' should be able to read what was written.", buffer.readShort(), short)
    }

    @Test
    fun readUShort() {
        // readUShort is tested in writeUShort.
    }

    @Test
    fun writeUShort() {
        buffer.writeUShort(ushort)
        assertEquals(
            "Method 'writeUShort' should advance the 'position' with 'UShort.SIZE_BYTES'.",
            buffer.position,
            UShort.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readUShort' should be able to read what was written.", ushort, buffer.readUShort())

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeUShort(ushort)
        assertEquals(
            "Method 'writeUShort' should advance the 'position' with 'UShort.SIZE_BYTES'.",
            buffer.position,
            UShort.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readUShort' should be able to read what was written.",
            ushort, buffer.readUShort())
    }

    @Test
    fun readInt() {
        // readInt is tested in writeInt.
    }

    @Test
    fun writeInt() {
        buffer.writeInt(-int)
        assertEquals(
            "Method 'writeInt' should advance the 'position' with 'Int.SIZE_BYTES'.",
            buffer.position,
            Int.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'ReadInt' should be able to read what was written.", buffer.readInt(), -int)

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeInt(-int)
        assertEquals(
            "Method 'writeInt' should advance the 'position' with 'Int.SIZE_BYTES'.",
            buffer.position,
            Int.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'ReadInt' should be able to read what was written.", buffer.readInt(), -int)
    }

    @Test
    fun readUInt() {
        // readUInt is tested in writeUInt.
    }

    @Test
    fun writeUInt() {
        buffer.writeUInt(uint)
        assertEquals(
            "Method 'writeUInt' should advance the 'position' with 'UInt.SIZE_BYTES'.",
            buffer.position,
            UInt.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'ReadUInt' should be able to read what was written.", buffer.readUInt(), uint)

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeUInt(uint)
        assertEquals(
            "Method 'writeUInt' should advance the 'position' with 'UInt.SIZE_BYTES'.",
            buffer.position,
            UInt.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'ReadUInt' should be able to read what was written.", buffer.readUInt(), uint)
    }

    @Test
    fun readLong() {
        // readLong is tested in writeLong.
    }

    @Test
    fun writeLong() {
        buffer.writeLong(long)
        assertEquals(
            "Method 'writeLong' should advance the 'position' with 'Long.SIZE_BYTES'.",
            buffer.position,
            Long.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readLong' should be able to read what was written.", buffer.readLong(), long)

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeLong(long)
        assertEquals(
            "Method 'writeLong' should advance the 'position' with 'Long.SIZE_BYTES'.",
            buffer.position,
            Long.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readLong' should be able to read what was written.", buffer.readLong(), long)
    }

    @Test
    fun readULong() {
        // readULong is tested in writeULong.
    }

    @Test
    fun writeULong() {
        buffer.writeULong(ulong)
        assertEquals(
            "Method 'writeLong' should advance the 'position' with 'ULong.SIZE_BYTES'.",
            buffer.position,
            ULong.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readULong' should be able to read what was written.", buffer.readULong(), ulong)

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeULong(ulong)
        assertEquals(
            "Method 'writeLong' should advance the 'position' with 'ULong.SIZE_BYTES'.",
            buffer.position,
            ULong.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readULong' should be able to read what was written.", buffer.readULong(), ulong)
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
            "Method 'writeFloat' should advance the 'position' with 'Float.SIZE_BYTES'.",
            buffer.position,
            Float.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readFloat' should be able to read what was written.", buffer.readFloat(), value)

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeFloat(value)
        assertEquals(
            "Method 'writeFloat' should advance the 'position' with 'Float.SIZE_BYTES'.",
            buffer.position,
            Float.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readFloat' should be able to read what was written.", buffer.readFloat(), value)
    }

    @Test
    fun readDouble() {
        // readDouble is tested in writeDouble.
    }

    @Test
    fun writeDouble() {
        val value: Double = (-234958739.324893498573495834753947535234571209347)
        buffer.writeDouble(value)
        assertEquals(
            "Method 'writeDouble' should advance the 'position' with 'Double.SIZE_BYTES'.",
            buffer.position,
            Double.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readDouble' should be able to read what was written.",
            buffer.readDouble().toLong(), value.toLong())

        buffer.order = ByteOrder.LITTLE_ENDIAN
        buffer.rewind()

        buffer.writeDouble(value)
        assertEquals(
            "Method 'writeDouble' should advance the 'position' with 'Double.SIZE_BYTES'.",
            buffer.position,
            Double.SIZE_BYTES
        )
        buffer.rewind()
        assertEquals("Method 'readDouble' should be able to read what was written.",
            buffer.readDouble().toLong(), value.toLong())
    }

    @Test
    fun testToString() {
    }

    @Test
    fun allocateDirect() {
        buffer = ByteBuffer.allocateDirect(size)
        assertEquals("Value 'capacity' should always be the same as the given size.", buffer.capacity, size)
        assertEquals("Property 'position' should implicitly be set to 0.", buffer.position, 0)
        assertEquals("Value 'direct' should implicitly be set to 'true'.", buffer.direct, true)
    }

    @Test
    fun allocate() {
        buffer = ByteBuffer.allocate(size)
        assertEquals("Value 'capacity' should always be the same as the given size.", buffer.capacity, size)
        assertEquals("Property 'position' should implicitly be set to 0.", buffer.position, 0)
        assertEquals("Value 'direct' should implicitly be set to 'false'.", buffer.direct, false)
    }

    @Test
    fun wrap() {
        buffer = ByteBuffer.wrap(ByteArray(size))
        assertEquals("Value 'capacity' should always be the same as the given size.", buffer.capacity, size)
        assertEquals("Property 'position' should implicitly be set to 0.", buffer.position, 0)
        assertEquals("Value 'direct' should implicitly be set to 'false'.", buffer.direct, false)
    }
}