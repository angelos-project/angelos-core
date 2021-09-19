/**
 * Copyright (c) 2021 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
 *
 * This software is available under the terms of the MIT license. Parts are licensed
 * under different terms if stated. The legal terms are attached to the LICENSE file
 * and are made available on:
 *
 *      https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *
 * Contributors:
 *      Kristoffer Paulsson - initial implementation
 */
package angelos.io

import angelos.interop.FileSystem

class File(path: RealPath) : FileObject(path) {
    val size: Long
        get() = _info.size

    fun open(option: OpenOption): FileDescriptor = FileDescriptor(this, option, FileSystem.openFile(this.path.toString(), option.ordinal))

    enum class OpenOption(option: Int) {
        READ_ONLY(0),
        WRITE_ONLY(1),
        READ_WRITE(2),
    }
}