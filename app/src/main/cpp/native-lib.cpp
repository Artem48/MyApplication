#include <jni.h>
#include <string>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include "libzip-1.5.2/lib/zip.h"
#include "../../../.cxx/cmake/debug/x86/libzip-1.5.2/config.h"
#include <iostream>

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_artem48_myapplication_MainActivity_pictureFromJNI(
        JNIEnv* env,
jobject p, jobject assetManager) {
    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
    AAsset* file =AAssetManager_open(mgr, "Canada_Mountains_Scenery_488936.jpg", AASSET_MODE_BUFFER);
    size_t fileLength = AAsset_getLength(file);
    jbyteArray array=env->NewByteArray(fileLength+1);
    jboolean isCopy;
    ///auto aaa = env->GetByteArrayElements(array, &isCopy);
    char* fileContent = new char[fileLength+1];
    AAsset_read(file, fileContent, fileLength);
    fileContent[fileLength] = '\0';
    //aaa[fileLength] = '\0';

    //jbyteArray array=env->NewByteArray(fileLength+1);
    env->SetByteArrayRegion(array, 0, fileLength+1, (const jbyte*) fileContent);
    delete[] fileContent;
    return array;
}
extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_artem48_myapplication_MainActivity_pictureFromJNIlibzip(
        JNIEnv* env,
        jobject p, jstring PathToAPK) {
    jbyteArray array;
    const char *apk_location = env->GetStringUTFChars((jstring)PathToAPK, 0);

    zip_t* apk_file = zip_open(apk_location, 0, NULL);

    if(apk_file != NULL) {
        const char *file_name = "assets/219186-frederika.png";

        int file_index;
        zip_file *file;
        struct zip_stat file_stat;

        file_index = zip_name_locate(apk_file, file_name, 0);

        if (file_index == -1) {
            zip_close(apk_file);

            return 0;
        }

        file = zip_fopen_index(apk_file, (zip_uint64_t)file_index, 0);
        if (file == NULL) {
            zip_close(apk_file);

            return 0;
        }

        zip_stat_init(&file_stat);
        zip_stat(apk_file, file_name, 0, &file_stat);
        char *buffer = new char[file_stat.size];

        int result = zip_fread(file, buffer, file_stat.size);
        if (result == -1) {
            delete[] buffer;
            zip_fclose(file);

            zip_close(apk_file);

            return 0;
        }


        array=env->NewByteArray(file_stat.size);
        env->SetByteArrayRegion(array, 0, file_stat.size, (const jbyte*) buffer);

        zip_fclose(file);
        zip_close(apk_file);
    }

    env->ReleaseStringUTFChars((jstring)PathToAPK, apk_location);
    return array;

}
