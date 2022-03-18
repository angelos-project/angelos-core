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
#if defined (__FreeBSD__) || defined (__NetBSD__) || defined (__OpenBSD__) || defined (__APPLE__)

#include "sig.h"


void handler(int signum, siginfo_t *info, void *context)
{
    // Blocking incoming signals
    sigset_t sigs;
    sigemptyset(&sigs);
    pthread_sigmask(0, NULL, &sigs);

    if (sigismember(&sigs, SIGIO)) {
        outbound_action_cb(signum);
        // Unblocking incoming signals
        sigaddset(&sigs, SIGIO);
        pthread_sigmask(SIG_UNBLOCK, &sigs, NULL);
    }
}


void init_signal_handler(outbound_signal_ptr outbound) {
    outbound_action_cb = outbound;

	signal_action_cb.sa_flags = SA_SIGINFO | SA_RESTART | SA_NODEFER;
    signal_action_cb.sa_sigaction = handler;
    sigfillset(&signal_action_cb.sa_mask);
    sigdelset(&signal_action_cb.sa_mask, SIGIO);

    if (sigaction(SIGIO, &signal_action_cb, NULL)) {
        printf("Sigaction failed\n");
        exit(1);
    }
}


int register_signal_handler(int signum) {
    if (outbound_action_cb == NULL)
        return -1;

    return sigaction(signum, &signal_action_cb, NULL);
}


const char* signal_abbreviation(int signum) {
    return sys_signame[signum % NSIG];
}

#endif