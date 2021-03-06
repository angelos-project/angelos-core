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

#include "sock.h"


int socket_attach(int fd) {
    sock_context obj = {fd};

    struct kevent events[2];
    EV_SET(&events[0], obj.fd, EVFILT_READ, EV_ADD | EV_CLEAR, 0, 0, &obj);
    EV_SET(&events[1], obj.fd, EVFILT_WRITE, EV_ADD | EV_CLEAR, 0, 0, &obj);
    return kevent(kq, events, 2, NULL, 0, NULL);
}


#endif

