package com.example.projettech.Model;

import android.graphics.Bitmap;
import android.graphics.Color;

public class AuxiliaryFunction {
    /**
     * Function to copy a bitmap image to another
     * @param old -> The image we want to copy
     * @param new_image -> The destination image
     */
    public static void copy_image(Bitmap old , Bitmap new_image){
        int[] pixels = new int[old.getHeight() * old.getWidth()];
        old.getPixels(pixels,0,old.getWidth(),0,0,old.getWidth(),old.getHeight());
        new_image.setPixels(pixels,0,new_image.getWidth(),0,0,new_image.getWidth(),new_image.getHeight());
    }

    /**
     * Function that separates an RGB color into three components (R,G and B)
     * @param rgb -> Color coded in RGB
     * @return an array that contains r, g and b separately
     */
    public static int[] RGBtoR_G_B(int rgb){
        int r = Color.red(rgb);
        int g = Color.green(rgb);
        int b = Color.blue(rgb);
        int[] r_g_b = new int[]{r, g, b};
        return  r_g_b;
    }

    /**
     * function that returns True if the hue we pass in parameter is close to the hue located, else False
     * @param reper the hue we spot
     * @param val the hue we want to compare
     * @param radius the margin that is accepted
     * @return
     */
    public static boolean Is_like(float reper , float val, int radius){
        final int RADIUS = radius ;
        float min_accept;
        float max_accept;
        max_accept = (reper + RADIUS)%360 ;
        min_accept = reper - RADIUS;
        if (min_accept < 0)
            min_accept = 360 + min_accept;

        return (( 0 < val && val < max_accept) || ( min_accept < val && val < 360 ));
    }



    /**
     * Function that calculates the histogram from an array that contains the pixels of the a gray image
     * @param pixels the table which contains the pixels of the image
     * @return the image histogram (table of 256 boxes)
     */
    public static int[] histogramme(int[] pixels){
        int[] histogramme = new int[256];
        for (int i = 0 ; i < pixels.length ; i++){
            int color = Color.red(pixels[i]);
            histogramme[color] +=1;
        }
        return histogramme ;
    }

    /**
     * Function that calculates the histogram from an array that contains the pixels of the a color image
     * in HSV mode (we take the biggest value between h, s and v)
     * @param pixels the table which contains the pixels of the image
     * @return the image histogram (table of 256 boxes)
     */
    public static int[] histogrammeHSV(int[] pixels){
        int[] histogramme = new int[256];
        int r, g, b, max;
        for (int i = 0; i < pixels.length; i++) {
            r = Color.red(pixels[i]);
            g = Color.green(pixels[i]);
            b = Color.blue(pixels[i]);
            max = r > g ? r : g;
            max = max > b ? max : b;
            histogramme[max]++;
        }
        return histogramme;
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
        int max = 0 ; int[] histo = new int[256];
        if (hsv){ histo = histogrammeHSV(pixels);}
        else { histo = histogramme(pixels);}
        for (int i = 0 ; i < 256 ; i++){
            if (histo[i] > 0){
                if (!min_t){
                    min = i ;
                    min_t = true ;
                }
                max = i ;
            }
        }
        int[] min_max = new int[2];
        min_max = new int[]{min, max};
        return min_max ;
    }

    /**
     * Function that calculates the min and max from a histogram
     * @param histo the histogram
     * @return an array of 2 boxes which contains the min and the max
     */
    public static int[] minmax(int[] histo){
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
     * @param minmax an array of 2 boxes which contains the min and the max
     * @param increase boolean to say if we want to increase or decrease the contrast of the image
     * @return the LUT table
     */
    public static int[] LUT_Init(int [] minmax, boolean increase) {
        int[] LUT = new int[256];
        for (int ng = 0; ng < 256; ng++){
            if (increase)
                LUT[ng] = (256 * (ng - minmax[0])) / (minmax[1] - minmax[0]);
            else
                LUT[ng] = ((ng * (minmax[1] - minmax[0])) / 255) + minmax[0];
        }
        return LUT;
    }
}
