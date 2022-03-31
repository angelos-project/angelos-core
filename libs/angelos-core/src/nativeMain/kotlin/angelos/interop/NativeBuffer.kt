package angelos.interop

import kotlinx.cinterop.Pinned

actual interface NativeBuffer {
    fun operation(block: (it: Pinned<ByteArray>) -> Long): Long

    actual companion object {
    }
}