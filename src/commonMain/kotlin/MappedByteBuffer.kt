package angelos.nio

abstract class MappedByteBuffer(capacity: Int, limit: Int, position: Int, mark: Int) : ByteBuffer(capacity, limit, position, mark) {
    private fun forceImpl(){
    }

    fun force(): MappedByteBuffer{
        forceImpl()
        return this
    }

    fun isLoadedImpl(): Boolean{
        load()
        return true
    }

    fun isLoaded(): Boolean{
        return isLoadedImpl()
    }

    fun loadImpl(){
    }

    fun load(): MappedByteBuffer{
        loadImpl()
        return this
    }

    fun unmapImpl(){
        forceImpl()
    }

    protected fun finalize(){
        unmapImpl()
    }
}