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

import angelos.io.file.WatcherException
import angelos.io.poll.PollAction
import angelos.io.signal.SigName
import angelos.sys.Error
import base.*
import kotlinx.cinterop.*
import platform.posix.errno
import platform.posix.strerror


actual class Base {
    actual companion object {
        init {
            // Setting up the outbound signal action callback
            // and initializing external signal handler.
            init_signal_handler(staticCFunction<Int, Unit> {
                incomingSignal(SigName.codeToName(it))
            })
            // Setting up polling.
            init_event_handler()
        }

        internal actual var interrupt: (sigNum: SigName) -> Unit = {}

        actual fun getEndian(): Int = endian()

        actual fun getPlatform(): Int = platform()

        actual fun setInterrupt(sigName: SigName): Boolean = when(register_signal_handler(SigName.nameToCode(sigName))){
            0 -> true
            else -> false
        }

        internal actual inline fun incomingSignal(sigName: SigName) { interrupt(sigName) }

        actual fun sigAbbr(sigNum: Int): String = memScoped{
            val abbr = signal_abbreviation(sigNum)
            abbr?.toKString().toString().uppercase()
        }

        actual inline fun getError() {
            Error.errNum.usePinned { errno }
            Error.errMsg.usePinned { strerror(errno)?.toKString().toString() }
        }

        actual fun pollAction(): PollAction = memScoped {
            val description = alloc<IntVar>()
            val event = alloc<IntVar>()
            if(event_poll(description.ptr, event.ptr) != 0) {
                Error.loadError()
                throw WatcherException("Failed to poll due to cause: (${Error.errNum}) ${Error.errMsg}")
            }

            return PollAction(description.value, event.value)
        }

        actual fun pollFinalize(): Unit = finalize_event_handler()

        actual fun attachStream(fd: Int): Int {
            val nfd = stream_attach(fd)
            if (nfd == -1) {
                Error.loadError()
                throw WatcherException("Failed to attach stream due to cause: (${Error.errNum}) ${Error.errMsg}")
            }
            return nfd
        }

        actual fun attachSocket(fd: Int) {
            if (socket_attach(fd) != 0) {
                Error.loadError()
                throw WatcherException("Failed to attach socket due to cause: (${Error.errNum}) ${Error.errMsg}")
            }
        }

        actual fun isOpenStream(fd: Int): Boolean = when (stream_is_open(fd)) {
            1 -> true
            else -> false
        }

        actual fun closeStream(fd: Int): Int = stream_close(fd)
    }
}