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
#include <jni.h>
#include "base.h"

#ifndef _Included_angelos_interop_Base
#define _Included_angelos_interop_Base
#ifdef __cplusplus
extern "C" {
#endif

static const char *JNIT_CLASS = "angelos/interop/Base";

/*
 * Class:     angelos_interop_Base
 * Method:    endian
 * Signature: ()I
 */
static jint get_endian(JNIEnv *env, jclass thisClass) {
    return endian();
}

/*
 * Class:     angelos_interop_Base
 * Method:    platform
 * Signature: ()I
 */
static jint get_platform(JNIEnv *env, jclass thisClass) {
    return platform();
}

static JNINativeMethod funcs[] = {
        {"endian",   "()I", (void *) &get_endian},
        {"platform", "()I", (void *) &get_platform}
};

#define CURRENT_JNI JNI_VERSION_1_6

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    jclass cls;
    jint res;

    (void) reserved;

    if ((*vm)->GetEnv(vm, (void **) &env, CURRENT_JNI) != JNI_OK)
        return -1;

    cls = (*env)->FindClass(env, JNIT_CLASS);
    if (cls == NULL)
        return -1;

    res = (*env)->RegisterNatives(env, cls, funcs, sizeof(funcs) / sizeof(*funcs));
    if (res != 0)
        return -1;

    return CURRENT_JNI;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    jclass cls;

    (void)reserved;

    if ((*vm)->GetEnv(vm,(void **)&env, CURRENT_JNI) != JNI_OK)
        return;

    cls = (*env)->FindClass(env, JNIT_CLASS);
    if (cls == NULL)
        return;

    (*env)->UnregisterNatives(env, cls);
}


#ifdef __cplusplus
}
#endif
#endif
