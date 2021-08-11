package angelos.nio

import kotlin.jvm.JvmStatic


abstract class Buffer(capacity: Int, limit: Int, position: Int, mark: Int) : Any() {
    private var _capacity: Int = 0
    var capacity: Int
        get() = _capacity
    private var _limit: Int = 0
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
    private var _position: Int = 0
    var position: Int
        get() = _position
        set(value) {
            if ((value < 0) || (value > _limit))
                throw IllegalArgumentException()

            if (value <= _mark)
                _mark = -1

            _position = value
        }
    private var _mark: Int = -1
    val mark: Int
        get() = _mark

    var address: Any? = null // TODO()

    init {
        if (capacity < 0)
            throw IllegalArgumentException()

        this.capacity = capacity
        this.limit = limit
        this.position = position

        if (mark >= 0) {
            if (mark > _position)
                throw IllegalArgumentException()

            _mark = mark
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

    abstract fun isReadOnly(): Boolean

    fun updateMark() {
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

    protected fun checkForUnderflow(length: Int = 0) {
        if (length == 0 && (!hasRemaining()))
            throw BufferUnderflowException()
        if (remaining() < length)
            throw BufferUnderflowException()
    }

    protected fun checkForOverflow(length: Int = 0) {
        if (length == 0 && (!hasRemaining()))
            throw BufferOverflowException()
        if (remaining() < length)
            throw BufferOverflowException()
    }

    protected fun checkIndex(index: Int) {
        if (index < 0 || index >= _limit)
            throw IndexOutOfBoundsException()
    }

    protected fun checkIfReadOnly() {
        if (isReadOnly())
            throw ReadOnlyBufferException()
    }

    companion object {
        @JvmStatic
        protected fun checkArraySize(arrayLength: Int, offset: Int, length: Int) {
            if ((offset < 0) || (length < 0) || (arrayLength < length + offset))
                throw IndexOutOfBoundsException()
        }
    }
}