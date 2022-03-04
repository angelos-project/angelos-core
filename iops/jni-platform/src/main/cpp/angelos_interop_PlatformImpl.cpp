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

#ifndef _Included_angelos_interop_Platform
#define _Included_angelos_interop_Platform
#ifdef __cplusplus
extern "C" {
#endif

static const char *JNIT_CLASS = "angelos/interop/Platform";

/*
 * Class:     angelos_interop_Platform
 * Method:    endian
 * Signature: ()Z
 */
static jboolean endian(JNIEnv * env, jclass thisClass){
    #define LITTLE_ENDIAN 0x41424344UL
    #define BIG_ENDIAN    0x44434241UL
    #define PDP_ENDIAN    0x42414443UL
    #define ENDIAN_ORDER  ('ABCD')

    #if ENDIAN_ORDER==LITTLE_ENDIAN
        return JNI_TRUE;
    #elif ENDIAN_ORDER==BIG_ENDIAN
        return JNI_FALSE;
    #elif ENDIAN_ORDER==PDP_ENDIAN
        #error "Can't compile machine is PDP"
    #else
        #error "Can't compile undistinguished endianness!"
    #endif
}

/*
 * Class:     angelos_interop_Platform
 * Method:    platform
 * Signature: ()I
 */
static jint platform(JNIEnv * env, jclass thisClass){
    #ifdef defined (__unix__) || (defined (__APPLE__) && defined (__MACH__))
        return 1;
    #elif defined (_WIN32)
        return 2;
    #else
        return 0;
    #endif
}

static JNINativeMethod funcs[] = {
        { "endian", "()Z", (void *)&endian }
        { "endian", "()I", (void *)&platform }
};

#define CURRENT_JNI JNI_VERSION_1_6

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
	JNIEnv *env;
	jclass  cls;
	jint    res;

	(void)reserved;

	if ((*vm)->GetEnv(vm, (void **)&env, CURRENT_JNI) != JNI_OK)
		return -1;

	cls = (*env)->FindClass(env, JNIT_CLASS);
	if (cls == NULL)
		return -1;

	res = (*env)->RegisterNatives(env, cls, funcs, sizeof(funcs)/sizeof(*funcs));
	if (res != 0)
		return -1;

	return CURRENT_JNI;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
	JNIEnv *env;
	jclass  cls;

	(void)reserved;

	if ((*vm)->GetEnv(vm, (void **)&env, CURRENT_JNI) != JNI_OK)
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
