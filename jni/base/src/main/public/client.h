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
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <unistd.h>


#ifndef _Included_angelos_interop_IO_client
#define _Included_angelos_interop_IO_client
#ifdef __cplusplus
extern "C" {
#endif


int client_connect(const char * host, short port, int domain, int type, int protocol);
int client_close(int sockfd);


#ifdef __cplusplus
}
#endif
#endif