package angelos.nio

import kotlin.jvm.JvmStatic
import kotlin.math.min

abstract class ByteBuffer(capacity: Int, limit: Int, position: Int, mark: Int) : Buffer(capacity, limit, position, mark), Comparable<ByteBuffer> {
    private var _endian: ByteOrder = ByteOrder.BIG_ENDIAN
    var order: ByteOrder
        get() = _endian
        set(value){
            _endian = value
        }
    private var _arrayOffset: Int = 0
    private var _backingBuffer: ByteArray? = null

    companion object {
        @JvmStatic
        fun allocateDirect(capacity: Int): ByteBuffer{
            return DirectByteBufferImpl.allocate(capacity)
        }

        @JvmStatic
        fun allocate(capacity: Int): ByteBuffer {
            return wrap(ByteArray(capacity), 0, capacity)
        }

        @JvmStatic
        fun wrap(array: ByteArray, offset: Int = 0, length: Int = array.size): ByteBuffer {
            return ByteBufferImpl(array, 0, array.size, offset + length, offset, -1, false)
        }
    }

    fun get(dst: ByteArray, offset: Int = 0, length: Int = dst.size) {
        checkArraySize(dst.size, offset, length)
        checkForUnderflow(length)

        for(i: Int in offset..length)
            dst[i] = get()
    }

    fun put(src: ByteBuffer) {
        if (src == this)
            throw IllegalArgumentException()

        checkForOverflow(src.remaining())

        if (src.remaining() > 0){
            val toPut: ByteArray = ByteArray(src.remaining())
            src.get(toPut)
            put(toPut)
        }
    }

    fun put(src: ByteArray, offset: Int = 0, length: Int = src.size){
        checkArraySize(src.size, offset, length)
        checkForOverflow(length)

        for(i: Int in offset..length)
            put(src[i])
    }

    fun hasArray(): Boolean{
        return (_backingBuffer != null && !isReadOnly())
    }

    fun array(): ByteArray{
        if(_backingBuffer == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _backingBuffer as ByteArray
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
            hashCode += (get(i) + 30)*multiplier
        }
        return hashCode
    }

    fun equals (obj: Any): Boolean {
        if (obj is ByteBuffer)
            return compareTo(obj) == 0

        return false
    }

    override fun compareTo(other: ByteBuffer): Int{
        val num: Int = min(remaining(), other.remaining())
        var posThis: Int = position
        var posOther: Int = other.position

        for (count: Int in 0..num) {
            val a: Byte = get(posThis++)
            val b: Byte = get(posOther++)

            if (a==b)
                continue

            if (a < b)
                return -1

            return 1
        }

        return remaining() - other.remaining()
    }

    abstract fun get(): Byte

    abstract fun put(b: Byte): ByteBuffer

    abstract fun get(index: Int): Byte

    abstract fun put(index: Int, b: Byte): ByteBuffer

    abstract fun compact(): ByteBuffer

    fun shiftDown(dstOffset: Int, srcOffset: Int, count: Int){
        for(i: Int in 0..count)
            put(dstOffset + i, get(srcOffset+i))
    }

    abstract fun isDirect(): Boolean

    abstract fun slice(): ByteBuffer

    abstract fun duplicate(): ByteBuffer

    abstract fun asReadOnlyBuffer(): ByteBuffer

    abstract fun asShortBuffer(): ShortBuffer

    abstract fun asCharBuffer(): CharBuffer

    abstract fun asIntBuffer(): IntBuffer

    abstract fun asLongBuffer(): LongBuffer

    abstract fun asFloatBuffer(): FloatBuffer

    abstract fun asDoubleBuffer(): DoubleBuffer

    abstract fun getChar(): Char

    abstract fun putChar(value: Char): ByteBuffer

    abstract fun getChar(index: Int)

    abstract fun putChar(index: Int, value: Char): ByteBuffer

    abstract fun getShort(): Short

    abstract fun putShort(value: Short): ByteBuffer

    abstract fun getShort(index: Int): Short

    abstract fun putShort(index: Int, value: Short): ByteBuffer

    abstract fun getInt(): Int

    abstract fun putInt(value: Int): ByteBuffer

    abstract fun getInt(index: Int): Int

    abstract fun putInt(index: Int, value: Int): ByteBuffer

    abstract fun getLong(): Long

    abstract fun putLong(value: Long): ByteBuffer

    abstract fun getLong(index: Int): Long

    abstract fun putLong(index: Int, value: Long): ByteBuffer

    abstract fun getFloat(): Float

    abstract fun putFloat(value: Float): ByteBuffer

    abstract fun getFloat(index: Int): Float

    abstract fun putFloat(index: Int, value: Float): ByteBuffer

    abstract fun getDouble(): Double

    abstract fun putDouble(value: Double): ByteBuffer

    abstract fun getDouble(index: Int): Double

    abstract fun putDouble(index: Int, value: Double): ByteBuffer

    override fun toString(): String {
        return "ByteBuffer" + "pos[=" + position + " lim=" + limit + "cap=" + capacity + "]"
    }
}
