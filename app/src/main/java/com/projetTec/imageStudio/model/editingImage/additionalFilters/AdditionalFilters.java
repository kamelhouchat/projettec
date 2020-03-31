package com.projetTec.imageStudio.model.editingImage.additionalFilters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;
import androidx.renderscript.Short4;

import com.android.rssample.ScriptC_additional_invert;
import com.android.rssample.ScriptC_additional_noise;
import com.android.rssample.ScriptC_additional_shading;
import com.android.rssample.ScriptC_additional_snow_black;
import com.projetTec.imageStudio.model.editingImage.Equalization;

import java.util.Random;

/**
 * <p>
 * class which contains additional filters which will be applied to images
 * </p>
 *
 * @author Kamel.H
 * @see com.projetTec.imageStudio.controller.adapters.FilterRecyclerViewAdapter
 * @see com.projetTec.imageStudio.model.filters.FilterType
 * @see com.projetTec.imageStudio.model.filters.FilterModel
 */

public class AdditionalFilters {

    private Bitmap imageBitmap;

    //The application context
    private final Context context;

    //Static values
    public static final int COLOR_MIN = 0;
    public static final int COLOR_MAX = 255;
    private static final String TAG = "AdditionalFilters";

    public AdditionalFilters(Bitmap imageBitmap, Context context) {
        this.imageBitmap = imageBitmap;
        this.context = context;
    }

