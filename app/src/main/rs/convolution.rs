#pragma version (1)
#pragma rs java_package_name (com.android.rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"

int *pixels;
int *filter;
int width, height;
int sum, sizeFilter;

int static red(int color){
    return (color >> 16) & 0xFF;
}

int static green(int color){
    return (color >> 8) & 0xFF;
}

int static blue(int color){
    return color & 0xFF;
}

uchar4 RS_KERNEL convolution(uchar4 in, uint32_t x, uint32_t y) {
    //float4 pixelf = rsUnpackColor8888(in) ;
    if (x < sizeFilter || y < sizeFilter || x >= width - sizeFilter || y >= height - sizeFilter)
           return in;
    uchar4 out;
    int r = 0, g = 0, b = 0;
    int pos = 0;
    for(int u = -sizeFilter; u < sizeFilter; u++){
        for(int v = -sizeFilter; v < sizeFilter; v++){
            int color = pixels[((y + u) * width + (x + v))];
            r = r + (red(color) * filter[pos]);
            g = g + (green(color) * filter[pos]);
            b = b + (blue(color) * filter[pos]);
            pos++;
        }
    }

    r = r / sum;    g = g / sum;    b = b / sum;
    out.r = r;  out.g = g;  out.b = b;  out.a = in.a;
    return out ;
}