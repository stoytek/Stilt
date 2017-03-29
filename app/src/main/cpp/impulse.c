#include "sigproc.h"


double mic_offset = 3.2498;
int ir_length = 41;
/*  The impulse response is based on the following: 
     A-weighing
     Relationship between frequency responses between the phone and 
     Scaled with reference mic, 94 dB
    The idea is that we have an 
*/
double impulse[] = {
    0.255720,
    0.516323,
    0.237158,
    -0.080659,
    -0.112334,
    -0.104892,
    -0.094411,
    -0.084629,
    -0.075798,
    -0.067853,
    -0.060707,
    -0.054281,
    -0.048502,
    -0.043306,
    -0.038634,
    -0.034434,
    -0.030659,
    -0.027267,
    -0.024218,
    -0.021479,
    -0.019018,
    -0.016809,
    -0.014824,
    -0.013043,
    -0.011444,
    -0.010010,
    -0.008723,
    -0.007570,
    -0.006536,
    -0.005609,
    -0.004780,
    -0.004037,
    -0.003373,
    -0.002779,
    -0.002249,
    -0.001776,
    -0.001353,
    -0.000977,
    -0.000642,
    -0.000344,
    -0.000080
};
