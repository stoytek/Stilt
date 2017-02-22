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

/* Native audio headers */
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

/* Native asset manager */
#include <sys/types.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>


/* Engine interfaces */
static SLObjectItf engineObjItf = NULL;
static SLEngineItf engineItf;

/* Mutex for locking audioengine during recording */

static pthread_mutex_t audioEngineLock = PTHREAD_MUTEX_INITIALIZER;

/* Audio recorder interfaces */

static SLObjectItf recObjItf = NULL;
static SLRecordItf recItf;
static SLAndroidSimpleBufferQueueItf recBufq;

/* Buffer size 1s @ 44.1 kHz */
#define RECORD_BUF_SIZE (44100)

static short recBuf[RECORD_BUF_SIZE];
static unsigned recSize = 0;


/* Callback called each time a buffer finishes recording */
/* TODO: See if anything else needs to be done is this method */

void bqRecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context) {
    assert(bq == recorderBufferQueue);
    assert(context == NULL);
    /* Stop the recording after filling the buffer */
    SLresult result = (*recItf)->SetRecordState(recItf, SL_RECORDSTATE_STOPPED);
    if (result == SL_RESULT_SUCCESS) {
        recorderSize = RECORD_BUF_SIZE * sizeof(short);
    }
    pthread_mutex_unlock(&audioEngineLock);
}

/* Create an audio engine interface */

void Java_com_stilt_stoytek_stilt_MainActivity_createEngine(JNIEnv* env, jobject obj) {
    SLresult result;

    result = slCreateEngine(&engineObjItf, 0, NULL, 0, NULL, NULL);
    SLASSERT(result);

    result = (*engineObjItf)->Realize(engineObjItf, SL_BOOLEAN_FALSE);
    SLASSERT(result);

    result = (*engineObjItf)->GetInterface(engineObjItf, SL_IID_ENGINE, &engineItf);
    SLASSERT(result);
}

/* Create an audio recorder interface */

jboolean Java_com_stilt_stoytek_stilt_MainActivity_createAudioRecorder(JNIEnv* env, jobject obj, jint sampleRate) {
    /* TODO: Implement a conversion from 'jint samplerate' to SL_SAMPLINGRATE_xx */
    SLresult result;

    /* Audio source */
    SLDataLocator_IODevice loc_dev = {
            SL_DATALOCATOR_IODEVICE,
            SL_IODEVICE_AUDIOINPUT,
            SL_DEFAULTDEVICEID_AUDIOINPUT,
            NULL
    };

    SLDataSource audioSrc = {&loc_dev, NULL};

    /* Audio sink */

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

    SLDataSink audioSnk = {&loc_bq, &format_pcm};

    const SLInterfaceID  id[1] = {SL_IID_ANDROIDSIMPLEBUFFERQUEUE};
    const SLboolean req[1] = {SL_BOOLEAN_TRUE};

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

    result = (*recObjItf)->Realize(recObjItf, SL_BOOLEAN_FALSE);
    if (result != SL_RESULT_SUCCESS) {
        return JNI_FALSE;
    } else {
        (void) result;
    }

    result = (*recObjItf)->GetInterface(recObjItf, SL_IID_RECORD, &recItf);
    SLASSERT(result);

    result = (*recObjItf)->GetInterface(recObjItf, SL_IID_ANDROIDSIMPLEBUFFERQUEUE, &recBufq);
    SLASSERT(result);

    result = (*recObjItf)->RegisterCallback(recBufq, bqRecorderCallback, NULL);
    assert(result);

    return JNI_TRUE;
}

#ifdef __cplusplus
} /* Extern C */
#endif
