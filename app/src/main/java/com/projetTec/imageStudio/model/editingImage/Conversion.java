package com.projetTec.imageStudio.model.editingImage;

import android.graphics.Color;

public class Conversion {
    /**
     * Function that converts from RGB to HSV
     * @param redInput the red value
     * @param greenInput the green value
     * @param blueInput the blue value
     * @param hsv a floating array of 3 empty boxes
     */
    public static void RGBToHSV_new(int redInput , int greenInput , int blueInput , float[] hsv){
        float cMax, cMin, delta, saturation, value, hue = 0;
        float red = (float) (redInput / 255.0);
        float green = (float) (greenInput / 255.0);
        float blue = (float) (blueInput / 255.0);
        cMin = Math.min(Math.min(red,green),blue);
        cMax = Math.max(Math.max(red,green),blue);
        delta = cMax - cMin;
        if (cMax == 0) {
            for (int i = 0; i < 3; i++) {
                hsv[i] = 0;
            }
            return;
        }
        else if (cMax == red)
            hue = (float) (60.0 * (((green - blue) / delta)));
        else if (cMax == green)
            hue = (float) (60.0 * (((blue - red) / delta) + 2));
        else if (cMax == blue)
            hue = (float) (60.0 * (((red - green) / delta) + 4));
        hsv[0] = hue;
        saturation = delta / cMax;
        value = (cMax);
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
        return Color.rgb((int)r,(int)g,(int)b);
    }
}
