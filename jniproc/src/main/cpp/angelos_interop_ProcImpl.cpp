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
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <inttypes.h>
#include <dirent.h>

#ifndef _Included_angelos_interop_IO
#define _Included_angelos_interop_IO
#ifdef __cplusplus
extern "C" {
#endif

static const char *JNIT_CLASS = "angelos/interop/Proc";


static JNIEnv * callback_env = NULL;


void callback(int signum)
{
    jclass local_cls = (*callback_env)->FindClass(callback_env, "angelos/interop/Proc$Companion");
    if (local_cls == NULL) // Quit program if Java class can't be found
        exit(1);

    jclass global_cls = (*callback_env)->NewGlobalRef(callback_env, local_cls);
    jmethodID cls_callback = (*callback_env)->GetMethodID(callback_env, global_cls, "interrupt", "(I)V");
    if (cls_callback == NULL) // Quit program if Java class constructor can't be found
        exit(1);

    (*callback_env)->CallStaticVoidMethod(callback_env, global_cls, cls_callback, signum);
}


/*
 * Class:     angelos_interop_Proc
 * Method:    pr_signal
 * Signature: (I)Z
 */
static jboolean pr_signal(JNIEnv * env, jclass thisClass, jint signum){
    if (callback_env != NULL)
        callback_env = env;
    else if (callback_env != env)
        exit(1);

    signal((int)signum, &callback);
    //void ( *signal(int signum, void (*handler)(int)) ) (int);

    return JNI_TRUE;
}


static JNINativeMethod funcs[] = {
	{ "pr_signal", "(I)Z", (void *)&pr_signal },
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
