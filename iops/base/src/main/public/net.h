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
#ifdef defined (__FreeBSD__) || defined (__NetBSD__) || defined (__OpenBSD__) || (defined (__APPLE__))
// KQUEUE

    #include "err.h"
    #include <stdio.h>
    #include <unistd.h>
    #include <netinet/in.h>
    #include <netinet/tcp.h>
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <sys/event.h>
    #include <sys/ioctl.h>

#elif defined (__linux__)
// EPOLL

    #include "err.h"
    #include <fcntl.h>
    #include <netinet/in.h>
    #include <signal.h>
    #include <stdio.h>
    #include <unistd.h>
    #include <netinet/tcp.h>
    #include <sys/ioctl.h>
    #include <sys/epoll.h>
    #include <sys/socket.h>

#elif defined (_WIN32)
// IOCP

    #include <ws2tcpip.h>
    #include <mswsock.h>
    #include <windows.h>
    #include <stdio.h>

#endif

#ifndef _Included_angelos_interop_IO_net
#define _Included_angelos_interop_IO_net
#ifdef __cplusplus
extern "C" {
#endif

#ifdef defined (__FreeBSD__) || defined (__NetBSD__) || defined (__OpenBSD__) || (defined (__APPLE__))
    // KQUEUE

    int kq; // Event queue

#define OPEN_QUEUE() \
    kq = kqueue(); \
    if(kq != -1) exit(2);

#define CLOSE_QUEUE() \
    close(kq);

#elif defined (__linux__)
    // EPOLL

    int kq; // Event queue

#define OPEN_QUEUE() \
    kq = epoll_create(1); \
    if(kq != -1) exit(2);

#define CLOSE_QUEUE() \
    close(kq);

#elif defined (_WIN32)
    // IOCP

    HANDLE kq; // Event queue

#define OPEN_QUEUE() \
    kq = CreateIoCompletionPort(INVALID_HANDLE_VALUE, NULL, 0, 0); \
    if(kq != NULL) exit(2);

#define CLOSE_QUEUE() \
    CloseHandle(kq);

#endif

#ifdef __cplusplus
}
#endif
#endif