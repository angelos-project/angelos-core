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

/**
 * How to call Kotlin from outside.
 * https://www.iitk.ac.in/esc101/05Aug/tutorial/native1.1/implementing/method.html
 */

actual class Proc: AbstractProc() {
     actual companion object {
         actual var errNum: Int = 0
         actual var errMsg: String = ""

         actual fun getError(): SystemError {
             val err = SystemError(errNum, errMsg)
             errNum = 0
             errMsg = ""
             return err
         }

         actual fun registerInterrupt(signum: Int) {
             pr_signal(signum)
         }

         @JvmStatic
         external fun pr_signal(signum: Int): Boolean

         init {
             System.loadLibrary("jni-proc")
         }
     }
}