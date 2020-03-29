package com.projetTec.imageStudio.model.editingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;


public class Convolution {

    private Bitmap imageBitmap;
    private final Context context;

    public Convolution(Bitmap imageBitmap, Context context) {
        this.imageBitmap = imageBitmap;
        this.context = context;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
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
        int red, green, blue;
        int sizeFilter = filter.length;
        int n = sizeFilter / 2;

        bmp.getPixels(pixel, 0, width, 0, 0, width, height);
        int som = somme(filter);
        for (int y = n; y < height - n; y++) {
            for (int x = n; x < width - n; x++) {
                red = green = blue = 0;
                for (int u = -n; u <= n; u++) {
                    for (int v = -n; v <= n; v++) {
                        int color = bmp.getPixel(x + u, y + v);
                        int r = Color.red(color);
                        int g = Color.green(color);
                        int b = Color.blue(color);

                        red = red + (r * filter[u + n][v + n]);
                        green = green + (g * filter[u + n][v + n]);
                        blue = blue + (b * filter[u + n][v + n]);
                    }
                }
                bmp.setPixel(x, y, Color.rgb(red / som, green / som, blue / som));
            }
        }

    }

    /**
     * <p>Static method which allows to convolve the values of the image.
     *
     * @param imageBitmap Image to convolve
     * @param filter      Filter to apply on the image
     */
    public static void convolutions(Bitmap imageBitmap, int[][] filter) {
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();
        int[] pixel = new int[width * height];
        int red, green, blue;
        int sizeFilter = filter.length;
        int n = sizeFilter / 2;

        int som = somme(filter);

        imageBitmap.getPixels(pixel, 0, width, 0, 0, width, height);
        for (int y = n; y < height - n; y++) {
            for (int x = n; x < width - n; x++) {
                red = green = blue = 0;
                for (int u = -n; u <= n; u++) {
                    for (int v = -n; v <= n; v++) {
                        int color = pixel[((y + u) * width + (x + v))];
                        int r = Color.red(color);
                        int g = Color.green(color);
                        int b = Color.blue(color);

                        red = red + (r * filter[u + n][v + n]);
                        green = green + (g * filter[u + n][v + n]);
                        blue = blue + (b * filter[u + n][v + n]);
                    }
                }
                pixel[(width * y) + x] = Color.rgb(red / som, green / som, blue / som);
            }
        }
        imageBitmap.setPixels(pixel, 0, width, 0, 0, width, height);
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
        int[] newPixels = new int[width * height];
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

                newPixels[(width * y) + x ] = Color.rgb(modGrad, modGrad, modGrad);
            }
        }
        bmp.setPixels(newPixels,0, width , 0, 0,width ,height);
    }

    /*--------------------------------------RenderScript---------------------------------------*/

    /*public void ConvulveAverageFilterRS(Bitmap imageBitmap){
        RenderScript rs = RenderScript.create(this);
        ScriptC_average_convulve ConvulveScript = new ScriptC_average_convulve(rs);

        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();
        int[] pixels = new int[height*width];
        imageBitmap.getPixels(pixels,0,width,0,0,width,height);

        Allocation pixels_rs = Allocation.createSized(rs, Element.I32(rs),pixels.length);
        pixels_rs.copyFrom(pixels);
        ConvulveScript.bind_pixels(pixels_rs);

    }*/

}
