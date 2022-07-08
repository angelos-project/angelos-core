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
import angelos.sys.Benchmark
import angelos.sys.Error
import base.*
import kotlinx.cinterop.*
import platform.posix.*


actual class Base : AbstractBase() {
    actual companion object {

        actual fun startUsage(): Long = memScoped {
            val usage = nativeHeap.alloc<rusage>()
            getrusage(RUSAGE_SELF, usage.ptr)
            return usage.ptr.toLong()
        }

        actual fun endUsage(usage: Long): Benchmark = memScoped {
            val end = nativeHeap.alloc<rusage>()
            getrusage(RUSAGE_SELF, end.ptr)
            val start = usage.toCPointer<rusage>()!!.pointed
            val bm = Benchmark((((
                    end.ru_utime.tv_sec * 1000000 + end.ru_utime.tv_usec) - (
                        start.ru_utime.tv_sec * 1000000 + start.ru_utime.tv_usec)) + ((
                        end.ru_stime.tv_sec * 1000000 + end.ru_stime.tv_usec) - (
                        start.ru_stime.tv_sec * 1000000 + start.ru_stime.tv_usec))),
                end.ru_maxrss - start.ru_maxrss,
                end.ru_inblock - start.ru_inblock,
                end.ru_oublock - start.ru_oublock
            )
            free(start.ptr)
            free(end.ptr)
            return bm
        }

        actual fun initializeTerminalMode(): Int = errorPredicate(init_terminal_mode(), "Failure in TERMIOS")

        actual fun finalizeTerminalMode(): Int = errorPredicate(finalize_terminal_mode(), "Failure in TERMIOS")

        actual fun getEndian(): Int = endian()

        actual fun getPlatform(): Int = platform()

        actual fun getPid(): Int = pid()

        actual fun getError() {
            Error.errNum.usePinned { errno }
            Error.errMsg.usePinned { strerror(errno)?.toKString().toString() }
        }

        actual fun pollAction(): PollAction = memScoped {
            val description = alloc<IntVar>()
            val event = alloc<IntVar>()
            errorPredicate(event_poll(description.ptr, event.ptr), "Failed to poll due to cause")
            return PollAction(description.value, event.value)
        }

        actual fun initializePolling(): Int =
            errorOnMinusOnePredicate(init_event_handler(), "Event queue initialization failure")

        actual fun finalizePolling(): Unit = finalize_event_handler()

        actual fun attachStream(fd: Int): Int =
            errorOnMinusOnePredicate(stream_attach(fd), "Failed to attach stream due to cause")

        actual fun attachSocket(fd: Int): Unit {
            errorPredicate(socket_attach(fd), "Failed to attach socket due to cause")
        }

        actual fun isOpenStream(fd: Int): Boolean = booleanOnOnePredicate(stream_is_open(fd))

        actual fun closeStream(fd: Int): Int = stream_close(fd)
        actual fun initializeSignalHandler() {
        }
    }
}