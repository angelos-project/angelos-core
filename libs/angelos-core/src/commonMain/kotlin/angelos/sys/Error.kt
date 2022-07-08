/**
 * Copyright (c) 2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
package angelos.sys

import angelos.interop.Base


class Error {
    @Suppress("VARIABLE_IN_SINGLETON_WITHOUT_THREAD_LOCAL")
    companion object {
        private var _errNum = 0
        var errNum: Int
            get() = _errNum
            set(value) { _errNum = value }

        private var _errMsg = ""
        var errMsg: String
            get() = _errMsg
            set(value) { _errMsg = value }

        inline fun loadError() = Base.getError()
    }

}