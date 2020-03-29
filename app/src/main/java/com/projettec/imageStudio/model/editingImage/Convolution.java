package com.projettec.imageStudio.model.editingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
//import com.projettec.imageStudio.model.editingImage.Filter;

public class Convolution {

    private Bitmap imagebitmap;
    private final Context context;

    public Convolution(Bitmap imagebitmap, Context context) {
        this.imagebitmap = imagebitmap;
        this.context = context;
    }

    public Bitmap getImagebitmap() {
        return imagebitmap;
    }
    public void setImagebitmap(Bitmap imagebitmap) {
        this.imagebitmap = imagebitmap;
    }

    /**
     * Allows to convolve the values of the image
     *
     * @param bmp    image to convolve
     * @param filter filter to apply on the image
     */
    public void convolution(Bitmap bmp, int[][] filter) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixel = new int[width * height];
        int red, green, bleu;
        int sizeFilter = filter.length;
        int n = sizeFilter / 2;

        bmp.getPixels(pixel, 0, width, 0, 0, width, height);
        int som = somme(filter);
        for (int y = n; y < height - n; y++) {
            for (int x = n; x < width - n; x++) {
                red = green = bleu = 0;
                for (int u = -n; u <= n; u++) {
                    for (int v = -n; v <= n; v++) {
                        int color = bmp.getPixel(x + u, y + v);
                        int r = Color.red(color);
                        int g = Color.green(color);
                        int b = Color.blue(color);

                        red = red + (r * filter[u + n][v + n]);
                        green = green + (g * filter[u + n][v + n]);
                        bleu = bleu + (b * filter[u + n][v + n]);
                    }
                }
                bmp.setPixel(x, y, Color.rgb(red / som, green / som, bleu / som));
            }
        }

    }

    /**
     * Permet de convoluer les valeurs de l'image
     *
     * @param bmp    image à convoluer
     * @param filter filtre à appliquer sur l'image
     */
    public static void convolutions(Bitmap bmp, int[][] filter) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixel = new int[width * height];
        int red, green, bleu;
        int sizeFilter = filter.length;
        int n = sizeFilter / 2;

        int som = somme(filter);

        bmp.getPixels(pixel, 0, width, 0, 0, width, height);
        for (int y = n; y < height - n; y++) {
            for (int x = n; x < width - n; x++) {
                red = green = bleu = 0;
                for (int u = -n; u <= n; u++) {
                    for (int v = -n; v <= n; v++) {
                        int color = pixel[((y + u) * width + (x + v))];
                        int r = Color.red(color);
                        int g = Color.green(color);
                        int b = Color.blue(color);

                        red = red + (r * filter[u + n][v + n]);
                        green = green + (g * filter[u + n][v + n]);
                        bleu = bleu + (b * filter[u + n][v + n]);
                    }
                }
                pixel[(width * y) + x] = Color.rgb(red / som, green / som, bleu / som);
            }
        }
        bmp.setPixels(pixel, 0, width, 0, 0, width, height);
    }

    /**
     * <p>This function calculates the weighted average of a filter
     *
     * @param pixel the table containing the weighted average
     * @return sum of the values in the array
     */
    private static int somme(int[][] pixel) {
        int som = 0;
        int n = pixel.length;
        for (int[] ints : pixel) {
            for (int anInt : ints) som += anInt;
        }
        return som;
    }


    /**
     *
     * @param bmp
     * @param gx
     * @param gy
     */
    public static void contours(Bitmap bmp, int[][] gx, int[][] gy) {
        //Filter filter = new Filter(bmp,context);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] newpixels = new int[width * height];
        int grayX, grayY;
        int sizeGx = gx.length;

        int n = sizeGx / 2;

        bmp.getPixels(pixels,0, width , 0, 0,width ,height);

        for (int y = n; y < height - n; y++) {
            for (int x = n; x < width - n; x++) {
                grayX = 0;
                grayY = 0;
                for (int u = -n; u <= n; u++) {
                    for (int v = -n; v <= n; v++) {
                        int color = pixels[((y + u) * width + (x + v))];
                        int g = Color.red(color);

                        grayX = grayX + (g * gx[u + n][v + n]);
                        grayY = grayY + (g * gy[u + n][v + n]);
                    }
                }
                int modGrad = (int) Math.sqrt((grayX * grayX) + (grayY * grayY));
                if (modGrad > 255) modGrad = 255;

                newpixels[(width * y) + x ] = Color.rgb(modGrad, modGrad, modGrad);
            }
        }
        bmp.setPixels(newpixels,0, width , 0, 0,width ,height);
    }

    /*--------------------------------------RenderScript---------------------------------------*/

    /*public void ConvulveAverageFilterRS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(this);
        ScriptC_average_convulve ConvulveScript = new ScriptC_average_convulve(rs);

        int width = imagebitmap.getWidth();
        int height = imagebitmap.getHeight();
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);

        Allocation pixels_rs = Allocation.createSized(rs, Element.I32(rs),pixels.length);
        pixels_rs.copyFrom(pixels);
        ConvulveScript.bind_pixels(pixels_rs);

    }*/

}
