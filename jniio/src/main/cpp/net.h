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

#ifndef _Included_angelos_interop_IO_net
#define _Included_angelos_interop_IO_net
#ifdef __cplusplus
extern "C" {
#endif


int client_connect(const char * host, short port, int domain, int type, int protocol);
int client_close(int sockfd);


#ifdef __cplusplus
}
#endif
#endif