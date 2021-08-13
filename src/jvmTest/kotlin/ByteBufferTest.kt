import angelos.nio.ByteBuffer
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ByteBufferTest {
    val size: Int = 128
    var buffer: ByteBuffer? = null

    @Before
    fun setUp() {
        buffer = ByteBuffer(size)
    }

    @After
    fun tearDown() {
        buffer = null
    }

    @Test
    fun getReadOnly() {
    }

    @Test
    fun getDirect() {
    }

    @Test
    fun getCapacity() {
    }

    @Test
    fun getLimit() {
    }

    @Test
    fun setLimit() {
    }

    @Test
    fun getPosition() {
    }

    @Test
    fun setPosition() {
    }

    @Test
    fun clear() {
    }

    @Test
    fun flip() {
    }

    @Test
    fun hasRemaining() {
    }

    @Test
    fun mark() {
    }

    @Test
    fun remaining() {
    }

    @Test
    fun reset() {
    }

    @Test
    fun rewind() {
    }

    @Test
    fun get() {
    }

    @Test
    fun set() {
    }

    @Test
    fun testSet() {
    }

    @Test
    fun hasArray() {
    }

    @Test
    fun array() {
    }

    @Test
    fun arrayOffset() {
    }

    @Test
    fun testHashCode() {
    }

    @Test
    fun testEquals() {
    }

    @Test
    fun compareTo() {
    }

    @Test
    fun testGet() {
    }

    @Test
    fun testSet1() {
    }

    @Test
    fun testGet1() {
    }

    @Test
    fun testSet2() {
    }

    @Test
    fun shiftDown() {
    }

    @Test
    fun slice() {
    }

    @Test
    fun duplicate() {
    }

    @Test
    fun asReadOnly() {
    }

    @Test
    fun readChar() {
    }

    @Test
    fun writeChar() {
    }

    @Test
    fun readShort() {
    }

    @Test
    fun writeShort() {
    }

    @Test
    fun readUShort() {
    }

    @Test
    fun writeUShort() {
    }

    @Test
    fun readInt() {
    }

    @Test
    fun writeInt() {
    }

    @Test
    fun readUInt() {
    }

    @Test
    fun writeUInt() {
    }

    @Test
    fun readLong() {
    }

    @Test
    fun writeLong() {
    }

    @Test
    fun readULong() {
    }

    @Test
    fun writeULong() {
    }

    @Test
    fun readFloat() {
    }

    @Test
    fun writeFloat() {
    }

    @Test
    fun readDouble() {
    }

    @Test
    fun writeDouble() {
    }

    @Test
    fun testToString() {
    }

    @Test
    fun allocateDirect() {
        buffer = ByteBuffer.allocateDirect(size)
        assertEquals(buffer!!.capacity, size, "Value 'capacity' should always be the same as the given size.")
        assertEquals(buffer!!.limit, buffer!!.capacity, "Property 'limit' should implicitly be set to capacity.")
        assertEquals(buffer!!.position, 0, "Property 'position' should implicitly be set to 0.")
        assertEquals(buffer!!.readOnly, false, "Value 'readOnly' should implicitly be set to 'false'.")
        assertEquals(buffer!!.direct, true, "Value 'direct' should implicitly be set to 'true'.")
    }

    @Test
    fun allocate() {
        buffer = ByteBuffer.allocate(size)
        assertEquals(buffer!!.capacity, size, "Value 'capacity' should always be the same as the given size.")
        assertEquals(buffer!!.limit, buffer!!.capacity, "Property 'limit' should implicitly be set to capacity.")
        assertEquals(buffer!!.position, 0, "Property 'position' should implicitly be set to 0.")
        assertEquals(buffer!!.readOnly, false, "Value 'readOnly' should implicitly be set to 'false'.")
        assertEquals(buffer!!.direct, false, "Value 'direct' should implicitly be set to 'false'.")
    }

    @Test
    fun wrap() {
        buffer = ByteBuffer.wrap(ByteArray(size))
        assertEquals(buffer!!.capacity, size, "Value 'capacity' should always be the same as the given size.")
        assertEquals(buffer!!.limit, buffer!!.capacity, "Property 'limit' should implicitly be set to capacity.")
        assertEquals(buffer!!.position, 0, "Property 'position' should implicitly be set to 0.")
        assertEquals(buffer!!.readOnly, false, "Value 'readOnly' should implicitly be set to 'false'.")
        assertEquals(buffer!!.direct, false, "Value 'direct' should implicitly be set to 'false'.")
    }
}