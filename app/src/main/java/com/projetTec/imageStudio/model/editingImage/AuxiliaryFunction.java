package com.projetTec.imageStudio.model.editingImage;

import android.graphics.Color;

class AuxiliaryFunction {

    /**
     * Function that separates an RGB color into three components (R,G and B)
     * @param rgb -> Color coded in RGB
     * @return an array that contains r, g and b separately
     */
    public static int[] RGBtoR_G_B(int rgb){
        int r = Color.red(rgb);
        int g = Color.green(rgb);
        int b = Color.blue(rgb);
        return new int[]{r, g, b};
    }

    /**
     * function that returns True if the hue we pass in parameter is close to the hue located, else False
     * @param graduation the hue we spot
     * @param val the hue we want to compare
     * @param radius the margin that is accepted
     * @return true if yes, else false
     */
    public static boolean Is_like(float graduation , float val, int radius){
        float min_accept;
        float max_accept;
        max_accept = (graduation + radius)%360 ;
        min_accept = graduation - radius;
        if (min_accept < 0)
            min_accept = 360 + min_accept;

        return (( 0 < val && val < max_accept) || ( min_accept < val && val < 360 ));
    }



    /**
     * Function that calculates the histogram from an array that contains the pixels of the a gray image
     * @param pixels the table which contains the pixels of the image
     * @return the image histogram (table of 256 boxes)
     */
    public static int[] histogram(int[] pixels){
        int[] histogram = new int[256];
        for (int pixel : pixels) {
            int color = Color.red(pixel);
            histogram[color] += 1;
        }
        return histogram ;
    }

    /**
     * Function that calculates the histogram from an array that contains the pixels of the a color image
     * in HSV mode (we take the biggest value between h, s and v)
     * @param pixels the table which contains the pixels of the image
     * @return the image histogram (table of 256 boxes)
     */
    private static int[] histogramHSV(int[] pixels){
        int[] histogram = new int[256];
        int r, g, b, max;
        for (int pixel : pixels) {
            r = Color.red(pixel);
            g = Color.green(pixel);
            b = Color.blue(pixel);
            max = Math.max(r, g);
            max = Math.max(max, b);
            histogram[max]++;
        }
        return histogram;
    }

    /**
     * Function that calculates the min and max of a histogram of an image (in color or black and white)
     * from its pixel array
     * @param pixels the table which contains the pixels of the image
     * @param hsv boolean to say if we want the histogram in hsv or in gray
     * @return an array of 2 boxes which contains the min and the max
     */
    public static int[] min_max_histo(int [] pixels, boolean hsv){
        int min = 0 ; boolean min_t = false ;
        int max = 0 ; int[] histo;
        if (hsv){ histo = histogramHSV(pixels);}
        else { histo = histogram(pixels);}
        for (int i = 0 ; i < 256 ; i++){
            if (histo[i] > 0){
                if (!min_t){
                    min = i ;
                    min_t = true ;
                }
                max = i ;
            }
        }
        int[] min_max;
        min_max = new int[]{min, max};
        return min_max ;
    }

    /**
     * Function that calculates the min and max from a histogram
     * @param histo the histogram
     * @return an array of 2 boxes which contains the min and the max
     */
    public static int[] minMax(int[] histo){
        int min=0 , max=0 ; boolean min_t = false ;
        for (int i = 0 ; i < 256; i++){
            if (histo[i] > 0){
                if (!min_t){
                    min = i ;
                    min_t = true ;
                }
                max = i ;
            }
        }
        return new int[]{min, max};
    }

    /**
     * Function which initializes the LUT table using the current view formula
     * and the reverse formula for decrement
     * @param minMax an array of 2 boxes which contains the min and the max
     * @param increase boolean to say if we want to increase or decrease the contrast of the image
     * @return the LUT table
     */
    public static int[] LUT_Init(int [] minMax, boolean increase) {
        int[] LUT = new int[256];
        for (int ng = 0; ng < 256; ng++){
            if (increase)
                LUT[ng] = (256 * (ng - minMax[0])) / (minMax[1] - minMax[0]);
            else
                LUT[ng] = ((ng * (minMax[1] - minMax[0])) / 255) + minMax[0];
        }
        return LUT;
    }
}
