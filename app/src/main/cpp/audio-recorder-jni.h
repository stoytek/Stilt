//
// Created by Frode Jacobsen on 22/02/17.
//

#ifndef STILT_AUDIO_RECORDER_JNI_H
#define STILT_AUDIO_RECORDER_JNI_H

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

#define SLASSERT(x)   do {\
    assert(SL_RESULT_SUCCESS == (x));\
    (void) (x);\
    } while (0)

#endif //STILT_AUDIO_RECORDER_JNI_H
