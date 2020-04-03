package com.projetTec.imageStudio.model.editingImage.basicFilters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.android.rssample.ScriptC_findMinMax;
import com.android.rssample.ScriptC_findMinMaxHSV;
import com.android.rssample.ScriptC_findMinMaxRGB;
import com.android.rssample.ScriptC_lutinit_and_contrast_HSV;
import com.android.rssample.ScriptC_lutinit_and_contrast_RGB;
import com.android.rssample.ScriptC_lutinit_and_contrast_gray;
import com.android.rssample.ScriptC_togray;

import androidx.renderscript.Allocation;
import androidx.renderscript.Int2;
import androidx.renderscript.RenderScript;

public class DynamicExtension {

    private Bitmap imageBitmap;
    private final Context context;

    private final Filters filters;

    public DynamicExtension(Bitmap imageBitmap, Context context) {
        this.imageBitmap = imageBitmap;
        this.context = context;

        this.filters = new Filters(imageBitmap,context);
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    /**
     * Function that increases the contrast of a gray image (without using the lut table)
     * (In JAVA)
     * @param imageBitmap a Bitmap image
     */
    public void contrastPlus(Bitmap imageBitmap){
        filters.toGrays(imageBitmap);
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        int[] pixels = new int[height*width];
        imageBitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] min_max = AuxiliaryFunction.min_max_histo(pixels, false);
        for (int i = 0 ; i < height*width ; i++){
            int R = Color.red(pixels[i]);
            int new_color = (255*(R-min_max[0]))/(min_max[1]-min_max[0]);
            pixels[i] = Color.rgb(new_color, new_color, new_color);
        }
        imageBitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function that increases the contrast of a gray image (using the lut table)
     * (In JAVA)
     * @param imageBitmap a Bitmap image
     */
    public void contrastPlusGrayLut(Bitmap imageBitmap) {
        filters.toGrays(imageBitmap);
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        int[] pixels = new int[height * width];
        imageBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] min_max = AuxiliaryFunction.min_max_histo(pixels, false);
        if (min_max[0] == min_max[1]) return;
        int[] LUT;
        // INITIALISATION DE LA LUT
        LUT = AuxiliaryFunction.LUT_Init(min_max, true);
        for (int i = 0; i < height * width; i++) {
            int R = Color.red(pixels[i]);
            int new_color = LUT[R];
            pixels[i] = Color.rgb(new_color, new_color, new_color);
        }
        imageBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     * Function that reduces the contrast of a gray image
     * (In JAVA)
     * @param imageBitmap a Bitmap image
     */
    public void contrastFewerGray(Bitmap imageBitmap){
        filters.toGrays(imageBitmap);
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        int[] pixels = new int[height*width];
        imageBitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] min_max = AuxiliaryFunction.min_max_histo(pixels, false);
        int diff = min_max[1]-min_max[0];
        int percentage = (diff*5)/100;
        min_max[0] = min_max[0]+percentage ;
        min_max[1] = min_max[1]-percentage ;

        if (min_max[0] < 0 || min_max[1] > 255) return;
        int[] LUT;
        // INITIALIZING THE LUT
        LUT = AuxiliaryFunction.LUT_Init(min_max,false);
        // CALCULATION OF TRANSFORMATION
        for (int i = 0 ; i < height*width ; i++){
            int R = Color.red(pixels[i]);
            int new_color = LUT[R];
            pixels[i] = Color.rgb(new_color,new_color,new_color);
        }
        imageBitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function that increases the contrast of a color image (RGB)
     * (In JAVA)
     * @param imageBitmap a Bitmap image
     */
    public void contrastPlusCouleurRGB(Bitmap imageBitmap){
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        int[] pixels = new int[height*width];
        imageBitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] histoR = new int[256];
        int[] histoG = new int[256];
        int[] histoB = new int[256];
        for (int i = 0 ; i < height*width ; i++){
            histoR[Color.red(pixels[i])] = histoR[Color.red(pixels[i])] + 1 ;
            histoG[Color.green(pixels[i])] = histoG[Color.green(pixels[i])] + 1 ;
            histoB[Color.blue(pixels[i])] = histoB[Color.blue(pixels[i])] + 1 ;
        }
        int[] min_max_R = AuxiliaryFunction.minMax(histoR);
        int minR = min_max_R[0] ; int maxR = min_max_R[1] ;
        int[] min_max_G = AuxiliaryFunction.minMax(histoG);
        int minG = min_max_G[0]; int maxG = min_max_G[1];
        int[] min_max_B = AuxiliaryFunction.minMax(histoB);
        int minB = min_max_B[0]; int maxB = min_max_B[0] ;

        int[] LUT_R = AuxiliaryFunction.LUT_Init(min_max_R, true);
        int[] LUT_G = AuxiliaryFunction.LUT_Init(min_max_G, true);
        int[] LUT_B = AuxiliaryFunction.LUT_Init(min_max_B, true);

        for (int i = 0 ; i < height*width ; i++){
            int R = Color.red(pixels[i]);
            int G = Color.green(pixels[i]);
            int B = Color.blue(pixels[i]);

            int new_R = LUT_R[R];
            int new_G = LUT_G[G];
            int new_B = LUT_B[B];

            pixels[i] = Color.rgb(new_R,new_G,new_B);
        }

        imageBitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function that increases the contrast of a color image (HSV)
     * (In JAVA)
     * @param imageBitmap a Bitmap image
     */
    public void contrastPlusCouleurHSV(Bitmap imageBitmap){
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        int color ;
        int[] pixels = new int[height*width];
        float[] hsv = new float[3];
        imageBitmap.getPixels(pixels,0,width,0,0,width,height);

        int[] min_max= AuxiliaryFunction.min_max_histo(pixels, true);
        if (min_max[0] == min_max[1]) return;

        int[] LUT = AuxiliaryFunction.LUT_Init(min_max,true);
        for (int i = 0; i < height * width; i++) {
            Conversion.RGBToHSV_new(Color.red(pixels[i]), Color.green(pixels[i]), Color.blue(pixels[i]), hsv);
            color = LUT[ (int) (hsv[2] * 255)];
            hsv[2] = color / 255.0f ;
            pixels[i] = Conversion.HSVToColor_new(hsv);
        }
        imageBitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /*--------------------------------------Render Script---------------------------------------*/

    /**
     * Function that increases the contrast of a gray image
     * (Using RenderScript)
     * @param imageBitmap a Bitmap image
     */
    public void contrastPlusGrayRS(Bitmap imageBitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imageBitmap);
        Allocation output_gray = Allocation.createTyped(rs,input.getType());
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_findMinMax FindMinMaxScript = new ScriptC_findMinMax(rs);
        ScriptC_togray TograyScript = new ScriptC_togray(rs);

        TograyScript.forEach_toGray(input,output_gray);
        TograyScript.destroy();

        Int2 MinMax = FindMinMaxScript.reduce_findMinAndMax(output_gray).get();
        FindMinMaxScript.destroy();
        if (MinMax.x == MinMax.y) return;

        ScriptC_lutinit_and_contrast_gray LutScript = new ScriptC_lutinit_and_contrast_gray(rs);

        LutScript.set_MinAndMax(MinMax);
        LutScript.invoke_ContrastPlusGray(output_gray,output);

        LutScript.destroy();

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        output_gray.destroy();
        rs.destroy();
    }

    /**
     * Function that reduces the contrast of a gray image
     * (Using RenderScript)
     * @param imageBitmap a Bitmap image
     */
    public void contrastFewerGrayRS(Bitmap imageBitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imageBitmap);
        Allocation output_gray = Allocation.createTyped(rs,input.getType());
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_findMinMax FindMinMaxScript = new ScriptC_findMinMax(rs);
        ScriptC_togray TograyScript = new ScriptC_togray(rs);

        TograyScript.forEach_toGray(input,output_gray);
        TograyScript.destroy();

        Int2 MinMax = FindMinMaxScript.reduce_findMinAndMax(output_gray).get();
        FindMinMaxScript.destroy();

        ScriptC_lutinit_and_contrast_gray LutScript = new ScriptC_lutinit_and_contrast_gray(rs);

        LutScript.set_MinAndMax(MinMax);
        LutScript.invoke_ContrastFewerGray(output_gray,output);

        LutScript.destroy();

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        output_gray.destroy();
        rs.destroy();
    }

