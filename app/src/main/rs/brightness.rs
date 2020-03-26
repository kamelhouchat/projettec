#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed

#include "rs_debug.rsh"
#include "colorize.rs"

float new_value ;

uchar4 RS_KERNEL Brightness ( uchar4 in ) {

    float4 h = rgbTohsv( rsUnpackColor8888(in) );
    h.s2 = new_value ;
    if (h.s2 < 0.0f) {
        h.s2 = 0.0f;
    } else if (h.s2 > 1.0f) {
        h.s2 = 1.0f;
    }
    uchar4 out = hsvTorgb(h);
    return out ;

}