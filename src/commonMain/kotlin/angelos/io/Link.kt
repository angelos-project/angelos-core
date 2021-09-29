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

class Link(path: RealPath) : FileObject(path) {
    private val _target: RealPath by lazy {
        RealPath.wrap(
            path.store.getLinkTarget(path.toString()),
            path.separator,
            path.store
        )
    }

    val target: String
        get() = _target.toString()

    fun goToTarget(): FileObject = _target.getItem()
}