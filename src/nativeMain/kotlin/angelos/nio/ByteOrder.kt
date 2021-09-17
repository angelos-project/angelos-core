package angelos.nio

import kotlin.native.Platform.isLittleEndian

internal actual inline fun checkNativeOrder(): Boolean = isLittleEndian