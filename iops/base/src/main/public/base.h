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
#ifndef BASE_BASE_H
#define BASE_BASE_H

#include "terminal.h"
#include "event.h"
#include "file.h"
#include "sig.h"
#include "sock.h"


/**
 * Gives the endianness of the hardware platform.
 * @return Integer representation of the hardware.
 */
extern int endian();


/**
 * Gives the system programmable API standard, currently POSIX and NT.
 * @return Integer representation of system API.
 */
extern int platform();


#endif //BASE_BASE_H