    /**
     * Function that increases the contrast of a color image (RGB)
     * (Using RenderScript)
     * @param imageBitmap a Bitmap image
     */
    public void contrastPlusRGB_RS(Bitmap imageBitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imageBitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_findMinMaxRGB FindMinMaxRGBScript = new ScriptC_findMinMaxRGB(rs);

        Int2[] MinMax = FindMinMaxRGBScript.reduce_findMinAndMaxRGB(input).get();
        FindMinMaxRGBScript.destroy();

        ScriptC_lutinit_and_contrast_RGB LutRGBScript = new ScriptC_lutinit_and_contrast_RGB(rs);

        LutRGBScript.set_MinAndMaxR(MinMax[0]);
        LutRGBScript.set_MinAndMaxG(MinMax[1]);
        LutRGBScript.set_MinAndMaxB(MinMax[2]);

        LutRGBScript.invoke_ContrastPlusRGB(input,output);

        LutRGBScript.destroy();

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        rs.destroy();
    }

    /**
     * Function that increases the contrast of a color image (HSV)
     * (Using RenderScript)
     * @param imageBitmap a Bitmap image
     */
    public void contrastPlusHSV_RS(Bitmap imageBitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imageBitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_findMinMaxHSV FindMinMaxHSVScript = new ScriptC_findMinMaxHSV(rs);

        Int2 MinMax = FindMinMaxHSVScript.reduce_findMinAndMaxHSV(input).get();
        FindMinMaxHSVScript.destroy();
        if (MinMax.x == MinMax.y) return;

        ScriptC_lutinit_and_contrast_HSV LutHSVScript = new ScriptC_lutinit_and_contrast_HSV(rs);

        LutHSVScript.set_MinAndMax(MinMax);

        LutHSVScript.invoke_ContrastPlusHSV(input,output);

        LutHSVScript.destroy();

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        rs.destroy();
    }
}
