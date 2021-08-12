package angelos.nio

import kotlin.jvm.JvmStatic
import kotlin.math.min

abstract class FloatBuffer(capacity: Int, limit: Int, position: Int, mark: Int) : Buffer(
    capacity,
    limit,
    position,
    mark
), Comparable<FloatBuffer> {
    private var _arrayOffset: Int = 0
    private var _backingBuffer: FloatArray? = null

    companion object{
        @JvmStatic
        fun allocate(capacity: Int): FloatBuffer{
            return FloatBufferImpl(capacity)
        }

        @JvmStatic
        fun wrap(array: FloatArray, offset: Int = 0, length: Int = array.size): FloatBuffer{
            return FloatBufferImpl(array, array.size, offset + length, offset, -1, false)
        }
    }

    fun get(dst: FloatArray, offset: Int = 0, length: Int = dst.size){
        checkArraySize(dst.size, offset, length)
        checkForUnderflow(length)

        for (i: Int in offset..(offset+length))
            dst[i] = get()
    }

    fun put (src: FloatBuffer){
        if(src == this)
            throw IllegalArgumentException()

        checkForOverflow(src.remaining())

        if(src.remaining() > 0){
            val toPut: FloatArray = FloatArray(src.remaining())
            src.get(toPut)
            put(toPut)
        }
    }

    fun put(src: FloatArray, offset: Int = 0, length: Int = src.size){
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
        if (obj is FloatBuffer)
            return compareTo(obj) == 0

        return false
    }

    override fun compareTo(other: FloatBuffer): Int{
        val num: Int = min(remaining(), other.remaining())
        var posThis: Int = position
        var posOther: Int = other.position

        for (count: Int in 0..num) {
            val a: Float = get(posThis++)
            val b: Float = get(posOther++)

            if (a==b)
                continue

            if (a < b)
                return -1

            return 1
        }

        return remaining() - other.remaining()
    }

    abstract fun order(): ByteOrder

    abstract fun get(): Float

    abstract fun put(b: Float): FloatBuffer

    abstract fun get(index: Int): Float

    abstract fun put(index: Int, b: Float): FloatBuffer

    abstract fun compact(): FloatBuffer

    abstract fun isDirect(): Boolean

    abstract fun slice(): FloatBuffer

    abstract fun duplicate(): FloatBuffer

    abstract fun asReadOnlyBuffer(): FloatBuffer
}