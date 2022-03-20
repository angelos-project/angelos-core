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

#ifndef BASE_BSD_EVENT_H
#define BASE_BSD_EVENT_H
#if defined (__FreeBSD__) || defined (__NetBSD__) || defined (__OpenBSD__) || defined (__APPLE__)

#include <errno.h>
#include <aio.h>
#include <stdlib.h>
//#include <stdio.h>
#include <unistd.h>
//#include <netinet/in.h>
//#include <netinet/tcp.h>
//#include <sys/types.h>
//#include <sys/socket.h>
#include <sys/event.h>
//#include <sys/ioctl.h>


#define E_QUEUE_READ 1
#define E_QUEUE_WRITE 2
#define E_QUEUE_EOF 3


typedef int b_socket_t;
typedef int e_queue_t;


typedef struct {
    b_socket_t fd;
} sock_context;


e_queue_t kq; // Event queue


/**
 * Initializethe  event queue that is poll-able.
 */
extern void init_event_handler();


/**
 * Finalize the event queue.
 */
extern void finalize_event_handler();


/**
 * Poll an event at a time, and get the descriptor.
 * @param descriptor Descriptor belonging to socket/stream/file
 * @param event Event which happened on a certain descriptor
 * @return If success 0 or if error -1
 */
extern int event_poll(int *descriptor, int *event);


#endif
#endif //BASE_BSD_EVENT_H
