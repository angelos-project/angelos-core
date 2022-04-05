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

#ifndef BASE_TERMINAL_H
#define BASE_TERMINAL_H

#include <stdlib.h>
#include <unistd.h>
#include <termios.h>


/**
 * Start terminal raw mode using termios.
 * @return 0 on success
 */
extern int init_terminal_mode();


/**
 * Turn off terminal raw mode.
 * @return 0 on success
 */
extern int finalize_terminal_mode();


#endif //BASE_TERMINAL_H
