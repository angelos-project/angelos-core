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
#include <errno.h>


#ifndef _Included_angelos_interop_IO_err
#define _Included_angelos_interop_IO_err
#ifdef __cplusplus
extern "C" {
#endif


#define CLEAR_ERROR() errno = 0;


#define SET_ERROR(env_, param_) \
    if (param_ == -1) { \
        int err_ = errno; \
        jclass proc_ = (*env_)->FindClass(env_, "angelos/interop/Proc"); \
        jfieldID err_num_ = (*env_)->GetStaticFieldID(env_, proc_, "errNum", "I"); \
        jfieldID err_msg_ = (*env_)->GetStaticFieldID(env_, proc_, "errMsg", "Ljava/lang/String;"); \
        (*env_)->SetStaticIntField(env_, proc_, err_num_, err_); \
        (*env_)->SetStaticObjectField(env_, proc_, err_msg_, (*env_)->NewStringUTF(env_, strerror(err_))); \
    } \


#ifdef __cplusplus
}
#endif
#endif