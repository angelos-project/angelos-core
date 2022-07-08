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
#include "base.h"


#define _LITTLE_ENDIAN 0x41424344UL
#define _BIG_ENDIAN    0x44434241UL
#define _PDP_ENDIAN    0x42414443UL
#define ENDIAN_ORDER  ('ABCD')


enum {
    kLittleEndian = 2,
    kBigEndian = 1
};

int endian() {
#if ENDIAN_ORDER == _LITTLE_ENDIAN
    return kLittleEndian;
#elif ENDIAN_ORDER == _BIG_ENDIAN
    return kBigEndian;
#elif ENDIAN_ORDER==_PDP_ENDIAN
#error "Can't compile machine is PDP"
#else
#error "Can't compile undistinguished endianness!"
#endif
}


enum {
    kUnknown = 0,
    kPosix = 1,
    kNT = 2,
};

int platform() {
#if defined (__unix__) || defined (__APPLE__)
    return kPosix;
#elif defined (_WIN32) || defined (_WIN64)
    return kNT;
#else
    return kUnknown;
#endif
}


int pid() {
    return getpid();
}