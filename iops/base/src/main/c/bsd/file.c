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

#include "file.h"
#include <stdlib.h>


int stream_attach(int fd) {
    sock_context obj = {fd};

    struct kevent event; // EVFILT_READ
    EV_SET(&event, obj.fd,  EVFILT_READ, EV_ADD | EV_CLEAR, 0, 0, &obj);
    int nfd = kevent(kq, &event, 1, NULL, 0, NULL);
    if(nfd > 0){
        return -1;
    }

    if (fcntl(fd, F_SETOWN, getpid()) < 0) {
        return -1;
    }
    if (fcntl(fd, F_SETFL, O_NONBLOCK|O_ASYNC) < 0) {
        return -1;
    }

    return nfd;
}


int stream_is_open(int fd) {
    return fcntl(fd, F_GETFD) != -1 || errno != EBADF;
}


int stream_close(int fd) {
    return close(fd);
}


#endif

