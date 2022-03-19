package angelos.io.signal

import kotlin.test.Test
import kotlin.test.assertEquals

class SigNameTest {

    @Test
    fun codeToName() {
        assertEquals(SigName.codeToName(6), SigName.SIGABRT)
        assertEquals(SigName.codeToName(14), SigName.SIGALRM)
        assertEquals(SigName.codeToName(8), SigName.SIGFPE)
        assertEquals(SigName.codeToName(1), SigName.SIGHUP)
        assertEquals(SigName.codeToName(4), SigName.SIGILL)
        assertEquals(SigName.codeToName(2), SigName.SIGINT)
        assertEquals(SigName.codeToName(9), SigName.SIGKILL)
        assertEquals(SigName.codeToName(13), SigName.SIGPIPE)
        assertEquals(SigName.codeToName(3), SigName.SIGQUIT)
        assertEquals(SigName.codeToName(11), SigName.SIGSEGV)
        assertEquals(SigName.codeToName(15), SigName.SIGTERM)
        assertEquals(SigName.codeToName(5), SigName.SIGTRAP)
    }

    @Test
    fun nameToCode() {
        assertEquals(SigName.nameToCode(SigName.SIGABRT), 6)
        assertEquals(SigName.nameToCode(SigName.SIGALRM), 14)
        assertEquals(SigName.nameToCode(SigName.SIGFPE), 8)
        assertEquals(SigName.nameToCode(SigName.SIGHUP), 1)
        assertEquals(SigName.nameToCode(SigName.SIGILL), 4)
        assertEquals(SigName.nameToCode(SigName.SIGINT), 2)
        assertEquals(SigName.nameToCode(SigName.SIGKILL), 9)
        assertEquals(SigName.nameToCode(SigName.SIGPIPE), 13)
        assertEquals(SigName.nameToCode(SigName.SIGQUIT), 3)
        assertEquals(SigName.nameToCode(SigName.SIGSEGV), 11)
        assertEquals(SigName.nameToCode(SigName.SIGTERM), 15)
        assertEquals(SigName.nameToCode(SigName.SIGTRAP), 5)
    }
}