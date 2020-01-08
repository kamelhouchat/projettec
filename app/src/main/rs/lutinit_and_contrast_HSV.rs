#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"
# include "colorize.rs"

#define ARRAY_SIZE 256

int2 MinAndMax ;
uchar LutTable[ARRAY_SIZE] ;

void RS_KERNEL FillLutTable_HSV(uchar in, uint32_t x){
    LutTable[x] = ((255 * (x - MinAndMax.x)) / (MinAndMax.y - MinAndMax.x));
}

uchar4 RS_KERNEL ApplyChanges_HSV(uchar4 in){
    uchar4 out ;
    uchar color ;
    float4 hsv = rgbTohsv( rsUnpackColor8888(in) );
    color = LutTable[(int) (hsv.s2 * 255)];
    hsv.s2 = color / 255.0f ;
    return hsvTorgb(hsv) ;
}

void ContrastPlusHSV(rs_allocation in, rs_allocation out){
    rs_allocation tmp_Allocation = rsCreateAllocation_uchar(256);
    rsForEach(FillLutTable_HSV, tmp_Allocation);
    rsForEach(ApplyChanges_HSV, in, out);
}
