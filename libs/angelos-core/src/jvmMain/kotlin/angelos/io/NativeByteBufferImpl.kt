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
actual class NativeByteBufferImpl internal actual constructor(
    capacity: Int,
    limit: Int,
    mark: Int,
    endianness: Endianness
) : ByteBuffer(capacity, limit, mark, endianness), NativeBuffer {

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
    override fun readUByte(): UByte = theUnsafe.getByte(calculateAddress(_mark)).toUByte()

    override fun readChar(): Char = when (_reverse) {
        true -> reverseShort(theUnsafe.getShort(calculateAddress(_mark))).toInt().toChar()
        false -> Buffer.getChar(calculateAddress(_mark))
    }

    override fun readShort(): Short = when (_reverse) {
        true -> reverseShort(theUnsafe.getShort(calculateAddress(_mark)))
        false -> theUnsafe.getShort(calculateAddress(_mark))
    }

    override fun readUShort(): UShort = when (_reverse) {
        true -> reverseShort(theUnsafe.getShort(calculateAddress(_mark)))
        false -> theUnsafe.getShort(calculateAddress(_mark))
    }.toUShort()

    override fun readInt(): Int = when (_reverse) {
        true -> reverseInt(theUnsafe.getInt(calculateAddress(_mark)))
        false -> theUnsafe.getInt(calculateAddress(_mark))
    }

    override fun readUInt(): UInt = when (_reverse) {
        true -> reverseInt(theUnsafe.getInt(calculateAddress(_mark)))
        false -> theUnsafe.getInt(calculateAddress(_mark))
    }.toUInt()

    override fun readLong(): Long = when (_reverse) {
        true -> reverseLong(theUnsafe.getLong(calculateAddress(_mark)))
        false -> theUnsafe.getLong(calculateAddress(_mark))
    }

    override fun readULong(): ULong = when (_reverse) {
        true -> reverseLong(theUnsafe.getLong(calculateAddress(_mark)))
        false -> theUnsafe.getLong(calculateAddress(_mark))
    }.toULong()

    override fun readFloat(): Int = when (_reverse) {
        true -> reverseInt(theUnsafe.getInt(calculateAddress(_mark)))
        false -> theUnsafe.getInt(calculateAddress(_mark))
    }

    override fun readDouble(): Long = when (_reverse) {
        true -> reverseLong(theUnsafe.getLong(calculateAddress(_mark)))
        false -> theUnsafe.getLong(calculateAddress(_mark))
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
    actual override fun load(offset: Int): UByte { TODO("Do no implement") }

    actual fun toByteBuffer(): ByteBufferImpl {
        val buffer = ByteArray(capacity)
        for (index in 0 until capacity)
            buffer[index] = theUnsafe.getByte(_array + index)
        return byteBufferFrom(buffer, endian)
    }

    actual fun toMutableNativeByteBuffer(): MutableNativeByteBufferImpl {
        val buffer = MutableNativeByteBufferImpl(capacity, limit, mark,  mark, endian)
        theUnsafe.copyMemory(_array, buffer._array, capacity.toLong())
        return buffer
    }
}