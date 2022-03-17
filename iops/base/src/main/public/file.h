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
#ifndef BASE_FILE_H
#define BASE_FILE_H
#if defined (__FreeBSD__) || defined (__NetBSD__) || defined (__OpenBSD__) || defined (__APPLE__)
# include "bsd/file.h"
#elif defined (__linux__)
# include "linux/file.h"
#elif defined(_WIN32) || defined(_WIN64)
# include "win/file.h"
#endif
#endif //BASE_FILE_H
