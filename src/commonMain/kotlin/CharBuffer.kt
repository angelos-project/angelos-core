package angelos.nio

import kotlin.jvm.JvmStatic
import kotlin.math.min

abstract class CharBuffer(capacity: Int, limit: Int, position: Int, mark: Int) : Buffer(
    capacity,
    limit,
    position,
    mark
), Comparable<CharBuffer>, CharSequence, Readable, Appendable {
    private var _arrayOffset: Int = 0
    private var _backingBuffer: CharArray? = null

    override val length: Int
        get() = remaining()

    companion object{
        @JvmStatic
        fun allocate(capacity: Int): CharBuffer{
            return CharBufferImpl(capacity)
        }

        @JvmStatic
        fun wrap(array: CharArray, offset: Int = 0, length: Int = array.size): CharBuffer{
            return CharBufferImpl(array, array.size, offset + length, offset, -1, false)
        }

        @JvmStatic
        fun wrap(seq: CharSequence, start: Int = 0, end: Int = seq.length): CharBuffer{
            val len: Int = end - start

            if (len < 0)
                throw IndexOutOfBoundsException()

            val buffer: CharArray = CharArray(len)

            for( i: Int in 0..len)
                buffer[i] = seq[i+start]

            return wrap(buffer).asReadOnlyBuffer()
        }
    }

    fun get(dst: CharArray, offset: Int = 0, length: Int = dst.size){
        checkArraySize(dst.size, offset, length)
        checkForUnderflow(length)

        for (i: Int in offset..(offset+length))
            dst[i] = get()
    }

    override fun read(buffer: CharBuffer): Int{
        val rem: Int = min(buffer.remaining(), remaining())
        val buf: CharArray = CharArray(rem)
        get(buf)
        buffer.put(buf)
        return rem
    }

    fun put (src: CharBuffer){
        if(src == this)
            throw IllegalArgumentException()

        checkForOverflow(src.remaining())

        if(src.remaining() > 0){
            val toPut: CharArray = CharArray(src.remaining())
            src.get(toPut)
            put(toPut)
        }
    }

    fun put(src: CharArray, offset: Int = 0, length: Int = src.size){
        checkArraySize(src.size, offset, length)
        checkForOverflow(length)

        for(i: Int in offset..(offset+length))
            put(src[i])
    }

    fun hasArray(): Boolean{
        return (_backingBuffer != null && !isReadOnly())
    }

    fun array(): CharArray{
        if(_backingBuffer == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _backingBuffer as CharArray
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
        if (obj is CharBuffer)
            return compareTo(obj) == 0

        return false
    }

    fun compareTo(other: ByteBuffer): Int{
        val num: Int = min(remaining(), other.remaining())
        var posThis: Int = position
        var posOther: Int = other.position

        for (count: Int in 0..num) {
            val a: Char = get(posThis++)
            val b: Char = get(posOther++)

            if (a==b)
                continue

            if (a < b)
                return -1

            return 1
        }

        return remaining() - other.remaining()
    }

    abstract fun order(): ByteOrder

    abstract fun get(): Char

    abstract fun put(b: Char): CharBuffer

    abstract override fun get(index: Int): Char

    abstract fun put(index: Int, b: Char): CharBuffer

    abstract fun compact()

    abstract fun isDirect(): Boolean

    abstract fun slice(): CharBuffer

    abstract fun duplicate(): CharBuffer

    abstract fun asReadOnlyBuffer(): CharBuffer

    override fun toString(): String{
        if(hasArray())
            return array().concatToString(position, position + length)

        val buf: CharArray = CharArray(length)
        val pos: Int = position
        get(buf)
        position = pos
        return buf.concatToString()
    }

    abstract override fun subSequence(start: Int, length: Int): CharSequence

    fun put(str: String, start: Int = 0, length: Int = str.length) {
        put(str.toCharArray(), start, length)
    }

    fun charAt(index: Int): Char{
        if(index < 0 || index >= remaining())
            throw IndexOutOfBoundsException()

        return get(position+index)
    }

    fun append(cs: CharSequence, start: Int = 0, end: Int = cs.length){
        put(if(cs === null) "null" else cs.subSequence(start, end).toString())
    }
}