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
#include "server.h"
#include "err.h"


int server_open(int domain, int type, int protocol) {
    CLEAR_ERROR()
    int sockfd = socket(domain, type, protocol);

    if (fcntl(sockfd, F_SETOWN, getpid()))
        return -1;

    if (fcntl(sockfd, F_SETFL, FASYNC))
        return -1;

    return sockfd;
}


int server_listen(int sockfd, const char *host, short port, int domain, int max_conn) {
    struct sockaddr_in server;
    server.sin_family = domain;
    server.sin_port = htons(port);

    CLEAR_ERROR()

    if (inet_pton(domain, host, &server.sin_addr.s_addr) != 1) {
        struct hostent *hp = gethostbyname(host);
        if (hp == 0) {
            return -1;
        }
        memcpy(&server.sin_addr, hp->h_addr, hp->h_length);
        free(hp);
    }

    if (bind(sockfd, (struct sockaddr *) &server, sizeof(server)))
        return errno;

    unsigned int length = sizeof(server);
    if (getsockname(sockfd, (struct sockaddr *) &server, &length))
        return errno;

    if (listen(sockfd, max_conn))
        return errno;

    return 0;
}


void server_close() {
}


int server_handle(int sockfd, const char *buffer) {
    int numbytes;
    unsigned int addr_len;
    struct sockaddr_in their_addr;    /* connector's address information	*/

    CLEAR_ERROR()

    return recvfrom(sockfd, (void *) buffer, MAXBUFLEN, 0, (struct sockaddr *) &their_addr, &addr_len);
}


// SIGIO signal handler, handling incoming connection.
int server_connection_handler(int signal) {
    int sockfd = 0;
    fd_set ready;
    struct timeval to;

    CLEAR_ERROR()

    FD_ZERO(&ready);
    FD_SET(sockfd, &ready);
    to.tv_sec = 5;
    if (select(sockfd + 1, &ready, 0, 0, &to) < 0) {
        return -1;
    }
    if (!FD_ISSET(sockfd, &ready))
        return -1;

    return accept(sockfd, (struct sockaddr *) 0, (int *) 0);
}


//
void server_stream_handler(int signal) {

}


// Example from
// https://github.com/frevib/kqueue-tcp-server/blob/master/tcpserver_kqueue.c