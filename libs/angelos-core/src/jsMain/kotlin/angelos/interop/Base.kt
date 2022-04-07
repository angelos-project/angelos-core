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

import angelos.io.poll.PollAction
import angelos.io.signal.SigName
import angelos.sys.Benchmark

actual class Base: AbstractBase() {

    actual companion object {
        actual fun initializeSignalHandler() {
            TODO("Not yet implemented")
        }

        actual fun initializeTerminalMode(): Int {
            TODO("Not yet implemented")
        }

        actual fun finalizeTerminalMode(): Int {
            TODO("Not yet implemented")
        }

        actual fun getEndian(): Int {
            TODO("Not yet implemented")
        }

        actual fun getPlatform(): Int {
            TODO("Not yet implemented")
        }

        @Suppress("VARIABLE_IN_SINGLETON_WITHOUT_THREAD_LOCAL")
        internal actual var interrupt: (sigNum: SigName) -> Unit
            get() = TODO("Not yet implemented")
            set(value) {}

        actual fun setInterrupt(sigName: SigName): Boolean {
            TODO("Not yet implemented")
        }

        internal actual fun incomingSignal(sigName: SigName) {
        }

        actual fun sigAbbr(sigNum: Int): String {
            TODO("Not yet implemented")
        }

        actual fun getError() {
        }

        actual fun pollAction(): PollAction {
            TODO("Not yet implemented")
        }

        actual fun initializePolling(): Int {
            TODO("Not yet implemented")
        }

        actual fun finalizePolling() {
            TODO("Not yet implemented")
        }

        actual fun attachStream(fd: Int): Int {
            TODO("Not yet implemented")
        }

        actual fun attachSocket(fd: Int) {
        }

        actual fun isOpenStream(fd: Int): Boolean {
            TODO("Not yet implemented")
        }

        actual fun closeStream(fd: Int): Int {
            TODO("Not yet implemented")
        }

        actual fun startUsage(): Long {
            TODO("Not yet implemented")
        }

        actual fun endUsage(usage: Long): Benchmark {
            TODO("Not yet implemented")
        }
    }
}