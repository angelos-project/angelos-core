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
) : AbstractByteBuffer(capacity, limit, mark, endianness), NativeBuffer {

    private val theUnsafe: Unsafe
    private val _array: Long

    init{
        val f: Field = Unsafe::class.java.getDeclaredField("theUnsafe")
        f.isAccessible = true
        theUnsafe = f.get(null) as Unsafe

        _array = theUnsafe.allocateMemory(capacity.toLong())
    }

    private fun calculateAddress(): Long = _array + _mark

    override fun readChar(): Char = when (_reverse) {
        true -> ByteBuffer.reverseShort(theUnsafe.getShort(calculateAddress())).toInt().toChar()
        false -> Buffer.getChar(calculateAddress())
    }

    override fun readShort(): Short = when (_reverse) {
        true -> ByteBuffer.reverseShort(theUnsafe.getShort(calculateAddress()))
        false -> theUnsafe.getShort(calculateAddress())
    }

    override fun readUShort(): UShort = when (_reverse) {
        true -> ByteBuffer.reverseShort(theUnsafe.getShort(calculateAddress()))
        false -> theUnsafe.getShort(calculateAddress())
    }.toUShort()

    override fun readInt(): Int = when (_reverse) {
        true -> ByteBuffer.reverseInt(theUnsafe.getInt(calculateAddress()))
        false -> theUnsafe.getInt(calculateAddress())
    }

    override fun readUInt(): UInt = when (_reverse) {
        true -> ByteBuffer.reverseInt(theUnsafe.getInt(calculateAddress()))
        false -> theUnsafe.getInt(calculateAddress())
    }.toUInt()

    override fun readLong(): Long = when (_reverse) {
        true -> ByteBuffer.reverseLong(theUnsafe.getLong(calculateAddress()))
        false -> theUnsafe.getLong(calculateAddress())
    }

    override fun readULong(): ULong = when (_reverse) {
        true -> ByteBuffer.reverseLong(theUnsafe.getLong(calculateAddress()))
        false -> theUnsafe.getLong(calculateAddress())
    }.toULong()

    override fun readFloat(): Int = when (_reverse) {
        true -> ByteBuffer.reverseInt(theUnsafe.getInt(calculateAddress()))
        false -> theUnsafe.getInt(calculateAddress())
    }

    override fun readDouble(): Long = when (_reverse) {
        true -> ByteBuffer.reverseLong(theUnsafe.getLong(calculateAddress()))
        false -> theUnsafe.getLong(calculateAddress())
    }

    protected fun finalize() {
        theUnsafe.freeMemory(_array)
    }

    actual override fun load(offset: Int): UByte {
        TODO("Do no implement")
    }

    actual override fun copyInto(buffer: MutableByteBuffer, range: IntRange) {
        TODO("Not yet implemented")
    }
}