package angelos.nio

import kotlin.jvm.JvmStatic
import kotlin.math.min

abstract class DoubleBuffer(capacity: Int, limit: Int, position: Int, mark: Int) : Buffer(
    capacity,
    limit,
    position,
    mark
), Comparable<DoubleBuffer> {
    private var _arrayOffset: Int = 0
    private var _backingBuffer: DoubleArray? = null

    companion object{
        @JvmStatic
        fun allocate(capacity: Int): DoubleBuffer{
            return DoubleBufferImpl(capacity)
        }

        @JvmStatic
        fun wrap(array: DoubleArray, offset: Int = 0, length: Int = array.size): DoubleBuffer{
            return DoubleBufferImpl(array, 0, array.size, offset + length, offset, -1, false)
        }
    }

    fun get(dst: DoubleArray, offset: Int = 0, length: Int = dst.size){
        checkArraySize(dst.size, offset, length)
        checkForUnderflow(length)

        for (i: Int in offset..(offset+length))
            dst[i] = get()
    }

    fun put (src: DoubleBuffer){
        if(src == this)
            throw IllegalArgumentException()

        checkForOverflow(src.remaining())

        if(src.remaining() > 0){
            val toPut: DoubleArray = DoubleArray(src.remaining())
            src.get(toPut)
            put(toPut)
        }
    }

    fun put(src: DoubleArray, offset: Int = 0, length: Int = src.size){
        checkArraySize(src.size, offset, length)
        checkForOverflow(length)

        for(i: Int in offset..(offset+length))
            put(src[i])
    }

    fun hasArray(): Boolean{
        return (_backingBuffer != null && !isReadOnly())
    }

    fun array(): DoubleArray{
        if(_backingBuffer == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _backingBuffer as DoubleArray
    }

    fun arrayOffset(): Int{
        if (_backingBuffer == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _arrayOffset
    }

    override fun hashCode(): Int{
        var hashCode: Int = get(position) as Int + 31
        var multiplier: Int = 1

        for(i: Int in (position +1)..limit){
            multiplier *= 31
            hashCode += (get(i) as Int + 30)*multiplier
        }
        return hashCode
    }

    fun equals (obj: Any): Boolean {
        if (obj is DoubleBuffer)
            return compareTo(obj) == 0

        return false
    }

    override fun compareTo(other: DoubleBuffer): Int{
        val num: Int = min(remaining(), other.remaining())
        var posThis: Int = position
        var posOther: Int = other.position

        for (count: Int in 0..num) {
            val a: Double = get(posThis++)
            val b: Double = get(posOther++)

            if (a==b)
                continue

            if (a < b)
                return -1

            return 1
        }

        return remaining() - other.remaining()
    }

    abstract fun order(): ByteOrder

    abstract fun get(): Double

    abstract fun put(b: Double): DoubleBuffer

    abstract fun get(index: Int): Double

    abstract fun put(index: Int, b: Double): DoubleBuffer

    abstract fun compact(): DoubleBuffer

    abstract fun isDirect(): Boolean

    abstract fun slice(): DoubleBuffer

    abstract fun duplicate(): DoubleBuffer

    abstract fun asReadOnlyBuffer(): DoubleBuffer
}