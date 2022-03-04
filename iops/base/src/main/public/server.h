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
#include <sys/socket.h>
#include <fcntl.h>
#include <unistd.h>
#include <netinet/in.h>

#include <arpa/inet.h>
#include <netdb.h>
#include <string.h>
#include <stdlib.h>


#ifndef _Included_angelos_interop_IO_server
#define _Included_angelos_interop_IO_server
#ifdef __cplusplus
extern "C" {
#endif


#define MAXBUFLEN 100


int server_open(int domain, int type, int protocol);

int server_listen(int sockfd, const char *host, short port, int domain, int max_conn);


#ifdef __cplusplus
}
#endif
#endif