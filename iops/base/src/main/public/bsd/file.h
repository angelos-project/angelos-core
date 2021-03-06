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
#ifndef BASE_BSD_FILE_H
#define BASE_BSD_FILE_H
#if defined (__FreeBSD__) || defined (__NetBSD__) || defined (__OpenBSD__) || defined (__APPLE__)

#include "event.h"
#include <fcntl.h>


extern int stream_attach(int fd);


extern int stream_is_open(int fd);


extern int stream_close(int fd);


#endif
#endif //BASE_BSD_FILE_H
