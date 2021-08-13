package angelos.nio

import kotlin.jvm.JvmStatic
import kotlin.math.min

class ByteBuffer internal constructor(
    array: ByteArray,
    capacity: Int = 0,
    limit: Int = 0,
    position: Int = 0,
    mark: Int = -1,
    readOnly: Boolean = false,
    direct: Boolean = false,
) : Any(), Comparable<ByteBuffer> {
    val readOnly: Boolean = readOnly
    val direct: Boolean = direct

    private var _capacity: Int
    private var _limit: Int = 0
    private var _position: Int = 0
    private var _mark: Int = -1

    private var order: ByteOrder = ByteOrder.BIG_ENDIAN

    private val _array: ByteArray = array

    val capacity: Int
        get() = _capacity

    var limit: Int
        get() = _limit
        set(value) {
            if ((value < 0) || (value > _capacity))
                throw IllegalArgumentException()

            if (value < _mark)
                _mark = -1

            if (_position > value)
                _position = value

            _limit = value
        }

    var position: Int
        get() = _position
        set(value) {
            if ((value < 0) || (value > _limit))
                throw IllegalArgumentException()

            if (value <= _mark)
                _mark = -1

            _position = value
        }

    init {
        if (capacity < 0)
            throw IllegalArgumentException()

        _capacity = capacity
        this.limit = limit
        this.position = position

        if (mark >= 0) {
            if (mark > _position)
                throw IllegalArgumentException()

            _mark = mark
        }
    }

    constructor(capacity: Int) : this(ByteArray(size = capacity), capacity, capacity, direct=true)

    companion object {
        @JvmStatic
        private inline fun checkArraySize(limit: Int, offset: Int, size: Int) {
            if ((offset < 0) || (size < 0) || (limit < size + offset))
                throw IndexOutOfBoundsException()
        }

        @JvmStatic
        fun allocateDirect(capacity: Int): ByteBuffer {
            return ByteBuffer.allocate(capacity)
        }

        @JvmStatic
        fun allocate(capacity: Int): ByteBuffer {
            return wrap(ByteArray(size = capacity), 0, capacity)
        }

        @JvmStatic
        fun wrap(array: ByteArray, offset: Int = 0, length: Int = array.size): ByteBuffer {
            return ByteBuffer(array, array.size, offset + length, offset, -1, direct = false)
        }
    }

    fun clear() {
        _limit = _capacity
        _position = 0
        _mark = -1
    }

    fun flip() {
        _limit = _position
        _position = 0
        _mark = -1
    }

    fun hasRemaining(): Boolean {
        return remaining() > 0
    }

    fun mark() {
        _mark = _position
    }

    fun remaining(): Int {
        return _limit - _position
    }

    fun reset() {
        if (_mark == -1)
            throw InvalidMarkException()

        _position = _mark
    }

    fun rewind() {
        _position = 0
        _mark = -1
    }

    private inline fun checkForUnderflow(length: Int = 0) {
        if (length == 0 && (!hasRemaining()))
            throw BufferUnderflowException()
        if (remaining() < length)
            throw BufferUnderflowException()
    }

    private inline fun checkForOverflow(length: Int = 0) {
        if (length == 0 && (!hasRemaining()))
            throw BufferOverflowException()
        if (remaining() < length)
            throw BufferOverflowException()
    }

    private inline fun checkIndex(index: Int) {
        if (index < 0 || index >= _limit)
            throw IndexOutOfBoundsException()
    }

    private inline fun checkIfReadOnly() { // TODO(Mitigate and remove)
        if (readOnly)
            throw ReadOnlyBufferException()
    }

    private var _arrayOffset: Int = 0 // TODO(Remove or what?)

    fun get(dst: ByteArray, offset: Int = 0, length: Int = dst.size) {
        checkArraySize(dst.size, offset, length)
        checkForUnderflow(length)

        for (i: Int in offset..length)
            dst[i] = get()
    }

    fun set(src: ByteBuffer) {
        if (src == this)
            throw IllegalArgumentException()

        checkForOverflow(src.remaining())

        if (src.remaining() > 0) {
            val toSet: ByteArray = ByteArray(src.remaining())
            src.get(toSet)
            set(toSet)
        }
    }

    fun set(src: ByteArray, offset: Int = 0, length: Int = src.size) {
        checkArraySize(src.size, offset, length)
        checkForOverflow(length)

        for (i: Int in offset..length)
            set(src[i])
    }

    fun hasArray(): Boolean {
        return (_array != null && !readOnly)
    }

    fun array(): ByteArray {
        if (_array == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _array
    }

    fun arrayOffset(): Int {
        if (_array == null)
            throw UnsupportedOperationException()

        checkIfReadOnly()

        return _position
    }

    override fun hashCode(): Int {
        var hashCode: Int = get(_position).toInt() + 31
        var multiplier: Int = 1

        for (i: Int in (_position + 1).._limit) {
            multiplier *= 31
            hashCode += (get(i).toInt() + 30) * multiplier
        }
        return hashCode
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is ByteBuffer)
            return compareTo(obj as ByteBuffer) == 0

        return false
    }

    override fun compareTo(other: ByteBuffer): Int {
        val num: Int = min(remaining(), other.remaining())
        var posThis: Int = position
        var posOther: Int = other.position

        for (count: Int in 0..num) {
            val a: Byte = get(posThis++)
            val b: Byte = get(posOther++)

            if (a == b)
                continue

            if (a < b)
                return -1

            return 1
        }

        return remaining() - other.remaining()
    }

    fun get(): Byte {
        checkArraySize(_limit, _position, 1)
        val b: Byte = _array[_position]
        _position++
        return b
    }

    fun set(b: Byte) {
        checkArraySize(_limit, _position, 1)
        _array[_position] = b
        _position++
    }

    fun get(index: Int): Byte {
        checkArraySize(_limit, index, 1)
        return _array[index]
    }

    fun set(index: Int, b: Byte) {
        checkArraySize(_limit, index, 1)
        _array[index] = b
    }

    fun shiftDown(dstOffset: Int, srcOffset: Int, count: Int) {
        for (i: Int in 0..count)
            set(dstOffset + i, get(srcOffset + i))
    }

    fun slice(): ByteBuffer {
        return this // TODO(Implement)
    }

    fun duplicate(): ByteBuffer {
        return this // TODO(Implement)
    }

    fun asReadOnly(): ByteBuffer {
        return this // TODO(Implement)
    }

    private inline fun scanBytes(size: Int): Long {
        checkArraySize(_limit, _position, size)
        var value: Long = 0
        for (i in 0..size)
            value = value or (_array[_position + i].toLong() shl 8 * i)
        _position += size
        return value
    }

    private inline fun printBytes(size: Int, value: Long) {
        checkArraySize(_limit, _position, size)
        val b: Long = value as Long
        for (i in 0..size)
            _array[_position + (size - i)] = ((b ushr 8 * i) and 0xFFFF).toByte()
        _position += size
    }

    fun readChar(): Char {
        checkArraySize(_limit, _position, Char.SIZE_BYTES)
        val value: Char = ((_array[_position]).toInt() or (_array[_position + 1].toInt() shl 8)).toChar()
        _position += 2
        return value
    }

    fun writeChar(value: Char) {
        printBytes(2, value.code.toLong())
    }

    fun readShort(): Short {
        return scanBytes(2).toShort()
    }

    fun writeShort(value: Short) {
        printBytes(2, value.toLong())
    }

    fun readUShort(): UShort {
        return scanBytes(2).toUShort()
    }

    fun writeUShort(value: UShort) {
        printBytes(2, value.toLong())
    }

    fun readInt(): Int {
        return scanBytes(4).toInt()
    }

    fun writeInt(value: Int) {
        printBytes(4, value.toLong())
    }

    fun readUInt(): UInt {
        return scanBytes(4).toUInt()
    }

    fun writeUInt(value: UInt) {
        printBytes(4, value.toLong())
    }

    fun readLong(): Long {
        return scanBytes(8)
    }

    fun writeLong(value: Long) {
        printBytes(8, value)
    }

    fun readULong(): ULong {
        return scanBytes(8).toULong()
    }

    fun writeULong(value: ULong) {
        printBytes(8, value.toLong())
    }

    fun readFloat(): Float {
        return Float.fromBits(scanBytes(4).toInt())
    }

    fun writeFloat(value: Float) {
        printBytes(4, value.toRawBits().toLong())
    }

    fun readDouble(): Double {
        return Double.fromBits(scanBytes(8))
    }

    fun writeDouble(value: Double) {
        printBytes(8, value.toRawBits())
    }

    override fun toString(): String {
        return "ByteBuffer" + "pos[=" + position + " lim=" + limit + "cap=" + capacity + "]"
    }
}