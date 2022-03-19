package angelos.sys

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class PlatformTest {
    @Test
    fun isUnknown() {
        val platform = Platform.UNKNOWN
        assertTrue(platform.isUnknown())
    }

    @Test
    fun isPosix() {
        val platform = Platform.POSIX
        assertTrue(platform.isPosix())
    }

    @Test
    fun isWindows() {
        val platform = Platform.WINDOWS
        assertTrue(platform.isWindows())
    }

    @Test
    fun nativeApi() {
        assertContains(arrayOf(Platform.POSIX, Platform.WINDOWS), Platform.nativeApi())
    }

    @Test
    fun testToString() {
        val platform = Platform.nativeApi()
        assertNotEquals(platform.toString(), "UNKNOWN")
    }
}