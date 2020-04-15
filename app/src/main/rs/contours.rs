#pragma version (1)
#pragma rs java_package_name (com.android.rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"

rs_allocation pixels;
int *filterX, *filterY;
int width, height;
int sizeFilter;

uchar4 RS_KERNEL contours(uchar4 in, uint32_t x, uint32_t y) {
    if (x < sizeFilter || y < sizeFilter || x >= width - sizeFilter || y >= height - sizeFilter)
           return in;
    uchar4 out;

    int grayX = 0, grayY = 0;
    int pos = 0;
    for(int u = -sizeFilter; u <= sizeFilter; ++u){
       for(int v = -sizeFilter; v <= sizeFilter; ++v){
             uchar4 color = rsGetElementAt_uchar4(pixels, x+v, y+u);
            grayX += (color.r * filterX[pos]);
            grayY += (color.r * filterY[pos]);
            pos++;
        }
    }

    float carre = (grayX * grayX) + (grayY * grayY);
    int modR = (int) sqrt(carre);
    if (modR > 255)
        modR = 255;

    out.r = modR;
    out.g = modR;
    out.b = modR;
    out.a = in.a; //keep original alpha
    return out;
}