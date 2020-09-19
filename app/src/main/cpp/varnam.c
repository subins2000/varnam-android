#include <jni.h>
#include "android/log.h"

#include "libvarnam/varnam.h"

JNIEXPORT jstring JNICALL Java_com_subinsb_varnam_MainActivity_stringFromJNI(JNIEnv* env, jobject this, jstring varnam_folderFromJNI) {

    varnam *handle;
    char *msg;
    int rc;
    jobjectArray result;

    result = (jobjectArray) (*env)->NewObjectArray(env, 10, (*env)->FindClass(env, "java/lang/String"), (*env)->NewStringUTF(env, ""));

    const char *varnam_folder = (*env)->GetStringUTFChars(env, varnam_folderFromJNI, NULL);
    setenv("VARNAM_SYMBOLS_DIR", varnam_folder, 0);
    setenv("VARNAM_SUGGESTIONS_DIR", varnam_folder, 0);

    __android_log_print(ANDROID_LOG_DEBUG, "varnam", "aaaaaaaa %s", varnam_folder);

    rc = varnam_init_from_id("ml", &handle, &msg);
    if (rc != VARNAM_SUCCESS) {
        (*env)->SetObjectArrayElement(env, result, 0, (*env)->NewStringUTF(env, msg));
    } else {
        varray *words;
        varnam_transliterate(handle, "nan", &words);

        int i;
        vword *word;
        for (i = 0; i < varray_length(words); i++) {
            word = varray_get(words, i);
            (*env)->SetObjectArrayElement(env, result, i, (*env)->NewStringUTF(env, word->text));
            __android_log_print(ANDROID_LOG_DEBUG, "varnam", "aaaaaaaa %s %d", word->text, varray_length(words));
        }
    }

    return result;
}
