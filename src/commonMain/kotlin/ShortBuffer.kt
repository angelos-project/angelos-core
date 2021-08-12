package angelos.nio

import kotlin.jvm.JvmStatic
import kotlin.math.min

abstract class ShortBuffer(capacity: Int, limit: Int, position: Int, mark: Int) : Buffer(
    capacity,
    limit,
    position,
    mark
), Comparable<ShortBuffer> {
    private var _arrayOffset: Int = 0
    private var _backingBuffer: ShortArray? = null

    companion object{
        @JvmStatic
        fun allocate(capacity: Int): ShortBuffer{
            return ShortBufferImpl(capacity)
        }

        @JvmStatic
        fun wrap(array: ShortArray, offset: Int = 0, length: Int = array.size): ShortBuffer{
            return ShortBufferImpl(array, array.size, offset + length, offset, -1, false)
        }
    }

    fun get(dst: ShortArray, offset: Int = 0, length: Int = dst.size){
        checkArraySize(dst.size, offset, length)
        checkForUnderflow(length)

        for (i: Int in offset..(offset+length))
            dst[i] = get()
    }

    fun put (src: ShortBuffer){
        if(src == this)
            throw IllegalArgumentException()

        checkForOverflow(src.remaining())

        if(src.remaining() > 0){
            val toPut: ShortArray = ShortArray(src.remaining())
            src.get(toPut)
            put(toPut)
        }
    }

    fun put(src: ShortArray, offset: Int = 0, length: Int = src.size){
        checkArraySize(src.size, offset, length)
        checkForOverflow(length)

        for(i: Int in offset..(offset+length))
            put(src[i])
    }

    fun hasArray(): Boolean{
        return (_backingBuffer != null && !isReadOnly())
    }

    fun array(): IntArray{
        if(_backingBuffer == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _backingBuffer as IntArray
    }

    fun arrayOffset(): Int{
        if (_backingBuffer == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _arrayOffset
    }

    override fun hashCode(): Int{
        var hashCode: Int = get(position) + 31
        var multiplier: Int = 1

        for(i: Int in (position +1)..limit){
            multiplier *= 31
            hashCode += (get(i).toInt() + 30)*multiplier
        }
        return hashCode
    }

    fun equals (obj: Any): Boolean {
        if (obj is ShortBuffer)
            return compareTo(obj) == 0

        return false
    }

    override fun compareTo(other: ShortBuffer): Int{
        val num: Int = min(remaining(), other.remaining())
        var posThis: Int = position
        var posOther: Int = other.position

        for (count: Int in 0..num) {
            val a: Short = get(posThis++)
            val b: Short = get(posOther++)

            if (a==b)
                continue

            if (a < b)
                return -1

            return 1
        }

        return remaining() - other.remaining()
    }

    abstract fun order(): ByteOrder

    abstract fun get(): Short

    abstract fun put(b: Short): ShortBuffer

    abstract fun get(index: Int): Short

    abstract fun put(index: Int, b: Short): ShortBuffer

    abstract fun compact(): ShortBuffer

    abstract fun isDirect(): Boolean

    abstract fun slice(): ShortBuffer

    abstract fun duplicate(): ShortBuffer

    abstract fun asReadOnlyBuffer(): ShortBuffer
}