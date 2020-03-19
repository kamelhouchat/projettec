package com.projettec.imageStudio.model.editingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.android.rssample.ScriptC_colorize;
import com.android.rssample.ScriptC_keepcolor;
import com.android.rssample.ScriptC_togray;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import static android.graphics.Color.HSVToColor;

public class Filter {

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
    public void keepcolor(Bitmap imagebitmap , int rgb, int radius){
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


    /*######################################"Render Sctipt#########################################*/

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
    public void colorizeRS(Bitmap imagebitmap, float new_hue){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_colorize ColorizeScript = new ScriptC_colorize(rs);

        ColorizeScript.set_new_hue(new_hue);

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
     * @param color_to_keep the color we want to keep (Hue)
     */
    public void KeepColorRS(Bitmap imagebitmap, float color_to_keep){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_keepcolor KeepColorScript = new ScriptC_keepcolor(rs);

        KeepColorScript.set_color_to_keep(color_to_keep);

        KeepColorScript.forEach_KeepColor(input,output);

        output.copyTo(imagebitmap);

        input.destroy();
        output.destroy();
        KeepColorScript.destroy();
        rs.destroy();
    }

    public void saveColors(Bitmap bmp, int color){
        int width  = bmp.getWidth();
        int height = bmp.getHeight();
        int mycolor , r , g , b;
        int [] pixel = new int[width * height];
        float[]  hsv = new float[3];

        float inf = (float) (color -10) + 360, sup = (float) ((color + 10) + 360) % 360;

        bmp.getPixels(pixel,0, width , 0, 0,width ,height);
        for (int i = 0; i < height * width -1; i ++){

            mycolor = pixel[i];

            r = Color.red(mycolor);
            g = Color.green(mycolor);
            b = Color.blue(mycolor);

            //Color.RGBToHSV(r,g,b,hsv);
            Conversion.RGBToHSV_new(r,g,b,hsv);


            if (hsv[0] < inf  && hsv[0] > sup){
                hsv[1] = 0;
            }

            //pixel[i] = Color.HSVToColor(hsv);
            pixel[i] = Conversion.HSVToColor_new(hsv);

        }
        bmp.setPixels(pixel,0, width , 0, 0,width ,height);
    }
}
