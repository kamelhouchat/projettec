package com.projettec.imageStudio.Model;

import android.graphics.Color;

public class Conversion {
    /**
     * Function that converts from RGB to HSV
     * @param redi the red value
     * @param greeni the green value
     * @param bluei the blue value
     * @param hsv a floating array of 3 empty boxes
     */
    public static void RGBToHSV_new(int redi , int greeni , int bluei , float[] hsv){
        float cmax = 0, cmin = 0, delta, saturation, value, hue = 0;
        float red = (float) (redi / 255.0);
        float green = (float) (greeni / 255.0);
        float blue = (float) (bluei / 255.0);
        cmin = Math.min(Math.min(red,green),blue);
        cmax = Math.max(Math.max(red,green),blue);
        delta = cmax - cmin;
        if (cmax == 0) {
            for (int i = 0; i < 3; i++) {
                hsv[i] = 0;
            }
            return;
        }
        else if (cmax == red)
            hue = (float) (60.0 * (((green - blue) / delta)));
        else if (cmax == green)
            hue = (float) (60.0 * (((blue - red) / delta) + 2));
        else if (cmax == blue)
            hue = (float) (60.0 * (((red - green) / delta) + 4));
        hsv[0] = hue;
        saturation = delta / cmax;
        value = (cmax);
        hsv[1] = saturation;
        hsv[2] = value;
    }

    /**
     * Function that converts from HSV to RGB
     * @param hsvColor An array of float that contains the three HSV values of the color
     * @return Color in RGB
     */
    public static int HSVToColor_new(float[] hsvColor){
        float r, g, b, k;
        k = (float) (5 + (hsvColor[0] / 60.0)) % 6;
        r = (int) ((hsvColor[2] - hsvColor[2] * hsvColor[1] * Math.max(Math.min(Math.min(k, 4 - k), 1), 0)) * 255.0);
        k = (float) (3 + (hsvColor[0] / 60.0)) % 6;
        g = (int) ((hsvColor[2] - hsvColor[2] * hsvColor[1] * Math.max(Math.min(Math.min(k, 4 - k), 1), 0)) * 255.0);
        k = (float) (1 + (hsvColor[0] / 60.0)) % 6;
        b = (int) ((hsvColor[2] - hsvColor[2] * hsvColor[1] * Math.max(Math.min(Math.min(k, 4 - k), 1), 0)) * 255.0);
        int color  = Color.rgb((int)r,(int)g,(int)b);
        return color;
    }
}
