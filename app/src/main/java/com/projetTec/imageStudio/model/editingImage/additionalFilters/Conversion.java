package com.projetTec.imageStudio.model.editingImage.additionalFilters;

import android.graphics.Color;

/**
 * <p>Class which contains static methods of conversion between different color spaces used by the class which contains additional filters.
 *
 * @author Kamel.H
 */

public class Conversion {

    /**
     * <p>static method that converts from rgb color space to YUV.
     *
     * @param color Color (in RGB).
     * @return An array of float that contains the three values Y, U and V.
     * @source https://fr.wikipedia.org/wiki/YUV
     */
    public static float[] rgbToYuv(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        float[] yuv = new float[3];
        yuv[0] = (float) (0.299 * r + 0.587 * g + 0.114 * b);
        yuv[1] = (float) (-0.14713 * r - 0.28886 * g + 0.436 * b);
        yuv[2] = (float) (0.615 * r - 0.51498 * g - 0.10001 * b);

        return yuv;
    }

    /**
     * <p>static method that converts from YUV space to rgb.
     *
     * @param yuv A not null array of float that contains the three values Y, U and V.
     * @return The Result color in rgb.
     * @source https://fr.wikipedia.org/wiki/YUV
     */
    public static int yuvToRgb(float[] yuv) {
        int r = (int) (yuv[0] + 1.13983 * yuv[2]);
        int g = (int) (yuv[0] - 0.39465 * yuv[1] - 0.58060 * yuv[2]);
        int b = (int) (yuv[0] + 2.03211 * yuv[1]);

        return Color.rgb(r, g, b);
    }

}
