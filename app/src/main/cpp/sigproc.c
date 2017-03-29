#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <sigproc.h>

double getdb(double* x, int N)
{
    /* This is the main sigproc function. 
     * Takes an audio signal (and length N),
     * filters it with the hard coded impulse from 'impulse.c'
     * and returns a relative value */

    // First, filter the input signal
        // From 'impulse.c':
        // - M is the filter length
        // - h is the impulse response 
    double* y = conv(x, impulse, N, ir_length);           

    // Second, find and return the dB value (here using RMS!)
    // Replace this with Leq later. 
    double rms = 0;
    for(int n = 0; n < N; n++)
    {
        rms += y[n]*y[n];
    }
    rms /= N;
    rms = sqrt(rms);
    
    // TODO: Calculate Leq OR just the dB SPL (decide)

    free(y);
    return 20*log10(mic_offset * rms / 0.00002); // Returns the dB SPL value
}



double* conv(double* x, double* h,int N, int M)
{
    // This function allocates memory! Be careful!
    // x = signal of length N (this is also the length of the output!)
    // h = filter of length M
    double* output = (double*) malloc(N*sizeof(double));
    for(int n = 0; n < N; n++)
    {
        output[n] = 0;
        for(int m = 0; m < M && m <= n; m++)
        {
           output[n] += h[m]*x[n - m];
        }
    }
    return output;
}
