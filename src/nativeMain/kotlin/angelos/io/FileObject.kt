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

import platform.posix.R_OK
import platform.posix.W_OK
import platform.posix.X_OK
import platform.posix.access

internal actual inline fun checkReadable(path: String): Boolean {
    return access(path, R_OK) == 0
}

internal actual inline fun checkWritable(path: String): Boolean {
    return access(path, W_OK) == 0
}

internal actual inline fun checkExecutable(path: String): Boolean {
    return access(path, X_OK) == 0
}