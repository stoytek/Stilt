//
// Created by Frode Jacobsen on 22/02/17.
//

#ifdef __cplusplus
extern "C" {
#endif

#include "audio-recorder-jni.h"

#include <jni.h>

#include <assert.h>
#include <string.h>

#include <pthread.h>

#include <android/log.h>


/* Engine interfaces */
static SLObjectItf engineObjItf = NULL;
static SLEngineItf engineItf;

/* Mutex for locking audioengine during recording */

static pthread_mutex_t audioEngineLock = PTHREAD_MUTEX_INITIALIZER;

/* Audio recorder interfaces */

static SLObjectItf recObjItf = NULL;
static SLRecordItf recItf;
static SLAndroidSimpleBufferQueueItf recBufq;

/*
 * Buffer size 1s @ 44.1 kHz. In combination with the callback that is called when
 * the buffer is full, this ensures that we get exactly 1 second of audio each time
 * startRecording() is called.
 */
#define RECORD_BUF_SIZE (44100)

static short recBuf[RECORD_BUF_SIZE];
static unsigned recSize = 0;

/* Java environment handles for use within the callback */
static JavaVM* jjvm = NULL;
static jobject jAudioRecorderObj;
static jobject jClassLoader;
static jmethodID jFindClassMethod;
static pthread_mutex_t jAudioRecorderLock = PTHREAD_MUTEX_INITIALIZER;

/* Callback called each time a buffer finishes recording */
/* TODO: See if anything else needs to be done is this method */

void bqRecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context) {
    __android_log_write(ANDROID_LOG_DEBUG, "bqRecorderCallback", "Entered callback function");

    /* Make sure that the buffer is the one we want */
    assert(bq == recBufq);
    /* Unsure about this one, tbh */
    assert(context == NULL);

    /* This callback is called whenever the buffer is full, so we simply stop recording */
    SLresult result = (*recItf)->SetRecordState(recItf, SL_RECORDSTATE_STOPPED);
    if (result == SL_RESULT_SUCCESS) {
        recSize = RECORD_BUF_SIZE * sizeof(short);
    }
    __android_log_write(ANDROID_LOG_DEBUG, "bqRecorderCallback", "Stopped recording");

    /******************************************************************************/
    /******** Callback to main thread to notify it that the audio is ready ********/
    /******************************************************************************/

    /* Attach the current thread to the JavaVM and get a reference to the JNI environment */
    JNIEnv* env;
    jint rs = (*jjvm)->AttachCurrentThread(jjvm, &env, NULL);
    assert(rs == JNI_OK);
    __android_log_write(ANDROID_LOG_DEBUG, "bqRecorderCallback", "Attached thread to VM");

    /*
     * FIXME: Unable to find classes in pure native threads (not on the Java stack). Need a pointer
     * to a classloader that knows where to find the class we want.
     * See https://stackoverflow.com/questions/13263340/findclass-from-any-thread-in-android-jni/16302771#16302771
     * for a workaround
     */

    /* Use the cached ClassLoader to get a reference to the class */
    jclass clazz = (*env)->CallObjectMethod(
            env,
            jClassLoader,
            jFindClassMethod,
            (*env)->NewStringUTF(env, "com/stilt/stoytek/stilt/audiorec/AudioRecorder")
    );

    assert(clazz != NULL);
    __android_log_write(ANDROID_LOG_DEBUG, "bqRecorderCallback", "Got AudioRecorder class reference");

    /* TODO: This is not working correctly, still cannot find a reference to the method */
    jmethodID callback = (*env)->GetMethodID(env, clazz, "audioReady", "()V");
    assert(callback != NULL);

    (*env)->CallVoidMethod(env, jAudioRecorderObj, callback);

    /* Unlock mutexes */
    pthread_mutex_unlock(&audioEngineLock);
    pthread_mutex_unlock(&jAudioRecorderLock);

    __android_log_write(ANDROID_LOG_DEBUG, "bqRecorderCallback", "Unlocked engine and jAudioRecorder");


}

