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
import angelos.sys.Benchmark
import angelos.sys.Error
import co.touchlab.stately.concurrency.AtomicReference
import sun.misc.Signal
import java.lang.System


actual class Base: AbstractBase() {
    actual companion object {
        init {
            System.loadLibrary("jni-base") // Load underlying library via JNI.
        }

        actual fun startUsage(): Long = start_usage()

        @JvmStatic
        private external fun start_usage(): Long

        actual fun endUsage(usage: Long): Benchmark = end_usage(usage)

        @JvmStatic
        private external fun end_usage(start: Long): Benchmark

        actual fun initializeSignalHandler(): Unit {
        }

        actual fun initializeTerminalMode(): Int = errorPredicate(init_terminal_mode(), "Failure in TERMIOS")

        @JvmStatic
        private external fun init_terminal_mode(): Int

        actual fun finalizeTerminalMode(): Int = errorPredicate(finalize_terminal_mode(), "Failure in TERMIOS")

        @JvmStatic
        private external fun finalize_terminal_mode(): Int

        @Suppress("VARIABLE_IN_SINGLETON_WITHOUT_THREAD_LOCAL")
        internal actual var interrupt = AtomicReference<(sigNum: SigName) -> Unit> {}

        actual fun getEndian(): Int = endian()

        actual fun getPlatform(): Int = platform()

        @JvmStatic
        private external fun endian(): Int

        @JvmStatic
        private external fun platform(): Int

        actual fun setInterrupt(sigName: SigName): Boolean {
            Signal.handle(Signal(sigName.sigName)) {
                incomingSignal(SigName.codeToName(it.number))
            }
            return true
        }

        internal actual fun incomingSignal(sigName: SigName) = interrupt.get()(sigName)

        actual fun sigAbbr(sigNum: Int): String = signal_abbreviation(sigNum).uppercase()

        @JvmStatic
        private external fun signal_abbreviation(sigNum: Int): String

        actual fun getError() {
            get_error()
        }

        @JvmStatic
        private external fun get_error()

        actual fun pollAction(): PollAction {
            val action = event_poll()
            if(action == null) {
                Error.loadError()
                throw BaseError("Failed to poll due to cause: (${Error.errNum}) ${Error.errMsg}")
            }
            return action
        }

        @JvmStatic
        private external fun event_poll(): PollAction?

        actual fun initializePolling(): Int = errorOnMinusOnePredicate(init_event_handler(), "Event queue initialization failure")

        @JvmStatic
        private external fun init_event_handler(): Int

        actual fun finalizePolling(): Unit = finalize_event_handler()

        @JvmStatic
        private external fun finalize_event_handler()

        actual fun attachStream(fd: Int): Int = errorOnMinusOnePredicate(stream_attach(fd), "Failed to attach stream due to cause")

        @JvmStatic
        private external fun stream_attach(fd: Int): Int

        actual fun attachSocket(fd: Int): Unit { errorPredicate(socket_attach(fd), "Failed to attach socket due to cause") }

        @JvmStatic
        private external fun socket_attach(fd: Int): Int

        actual fun isOpenStream(fd: Int): Boolean = stream_is_open(fd)

        @JvmStatic
        private external fun stream_is_open(fd: Int): Boolean

        actual fun closeStream(fd: Int): Int = stream_close(fd)

        @JvmStatic
        private external fun stream_close(fd: Int): Int
    }
}