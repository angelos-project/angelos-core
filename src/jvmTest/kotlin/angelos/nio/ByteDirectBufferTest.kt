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
        buffer.putChar(letter)
        assertEquals(buffer.position, Char.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getChar(), letter)

        reverseEndian()
        buffer.rewind()

        buffer.putChar(letter)
        assertEquals(buffer.position, Char.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getChar(), letter)
    }

    @Test
    fun readShort() {
        // readShort is tested in writeShort.
    }

    @Test
    fun writeShort() {
        buffer.putShort(short)
        assertEquals(buffer.position, Short.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getShort(), short)

        reverseEndian()
        buffer.rewind()

        buffer.putShort(short)
        assertEquals(buffer.position, Short.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getShort(), short)
    }

    @Test
    fun readUShort() {
        // readUShort is tested in writeUShort.
    }

    @Test
    fun writeUShort() {
        buffer.putUShort(ushort)
        assertEquals(buffer.position, UShort.SIZE_BYTES)

        buffer.rewind()
        assertEquals(ushort, buffer.getUShort())

        reverseEndian()
        buffer.rewind()

        buffer.putUShort(ushort)
        assertEquals(buffer.position, UShort.SIZE_BYTES)

        buffer.rewind()
        assertEquals(ushort, buffer.getUShort())
    }

    @Test
    fun readInt() {
        // readInt is tested in writeInt.
    }

    @Test
    fun writeInt() {
        buffer.putInt(-int)
        assertEquals(buffer.position, Int.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getInt(), -int)

        reverseEndian()
        buffer.rewind()

        buffer.putInt(-int)
        assertEquals(buffer.position, Int.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getInt(), -int)
    }

    @Test
    fun readUInt() {
        // readUInt is tested in writeUInt.
    }

    @Test
    fun writeUInt() {
        buffer.putUInt(uint)
        kotlin.test.assertEquals(buffer.position, UInt.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getUInt(), uint)

        reverseEndian()
        buffer.rewind()

        buffer.putUInt(uint)
        assertEquals(buffer.position, UInt.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getUInt(), uint)
    }

    @Test
    fun readLong() {
        // readLong is tested in writeLong.
    }

    @Test
    fun writeLong() {
        buffer.putLong(long)
        assertEquals(buffer.position, Long.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getLong(), long)

        reverseEndian()
        buffer.rewind()

        buffer.putLong(long)
        assertEquals(buffer.position, Long.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getLong(), long)
    }

    @Test
    fun readULong() {
        // readULong is tested in writeULong.
    }

    @Test
    fun writeULong() {
        buffer.putULong(ulong)
        assertEquals(buffer.position, ULong.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getULong(), ulong)

        reverseEndian()
        buffer.rewind()

        buffer.putULong(ulong)
        assertEquals(buffer.position, ULong.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getULong(), ulong)
    }

    @Test
    fun readFloat() {
        // readFloat is tested in writeFloat.
    }

    @Test
    fun writeFloat() {
        val value: Float = -123.565F
        buffer.putFloat(value)
        assertEquals(buffer.position, Float.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getFloat(), value)

        reverseEndian()
        buffer.rewind()

        buffer.putFloat(value)
        assertEquals(buffer.position, Float.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getFloat(), value)
    }

    @Test
    fun readDouble() {
        // readDouble is tested in writeDouble.
    }

    @Test
    fun writeDouble() {
        val value: Double = (-234958739.324893498573495834753947535234571209347F).toDouble()
        buffer.putDouble(value)
        assertEquals(buffer.position, Double.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getDouble(), value, 0.0)

        reverseEndian()
        buffer.rewind()

        buffer.putDouble(value)
        assertEquals(buffer.position, Double.SIZE_BYTES)

        buffer.rewind()
        assertEquals(buffer.getDouble(), value, 0.0)
    }

    @Test
    fun load() {
    }

    @Test
    fun save() {
    }
}