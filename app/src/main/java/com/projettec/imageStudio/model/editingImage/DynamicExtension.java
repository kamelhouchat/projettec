package com.projettec.imageStudio.model.editingImage;

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

    private Bitmap imagebitmap;
    private final Context context;

    private final Filter filter;

    public DynamicExtension(Bitmap imagebitmap, Context context) {
        this.imagebitmap = imagebitmap;
        this.context = context;

        this.filter = new Filter(imagebitmap,context);
    }

    public Bitmap getImagebitmap() {
        return imagebitmap;
    }
    public void setImagebitmap(Bitmap imagebitmap) {
        this.imagebitmap = imagebitmap;
    }

    /**
     * Function that increases the contrast of a gray image (without using the lut table)
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void  contrasteplus(Bitmap imagebitmap){
        filter.toGrays(imagebitmap);
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] min_max = AuxiliaryFunction.min_max_histo(pixels, false);
        for (int i = 0 ; i < height*width ; i++){
            int R = Color.red(pixels[i]);
            int new_color = (255*(R-min_max[0]))/(min_max[1]-min_max[0]);
            pixels[i] = Color.rgb(new_color, new_color, new_color);
        }
        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function that increases the contrast of a gray image (using the lut table)
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void contrastePlusGrayLut(Bitmap imagebitmap) {
        filter.toGrays(imagebitmap);
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int[] pixels = new int[height * width];
        imagebitmap.getPixels(pixels, 0, width, 0, 0, width, height);
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
        imagebitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     * Function that reduces the contrast of a gray image
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void contrasteFewerGray(Bitmap imagebitmap){
        filter.toGrays(imagebitmap);
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] min_max = AuxiliaryFunction.min_max_histo(pixels, false);
        int diff = min_max[1]-min_max[0];
        int perc = (diff*5)/100;
        min_max[0] = min_max[0]+perc ;
        min_max[1] = min_max[1]-perc ;

        if (min_max[0] < 0 || min_max[1] > 255) return;
        int[] LUT;
        // INITIALISATION DE LA LUT
        LUT = AuxiliaryFunction.LUT_Init(min_max,false);
        // CALCUL DE LA TRANSFORMATION
        for (int i = 0 ; i < height*width ; i++){
            int R = Color.red(pixels[i]);
            int new_color = LUT[R];
            pixels[i] = Color.rgb(new_color,new_color,new_color);
        }
        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function that increases the contrast of a color image (RGB)
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void contrastePlusCouleurRGB(Bitmap imagebitmap){
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] histoR = new int[256];
        int[] histoG = new int[256];
        int[] histoB = new int[256];
        for (int i = 0 ; i < height*width ; i++){
            histoR[Color.red(pixels[i])] = histoR[Color.red(pixels[i])] + 1 ;
            histoG[Color.green(pixels[i])] = histoG[Color.green(pixels[i])] + 1 ;
            histoB[Color.blue(pixels[i])] = histoB[Color.blue(pixels[i])] + 1 ;
        }
        int[] min_max_R = AuxiliaryFunction.minmax(histoR);
        int minR = min_max_R[0] ; int maxR = min_max_R[1] ;
        int[] min_max_G = AuxiliaryFunction.minmax(histoG);
        int minG = min_max_G[0]; int maxG = min_max_G[1];
        int[] min_max_B = AuxiliaryFunction.minmax(histoB);
        int minB = min_max_B[0]; int maxB = min_max_B[0] ;

        int[] LUTR = AuxiliaryFunction.LUT_Init(min_max_R, true);
        int[] LUTG = AuxiliaryFunction.LUT_Init(min_max_G, true);
        int[] LUTB = AuxiliaryFunction.LUT_Init(min_max_B, true);

        for (int i = 0 ; i < height*width ; i++){
            int R = Color.red(pixels[i]);
            int G = Color.green(pixels[i]);
            int B = Color.blue(pixels[i]);

            int new_R = LUTR[R];
            int new_G = LUTG[G];
            int new_B = LUTB[B];

            pixels[i] = Color.rgb(new_R,new_G,new_B);
        }

        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function that increases the contrast of a color image (HSV)
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void contrastePlusCouleurHSV(Bitmap imagebitmap){
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int color ;
        int[] pixels = new int[height*width];
        float[] hsv = new float[3];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);

        int[] min_max= AuxiliaryFunction.min_max_histo(pixels, true);
        if (min_max[0] == min_max[1]) return;

        int[] LUT = AuxiliaryFunction.LUT_Init(min_max,true);
        for (int i = 0; i < height * width; i++) {
            Conversion.RGBToHSV_new(Color.red(pixels[i]), Color.green(pixels[i]), Color.blue(pixels[i]), hsv);
            color = LUT[ (int) (hsv[2] * 255)];
            hsv[2] = color / 255.0f ;
            pixels[i] = Conversion.HSVToColor_new(hsv);
        }
        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /*--------------------------------------Render Script---------------------------------------*/

    /**
     * Function that increases the contrast of a gray image
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void contrastePlusGrayRS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
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

        output.copyTo(imagebitmap);

        input.destroy();
        output.destroy();
        output_gray.destroy();
        rs.destroy();
    }

    /**
     * Function that reduces the contrast of a gray image
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void contrasteFewerGrayRS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
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

        output.copyTo(imagebitmap);

        input.destroy();
        output.destroy();
        output_gray.destroy();
        rs.destroy();
    }

    /**
     * Function that increases the contrast of a color image (RGB)
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void contrastePlusRGB_RS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
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

        output.copyTo(imagebitmap);

        input.destroy();
        output.destroy();
        rs.destroy();
    }

    /**
     * Function that increases the contrast of a color image (HSV)
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void contrastePlusHSV_RS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_findMinMaxHSV FindMinMaxHSVScript = new ScriptC_findMinMaxHSV(rs);

        Int2 MinMax = FindMinMaxHSVScript.reduce_findMinAndMaxHSV(input).get();
        FindMinMaxHSVScript.destroy();
        if (MinMax.x == MinMax.y) return;

        ScriptC_lutinit_and_contrast_HSV LutHSVScript = new ScriptC_lutinit_and_contrast_HSV(rs);

        LutHSVScript.set_MinAndMax(MinMax);

        LutHSVScript.invoke_ContrastPlusHSV(input,output);

        LutHSVScript.destroy();

        output.copyTo(imagebitmap);

        input.destroy();
        output.destroy();
        rs.destroy();
    }
}
