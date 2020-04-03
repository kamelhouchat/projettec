#pragma version (1)
#pragma rs java_package_name (com.android.rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"

rs_allocation pixels;
int *filter;
int width, height;
int sum, sizeFilter;

uchar4 RS_KERNEL convolution(uchar4 in, uint32_t x, uint32_t y) {
    if (x < sizeFilter || y < sizeFilter || x >= width - sizeFilter || y >= height - sizeFilter)
           return in;
    uchar4 out;
    int r = 0, g = 0, b = 0;
    int pos = 0;
    for(int u = -sizeFilter; u <= sizeFilter; ++u){
        for(int v = -sizeFilter; v <= sizeFilter; ++v){
            uchar4 color = rsGetElementAt_uchar4(pixels, x+v, y+u);
            r += (color.r * filter[pos]);
            g += (color.g * filter[pos]);
            b += (color.b * filter[pos]);
            ++pos;
        }
    }

    r /= sum;
    g /= sum;
    b /= sum;
    out.r = r;
    out.g = g;
    out.b = b;
    out.a = in.a; //keep original alpha
    return out ;
}