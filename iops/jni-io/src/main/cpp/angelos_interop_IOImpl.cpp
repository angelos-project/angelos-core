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

#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>

#include <string.h>

#include "net.h"
 #include "client.h"
#include "server.h"
#include "err.h"

#ifndef _Included_angelos_interop_IO
#define _Included_angelos_interop_IO
#ifdef __cplusplus
extern "C" {
#endif


static const char *JNIT_CLASS = "angelos/interop/IO";


/* ==== ==== ==== ==== FILESYSTEM ==== ==== ==== ==== */


/*
 * Class:     angelos_interop_IO
 * Method:    fs_close
 * Signature: (I)I
 */
static jint fs_close(JNIEnv * env, jclass thisClass, jint fd) {
    return (jint)close((int)fd);
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_read
 * Signature: (I[BIJ)J
 */
static jlong fs_read(JNIEnv * env, jclass thisClass, jint fd, jbyteArray output, jint index, jlong count) {
    jsize size = (*env)->GetArrayLength(env, output);
    signed char* buf = (*env)->GetByteArrayElements(env, output, NULL);
    if (size < count)
        count = size;

    ssize_t length = read((int)fd, buf, (size_t)count);

    (*env)->ReleaseByteArrayElements(env, output, (void*)buf, 0);

    return length;
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_pread
 * Signature: (IJIJJ)J
 */
static jlong fs_pread(JNIEnv * env, jclass thisClass, jint fd, jlong output, jint index, jlong count, jlong size) {
    void* buf = (void*)output+index;
    if (size < (index + count))
        count = size;

    return read((int)fd, buf, (size_t)count);
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_write
 * Signature: (I[BIJ)J
 */
static jlong fs_write(JNIEnv * env, jclass thisClass, jint fd, jbyteArray input, jint index, jlong count) {
    unsigned char* buf = malloc((int)count);

    (*env)->GetByteArrayRegion(env, input, index, count, (jbyte*)buf);
    ssize_t length = write((int)fd, (void*)buf, (size_t)count);

    free(buf);
    return length;
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_pwrite
 * Signature: (IJIJJ)J
 */
static jlong fs_pwrite(JNIEnv * env, jclass thisClass, jint fd, jlong input, jint index, jlong count, jlong size) {
    void* buf = (void*)input+index;
    if (size < (index + count))
        count = size;

    return write((int)fd, buf, (size_t)count);
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_lseek
 * Signature: (IJI)J
 */
static jlong fs_lseek(JNIEnv * env, jclass thisClass, jint fd, jlong offset, jint whence) {
    return (jlong)lseek((int)fd, (off_t)offset, (int)whence);
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_access
 * Signature: (Ljava/lang/String;I)I
 */
static jint fs_access(JNIEnv * env, jclass thisClass, jstring path, jint amode) {
    const char *buf = (*env)->GetStringUTFChars(env, path, NULL);
    int result = access(buf, (int)amode);
    (*env)->ReleaseStringUTFChars(env, path, buf);
    return (jint)result;
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_filetype
 * Signature: (Ljava/lang/String;)I
 */
static jint fs_filetype(JNIEnv * env, jclass thisClass, jstring path) {
    const char *path_buf = (*env)->GetStringUTFChars(env, path, NULL);
    struct stat* file_stat = malloc(sizeof(struct stat));
    int success = lstat(path_buf, file_stat);
    (*env)->ReleaseStringUTFChars(env, path, path_buf);

    int type = 0;
    if(success == 0){
        switch(file_stat->st_mode & S_IFMT) {
            case S_IFLNK:
                type = 1;
                break;
            case S_IFDIR:
                type = 2;
                break;
            case S_IFREG:
                type = 3;
                break;
            default:
                type = 0;
        }
    } else {
        type = -1;
    }

    free(file_stat);
    return (jint)type;
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_fileinfo
 * Signature: (Ljava/lang/String;)Langelos/io/FileObject/Info;
 */
static jobject fs_fileinfo(JNIEnv * env, jclass thisClass, jstring path) {
    const char *path_buf = (*env)->GetStringUTFChars(env, path, NULL);
    struct stat* file_stat = malloc(sizeof(struct stat));
    int success = stat(path_buf, file_stat);
    (*env)->ReleaseStringUTFChars(env, path, path_buf);

    if (success != 0){
        free(file_stat);
        return NULL; // Return NULL if file not found
    }

    jclass local_cls = (*env)->FindClass(env, "angelos/io/FileSystem$Info");
    if (local_cls == NULL) // Quit program if Java class can't be found
        exit(1);

    jclass global_cls = (*env)->NewGlobalRef(env, local_cls);
    jmethodID cls_init = (*env)->GetMethodID(env, global_cls, "<init>", "(IIJJJJJ)V");
    if (cls_init == NULL) // Quit program if Java class constructor can't be found
        exit(1);

    jobject file_info = (*env)->NewObject(
        env, global_cls, cls_init,
        file_stat->st_uid,
        file_stat->st_gid,
        file_stat->st_atimespec.tv_sec,
        file_stat->st_mtimespec.tv_sec,
        file_stat->st_ctimespec.tv_sec,
        file_stat->st_birthtimespec.tv_sec,
        file_stat->st_size
    );
    free(file_stat);

    return file_info;
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_readlink
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
static jstring fs_readlink(JNIEnv * env, jclass thisClass, jstring path) {
    const char *link = (*env)->GetStringUTFChars(env, path, NULL);
    char* target = malloc(4096);

    ssize_t length = readlink(link, target, 4096);
    if (length == -1){
        free(target);
        return NULL;
    }

    target[length > 2095 ? 2095 : length] = '\0';
    (*env)->ReleaseStringUTFChars(env, path, link);
    jstring target_str = (*env)->NewStringUTF(env,target);

    free(target);
    return target_str;
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_opendir
 * Signature: (Ljava/lang/String;)J
 */
static jlong fs_opendir(JNIEnv * env, jclass thisClass, jstring name) {
    const char *link = (*env)->GetStringUTFChars(env, name, NULL);
    intptr_t dir = (intptr_t)opendir(link);
    (*env)->ReleaseStringUTFChars(env, name, link);
    return (jlong)dir;
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_readdir
 * Signature: (J)Langelos/io/Dir$FileEntry;
 */
static jobject fs_readdir(JNIEnv * env, jclass thisClass, jlong dirp) {
    struct dirent *entry = readdir((DIR *)dirp);

    jclass local_cls = (*env)->FindClass(env, "angelos/io/FileSystem$FileEntry");
    if (local_cls == NULL) // Quit program if Java class can't be found
        exit(1);

    jclass global_cls = (*env)->NewGlobalRef(env, local_cls);
    jmethodID cls_init = (*env)->GetMethodID(env, global_cls, "<init>", "(Ljava/lang/String;I)V");
    if (cls_init == NULL) // Quit program if Java class constructor can't be found
        exit(1);

    int type = 0;
    if (entry != NULL)
        switch(entry->d_type){
            case DT_LNK:
                type = 1;
                break;
            case DT_DIR:
                type = 2;
                break;
            case DT_REG:
                type = 3;
                break;
            default:
                type = 0;
        }

    jobject file_entry = (*env)->NewObject(
        env, global_cls, cls_init, (*env)->NewStringUTF(
            env, entry != NULL ? entry->d_name : ""), type);
    if (entry == NULL)
        free(entry);

    return file_entry;
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_closedir
 * Signature: (J)I
 */
static jint fs_closedir(JNIEnv * env, jclass thisClass, jlong dirp) {
    return (jint)closedir((DIR *)dirp);
}

/*
 * Class:     angelos_interop_IO
 * Method:    fs_open
 * Signature: (Ljava/lang/String;I)I
 */
static jint fs_open(JNIEnv * env, jclass thisClass, jstring path, jint perm) {
    const char *buf = (*env)->GetStringUTFChars(env, path, NULL);
    int fd = open(buf, O_CREAT | O_NONBLOCK | (int)perm);
    (*env)->ReleaseStringUTFChars(env, path, buf);
    return (jint)fd;
}


/*
 * Class:     angelos_interop_IO
 * Method:    ep_pull
 * Signature: ()Langelos/interop/PollAction;
 */
static jobject ep_pull(JNIEnv * env, jclass thisClass) {
    int description = -1;
    int action = -1;
    int err = 0;

    err = event_poll(&description, &action);
    if (err != 0)
        exit(1);

    jclass local_cls = (*env)->FindClass(env, "angelos/interop/PollAction");
    if (local_cls == NULL) // Quit program if Java class can't be found
        exit(1);

    jclass global_cls = (*env)->NewGlobalRef(env, local_cls);
    jmethodID cls_init = (*env)->GetMethodID(env, global_cls, "<init>", "(I;I)V");
    if (cls_init == NULL) // Quit program if Java class constructor can't be found
        exit(1);

    return (*env)->NewObject(env, global_cls, cls_init, description, action);
}


/* ==== ==== ==== ==== SERVER ==== ==== ==== ==== */


/*
 * Class:     angelos_interop_IO
 * Method:    server_open
 * Signature: (III)I
 */
static jint server1_open(JNIEnv * env, jclass thisClass, jint domain, jint type, jint protocol) {
    int err =  server_open(domain, type, protocol);
    SET_ERROR(env, err)
    return err;
}


/*
 * Class:     angelos_interop_IO
 * Method:    server_listen
 * Signature: (ILjava/lang/String;sI)I
 */
static jint server1_listen(JNIEnv * env, jclass thisClass, jint sockfd, jstring host, jshort port, int domain, jint max_conn) {
    const char *name = (*env)->GetStringUTFChars(env, host, NULL);
    int err = server_listen(sockfd, name, port, domain, max_conn);
    (*env)->ReleaseStringUTFChars(env, host, name);
    SET_ERROR(env, err)
    return err;
}


static jint server_close(JNIEnv * env, jclass thisClass) {
}


static jint server_handle(JNIEnv * env, jclass thisClass) {
}


/* ==== ==== ==== ==== CLIENT ==== ==== ==== ==== */


/*
 * Class:     angelos_interop_IO
 * Method:    client_connect
 * Signature: (Ljava/lang/String;sIII)I
 */
static jint client1_connect(JNIEnv * env, jclass thisClass, jstring host, jshort port, jint domain, jint type, jint protocol) {
    const char *name = (*env)->GetStringUTFChars(env, host, NULL);
    int sockfd = client_connect(name, port, domain, type, protocol);
    (*env)->ReleaseStringUTFChars(env, host, name);
    SET_ERROR(env, sockfd)
    return sockfd;
}


/* ==== ==== ==== ==== NETWORK ==== ==== ==== ==== */


static JNINativeMethod funcs[] = {
	{ "fs_close", "(I)I", (void *)&fs_close },
	{ "fs_read", "(I[BIJ)J", (void *)&fs_read },
	{ "fs_pread", "(IJIJJ)J", (void *)&fs_pread },
	{ "fs_write", "(I[BIJ)J", (void *)&fs_write },
	{ "fs_pwrite", "(IJIJJ)J", (void *)&fs_pwrite },
	{ "fs_lseek", "(IJI)J", (void *)&fs_lseek },
	{ "fs_access", "(Ljava/lang/String;I)I", (void *)&fs_access },
	{ "fs_filetype", "(Ljava/lang/String;)I", (void *)&fs_filetype },
	{ "fs_fileinfo", "(Ljava/lang/String;)Langelos/io/FileSystem$Info;", (void *)&fs_fileinfo },
	{ "fs_readlink", "(Ljava/lang/String;)Ljava/lang/String;", (void *)&fs_readlink },
	{ "fs_opendir", "(Ljava/lang/String;)J", (void *)&fs_opendir },
	{ "fs_readdir", "(J)Langelos/io/FileSystem$FileEntry;", (void *)&fs_readdir },
	{ "fs_closedir", "(J)I", (void *)&fs_closedir },
	{ "fs_open", "(Ljava/lang/String;I)I", (void *)&fs_open },

    { "ep_pull", "()Langelos/interop/PollAction;", (void *)&ep_pull },

	{ "server_open", "(III)I", (void *)&server1_open },
	{ "server_listen", "(ILjava/lang/String;SII)I", (void *)&server1_listen },
	{ "client_connect", "(Ljava/lang/String;SIII)I", (void *)&client1_connect },
};

#define CURRENT_JNI JNI_VERSION_1_6

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
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

    OPEN_QUEUE()

	return CURRENT_JNI;
}


JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
	JNIEnv *env;
	jclass  cls;

	(void)reserved;

	if ((*vm)->GetEnv(vm, (void **)&env, CURRENT_JNI) != JNI_OK)
		return;

	cls = (*env)->FindClass(env, JNIT_CLASS);
	if (cls == NULL)
		return;

    CLOSE_QUEUE()

	(*env)->UnregisterNatives(env, cls);
}


#ifdef __cplusplus
}
#endif
#endif
