#ifndef __SIGPROC_H
#define __SIGPROC_H

double getdb(double*, int);
double* conv(double*, double*, int, int);

extern int ir_length;
extern double impulse[];
extern double mic_offset;

#endif