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
#ifndef BASE_BSD_SIG_H
#define BASE_BSD_SIG_H
#if defined (__FreeBSD__) || defined (__NetBSD__) || defined (__OpenBSD__) || defined (__APPLE__)

#include <stdlib.h>
#include <stdio.h>
#include <signal.h>


typedef void (*outbound_signal_ptr)(int);

/**
 * Signal action struct, used as standard action handler.
 */
struct sigaction signal_action_cb;

/**
 * Pointer of outbound signal interrupt function.
 * This pointer should be set before initializing.
 */
outbound_signal_ptr outbound_action_cb = NULL;


/**
 * Handler function that is registered to deliver the signal actions.
 *
 * It is called from within the system and then calls an outbound callback
 * of that specific environment using this signal setup.
 * @param signum Signal number
 * @param info Sgnal info
 * @param context Signal context
 */
extern void handler(int signum, siginfo_t *info, void *context);


/**
 * Initialize the signal handler.
 */
extern void init_signal_handler(outbound_signal_ptr outbound);


/**
 * Registering a signal number.
 * @param signum Signal number
 * @return The result of the sigaction
 */
extern int register_signal_handler(int signum);


/**
 * Abbreviation of the signal number given.
 * @param signum Signal number
 * @return abbreviation
 */
extern const char* signal_abbreviation(int signum);


#endif
#endif //BASE_BSD_SIG_H
