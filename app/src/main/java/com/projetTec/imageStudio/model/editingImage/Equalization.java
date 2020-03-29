package com.projetTec.imageStudio.model.editingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.android.rssample.ScriptC_histogramme;
import com.android.rssample.ScriptC_lutinit_and_equalize_RGB;
import com.android.rssample.ScriptC_lutinit_and_equalize_gray;
import com.android.rssample.ScriptC_togray;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

public class Equalization {
    private Bitmap imageBitmap;
    private final Context context;

    private final Filters filters;

    public Equalization(Bitmap imageBitmap, Context context) {
        this.imageBitmap = imageBitmap;
        this.context = context;

        filters = new Filters(imageBitmap,context);
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }


    /**
     * Function to apply histogram equalization to a gray image
     * (In JAVA)
     *
     * @param imageBitmap a Bitmap image
     */
    public void equalizationGray(Bitmap imageBitmap) {
        filters.toGrays(imageBitmap);
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        int accumulator = 0;
        int[] pixels = new int[height * width];
        imageBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] histo = AuxiliaryFunction.histogram(pixels);
        int[] histo_cum = new int[256];
        int[] LUT = new int[256];
        for (int i = 0; i < 256; i++) {
            accumulator += histo[i];
            LUT[i] = ((accumulator * 255) / ((width * height)));
        }
        for (int i = 0; i < height * width; i++) {
            int R = Color.red(pixels[i]);
            int new_color = LUT[R];
            pixels[i] = Color.rgb(new_color, new_color, new_color);
        }
        imageBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     * Function to apply histogram equalization to a color image (RGB)
     * (In JAVA)
     * @param imageBitmap a Bitmap image
     */
    public void equalizationCouleur(Bitmap imageBitmap){
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();
        int accumulator = 0 ;
        int[] pixels = new int[height*width];
        imageBitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] histo = new int[256];
        int[] LUT = new int[256];

        for (int i = 0 ; i < height*width ; i++){
            histo[ (int) (Color.red(pixels[i])*0.3 + Color.green(pixels[i])*0.59 + Color.blue(pixels[i])*0.11) ] +=1;
        }
        int[] histo_cum= new int[256];
        for (int i = 0 ; i < 256 ; i++){
            accumulator += histo[i];
            LUT[i] = ((accumulator * 255) / ((width * height) )) ;
        }
        for (int i = 0 ; i < height*width ; i++){
            int R = Color.red(pixels[i]);
            int G = Color.green(pixels[i]);
            int B = Color.blue(pixels[i]);
            int new_colorR = LUT[R];
            int new_colorG = LUT[G];
            int new_colorB = LUT[B];
            pixels[i] = Color.rgb(new_colorR,new_colorG,new_colorB);
        }
        imageBitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /*--------------------------------------Render Script---------------------------------------*/

    /**
     * Function to apply histogram equalization to a gray image
     * (Using RenderScript)
     * @param imageBitmap a Bitmap image
     */
    public void equalizationGrayRS(Bitmap imageBitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imageBitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());
        Allocation output_gray = Allocation.createTyped(rs,input.getType());

        ScriptC_togray ToGray = new ScriptC_togray(rs);
        ToGray.forEach_toGray(input,output_gray);
        ToGray.destroy();

        ScriptC_histogramme HistogramScript = new ScriptC_histogramme(rs);
        HistogramScript.set_size(imageBitmap.getHeight()*imageBitmap.getWidth());
        int[] histo_cum = HistogramScript.reduce_calculate_histogram_CUM(output_gray).get();

        ScriptC_lutinit_and_equalize_gray LutEqualizeScript = new ScriptC_lutinit_and_equalize_gray(rs);

        LutEqualizeScript.set_LutTable(histo_cum);
        LutEqualizeScript.forEach_ApplyChanges_Equalize_Gray(output_gray,output);
        LutEqualizeScript.destroy();

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        rs.destroy();
    }

    /**
     * Function to apply histogram equalization to a color image (RGB)
     * (Using RenderScript)
     * @param imageBitmap a Bitmap image
     */
    public void equalizationRGB_RS(Bitmap imageBitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imageBitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());
        Allocation output_gray = Allocation.createTyped(rs,input.getType());

        ScriptC_togray ToGray = new ScriptC_togray(rs);
        ToGray.forEach_toGray(input,output_gray);
        ToGray.destroy();

        ScriptC_histogramme HistogramScript = new ScriptC_histogramme(rs);
        HistogramScript.set_size(imageBitmap.getHeight()*imageBitmap.getWidth());
        int[] histo_cum = HistogramScript.reduce_calculate_histogram_CUM(output_gray).get();

        ScriptC_lutinit_and_equalize_RGB LutEqualizeScript = new ScriptC_lutinit_and_equalize_RGB(rs);

        LutEqualizeScript.set_LutTable(histo_cum);
        LutEqualizeScript.forEach_ApplyChanges_Equalize_RGB(input,output);
        LutEqualizeScript.destroy();

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        rs.destroy();
    }
}
