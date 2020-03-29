#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed

#define MAX_VALUE 255

uchar4 RS_KERNEL noiseEffect ( uchar4 in ) {
    uchar4 randColor;
    randColor.r = rsRand(MAX_VALUE);
    randColor.g = rsRand(MAX_VALUE);
    randColor.b = rsRand(MAX_VALUE);
    randColor.a = 1;
    in |= randColor ;
    return in;
}
