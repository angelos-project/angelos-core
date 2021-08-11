package angelos.nio


enum class ByteOrder (val order: ByteOrder){
    BIG_ENDIAN(false),
    LITTLE_ENDIAN(true);
    companion object{
        fun nativeOrder(): ByteOrder{
            throw UnsupportedOperationException()
            //return if (Platform.isLittleEndian) LITTLE_ENDIAN else BIG_ENDIAN
        }
    }

    override fun toString(): String {
        return if (this == BIG_ENDIAN) "BIG_ENDIAN" else "LITTLE_ENDIAN"
    }
}