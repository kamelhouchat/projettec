package com.projetTec.imageStudio.model.editingImage.basicFilters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;

import com.android.rssample.ScriptC_contours;
import com.android.rssample.ScriptC_convolution;


public class Convolution {

    private final Context context;

    public Convolution(Context context) {
        this.context = context;
    }

    /**
     * Allows to convolve the values of the image
     *
     * @param bmp    image to convolve
     * @param filter filter to apply on the image
     */
    @SuppressWarnings("unused")
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
    public void convolutions(Bitmap imageBitmap, int[][] filter) {
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
    private int somme(int[][] pixel) {
        int som = 0;
        @SuppressWarnings("unused") int n = pixel.length;
        for (int[] ints : pixel) {
            for (int anInt : ints) som += anInt;
        }
        return som;
    }

    private int somme(int[] pixel) {
        int som = 0;
        @SuppressWarnings("unused") int n = pixel.length;
        for (int anInt : pixel)
            som += anInt;
        return som;
    }

    /**
     * <p> This function allows you to apply the sobel filter to a gray image </p>
     * @param bmp image on which we want to apply the filter
     * @param gx  first filter to apply
     * @param gy  second filter to apply
     */
    public void contours(Bitmap bmp, int[][] gx, int[][] gy) {
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

    /**
     * <p> this function implement the convolution function in renderscript </p>
     * @param imageBitmap image on which we want to apply the filter
     * @param filters the filter to apply
     */
    public void convolutionAverageFilterRS(Bitmap imageBitmap, int[] filters){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap (rs, imageBitmap);
        Allocation output = Allocation.createTyped (rs, input.getType());
        ScriptC_convolution convolutionScript = new ScriptC_convolution(rs);

        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        convolutionScript.set_height(height);
        convolutionScript.set_width(width);
        convolutionScript.set_sum(somme(filters));
        int size = (int) Math.sqrt(filters.length);


        convolutionScript.set_sizeFilter(size/2);

        convolutionScript.set_pixels(input);

        Allocation filter_rs = Allocation.createSized(rs, Element.I32(rs),filters.length);
        filter_rs.copyFrom(filters);
        convolutionScript.bind_filter(filter_rs);

        convolutionScript.forEach_convolution(input, output);

        output.copyTo(imageBitmap) ;
        filter_rs.destroy();
        input.destroy(); output.destroy();
        convolutionScript.destroy(); rs.destroy();

    }

    /**
     * <p> this function implement the contour function in renderscript </p>
     * @param imageBitmap image on which we want to apply the filter
     * @param gx first filter to apply
     * @param gy second filter to apply
     */
    public void contoursFilterRS(Bitmap imageBitmap, int[] gx, int[] gy){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap (rs, imageBitmap);
        Allocation output = Allocation.createTyped (rs, input.getType());
        ScriptC_contours contourScript = new ScriptC_contours(rs);

        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        contourScript.set_height(height);
        contourScript.set_width(width);

        int size = (int) Math.sqrt(gx.length);
        contourScript.set_sizeFilter(size/2);
        contourScript.set_pixels(input);

        Allocation gxRs = Allocation.createSized(rs, Element.I32(rs),gx.length);
        gxRs.copyFrom(gx);
        contourScript.bind_filterX(gxRs);

        Allocation gyRs = Allocation.createSized(rs, Element.I32(rs),gy.length);
        gxRs.copyFrom(gy);
        contourScript.bind_filterY(gyRs);

        contourScript.forEach_contours(input, output);
        output.copyTo(imageBitmap);

        gxRs.destroy(); gyRs.destroy();
        input.destroy(); output.destroy();
        rs.destroy(); contourScript.destroy();
    }
}
