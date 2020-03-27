package com.projettec.imageStudio.model.editingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.android.rssample.ScriptC_brightness;
import com.android.rssample.ScriptC_colorize;
import com.android.rssample.ScriptC_keepcolor;
import com.android.rssample.ScriptC_togray;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;


import static android.graphics.Color.HSVToColor;

public class Filter {

    private static final String TAG = "Filter";
    
    private Bitmap imagebitmap ;
    private Context context;
    public Filter(Bitmap imagebitmap, Context context) {
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
     * Function which converts the image pass into parameter in gray ussing getPixel() fonction
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void toGray(Bitmap imagebitmap){
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();

        for (int i = 0 ; i < width; i++ ){
            for (int j = 0 ; j < height; j++){
                int couleur = imagebitmap.getPixel(i,j);
                int R = Color.red(couleur);
                int G = Color.green(couleur);
                int B = Color.blue(couleur);
                int new_color = (int) (R*0.3 + G*0.59 + B*0.11);
                imagebitmap.setPixel(i,j,Color.rgb(new_color,new_color,new_color));
            }
        }
    }

    /**
     * Function which converts the image pass into parameter in gray ussing getPixels() fonction
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void toGrays(Bitmap imagebitmap){
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();

        int[] pixels = new int[height * width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        for(int i = 0 ; i < height * width - 1; i++){
            int R = Color.red(pixels[i]);
            int G = Color.green(pixels[i]);
            int B = Color.blue(pixels[i]);
            int new_color = (int) (R*0.3 + G*0.59 + B*0.11);
            pixels[i]= Color.rgb(new_color,new_color,new_color);
        }

        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function that allows you to apply a colorization filter (random color)
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void colorize(Bitmap imagebitmap){
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        float[] h = new float[3];
        int nbr_random = (int) (Math.random() * 360) ;
        int[] pixels = new int[height * width];

        int[] r_g_b = new int[3];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        for (int i = 0 ; i < height*width-1 ; i++){
            r_g_b = AuxiliaryFunction.RGBtoR_G_B(pixels[i]);
            Conversion.RGBToHSV_new(r_g_b[0],r_g_b[1],r_g_b[2],h);
            h[0] =  (float) nbr_random  ;
            pixels[i] = HSVToColor(h);
        }

        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function which allows to keep only one color in the image passed in parameter
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     * @param rgb the RGB color we want to keep
     * @param radius the margin that we will accept
     */
    public void keepColor(Bitmap imagebitmap , int rgb, int radius){
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        float[] h = new float[3];

        int[] r_g_b_reper = AuxiliaryFunction.RGBtoR_G_B(rgb);
        float[] hh = new float[3];
        Conversion.RGBToHSV_new(r_g_b_reper[0],r_g_b_reper[1],r_g_b_reper[2],hh);
        float reper = hh[0];  // Le H de la couleur passer en paramétre

        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        for (int i = 0 ; i < height * width ; i++){
            int[] r_g_b_image = AuxiliaryFunction.RGBtoR_G_B(pixels[i]);

            Conversion.RGBToHSV_new(r_g_b_image[0],r_g_b_image[1],r_g_b_image[2],h);
            if (!AuxiliaryFunction.Is_like(reper , h[0], radius)){
                int new_color = (int) (r_g_b_image[0]*0.3 + r_g_b_image[1]*0.59 + r_g_b_image[2]*0.11);
                pixels[i]= Color.rgb(new_color,new_color,new_color);
            }
        }
        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * <p>Method which allows to change the value or the saturation of all the pixels of a bitmap image passed in parameter.
     * <p>(HSV)
     *
     * @param imagebitmap  A Bitmap image
     * @param newValue     The new value we want to put
     * @param isBrightness Brightness if true, saturation if false
     */
    public Bitmap brightnessAndSaturationHSV(Bitmap imagebitmap, float newValue, boolean isBrightness) {
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        float[] h = new float[3];
        int[] pixels = new int[height * width];

        int[] r_g_b = new int[3];
        imagebitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height * width - 1; i++) {
            r_g_b = AuxiliaryFunction.RGBtoR_G_B(pixels[i]);
            Conversion.RGBToHSV_new(r_g_b[0], r_g_b[1], r_g_b[2], h);

            if (isBrightness) {
                h[2] = h[2] + newValue;
                if (h[2] < 0.0f) h[2] = 0.0f;
                else if (h[2] > 1.0f) h[2] = 1.0f;
            }
            else {
                h[1] = h[1] + newValue;
                if (h[1] < 0.0f) h[1] = 0.0f;
                else if (h[1] > 1.0f) h[1] = 1.0f;
            }

            pixels[i] = HSVToColor(h);
        }

        //imagebitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    public Bitmap brightnessRGB(Bitmap imagebitmap, int newValue) {
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int[] pixels = new int[height * width];

        int[] r_g_b = new int[3];
        imagebitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height * width - 1; i++) {
            r_g_b = AuxiliaryFunction.RGBtoR_G_B(pixels[i]);

            r_g_b[0] = r_g_b[0] + newValue ;
            r_g_b[1] = r_g_b[1] + newValue ;
            r_g_b[2] = r_g_b[2] + newValue ;

            if (r_g_b[0] < 0) r_g_b[0] = 0;
            else if (r_g_b[0] > 255) r_g_b[0] = 255;

            if (r_g_b[1] < 0) r_g_b[1] = 0;
            else if (r_g_b[1] > 255) r_g_b[1] = 255;

            if (r_g_b[2] < 0) r_g_b[2] = 0;
            else if (r_g_b[2] > 255) r_g_b[2] = 255;

            pixels[i] = Color.rgb(r_g_b[0], r_g_b[1], r_g_b[2]);
        }

        //imagebitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    /*######################################"Render Script#########################################*/

    /**
     * Function which converts the image pass into parameter in gray
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void tograyRS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_togray GrayScript = new ScriptC_togray(rs);

        GrayScript.forEach_toGray(input,output);

        output.copyTo(imagebitmap);

        input.destroy();
        output.destroy();
        GrayScript.destroy();
        rs.destroy();
    }

    /**
     * Function that allows you to apply a colorization filter (random color)
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void colorizeRS(Bitmap imagebitmap, float newHue){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_colorize ColorizeScript = new ScriptC_colorize(rs);

        ColorizeScript.set_new_hue(newHue);

        ColorizeScript.forEach_Colorize(input,output);

        output.copyTo(imagebitmap);

        input.destroy();
        output.destroy();
        ColorizeScript.destroy();
        rs.destroy();
    }

    /**
     * Function which allows to keep only one color in the image passed in parameter
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     * @param colorToKeep the color we want to keep (Hue)
     */
    public void KeepColorRS(Bitmap imagebitmap, float colorToKeep){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_keepcolor KeepColorScript = new ScriptC_keepcolor(rs);

        KeepColorScript.set_color_to_keep(colorToKeep);

        KeepColorScript.forEach_KeepColor(input,output);

        output.copyTo(imagebitmap);

        input.destroy();
        output.destroy();
        KeepColorScript.destroy();
        rs.destroy();
    }
    /**
     * <p>Method which allows to change the value of all the pixels of a bitmap image passed in parameter.
     * (Using RenderScript)
     *
     * @param imagebitmap A Bitmap image
     * @param newValue    The new value we want to put
     */
    public Bitmap brightnessRS(Bitmap imagebitmap, float newValue){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_brightness brightnessScript = new ScriptC_brightness(rs);

        brightnessScript.set_new_value(newValue);

        brightnessScript.forEach_Brightness(input, output);

        Bitmap returnBitmap = imagebitmap.copy(imagebitmap.getConfig(), true);
        output.copyTo(returnBitmap);

        input.destroy();
        output.destroy();
        brightnessScript.destroy();
        rs.destroy();
        return returnBitmap ;
    }

}