int storeReferences(JNIEnv* env, jobject obj) {

    /* Create temporary global references to the calling object */

    /* Lock the object references */
    if (pthread_mutex_trylock(&jAudioRecorderLock)) return SL_RESULT_UNKNOWN_ERROR;

    /* Create a new global reference of the AudioRecorder object */
    jAudioRecorderObj = (*env)->NewGlobalRef(env, obj); // Create global reference to calling object
    assert(jAudioRecorderObj != NULL);
    __android_log_write(ANDROID_LOG_DEBUG, "storeReferences", "Aquired reference to AudioRecorder object");

    /* Create a reference to the JavaVM */
    (*env)->GetJavaVM(env, &jjvm); // Create a reference to the VM so we can attach to it later
    assert(jjvm != NULL);
    __android_log_write(ANDROID_LOG_DEBUG, "storeReferences", "Aquired JavaVM reference");

    /* Get a reference to the AudioRecorder class */
    jclass clazz = (*env)->FindClass(env, "com/stilt/stoytek/stilt/audiorec/AudioRecorder");
    assert(clazz != NULL);
    __android_log_write(ANDROID_LOG_DEBUG, "storeReferences", "Got AudioRecorder class reference");

    /**********************************************************************************************
     * See stackoverflow.com/questions/13263340/findclass-from-any-thread-in-android-jni/16302771#16302771
     * for an explanation of the following. In short, we have to get and store a reference to the
     * classloader tied to the correct JNI environment where the instantance of AudioRecorder that
     * we are interested in is located. The (*env)->FindClass method relies on this to find the
     * AudioRecord _class_. If we do not have this reference, FindClass uses the system classloader
     * (that is only able to find java.x.x/system classes) and we are unable to retrieve a reference
     * to the class.
     * ********************************************************************************************/

    /* Get a reference to the Object class of AudioRecroder */
    jclass clazzClazz = (*env)->GetObjectClass(env, clazz);
    assert(clazzClazz != NULL);
    __android_log_write(ANDROID_LOG_DEBUG, "storeReferences", "Got AudioRecorder Object class reference");

    /* Get a reference to the ClassLoader class */
    jclass classLoaderClazz = (*env)->FindClass(env, "java/lang/ClassLoader");
    assert(classLoaderClazz != NULL);
    __android_log_write(ANDROID_LOG_DEBUG, "storeReferences", "Got ClassLoader reference");

    /* Get a reference to the getClassLoader method of ClassLoader */
    jmethodID getClassLoaderMethod = (*env)->GetMethodID(env, clazzClazz, "getClassLoader", "()Ljava/lang/ClassLoader;");
    assert(getClassLoaderMethod != NULL);
    __android_log_write(ANDROID_LOG_DEBUG, "storeReferences", "Got getClassLoaderMethod reference");

    /* Get a reference to the ClassLoader of AudioRecorder */
    jClassLoader = (*env)->NewGlobalRef(env, (*env)->CallObjectMethod(env, clazz, getClassLoaderMethod));
    assert(jClassLoader != NULL);
    __android_log_write(ANDROID_LOG_DEBUG, "storeReferences", "Got AudioRecorder ClassLoader reference");

    /* Get a reference to the FindClass method of ClassLoader */
    jFindClassMethod = (*env)->GetMethodID(env, classLoaderClazz, "findClass", "(Ljava/lang/String;)Ljava/lang/Class;");
    assert(jFindClassMethod != NULL);
    __android_log_write(ANDROID_LOG_DEBUG, "storeReferences", "Got FindClass method reference");

    return SL_RESULT_SUCCESS;
}


/* Create an audio engine interface */

void Java_com_stilt_stoytek_stilt_audiorec_AudioRecorder_createEngine(JNIEnv* env, jobject obj) {

    SLresult result;

    __android_log_write(ANDROID_LOG_DEBUG, "createEngine", "Created result variable.");

    /* Create an engine object interface */
    result = slCreateEngine(&engineObjItf, 0, NULL, 0, NULL, NULL);
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "createEngine", "Instantiated engineObjItf.");

    /* Realize the engine object interface */
    result = (*engineObjItf)->Realize(engineObjItf, SL_BOOLEAN_FALSE);
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "createEngine", "Realized engineObjItf.");

    /* Get an engine interface */
    result = (*engineObjItf)->GetInterface(engineObjItf, SL_IID_ENGINE, &engineItf);
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "createEngine", "Instantiated engineItf.");
}

/* Create an audio recorder interface */

