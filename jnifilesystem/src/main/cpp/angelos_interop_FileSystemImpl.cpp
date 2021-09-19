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
#include <unistd.h>

#ifndef _Included_angelos_interop_FileSystem
#define _Included_angelos_interop_FileSystem
#ifdef __cplusplus
extern "C" {
#endif

static const char *JNIT_CLASS = "angelos/interop/FileSystem";

/*
 * Class:     angelos_interop_FileSystem
 * Method:    fs_close
 * Signature: (I)I
 */
static jint fs_close(JNIEnv * env, jclass thisClass, jint fd){
    return 1;
}

/*
 * Class:     angelos_interop_FileSystem
 * Method:    fs_read
 * Signature: (I[BJ)J
 */
static jlong fs_read(JNIEnv * env, jclass thisClass, jint fd, jbyteArray buf, jlong n){
    return 1;
}

/*
 * Class:     angelos_interop_FileSystem
 * Method:    fs_write
 * Signature: (I[BJ)J
 */
static jlong fs_write(JNIEnv * env, jclass thisClass, jint fd, jbyteArray buf, jlong n){
    return 1;
}

/*
 * Class:     angelos_interop_FileSystem
 * Method:    fs_lseek
 * Signature: (IJI)J
 */
static jlong fs_lseek(JNIEnv * env, jclass thisClass, jint fd, jlong offset, jint whence){
    return 1;
}

/*
 * Class:     angelos_interop_FileSystem
 * Method:    fs_access
 * Signature: (Ljava/lang/String;I)I
 */
static jint fs_access(JNIEnv * env, jclass thisClass, jstring path, jint amode){
    const char *buf = (*env)->GetStringUTFChars(env, path, 0);
    int result = access(buf, (int)amode);
    (*env)->ReleaseStringUTFChars(env, path, buf);
    return (jint)result;
}

/*
 * Class:     angelos_interop_FileSystem
 * Method:    fs_readlink
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
static jstring fs_readlink(JNIEnv * env, jclass thisClass, jstring path){
    return NULL;
}

/*
 * Class:     angelos_interop_FileSystem
 * Method:    fs_opendir
 * Signature: (Ljava/lang/String;)Langelos/jni/DIR;
 */
static jobject fs_opendir(JNIEnv * env, jclass thisClass, jstring name){
    return NULL;
}

/*
 * Class:     angelos_interop_FileSystem
 * Method:    fs_readdir
 * Signature: (Langelos/jni/DIR;)[B
 */
static jbyteArray fs_readdir(JNIEnv * env, jclass thisClass, jobject dirp){
    return NULL;
}

/*
 * Class:     angelos_interop_FileSystem
 * Method:    fs_closedir
 * Signature: (Langelos/jni/DIR;)I
 */
static jint fs_closedir(JNIEnv * env, jclass thisClass, jobject dirp){
    return 1;
}

/*
 * Class:     angelos_interop_FileSystem
 * Method:    fs_open
 * Signature: (Ljava/lang/CharSequence;II)I
 */
static jint fs_open(JNIEnv * env, jclass thisClass, jstring path, jint flags, jint perm){
    return 1;
}

static JNINativeMethod funcs[] = {
	/*{ "fs_close", "(I)I", (void *)&fs_close },
	{ "fs_read", "(I[BJ)J", (void *)&fs_read },
	{ "fs_write", "(I[BJ)J", (void *)&fs_write },
	{ "fs_lseek", "(IJI)J", (void *)&fs_lseek },*/
	{ "fs_access", "(Ljava/lang/String;I)I", (void *)&fs_access },
	/*{ "fs_readlink", "(Ljava/lang/String;)Ljava/lang/String;", (void *)&fs_readlink },
	{ "fs_opendir", "(Ljava/lang/String;)Langelos/jni/DIR;", (void *)&fs_opendir },
	{ "fs_readdir", "(Langelos/jni/DIR;)[B", (void *)&fs_readdir },
	{ "fs_closedir", "(Langelos/jni/DIR;)I", (void *)&fs_closedir },
	{ "fs_open", "(Ljava/lang/CharSequence;II)I", (void *)&fs_open },*/
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
