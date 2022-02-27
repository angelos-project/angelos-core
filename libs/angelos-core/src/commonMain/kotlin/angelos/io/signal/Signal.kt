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
package angelos.io.signal

import angelos.interop.AbstractProc
import angelos.interop.Proc

class Signal internal constructor(
    private val handlers: MutableList<SignalHandler> = mutableListOf(),
    private val signals: MutableList<Int> = mutableListOf(),
    private val map: HashMap<Int, SignalHandler> = HashMap()
){
    enum class Num(val signum: Int){
        SIGABRT(8), // P1990, Core, Abort signal from abort(3)
        SIGALRM(14), // P1990, Term, Timer signal from alarm(2)
        SIGBUS(7), // P2001, Core, Bus error (bad memory access)
        SIGCHLD(17), // P1990, Ign, Child stopped or terminated
        SIGCONT(18), // P1990, Cont, Continue if stopped
        SIGFPE(8), // P1990, Core, Floating-point exception
        SIGHUP(1), // P1990, Term, Hangup detected on controlling terminal or death of controlling process
        SIGILL(4), // P1990, Core, Illegal Instruction
        SIGINT(2), // P1990, Term, Interrupt from keyboard
        SIGIO(29), // -, Term, I/O now possible (4.2BSD)
        SIGKILL(9), // P1990, Term, Kill signal
        //SIGLOST // -, Term, File lock lost (unused)
        SIGPIPE(13), // P1990, Term, Broken pipe: write to pipe with no readers; see pipe(7)
        SIGPROF(27), // P2001, Term, Profiling timer expired
        SIGPWR(30), // -, Term, Power failure (System V)
        SIGQUIT(3), // P1990, Core, Quit from keyboard
        SIGSEGV(11), // P1990, Core, Invalid memory reference
        SIGSTKFLT(6), // -, Term, Stack fault on coprocessor (unused)
        SIGSTOP(19), // P1990, Stop, Stop process
        SIGTSTP(20), // P1990, Stop, Stop typed at terminal
        SIGSYS(31), // P2001, Core, Bad system call (SVr4); see also seccomp(2)
        SIGTERM(15), // P1990, Term, Termination signal
        SIGTRAP(5), // P2001,  Core, Trace/breakpoint trap
        SIGTTIN(21), // P1990, Stop, Terminal input for background process
        SIGTTOU(22), // P1990, Stop, Terminal output for background process
        SIGURG(23), // P2001, Ign, Urgent condition on socket (4.2BSD)
        SIGUSR1(10), // P1990, Term, User-defined signal 1
        SIGUSR2(12), // P1990, Term, User-defined signal 2
        SIGVTALRM(26), // P2001, Term, Virtual alarm clock (4.2BSD)
        SIGXCPU(24), // P2001, Core, CPU time limit exceeded (4.2BSD); see setrlimit(2)
        SIGXFSZ(25), // P2001, Core, File size limit exceeded (4.2BSD); see setrlimit(2)
        SIGWINCH(28) // -, Ign, Window resize signal (4.3BSD, Sun)
    }

    init {
        AbstractProc.sigHandler = this
    }

    fun handler(signum: Int) = map[signum]?.send(signum)

    private fun add(handler: SignalHandler) {
        if (handler in handlers)
            throw IllegalArgumentException("Handler already added.")

        val used = handler.signals.intersect(signals)
        if (used.isNotEmpty())
            throw IllegalArgumentException("Handler signals already used by different handler.")

        handlers.add(handler)
        handler.signals.forEach {
            if(!Proc.registerInterrupt(it)) {
                val syserr = Proc.getError()
                throw SignalError("Couldn't register signal handler for (signum: $it) because of (errnum: ${syserr.first} errmsg: ${syserr.second})")
            }
            signals.add(it)
            map[it] = handler
        }
    }

    companion object {
        private val instance = Signal()

        fun register(handler: SignalHandler) {
            instance.add(handler)
        }
    }
}