#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed

uchar red;
uchar green;
uchar blue;

uchar4 RS_KERNEL shadingEffect ( uchar4 in ) {

    in.r &= red;
    in.g &= green;
    in.b &= blue;

    return in;
}