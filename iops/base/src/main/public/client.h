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


#ifndef _Included_angelos_interop_IO_client
#define _Included_angelos_interop_IO_client
#ifdef __cplusplus
extern "C" {
#endif

#ifdef defined (__FreeBSD__) || defined (__NetBSD__) || defined (__OpenBSD__) || (defined (__APPLE__))
// KQUEUE

    typedef int b_socket_t

#elif defined (__linux__)
// EPOLL

    typedef int b_socket_t

#elif defined (_WIN32)
// IOCP

    typedef SOCKET b_socket_t

#endif


int client_connect(const char * host, short port, int domain, int type, int protocol);
int client_close(b_socket_t sockfd);

#ifdef __cplusplus
}
#endif
#endif