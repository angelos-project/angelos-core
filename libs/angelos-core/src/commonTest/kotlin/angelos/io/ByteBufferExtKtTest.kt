package angelos.io

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ByteBufferExtKtTest {

    @Test
    fun byteBufferFrom() {
        assertNotNull(byteBufferFrom(ByteArray(4096)))
    }

    @Test
    fun mutableByteBufferWith() {
        assertNotNull(mutableByteBufferWith(4096))
    }

    @Test
    fun mutableByteBufferFrom() {
        assertNotNull(mutableByteBufferFrom(ByteArray(4096)))
    }

    @Test
    fun mutableNativeByteBufferWith() {
        assertNotNull(mutableNativeByteBufferWith(4096))
    }

    @Test
    fun nativeByteBufferWith() {
        assertNotNull(nativeByteBufferWith(4096))
    }

    @Test
    fun toMutableByteBuffer() {
        val buffer = byteBufferFrom(ByteArray(4096)).toMutableByteBuffer()
        assertEquals(buffer.capacity, 4096)
    }

    @Test
    fun toMutableNativeByteBuffer() {
        val buffer = byteBufferFrom(ByteArray(4096)).toMutableNativeByteBuffer()
        assertEquals(buffer.capacity, 4096)
    }

    @Test
    fun toNativeByteBuffer() {
        val buffer = byteBufferFrom(ByteArray(4096)).toNativeByteBuffer()
        assertEquals(buffer.capacity, 4096)
    }

    @Test
    fun toByteBuffer() {
        val buffer = nativeByteBufferWith(4096).toByteBuffer()
        assertEquals(buffer.capacity, 4096)
    }

    @Test
    fun testToMutableNativeByteBuffer() {
        val buffer = mutableByteBufferWith(4096).toMutableNativeByteBuffer()
        assertEquals(buffer.capacity, 4096)
    }

    @Test
    fun testToMutableByteBuffer() {
        val buffer = mutableNativeByteBufferWith(4096).toMutableByteBuffer()
        assertEquals(buffer.capacity, 4096)
    }

    @Test
    fun testToMutableNativeByteBuffer1() {
        val buffer = nativeByteBufferWith(4096).toMutableNativeByteBuffer()
        assertEquals(buffer.capacity, 4096)
    }
}