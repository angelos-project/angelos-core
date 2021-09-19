
#include <jni.h>

#ifndef _Included_angelos_jni_Posix
#define _Included_angelos_jni_Posix
#ifdef __cplusplus
extern "C" {
#endif

static const char *JNIT_CLASS = "angelos/jni/Posix";

/*
 * Class:     angelos_jni_Posix
 * Method:    close
 * Signature: (I)I
 */
static jint posix_close(JNIEnv * env, jclass thisClass, jint fd){
    return 1;
}

/*
 * Class:     angelos_jni_Posix
 * Method:    read
 * Signature: (I[BJ)J
 */
static jlong posix_read(JNIEnv * env, jclass thisClass, jint fd, jbyteArray buf, jlong n){
    return 1;
}

/*
 * Class:     angelos_jni_Posix
 * Method:    write
 * Signature: (I[BJ)J
 */
static jlong posix_write(JNIEnv * env, jclass thisClass, jint fd, jbyteArray buf, jlong n){
    return 1;
}

/*
 * Class:     angelos_jni_Posix
 * Method:    lseek
 * Signature: (IJI)J
 */
static jlong posix_lseek(JNIEnv * env, jclass thisClass, jint fd, jlong offset, jint whence){
    return 1;
}

/*
 * Class:     angelos_jni_Posix
 * Method:    access
 * Signature: (Ljava/lang/CharSequence;I)I
 */
static jint posix_access(JNIEnv * env, jclass thisClass, jstring path, jint amode){
    return 1;
}

/*
 * Class:     angelos_jni_Posix
 * Method:    readlink
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
static jstring posix_readlink(JNIEnv * env, jclass thisClass, jstring path){
    return NULL;
}

/*
 * Class:     angelos_jni_Posix
 * Method:    opendir
 * Signature: (Ljava/lang/String;)Langelos/jni/DIR;
 */
static jobject posix_opendir(JNIEnv * env, jclass thisClass, jstring name){
    return NULL;
}

/*
 * Class:     angelos_jni_Posix
 * Method:    readdir
 * Signature: (Langelos/jni/DIR;)[B
 */
static jbyteArray posix_readdir(JNIEnv * env, jclass thisClass, jobject dirp){
    return NULL;
}

/*
 * Class:     angelos_jni_Posix
 * Method:    closedir
 * Signature: (Langelos/jni/DIR;)I
 */
static jint posix_closedir(JNIEnv * env, jclass thisClass, jobject dirp){
    return 1;
}

/*
 * Class:     angelos_jni_Posix
 * Method:    open
 * Signature: (Ljava/lang/CharSequence;II)I
 */
static jint posix_open(JNIEnv * env, jclass thisClass, jstring path, jint flags, jint perm){
    return 1;
}

/*
 * Class:     angelos_jni_Posix
 * Method:    isLittleEndian
 * Signature: ()Z
 */
static jboolean posix_endian(JNIEnv * env, jclass thisClass){
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

static JNINativeMethod funcs[] = {
	/*{ "posix_close", "(I)I", (void *)&posix_close },
	{ "posix_read", "(I[BJ)J", (void *)&posix_read },
	{ "posix_write", "(I[BJ)J", (void *)&posix_write },
	{ "posix_lseek", "(IJI)J", (void *)&posix_lseek },
	{ "posix_access", "(Ljava/lang/CharSequence;I)I", (void *)&posix_access },
	{ "posix_readlink", "(Ljava/lang/String;)Ljava/lang/String;", (void *)&posix_readlink },
	{ "posix_opendir", "(Ljava/lang/String;)Langelos/jni/DIR;", (void *)&posix_opendir },
	{ "posix_readdir", "(Langelos/jni/DIR;)[B", (void *)&posix_readdir },
	{ "posix_closedir", "(Langelos/jni/DIR;)I", (void *)&posix_closedir },
	{ "posix_open", "(Ljava/lang/CharSequence;II)I", (void *)&posix_open },*/
	{ "posix_endian", "()Z", (void *)&posix_endian }
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
