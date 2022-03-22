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

expect class Base {
    companion object {

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
        fun pollFinalize()

        fun attachStream(fd: Int)
        fun attachSocket(fd: Int)
    }
}