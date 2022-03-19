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
#include <string.h>
#include <errno.h>
#include "base.h"

#ifndef _Included_angelos_interop_Base
#define _Included_angelos_interop_Base
#ifdef __cplusplus
extern "C" {
#endif

static const char *JNIT_CLASS = "angelos/interop/Base";

/*
 * Class:     angelos_interop_Base
 * Method:    get_endian
 * Signature: ()I
 */
static jint get_endian(JNIEnv *env, jclass thisClass) {
    return endian();
}

/*
 * Class:     angelos_interop_Base
 * Method:    get_platform
 * Signature: ()I
 */
static jint get_platform(JNIEnv *env, jclass thisClass) {
    return platform();
}

/*
 * Class:     angelos_interop_Base
 * Method:    get_signal_abbreviation
 * Signature: (I)Ljava/lang/String;
 */
static jstring get_signal_abbreviation(JNIEnv *env, jclass thisClass, jint signum) {
    const char* abbr = signal_abbreviation(signum);
    return (*env)->NewStringUTF(env, abbr);
}

/*
 * Class:     angelos_interop_Base
 * Method:    get_error
 * Signature: ()V
 */
static void get_error(JNIEnv * env, jclass thisClass){
    if (errno == 0)
        return;

    jclass proc = (*env)->FindClass(env, "angelos/sys/Error");
    jfieldID err_num = (*env)->GetStaticFieldID(env, proc, "errNum", "I");
    jfieldID err_msg = (*env)->GetStaticFieldID(env, proc, "errMsg", "Ljava/lang/String;");
    (*env)->SetStaticIntField(env, proc, err_num, errno);
    (*env)->SetStaticObjectField(env, proc, err_msg, (*env)->NewStringUTF(env, strerror(errno)));
}

static JNINativeMethod funcs[] = {
        {"endian",   "()I", (void *) &get_endian},
        {"platform", "()I", (void *) &get_platform},
        {"signal_abbreviation", "(I)Ljava/lang/String;", (void *) &get_signal_abbreviation},
        {"get_error", "()V", (void *) &get_error},
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
