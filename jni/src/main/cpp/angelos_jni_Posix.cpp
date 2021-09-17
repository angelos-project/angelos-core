
#include "angelos_jni_Posix.h"
/* Header for class angelos_jni_Posix */

#ifndef _Included_angelos_jni_Posix
#define _Included_angelos_jni_Posix
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     angelos_jni_Posix
 * Method:    close
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_angelos_jni_Posix_close(JNIEnv *, jclass thisClass, jint fd){}

/*
 * Class:     angelos_jni_Posix
 * Method:    read
 * Signature: (I[BJ)J
 */
JNIEXPORT jlong JNICALL Java_angelos_jni_Posix_read(JNIEnv * env, jclass thisClass, jint fd, jbyteArray buf, jlong n){}

/*
 * Class:     angelos_jni_Posix
 * Method:    write
 * Signature: (I[BJ)J
 */
JNIEXPORT jlong JNICALL Java_angelos_jni_Posix_write(JNIEnv * env, jclass thisClass, jint fd, jbyteArray buf, jlong n){}

/*
 * Class:     angelos_jni_Posix
 * Method:    lseek
 * Signature: (IJI)J
 */
JNIEXPORT jlong JNICALL Java_angelos_jni_Posix_lseek(JNIEnv * env, jclass thisClass, jint fd, jlong offset, jint whence){}

/*
 * Class:     angelos_jni_Posix
 * Method:    alloc
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_angelos_jni_Posix_alloc(JNIEnv * env, jclass thisClass){}

/*
 * Class:     angelos_jni_Posix
 * Method:    access
 * Signature: (Ljava/lang/CharSequence;I)I
 */
JNIEXPORT jint JNICALL Java_angelos_jni_Posix_access(JNIEnv * env, jclass thisClass, jstring path, jint amode){}

/*
 * Class:     angelos_jni_Posix
 * Method:    readlink
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_angelos_jni_Posix_readlink(JNIEnv * env, jclass thisClass, jstring path){}

/*
 * Class:     angelos_jni_Posix
 * Method:    opendir
 * Signature: (Ljava/lang/String;)Langelos/jni/DIR;
 */
JNIEXPORT jobject JNICALL Java_angelos_jni_Posix_opendir(JNIEnv * env, jclass thisClass, jstring name){}

/*
 * Class:     angelos_jni_Posix
 * Method:    readdir
 * Signature: (Langelos/jni/DIR;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_angelos_jni_Posix_readdir(JNIEnv * env, jclass thisClass, jobject dirp){}

/*
 * Class:     angelos_jni_Posix
 * Method:    closedir
 * Signature: (Langelos/jni/DIR;)I
 */
JNIEXPORT jint JNICALL Java_angelos_jni_Posix_closedir(JNIEnv * env, jclass thisClass, jobject dirp){}

/*
 * Class:     angelos_jni_Posix
 * Method:    open
 * Signature: (Ljava/lang/CharSequence;II)I
 */
JNIEXPORT jint JNICALL Java_angelos_jni_Posix_open(JNIEnv * env, jclass thisClass, jstring path, jint flags, jint perm){}

/*
 * Class:     angelos_jni_Posix
 * Method:    isLittleEndian
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_angelos_jni_Posix_isLittleEndian(JNIEnv * env, jclass thisClass){}

#ifdef __cplusplus
}
#endif
#endif
