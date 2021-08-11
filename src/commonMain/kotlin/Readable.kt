package angelos.nio

interface Readable {
    fun read(buf: CharBuffer): Int{
        throw IOException()
    }
}