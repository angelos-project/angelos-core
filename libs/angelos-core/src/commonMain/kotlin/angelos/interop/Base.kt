/**
 * Copyright (c) 2021-2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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

import angelos.io.poll.PollAction
import angelos.io.signal.SigName
import angelos.sys.Error

abstract class AbstractBase {
    companion object {
        inline fun errorPredicate(errCode: Int, errMessage: String): Int = errorOnNonZeroPredicate(errCode, errMessage)

        inline fun errorOnNonZeroPredicate(errCode: Int, errMsg: String): Int = when(errCode) {
            0 -> errCode
            else -> {
                Error.loadError()
                throw BaseError("$errMsg: (${Error.errNum}) ${Error.errMsg}")
            }
        }

        inline fun errorOnMinusOnePredicate(errCode: Int, errMsg: String): Int = when(errCode) {
            -1 -> {
                Error.loadError()
                throw BaseError("$errMsg: (${Error.errNum}) ${Error.errMsg}")
            }
            else -> errCode
        }

        inline fun booleanPredicate(retCode: Int): Boolean = booleanOnZeroPredicate(retCode)

        inline fun booleanOnZeroPredicate(retCode: Int): Boolean = when (retCode) {
            0 -> true
            else -> false
        }

        inline fun booleanOnOnePredicate(retCode: Int): Boolean = when (retCode) {
            1 -> true
            else -> false
        }
    }
}

expect class Base: AbstractBase {
    companion object {

        fun initializeSignalHandler(): Unit

        fun initializeTerminalMode(): Int
        fun finalizeTerminalMode(): Int

        // System endianness
        fun getEndian(): Int

        // System API
        fun getPlatform(): Int

        // Methods and properties relating to signal interrupts.
        @Suppress("VARIABLE_IN_SINGLETON_WITHOUT_THREAD_LOCAL")
        internal var interrupt: (sigNum: SigName) -> (Unit)
        fun setInterrupt(sigName: SigName): Boolean
        internal fun incomingSignal(sigName: SigName)
        fun sigAbbr(sigNum: Int): String

        // Method to load and populate error number and message from the system.
        fun getError()

        // Polling events
        fun pollAction(): PollAction
        fun initializePolling(): Int
        fun finalizePolling(): Unit

        fun attachStream(fd: Int): Int
        fun attachSocket(fd: Int): Unit
        fun isOpenStream(fd: Int): Boolean
        fun closeStream(fd: Int): Int
    }
}