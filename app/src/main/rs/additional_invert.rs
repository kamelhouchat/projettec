#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed

#define MAX_VALUE 255

uchar4 RS_KERNEL invertEffect ( uchar4 in ) {

    in.r = MAX_VALUE - in.r;
    in.g = MAX_VALUE - in.g;
    in.b = MAX_VALUE - in.b;
    return in;

}
