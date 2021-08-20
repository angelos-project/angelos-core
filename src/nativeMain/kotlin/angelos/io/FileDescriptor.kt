package angelos.io

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.*

internal actual inline fun closeFile(number: Int): Boolean = close(number) == 0
internal actual inline fun readFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong {
    array.usePinned {
        return read(number, it.addressOf(index), count).toULong()
    }
}

internal actual inline fun writeFile(number: Int, array: UByteArray, index: Int, count: ULong): ULong {
    array.usePinned {
        return write(number, it.addressOf(index), count).toULong()
    }
}

internal actual inline fun tellFile(number: Int): ULong = lseek(number, 0, SEEK_CUR).toULong()