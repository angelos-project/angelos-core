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

import angelos.nio.Buffer
import angelos.nio.ByteOrder
import kotlin.math.min

abstract class LongBuffer(capacity: Int, limit: Int, position: Int, mark: Int) : Buffer(
    capacity,
    limit,
    position,
    mark
), Comparable<LongBuffer> {
    private var _arrayOffset: Int = 0
    private var _backingBuffer: LongArray? = null

    companion object{
        /*@JvmStatic
        fun allocate(capacity: Int): LongBuffer{
            //return LongBufferImpl(size=capacity)
        }

        @JvmStatic
        fun wrap(array: LongArray, offset: Int = 0, length: Int = array.size): LongBuffer{
            //return LongBufferImpl(array, array.size, offset + length, offset, offset, -1, false)
        }*/
    }

    fun get(dst: LongArray, offset: Int = 0, length: Int = dst.size){
        checkArraySize(dst.size, offset, length)
        checkForUnderflow(length)

        for (i: Int in offset..(offset+length))
            dst[i] = get()
    }

    fun put (src: LongBuffer){
        if(src == this)
            throw IllegalArgumentException()

        checkForOverflow(src.remaining())

        if(src.remaining() > 0){
            val toPut: LongArray = LongArray(src.remaining())
            src.get(toPut)
            put(toPut)
        }
    }

    fun put(src: LongArray, offset: Int = 0, length: Int = src.size){
        checkArraySize(src.size, offset, length)
        checkForOverflow(length)

        for(i: Int in offset..(offset+length))
            put(src[i])
    }

    fun hasArray(): Boolean{
        return (_backingBuffer != null && !isReadOnly())
    }

    fun array(): LongArray{
        if(_backingBuffer == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _backingBuffer as LongArray
    }

    fun arrayOffset(): Int{
        if (_backingBuffer == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _arrayOffset
    }

    override fun hashCode(): Int{
        var hashCode: Long = get(position) + 31
        var multiplier: Long = 1

        for(i: Int in (position +1)..limit){
            multiplier *= 31
            hashCode += (get(i) + 30)*multiplier
        }
        return hashCode.toInt()
    }

    override fun equals (obj: Any?): Boolean {
        if (obj is LongBuffer)
            return compareTo(obj) == 0

        return false
    }

    override fun compareTo(other: LongBuffer): Int{
        val num: Int = min(remaining(), other.remaining())
        var posThis: Int = position
        var posOther: Int = other.position

        for (count: Int in 0..num) {
            val a: Long = get(posThis++)
            val b: Long = get(posOther++)

            if (a==b)
                continue

            if (a < b)
                return -1

            return 1
        }

        return remaining() - other.remaining()
    }

    abstract fun order(): ByteOrder

    abstract fun get(): Long

    abstract fun put(b: Long): LongBuffer

    abstract fun get(index: Int): Long

    abstract fun put(index: Int, b: Long): LongBuffer

    abstract fun compact(): LongBuffer

    abstract fun isDirect(): Boolean

    abstract fun slice(): LongBuffer

    abstract fun duplicate(): LongBuffer

    abstract fun asReadOnlyBuffer(): LongBuffer
}
