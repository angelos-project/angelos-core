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

    struct kevent eevent;
    struct timespec timeout = {0, 0}; // return immediately
    int n = kevent(kq, NULL, 0, &eevent, 1, &timeout);
    if (n <= 0){
        return -1;
    } else if (eevent.flags & EV_EOF) {
        errno = eevent.fflags;
        error = -1;
    } else if (eevent.filter == EVFILT_AIO) { // Streams and files
        struct aiocb *acb = (void*)eevent.ident;
        *event = E_QUEUE_READ;
        *descriptor = acb->aio_fildes;
    } else if (eevent.filter == EVFILT_READ) { // Sockets
        *event = E_QUEUE_READ;
        sock_context *so = eevent.udata;
        *descriptor = so->fd;
    } else if (eevent.filter == EVFILT_WRITE) { // Sockets
        *event = E_QUEUE_WRITE;
        sock_context *so = eevent.udata;
        *descriptor = so->fd;
    }

    return error;
}


#endif

