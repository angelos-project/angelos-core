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

import angelos.nio.file.FileVault

class VirtualPath(path: String, separator: PathSeparator = PathSeparator.POSIX) : Path(path, separator) {

    fun toRealPath(fileSystem: FileVault): RealPath = RealPath(root, path, separator, fileSystem)

    override fun join(vararg elements: String): VirtualPath =
        VirtualPath(joinStrings(elements = elements.asList()), separator)

    override fun join(path: String): VirtualPath =
        VirtualPath(joinStrings(elements = splitString(path, separator)), separator)
}