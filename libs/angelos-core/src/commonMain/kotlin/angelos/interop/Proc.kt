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
package angelos.interop

import angelos.io.signal.Signal

/**
 * How to call Kotlin from outside.
 * https://www.iitk.ac.in/esc101/05Aug/tutorial/native1.1/implementing/method.html
 * https://kotlinlang.org/docs/mapping-function-pointers-from-c.html#c-function-pointers-in-kotlin
 */

typealias SystemError = Pair<Int, String>

abstract class AbstractProc{
    @Suppress("VARIABLE_IN_SINGLETON_WITHOUT_THREAD_LOCAL")
    companion object{
        internal lateinit var sigHandler: Signal

        fun interrupt(signum: Int) {
            //sigHandler.handler(signum)
        }

    }
}


expect class Proc: AbstractProc {
    @Suppress("VARIABLE_IN_SINGLETON_WITHOUT_THREAD_LOCAL")
    companion object{
        var errNum: Int
        var errMsg: String

        fun getError(): SystemError
        fun registerInterrupt(signum: Int): Boolean
    }
}
