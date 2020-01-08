#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"

#define ARRAY_SIZE 256

int LutTable[ARRAY_SIZE] ;

uchar4 RS_KERNEL ApplyChanges_Equalize_Gray(uchar4 in){
    uchar4 out ;
    out.r = LutTable[in.r] ;
    out.g = out.r;
    out.b = out.r;
    out.a = in.a ;
    return out ;
}
