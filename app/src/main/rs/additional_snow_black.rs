#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed

#define MAX_VALUE 255
#define MIN_VALUE 0

bool isSnow;

uchar4 RS_KERNEL snowBlackEffect ( uchar4 in ) {
    int random = rsRand(MAX_VALUE);
    if (in.r > random && in.g > random && in.b > random) {
        if (isSnow) {
            in.r = MAX_VALUE;
            in.g = MAX_VALUE;
            in.b = MAX_VALUE;
        }
        else if (!isSnow) {
            in.r = MIN_VALUE;
            in.g = MIN_VALUE;
            in.b = MIN_VALUE;
        }
    }
    return in;
}
