#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"

#define ARRAY_SIZE 256

int2 MinAndMax ;
uchar LutTable[ARRAY_SIZE] ;

void RS_KERNEL FillLutTable_Plus(uchar in, uint32_t x){
    LutTable[x] = ((255 * (x - MinAndMax.x)) / (MinAndMax.y - MinAndMax.x));
}

void RS_KERNEL FillLutTable_Fewer(uchar in, uint32_t x){
    LutTable[x] = ((x * (MinAndMax.y - MinAndMax.x)) / 255) + MinAndMax.x;
}

uchar4 RS_KERNEL ApplyChanges_Gray(uchar4 in){
    uchar4 out ;
    out.r = LutTable[in.r];
    out.g = out.r ;
    out.b = out.r ;
    out.a = in.a;
    return out ;
}

void ContrastPlusGray(rs_allocation in, rs_allocation out){
    rs_allocation tmp_Allocation = rsCreateAllocation_uchar(256);
    rsForEach(FillLutTable_Plus, tmp_Allocation);
    rsForEach(ApplyChanges_Gray, in, out);
}

void ContrastFewerGray(rs_allocation in, rs_allocation out){
    rs_allocation tmp_Allocation = rsCreateAllocation_uchar(256);
    rsForEach(FillLutTable_Fewer, tmp_Allocation);
    rsForEach(ApplyChanges_Gray, in, out);
}