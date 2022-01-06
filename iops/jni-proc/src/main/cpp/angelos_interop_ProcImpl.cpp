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
#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <errno.h>

#ifndef _Included_angelos_interop_IO
#define _Included_angelos_interop_IO
#ifdef __cplusplus
extern "C" {
#endif


void set_error(JNIEnv * env) {
    jclass proc = (*env)->FindClass(env, "angelos/interop/Proc$Companion");
    jfieldID err_num = (*env)->GetStaticFieldID(env, proc, "errNum", "I");
    jfieldID err_msg = (*env)->GetStaticFieldID(env, proc, "errMsg", "Ljava/lang/String;");
    (*env)->SetStaticIntField(env, proc, err_num, errno);
    (*env)->SetStaticIntField(env, proc, err_msg, (*env)->NewStringUTF(env, strerror(errno)));
}


static const char *JNIT_CLASS = "angelos/interop/Proc";


JNIEnv * callback_env = NULL;
struct sigaction cb_action;


void sig_handler(int signum, siginfo_t *info, void *context)
{
    // Blocking incoming signals
    sigset_t sigs;
    sigemptyset(&sigs);
    pthread_sigmask(0, NULL, &sigs);

    if (sigismember(&sigs, SIGIO)) {
        // Doing JNI interrupt stuff
        jclass local_cls = (*callback_env)->FindClass(callback_env, "angelos/interop/Proc$Companion");
        if (local_cls == NULL) // Quit program if Java class can't be found
            exit(1);

        jclass global_cls = (*callback_env)->NewGlobalRef(callback_env, local_cls);
        jmethodID cls_callback = (*callback_env)->GetMethodID(callback_env, global_cls, "interrupt", "(I)V");
        if (cls_callback == NULL) // Quit program if Java class constructor can't be found
            exit(1);

        (*callback_env)->CallStaticVoidMethod(callback_env, global_cls, cls_callback, signum);

        // Unblocking incoming signals
        sigaddset(&sigs, SIGIO);
        pthread_sigmask(SIG_UNBLOCK, &sigs, NULL);
    }
}


/*
 * Class:     angelos_interop_Proc
 * Method:    pr_signal
 * Signature: (I)Z
 */
static jboolean pr_signal(JNIEnv * env, jclass thisClass, jint signum){
    if (callback_env == NULL)
        callback_env = env;
    else if (callback_env != env)
        exit(1);

    return sigaction(signum, &cb_action, NULL) ? JNI_FALSE : JNI_TRUE;
}


/*
 * Class:     angelos_interop_Proc
 * Method:    pr_error
 * Signature: ()Ljava/lang/String;
 */
/*static jstring pr_error(JNIEnv * env, jclass thisClass){
    if (errno == 0)
        return NULL;

    unsigned char* target = strerror(errno);
    jstring target_str = (*env)->NewStringUTF(env,target);
    free(target);
    errno = 0;

    return target_str;
}*/


static JNINativeMethod funcs[] = {
	{ "pr_signal", "(I)Z", (void *)&pr_signal },
	//{ "pr_error", "()Ljava/lang/String;", (void *)&pr_error },
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

	// INITIALIZING SIGNAL HANDLER
	cb_action.sa_flags = SA_SIGINFO | SA_RESTART | SA_NODEFER;
    cb_action.sa_sigaction = sig_handler;
    sigfillset(&cb_action.sa_mask);
    sigdelset(&cb_action.sa_mask, SIGIO);

    if (sigaction(SIGIO, &cb_action, NULL)) {
        printf("Sigaction failed\n");
        exit(1);
    }
	// SIGNAL HANDLER OVER

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
