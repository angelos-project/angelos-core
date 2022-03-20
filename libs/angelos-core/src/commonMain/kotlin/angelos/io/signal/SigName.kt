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
package angelos.io.signal

import angelos.interop.Base

enum class SigName(val sigName: String){
    SIGABRT("ABRT"), // Process abort signal
    SIGALRM("ALRM"), // Alarm clock
    SIGBUS("BUS"), // Access to an undefined portion of a memory object
    SIGCHLD("CHLD"), // Child process terminated, stopped, or continued
    SIGCONT("CONT"), // Continue executing, if stopped
    SIGFPE("FPE"), // Erroneous arithmetic operation
    SIGHUP("HUP"), // Hangup
    SIGILL("ILL"), // Illegal instruction
    SIGINT("INT"), // Terminal interrupt signal
    SIGKILL("KILL"), // Kill (cannot be caught or ignored)
    SIGPIPE("PIPE"), // Write on a pipe with no one to read it
    SIGIO("IO"), // Pollable event
    SIGPOLL("POLL"), // Pollable event
    SIGPROF("PROF"), // Profiling timer expired
    SIGQUIT("QUIT"), // Terminal quit signal
    SIGSEGV("SEGV"), // Invalid memory reference
    SIGSTOP("STOP"), // Stop executing (cannot be caught or ignored)
    SIGSYS("SYS"), // Bad system call
    SIGTERM("TERM"), // Termination signal
    SIGTRAP("TRAP"), // Trace/breakpoint trap
    SIGTSTP("TSTP"), // Terminal stop signal
    SIGTTIN("TTIN"), // Background process attempting read
    SIGTTOU("TTOU"), // Background process attempting write
    SIGUSR1("USR1"), // User-defined signal 1
    SIGUSR2("USR2"), // User-defined signal 2
    SIGURG("URG"), // Out-of-band data is available at a socket
    SIGVTALRM("VTALRM"), // Virtual timer expired
    SIGXCPU("XCPU"), // CPU time limit exceeded
    SIGXFSZ("XFSZ"), // File size limit exceeded
    SIGWINCH("WINCH"); // Terminal window size changed


    override fun toString(): String = "SIG$sigName"

    companion object {
        private val numCache = mutableMapOf<Int, SigName>()
        private val nameCache = mutableMapOf<SigName, Int>()

        init {
            for(num in 1..32) {
                val abbr = Base.sigAbbr(num)
                try {
                    val sig = valueOf("SIG$abbr")
                    numCache[num] = sig
                    nameCache[sig] = num
                } catch (_: Exception) {}
            }
        }

        fun codeToName(sigNum: Int): SigName = numCache[sigNum] ?: throw SignalError("Unsupported signal number: $sigNum")
        fun nameToCode(sigName: SigName): Int = nameCache[sigName] ?: throw SignalError("Unsupported signal: $sigName")
        fun isImplemented(sigNum: Int) = numCache.containsKey(sigNum)
        fun isImplemented(sigName: SigName) = nameCache.containsKey(sigName)
    }
}

// https://en.wikipedia.org/wiki/Signal_(IPC)