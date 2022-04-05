cmake_minimum_required(VERSION 3.21)
project(base C)

set(CMAKE_C_STANDARD 11)

source_group(src/main/c)
include_directories(src/main/public)

add_library(base
        src/main/c/terminal.c
        src/main/c/base.c
        src/main/c/bsd/event.c
        src/main/c/bsd/file.c
        src/main/c/bsd/sig.c
        src/main/c/bsd/sock.c
        src/main/c/linux/event.c
        src/main/c/linux/file.c
        src/main/c/linux/sig.c
        src/main/c/linux/sock.c
        src/main/c/win/event.c
        src/main/c/win/file.c
        src/main/c/win/sig.c
        src/main/c/win/sock.c
        )