jboolean Java_com_stilt_stoytek_stilt_audiorec_AudioRecorder_createAudioRecorder(JNIEnv* env, jobject obj, jint sampleRate) {
    /* TODO: Implement a conversion from 'jint samplerate' to SL_SAMPLINGRATE_xx */
    SLresult result;

    __android_log_write(ANDROID_LOG_DEBUG, "createAudioRecorder", "Entered createAudioRecorder");

    /* Audio source locator */
    SLDataLocator_IODevice loc_dev = {
            SL_DATALOCATOR_IODEVICE,
            SL_IODEVICE_AUDIOINPUT,
            SL_DEFAULTDEVICEID_AUDIOINPUT,
            NULL
    };

    /* Get a reference to the audio input source */
    SLDataSource audioSrc = {&loc_dev, NULL};

    /* Audio sink locator */
    SLDataLocator_AndroidSimpleBufferQueue loc_bq = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2};

    SLDataFormat_PCM format_pcm = {
            SL_DATAFORMAT_PCM,
            1,
            SL_SAMPLINGRATE_44_1,
            SL_PCMSAMPLEFORMAT_FIXED_16,
            SL_PCMSAMPLEFORMAT_FIXED_16,
            SL_SPEAKER_FRONT_CENTER,
            SL_BYTEORDER_LITTLEENDIAN
    };
    /* Get a reference to the audio sink */
    SLDataSink audioSnk = {&loc_bq, &format_pcm};

    const SLInterfaceID  id[1] = {SL_IID_ANDROIDSIMPLEBUFFERQUEUE};
    const SLboolean req[1] = {SL_BOOLEAN_TRUE};

    /* Create audiorecorder object interface */
    result = (*engineItf)->CreateAudioRecorder(
            engineItf,
            &recObjItf,
            &audioSrc,
            &audioSnk,
            1,
            id,
            req
    );
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "createAudioRecorder", "Created recObjItf, audioSrc and audioSnk");

    /* Realize the audiorecorder object interface */
    result = (*recObjItf)->Realize(recObjItf, SL_BOOLEAN_FALSE);
    if (result != SL_RESULT_SUCCESS) {
        return JNI_FALSE;
    } else {
        (void) result;
    }
    __android_log_write(ANDROID_LOG_DEBUG, "createAudioRecorder", "Realized recObjItf");

    /* Get an audiorecroder interface */
    result = (*recObjItf)->GetInterface(recObjItf, SL_IID_RECORD, &recItf);
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "createAudioRecorder", "Created recItf");

    /* Register the record buffer with the audiorecorder interface */
    result = (*recObjItf)->GetInterface(recObjItf, SL_IID_ANDROIDSIMPLEBUFFERQUEUE, &recBufq);
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "createAudioRecorder", "Created recBufq");

    /**
     * Register the callback function with the record buffer. This is called
     * whenever the buffer is full. In our case this is (hopefully) after 44100
     * samples or 1 second.
     * */
    result = (*recBufq)->RegisterCallback(recBufq, bqRecorderCallback, NULL);
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "createAudioRecorder", "Registered recBufq callback");

    return JNI_TRUE;
}

void Java_com_stilt_stoytek_stilt_audiorec_AudioRecorder_startRecord(JNIEnv* env, jobject obj) {
    SLresult result;

    /*
     * Try to aquire a lock on the audio engine. If it fails, simply return (no waiting
     * for it to become available).
     */
    if (pthread_mutex_trylock(&audioEngineLock)) return;

    /* Call convenience method to store environment references */
    result = storeReferences(env, obj);
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "startRecord", "Aquired lock on engine and jAudioRecorder");

    /* Stop any ongoing recording */
    result = (*recItf)->SetRecordState(recItf, SL_RECORDSTATE_STOPPED);
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "startRecord", "Force-stopped recording");

    /* Clear record buffer */
    result = (*recBufq)->Clear(recBufq);
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "startRecord", "Cleared recBufq");

    recSize = 0;

    /* Enqueue the (now cleared) record buffer */
    result = (*recBufq)->Enqueue(recBufq, recBuf, RECORD_BUF_SIZE*sizeof(short));
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "startRecord", "Enqueued recBuf");

    /* Start recording */
    result = (*recItf)->SetRecordState(recItf, SL_RECORDSTATE_RECORDING);
    SLASSERT(result);
    __android_log_write(ANDROID_LOG_DEBUG, "startRecord", "Recording started");
}

#ifdef __cplusplus
} /* Extern C */
#endif

