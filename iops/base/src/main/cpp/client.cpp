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
#include "client.h"

int client_connect(const char *host, short port, int domain, int type, int protocol) {
    int sockfd, conn;
    /*struct hostent *he;
    struct sockaddr_in them;

    CLEAR_ERROR()

    he = gethostbyname(host);
    if (he == NULL)
        return -1;

    sockfd = socket(domain, type, protocol);
    if (sockfd == -1)
        return -1;

    them.sin_family = domain;
    them.sin_port = port;
    them.sin_addr = *((struct in_addr *) he->h_addr);
    bzero(&(them.sin_zero), 8);

    conn = connect(sockfd, (struct sockaddr *)&them, sizeof(them)); // [Errno 61]
    if (conn == -1)
        return -1;*/

    return sockfd;
}


int client_close(b_socket_t sockfd) {

}