    /**
     * <p>
     * method which allows to apply a snow and a black effect to the image passed in parameter
     * (Using JAVA)
     * </p>
     *
     * @param imageBitmap A Bitmap image
     * @see com.projetTec.imageStudio.model.filters.FilterType
     */
    @SuppressWarnings("ConstantConditions")
    public void snowAndBlackEffect(Bitmap imageBitmap, boolean isSnow) {
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        int[] pixels = new int[width * height];
        imageBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        //Random object
        Random random = new Random();
        for (int i = 0; i < height * width - 1; i++) {
            int R = Color.red(pixels[i]);
            int G = Color.green(pixels[i]);
            int B = Color.blue(pixels[i]);

            int randomValue = random.nextInt(COLOR_MAX);
            if (R > randomValue && G > randomValue && B > randomValue) {
                if (isSnow)
                    pixels[i] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX);
                else if (!isSnow)
                    pixels[i] = Color.rgb(COLOR_MIN, COLOR_MIN, COLOR_MIN);
            }
        }
        imageBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     * <p>
     * method which allows to apply a noise effect to the image passed in parameter
     * (Using JAVA)
     * </p>
     *
     * @param imageBitmap A Bitmap image
     * @see com.projetTec.imageStudio.model.filters.FilterType
     */
    public void noiseEffect(Bitmap imageBitmap) {
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        int[] pixels = new int[width * height];
        imageBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        //Random object
        Random random = new Random();
        for (int i = 0; i < height * width - 1; i++) {
            int randColor = Color.rgb(random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX));
            pixels[i] |= randColor;
        }
        imageBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     * <p>
     * Method which boosts one of the three pixel channels of the image passed in parameter
     * (Using JAVA)
     * </p>
     *
     * @param imageBitmap A bitmap image
     * @param type        The canal to boost (1 -> Red | 2 -> Green | 3 -> Blue)
     * @param percent     The percentage of the change
     * @see com.projetTec.imageStudio.model.filters.FilterType
     */
    public void boostCanals(Bitmap imageBitmap, int type, float percent) {
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        int[] pixels = new int[width * height];
        imageBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < height * width - 1; i++) {
            int R = Color.red(pixels[i]);
            int G = Color.green(pixels[i]);
            int B = Color.blue(pixels[i]);
            if (type == 1) {
                R = (int) (R * (1 + percent));
                if (R > 255) R = 255;
            } else if (type == 2) {
                G = (int) (G * (1 + percent));
                if (G > 255) G = 255;
            } else if (type == 3) {
                B = (int) (B * (1 + percent));
                if (B > 255) B = 255;
            }
            pixels[i] = Color.rgb(R, G, B);
        }
        imageBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     * <p>
     * Method which allows to apply a invert effect to the image passed in parameter
     * (Using JAVA)
     * </p>
     *
     * @param imageBitmap A Bitmap image
     * @see com.projetTec.imageStudio.model.filters.FilterType
     */
    public void invertEffect(Bitmap imageBitmap) {
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        int[] pixels = new int[width * height];
        imageBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < height * width - 1; i++) {

            int R = 255 - Color.red(pixels[i]);
            int G = 255 - Color.green(pixels[i]);
            int B = 255 - Color.blue(pixels[i]);

            pixels[i] = Color.rgb(R, G, B);

        }
        imageBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     * <p>
     * Method which allows to apply a shading effect to the image passed in parameter
     * (Using JAVA)
     * </p>
     *
     * @param imageBitmap  A Bitmap image
     * @param shadingColor Shading color (RGB)
     * @return The result in Bitmap
     * @see com.projetTec.imageStudio.model.filters.FilterType
     */
    public Bitmap shadingFilter(Bitmap imageBitmap, int shadingColor) {
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        int[] pixels = new int[width * height];
        imageBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < height * width - 1; i++) {
            pixels[i] &= shadingColor;
        }
        Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        returnBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return returnBitmap;
    }

    /**
     * <p>
     * Method which makes it possible to apply a histogram equalization to the Y value of a pixel
     * after conversion into YUV and to convert the image back to rgb.
     * (Using JAVA)
     * </p>
     *
     * @param imageBitmap A bitmap image.
     * @see Conversion
     * @see AuxiliaryFunction
     */
    public void equalizationYuvY(Bitmap imageBitmap) {
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        int accumulator = 0;
        int[] pixels = new int[height * width];
        imageBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] histo = AuxiliaryFunction.histogramYuv(pixels);
        int[] LUT = new int[256];
        for (int i = 0; i < 256; i++) {
            accumulator += histo[i];
            LUT[i] = ((accumulator * 255) / ((width * height)));
        }
        for (int i = 0; i < height * width; i++) {
            float[] yuv = Conversion.rgbToYuv(pixels[i]);
            yuv[0] = LUT[(int) yuv[0]];
            pixels[i] = Conversion.yuvToRgb(yuv);
        }
        imageBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     * <p>
     * Method that applies RGB equalization and YUV equalization and mixes them all in a single
     * image.
     * (Using JAVA)
     * </p>
     *
     * @param imageBitmap A bitmap image
     * @see AdditionalFilters
     * @see Equalization
     */
    public void mixEqualizationRgbYuv(Bitmap imageBitmap) {
        Equalization equalization = new Equalization(imageBitmap, context);

        Bitmap ega = imageBitmap.copy(imageBitmap.getConfig(), true);
        Bitmap egaYUV = imageBitmap.copy(imageBitmap.getConfig(), true);

        equalizationYuvY(egaYUV);
        equalization.equalizationRGB_RS(ega);

        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        int[] pixels = new int[width * height];
        int[] pixelsEga = new int[width * height];
        int[] pixelsEgaYUV = new int[width * height];

        ega.getPixels(pixelsEga, 0, width, 0, 0, width, height);
        egaYUV.getPixels(pixelsEgaYUV, 0, width, 0, 0, width, height);

        for (int i = 0; i < height * width - 1; i++) {
            pixels[i] = pixelsEga[i] & pixelsEgaYUV[i];
        }
        imageBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /*######################################"Render Script#########################################*/

    /**
     * <p>
     * method which allows to apply a snow and a black effect to the image passed in parameter
     * (Using Render Script)
     * </p>
     *
     * @param imageBitmap A Bitmap image
     * @see com.projetTec.imageStudio.model.filters.FilterType
     */
    public void snowAndBlackEffectRS(Bitmap imageBitmap, boolean isSnow) {
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, imageBitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_additional_snow_black scriptCAdditionalSnowBlack = new ScriptC_additional_snow_black(rs);

        scriptCAdditionalSnowBlack.set_isSnow(isSnow);

        scriptCAdditionalSnowBlack.forEach_snowBlackEffect(input, output);

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        scriptCAdditionalSnowBlack.destroy();
        rs.destroy();
    }

    /**
     * <p>
     * method which allows to apply a noise effect to the image passed in parameter
     * (Using Render Script)
     * </p>
     *
     * @param imageBitmap A Bitmap image
     * @see com.projetTec.imageStudio.model.filters.FilterType
     */
    public void noiseEffectRS(Bitmap imageBitmap) {
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, imageBitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_additional_noise scriptCAdditionalNoise = new ScriptC_additional_noise(rs);

        scriptCAdditionalNoise.forEach_noiseEffect(input, output);

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        scriptCAdditionalNoise.destroy();
        rs.destroy();
    }

    /**
     * <p>
     * Method which allows to apply a invert effect to the image passed in parameter
     * (Using Render Script)
     * </p>
     *
     * @param imageBitmap A Bitmap image
     * @see com.projetTec.imageStudio.model.filters.FilterType
     */
    public void invertEffectRS(Bitmap imageBitmap) {
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, imageBitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_additional_invert scriptCAdditionalInvert = new ScriptC_additional_invert(rs);

        scriptCAdditionalInvert.forEach_invertEffect(input, output);

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        scriptCAdditionalInvert.destroy();
        rs.destroy();
    }

    /**
     * <p>
     * Method which allows to apply a shading effect to the image passed in parameter
     * (Using Render Script)
     * </p>
     *
     * @param imageBitmap  A Bitmap image
     * @param shadingColor Shading color (RGB)
     * @return The result in Bitmap
     * @see com.projetTec.imageStudio.model.filters.FilterType
     */
    public Bitmap shadingFilterRS(Bitmap imageBitmap, int shadingColor) {
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, imageBitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_additional_shading scriptCAdditionalShading = new ScriptC_additional_shading(rs);

        scriptCAdditionalShading.set_red((short) Color.red(shadingColor));
        scriptCAdditionalShading.set_green((short) Color.green(shadingColor));
        scriptCAdditionalShading.set_blue((short) Color.blue(shadingColor));

        scriptCAdditionalShading.forEach_shadingEffect(input, output);

//        Bitmap returnBitmap = imageBitmap.copy(imageBitmap.getConfig(), true);
        Bitmap returnBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        output.copyTo(returnBitmap);

        input.destroy();
        output.destroy();
        scriptCAdditionalShading.destroy();
        rs.destroy();

        return returnBitmap;
    }

}
