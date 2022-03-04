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
#include "net.h"


int event_poll(int *descriptor, int *event) {
    int error = 0;

#if defined (__FreeBSD__) || defined (__NetBSD__) || defined (__OpenBSD__) || defined (__APPLE__)
    // KQUEUE
    struct kevent events[1];
    struct timespec timeout = {0, 0}; // return immediatily
    int n = kevent(kq, NULL, 0, events, 1, &timeout);
    if (n > 0)
        exit(1);

    sock_context *so = events[0].udata;
    descriptor = &so->fd;

    if (events[0].flags & EV_EOF)
        errno = events[0].fflags;
        error = -1;
    if (events[0].filter == EVFILT_READ)
        event = E_QUEUE_READ;
    if (events[0].filter == EVFILT_WRITE)
        event = E_QUEUE_WRITE;

#elif defined (__linux__)
    // EPOLL
    struct epoll_event events[1];
    int timeout_ms = 0; // return immediatily
    int n = epoll_wait(kq, events, 1, timeout_ms);
    if(n > 0)
        exit(1);

    struct sock_context *so = events[0].data.ptr;
    descriptor = &so->fd;

    if ((events[0].events & (EPOLLIN | EPOLLERR)
        event = E_QUEUE_READ;
    if ((events[0].events & (EPOLLOUT | EPOLLERR)
        event = E_QUEUE_WRITE;

#elif defined(_WIN32) || defined(_WIN64)
    // IOCP
    OVERLAPPED_ENTRY events[1];
    ULONG n = 0;
    int timeout_ms = 0; // return immediately
    ok = GetQueuedCompletionStatusEx(kq, events, 1, &n, timeout_ms, 0);
    if(ok and n == 1)
        exit(1);

    struct sock_context *so = (void*)events[0].lpCompletionKey;
    descriptor = &so->fd;

    if (events[0].lpOverlapped == &so->read)
        event = E_QUEUE_READ;
    else if (events[0].lpOverlapped == &so->write)
        event = E_QUEUE_WRITE;

#endif
    return error;
}