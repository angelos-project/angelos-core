package angelos.interop

import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class ProcTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun registerInterrupt(){
        Proc.registerInterrupt(500)
    }
}