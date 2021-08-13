package angelos.nio

interface Readable<A> {
    fun read(ByteBuffer: A): Int
}