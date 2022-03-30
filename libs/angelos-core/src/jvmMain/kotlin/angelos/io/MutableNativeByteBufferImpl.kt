/**
 * Copyright (c) 2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
package angelos.io

import angelos.interop.Buffer
import angelos.interop.NativeBuffer
import sun.misc.Unsafe
import java.lang.reflect.Field

@Suppress("DiscouragedPrivateApi")
actual class MutableNativeByteBufferImpl internal actual constructor(
    capacity: Int,
    limit: Int,
    position: Int,
    mark: Int,
    endianness: Endianness
) : MutableByteBuffer(capacity, limit, position, mark, endianness), NativeBuffer {

    internal val theUnsafe: Unsafe
    internal val _array: Long

    init{
        val f: Field = Unsafe::class.java.getDeclaredField("theUnsafe")
        f.isAccessible = true
        theUnsafe = f.get(null) as Unsafe

        _array = theUnsafe.allocateMemory(capacity.toLong())
    }

    private fun calculateAddress(cursor: Int): Long = _array + cursor

    override fun readByte(): Byte = theUnsafe.getByte(calculateAddress(_mark))
    override fun writeByte(value: Byte) = theUnsafe.putByte(calculateAddress(_position), value)

    override fun readUByte(): UByte = theUnsafe.getByte(calculateAddress(_mark)).toUByte()
    override fun writeUByte(value: UByte) = theUnsafe.putByte(calculateAddress(_position), value.toByte())

    override fun readChar(): Char = when (_reverse) {
        true -> reverseShort(theUnsafe.getShort(calculateAddress(_mark))).toInt().toChar()
        false -> Buffer.getChar(calculateAddress(_mark))
    }

    override fun writeChar(value: Char) = when (_reverse) {
        true -> theUnsafe.putShort(calculateAddress(_position), reverseShort(value.code.toShort()))
        false -> theUnsafe.putChar(calculateAddress(_position), value)
    }

    override fun readShort(): Short = when (_reverse) {
        true -> reverseShort(theUnsafe.getShort(calculateAddress(_mark)))
        false -> theUnsafe.getShort(calculateAddress(_mark))
    }

    override fun writeShort(value: Short) = when (_reverse) {
        true -> theUnsafe.putShort(calculateAddress(_position), reverseShort(value))
        false -> theUnsafe.putShort(calculateAddress(_position), value)
    }

    override fun readUShort(): UShort = when (_reverse) {
        true -> reverseShort(theUnsafe.getShort(calculateAddress(_mark)))
        false -> theUnsafe.getShort(calculateAddress(_mark))
    }.toUShort()

    override fun writeUShort(value: UShort) = when (_reverse) {
        true -> theUnsafe.putShort(calculateAddress(_position), reverseShort(value.toShort()))
        false -> theUnsafe.putShort(calculateAddress(_position), value.toShort())
    }

    override fun readInt(): Int = when (_reverse) {
        true -> reverseInt(theUnsafe.getInt(calculateAddress(_mark)))
        false -> theUnsafe.getInt(calculateAddress(_mark))
    }

    override fun writeInt(value: Int) = when (_reverse) {
        true -> theUnsafe.putInt(calculateAddress(_position), reverseInt(value))
        false -> theUnsafe.putInt(calculateAddress(_position), value)
    }

    override fun readUInt(): UInt = when (_reverse) {
        true -> reverseInt(theUnsafe.getInt(calculateAddress(_mark)))
        false -> theUnsafe.getInt(calculateAddress(_mark))
    }.toUInt()

    override fun writeUInt(value: UInt) = when (_reverse) {
        true -> theUnsafe.putInt(calculateAddress(_position), reverseInt(value.toInt()))
        false -> theUnsafe.putInt(calculateAddress(_position), value.toInt())
    }

    override fun readLong(): Long = when (_reverse) {
        true -> reverseLong(theUnsafe.getLong(calculateAddress(_mark)))
        false -> theUnsafe.getLong(calculateAddress(_mark))
    }

    override fun writeLong(value: Long) = when (_reverse) {
        true -> theUnsafe.putLong(calculateAddress(_position), reverseLong(value))
        false -> theUnsafe.putLong(calculateAddress(_position), value)
    }

    override fun readULong(): ULong = when (_reverse) {
        true -> reverseLong(theUnsafe.getLong(calculateAddress(_mark)))
        false -> theUnsafe.getLong(calculateAddress(_mark))
    }.toULong()

    override fun writeULong(value: ULong) = when (_reverse) {
        true -> theUnsafe.putLong(calculateAddress(_position), reverseLong(value.toLong()))
        false -> theUnsafe.putLong(calculateAddress(_position), value.toLong())
    }

    override fun readFloat(): Int = when (_reverse) {
        true -> reverseInt(theUnsafe.getInt(calculateAddress(_mark)))
        false -> theUnsafe.getInt(calculateAddress(_mark))
    }

    override fun writeFloat(value: Int) = when (_reverse) {
        true -> theUnsafe.putInt(calculateAddress(_position), reverseInt(value))
        false -> theUnsafe.putInt(calculateAddress(_position), value)
    }

    override fun readDouble(): Long = when (_reverse) {
        true -> reverseLong(theUnsafe.getLong(calculateAddress(_mark)))
        false -> theUnsafe.getLong(calculateAddress(_mark))
    }

    override fun writeDouble(value: Long) = when (_reverse) {
        true -> theUnsafe.putLong(calculateAddress(_position), reverseLong(value))
        false -> theUnsafe.putLong(calculateAddress(_position), value)
    }

    protected fun finalize() {
        theUnsafe.freeMemory(_array)
    }

    override fun copyInto(buffer: MutableByteBuffer, range: IntRange) {
        if (0 <= range.first && range.first <= range.last && range.last <= _capacity && range.last - range.first <= buffer.capacity - buffer.position) when (buffer) {
            is MutableNativeByteBufferImpl -> {
                theUnsafe.copyMemory(
                    _array + range.first,
                    buffer._array + buffer.position,
                    (range.last - range.first).toLong()
                )
            }
            else -> {
                val array = buffer.getArray()
                for (index in range.first until range.last)
                    array[buffer.position + index - range.first] = theUnsafe.getByte(_array + index.toLong())
            }
        } else {
            throw IndexOutOfBoundsException()
        }
    }

    actual override fun getArray(): ByteArray { TODO("Do not implement") }
    actual override fun load(offset: Int): UByte { TODO("Do not implement") }
    actual override fun save(value: UByte, offset: Int) { TODO("Do not implement") }

    actual fun toMutableByteBuffer(): MutableByteBufferImpl {
        val buffer = ByteArray(capacity)
        for (index in 0 until capacity)
            buffer[index] = theUnsafe.getByte(_array + index)
        return mutableByteBufferFrom(buffer, endian)
    }
}