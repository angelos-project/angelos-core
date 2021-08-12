package angelos.nio

import kotlin.jvm.JvmStatic
import kotlin.math.min

abstract class IntBuffer(capacity: Int, limit: Int, position: Int, mark: Int) : Buffer(capacity, limit, position, mark), Comparable<IntBuffer> {
    private var _arrayOffset: Int = 0
    private var _backingBuffer: IntArray? = null

    companion object{
        @JvmStatic
        fun allocate(capacity: Int): IntBuffer{
            return IntBufferImpl(capacity)
        }

        @JvmStatic
        fun wrap(array: IntArray, offset: Int = 0, length: Int = array.size): IntBuffer{
            return IntBufferImpl(array, array.size, offset + length, offset, -1, false)
        }
    }

    fun get(dst: IntArray, offset: Int = 0, length: Int = dst.size){
        checkArraySize(dst.size, offset, length)
        checkForUnderflow(length)

        for (i: Int in offset..(offset+length))
            dst[i] = get()
    }

    fun put (src: IntBuffer){
        if(src == this)
            throw IllegalArgumentException()

        checkForOverflow(src.remaining())

        if(src.remaining() > 0){
            val toPut: IntArray = IntArray(src.remaining())
            src.get(toPut)
            put(toPut)
        }
    }

    fun put(src: IntArray, offset: Int = 0, length: Int = src.size){
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
        var hashCode: Int = get(position).toInt() + 31
        var multiplier: Int = 1

        for(i: Int in (position +1)..limit){
            multiplier *= 31
            hashCode += (get(i).toInt() + 30)*multiplier
        }
        return hashCode
    }

    fun equals (obj: Any): Boolean {
        if (obj is IntBuffer)
            return compareTo(obj) == 0

        return false
    }

    override fun compareTo(other: IntBuffer): Int{
        val num: Int = min(remaining(), other.remaining())
        var posThis: Int = position
        var posOther: Int = other.position

        for (count: Int in 0..num) {
            val a: Int = get(posThis++)
            val b: Int = get(posOther++)

            if (a==b)
                continue

            if (a < b)
                return -1

            return 1
        }

        return remaining() - other.remaining()
    }

    abstract fun order(): ByteOrder

    abstract fun get(): Int

    abstract fun put(b: Int): IntBuffer

    abstract fun get(index: Int): Int

    abstract fun put(index: Int, b: Int): IntBuffer

    abstract fun compact(): IntBuffer

    abstract fun isDirect(): Boolean

    abstract fun slice(): IntBuffer

    abstract fun duplicate(): IntBuffer

    abstract fun asReadOnlyBuffer(): IntBuffer
}