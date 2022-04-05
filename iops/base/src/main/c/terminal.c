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

#include "terminal.h"

#include <stdio.h>
#include <fcntl.h>

static struct termios termios_backup;
static int termios_mode_on = 0;
int fd = STDIN_FILENO;


int init_terminal_mode() {
    if (termios_mode_on > 0)
        exit(1);

    struct termios mode;

    if (!isatty(fd)) {
        printf("File descriptor %d is not a TTY!\n", fd);
        return -1;
    }


    // This is derived from from Stevens, "Advanced Programming in the UNIX Environment"
    if (tcgetattr(fd, &termios_backup) < 0) /* get the original state */
        return -1;

    mode = termios_backup;

    mode.c_lflag &= ~(ECHO | ICANON | IEXTEN | ISIG);
    mode.c_iflag |= BRKINT | ICRNL;
    mode.c_cflag &= ~(CSIZE | PARENB);
    mode.c_cflag |= CS8;
    mode.c_oflag &= ~(OPOST);
    mode.c_cc[VMIN] = 1;
    mode.c_cc[VTIME] = 0;

    if (tcsetattr(fd, TCSAFLUSH, &mode) < 0)
        return -1;

    termios_mode_on = 1;
    return 0;
}

int finalize_terminal_mode() {
    if(termios_mode_on == 0)
        exit(1);
    tcsetattr(fd, TCSAFLUSH, &termios_backup);
    termios_mode_on = 0;
    return 0;
}