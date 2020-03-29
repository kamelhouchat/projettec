package com.projettec.imageStudio.model.editingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.android.rssample.ScriptC_brightness;
import com.android.rssample.ScriptC_brightnessRGB;
import com.android.rssample.ScriptC_colorize;
import com.android.rssample.ScriptC_keepcolor;
import com.android.rssample.ScriptC_togray;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;


import static android.graphics.Color.HSVToColor;

public class Filter {

    private static final String TAG = "Filter";
    
    private Bitmap imageBitmap;
    private final Context context;
    public Filter(Bitmap imageBitmap, Context context) {
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
     * Function which converts the image pass into parameter in gray using getPixel() function
     * (In JAVA)
     * @param imageBitmap a Bitmap image
     */
    public void toGray(Bitmap imageBitmap){
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();

        for (int i = 0 ; i < width; i++ ){
            for (int j = 0 ; j < height; j++){
                int color = imageBitmap.getPixel(i,j);
                int R = Color.red(color);
                int G = Color.green(color);
                int B = Color.blue(color);
                int new_color = (int) (R*0.3 + G*0.59 + B*0.11);
                imageBitmap.setPixel(i,j,Color.rgb(new_color,new_color,new_color));
            }
        }
    }

    /**
     * Function which converts the image pass into parameter in gray using getPixels() function
     * (In JAVA)
     * @param imageBitmap a Bitmap image
     */
    public void toGrays(Bitmap imageBitmap){
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();

        int[] pixels = new int[height * width];
        imageBitmap.getPixels(pixels,0,width,0,0,width,height);
        for(int i = 0 ; i < height * width - 1; i++){
            int R = Color.red(pixels[i]);
            int G = Color.green(pixels[i]);
            int B = Color.blue(pixels[i]);
            int new_color = (int) (R*0.3 + G*0.59 + B*0.11);
            pixels[i]= Color.rgb(new_color,new_color,new_color);
        }

        imageBitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function that allows you to apply a colorization filter (random color)
     * (In JAVA)
     * @param imageBitmap a Bitmap image
     */
    public void colorize(Bitmap imageBitmap, float newHue){
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        float[] h = new float[3];
        int[] pixels = new int[height * width];

        int[] r_g_b;
        imageBitmap.getPixels(pixels,0,width,0,0,width,height);
        for (int i = 0 ; i < height*width-1 ; i++){
            r_g_b = AuxiliaryFunction.RGBtoR_G_B(pixels[i]);
            Conversion.RGBToHSV_new(r_g_b[0],r_g_b[1],r_g_b[2],h);
            h[0] =  newHue  ;
            pixels[i] = HSVToColor(h);
        }

        imageBitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function which allows to keep only one color in the image passed in parameter
     * (In JAVA)
     * @param imageBitmap a Bitmap image
     * @param rgb the RGB color we want to keep
     * @param radius the margin that we will accept
     */
    public void keepColor(Bitmap imageBitmap , int rgb, int radius){
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        float[] h = new float[3];

        int[] r_g_b_landmark = AuxiliaryFunction.RGBtoR_G_B(rgb);
        float[] hh = new float[3];
        Conversion.RGBToHSV_new(r_g_b_landmark[0],r_g_b_landmark[1],r_g_b_landmark[2],hh);
        float landmark = hh[0];  // Le H de la couleur passer en paramétre

        int[] pixels = new int[height*width];
        imageBitmap.getPixels(pixels,0,width,0,0,width,height);
        for (int i = 0 ; i < height * width ; i++){
            int[] r_g_b_image = AuxiliaryFunction.RGBtoR_G_B(pixels[i]);

            Conversion.RGBToHSV_new(r_g_b_image[0],r_g_b_image[1],r_g_b_image[2],h);
            if (!AuxiliaryFunction.Is_like(landmark , h[0], radius)){
                int new_color = (int) (r_g_b_image[0]*0.3 + r_g_b_image[1]*0.59 + r_g_b_image[2]*0.11);
                pixels[i]= Color.rgb(new_color,new_color,new_color);
            }
        }
        imageBitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * <p>Method which allows to change the value or the saturation of all the pixels of a bitmap image passed in parameter.
     * <p>(HSV)
     *
     * @param imageBitmap  A Bitmap image
     * @param newValue     The new value we want to put
     * @param isBrightness Brightness if true, saturation if false
     * @return A bitmap to which the changes have been applied
     */
    public Bitmap brightnessAndSaturationHSV(Bitmap imageBitmap, float newValue, boolean isBrightness) {
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        float[] h = new float[3];
        int[] pixels = new int[height * width];

        int[] r_g_b;
        imageBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height * width - 1; i++) {
            r_g_b = AuxiliaryFunction.RGBtoR_G_B(pixels[i]);
            Conversion.RGBToHSV_new(r_g_b[0], r_g_b[1], r_g_b[2], h);

            if (isBrightness) {
                h[2] = h[2] + newValue;
                if (h[2] < 0.0f) h[2] = 0.0f;
                else if (h[2] > 1.0f) h[2] = 1.0f;
            } else {
                h[1] = h[1] + newValue;
                if (h[1] < 0.0f) h[1] = 0.0f;
                else if (h[1] > 1.0f) h[1] = 1.0f;
            }

            pixels[i] = HSVToColor(h);
        }

        //imageBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    /**
     * <p>Method which allows the value passed in parameter to be added to the three channels R, G and B.
     *
     * @param imageBitmap A Bitmap image
     * @param newValue    The value we want to add to the old
     * @return A bitmap to which the changes have been applied
     */
    public Bitmap brightnessRGB(Bitmap imageBitmap, int newValue) {
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        int[] pixels = new int[height * width];

        int[] r_g_b;
        imageBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height * width - 1; i++) {
            r_g_b = AuxiliaryFunction.RGBtoR_G_B(pixels[i]);

            r_g_b[0] = r_g_b[0] + newValue;
            r_g_b[1] = r_g_b[1] + newValue;
            r_g_b[2] = r_g_b[2] + newValue;

            if (r_g_b[0] < 0) r_g_b[0] = 0;
            else if (r_g_b[0] > 255) r_g_b[0] = 255;

            if (r_g_b[1] < 0) r_g_b[1] = 0;
            else if (r_g_b[1] > 255) r_g_b[1] = 255;

            if (r_g_b[2] < 0) r_g_b[2] = 0;
            else if (r_g_b[2] > 255) r_g_b[2] = 255;

            pixels[i] = Color.rgb(r_g_b[0], r_g_b[1], r_g_b[2]);
        }

        //imageBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    /*######################################"Render Script#########################################*/

    /**
     * Function which converts the image pass into parameter in gray
     * (Using RenderScript)
     * @param imageBitmap a Bitmap image
     */
    public void tograyRS(Bitmap imageBitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imageBitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_togray GrayScript = new ScriptC_togray(rs);

        GrayScript.forEach_toGray(input,output);

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        GrayScript.destroy();
        rs.destroy();
    }

    /**
     * Function that allows you to apply a colorization filter (random color)
     * (Using RenderScript)
     * @param imageBitmap a Bitmap image
     */
    public void colorizeRS(Bitmap imageBitmap, float newHue){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imageBitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_colorize ColorizeScript = new ScriptC_colorize(rs);

        ColorizeScript.set_new_hue(newHue);

        ColorizeScript.forEach_Colorize(input,output);

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        ColorizeScript.destroy();
        rs.destroy();
    }

    /**
     * Function which allows to keep only one color in the image passed in parameter
     * (Using RenderScript)
     * @param imageBitmap a Bitmap image
     * @param colorToKeep the color we want to keep (Hue)
     */
    public void KeepColorRS(Bitmap imageBitmap, float colorToKeep){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imageBitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_keepcolor KeepColorScript = new ScriptC_keepcolor(rs);

        KeepColorScript.set_color_to_keep(colorToKeep);

        KeepColorScript.forEach_KeepColor(input,output);

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        KeepColorScript.destroy();
        rs.destroy();
    }

    /**
     * <p>Method which allows to change the value of all the pixels of a bitmap image passed in parameter.
     * (Using RenderScript)
     *
     * @param imageBitmap A Bitmap image
     * @param newValue    The value we want to add to the old
     * @return A bitmap to which the changes have been applied
     */
    public Bitmap brightnessAndSaturationHSV_RS(Bitmap imageBitmap, float newValue) {
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, imageBitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_brightness brightnessScript = new ScriptC_brightness(rs);

        brightnessScript.set_new_value(newValue);

        brightnessScript.forEach_Brightness(input, output);

        Bitmap returnBitmap = imageBitmap.copy(imageBitmap.getConfig(), true);
        output.copyTo(returnBitmap);

        input.destroy();
        output.destroy();
        brightnessScript.destroy();
        rs.destroy();
        return returnBitmap;
    }

    /**
     * <p>
     * Method which allows the value passed in parameter to be added to the three channels R, G and B.
     * (Using Render Script)
     * </p>
     *
     * @param imageBitmap A Bitmap image
     * @param newValue    The value we want to add to the old
     * @return A bitmap to which the changes have been applied
     */
    public Bitmap brightnessRGB_RS(Bitmap imageBitmap, int newValue) {
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, imageBitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_brightnessRGB scriptC_brightnessRGB = new ScriptC_brightnessRGB(rs);

        scriptC_brightnessRGB.set_new_value(newValue);

        scriptC_brightnessRGB.forEach_Brightness(input, output);

        Bitmap returnBitmap = imageBitmap.copy(imageBitmap.getConfig(), true);
        output.copyTo(returnBitmap);

        input.destroy();
        output.destroy();
        scriptC_brightnessRGB.destroy();
        rs.destroy();
        return returnBitmap;
    }

}
