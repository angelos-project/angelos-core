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

#include "event.h"


void init_event_handler()
{
    kq = kqueue();
    if(kq == -1)
        exit(1);
}


void finalize_event_handler()
{
    close(kq);
}


int event_poll(int *descriptor, int *event)
{
    int error = 0;

    struct kevent events[1];
    struct timespec timeout = {0, 0}; // return immediately
    int n = kevent(kq, NULL, 0, events, 1, &timeout);
    if (n <= 0)
        exit(1);

    if (events[0].flags & EV_EOF) {
        errno = events[0].fflags;
        error = -1;
    }

    // Sockets
    if (events[0].filter == EVFILT_READ) {
        *event = E_QUEUE_READ;
        sock_context *so = events[0].udata;
        *descriptor = so->fd;
    }

    // Sockets
    if (events[0].filter == EVFILT_WRITE) {
        *event = E_QUEUE_WRITE;
        sock_context *so = events[0].udata;
        *descriptor = so->fd;
    }

    // Streams and files
    if (events[0].filter == EVFILT_AIO) {
        struct aiocb *acb = (void*)events[0].ident;
        *event = E_QUEUE_READ;
        *descriptor = acb->aio_fildes;
    }

    return error;
}

#endif

