package com.projettec.imageStudio.model.editingImage;

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
    private Bitmap imagebitmap;
    private Context context;

    public Equalization(Bitmap imagebitmap, Context context) {
        this.imagebitmap = imagebitmap;
        this.context = context;
    }

    public Bitmap getImagebitmap() {
        return imagebitmap;
    }
    public void setImagebitmap(Bitmap imagebitmap) {
        this.imagebitmap = imagebitmap;
    }

    Filter filter = new Filter(imagebitmap,context);

    /**
     * Function to apply histogram equalization to a gray image
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void egalisationNB(Bitmap imagebitmap){
        filter.tograyRS(imagebitmap);
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int accumulator = 0;
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] histo = AuxiliaryFunction.histogramme(pixels);
        int[] histo_cum = new int[256];
        int[] LUT = new int[256];
        for (int i = 0; i < 256; i++) {
            accumulator += histo[i];
            LUT[i] = ((accumulator * 255) / ((width * height) )) ;
        }
        for (int i = 0 ; i < height*width ; i++){
            int R = Color.red(pixels[i]);
            int new_color = LUT[R];
            pixels[i] = Color.rgb(new_color,new_color,new_color);
        }
        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function to apply histogram equalization to a color image (RGB)
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void egalisationcouleur(Bitmap imagebitmap){
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int accumulator = 0 ;
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
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
        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /*--------------------------------------Render Script---------------------------------------*/

    /**
     * Function to apply histogram equalization to a gray image
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void egalisationGrayRS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());
        Allocation output_gray = Allocation.createTyped(rs,input.getType());

        ScriptC_togray ToGray = new ScriptC_togray(rs);
        ToGray.forEach_toGray(input,output_gray);
        ToGray.destroy();

        ScriptC_histogramme HistogramScript = new ScriptC_histogramme(rs);
        HistogramScript.set_size(imagebitmap.getHeight()*imagebitmap.getWidth());
        int[] histo_cum = HistogramScript.reduce_calculate_histogram_CUM(output_gray).get();

        ScriptC_lutinit_and_equalize_gray LutEqualizeScript = new ScriptC_lutinit_and_equalize_gray(rs);

        LutEqualizeScript.set_LutTable(histo_cum);
        LutEqualizeScript.forEach_ApplyChanges_Equalize_Gray(output_gray,output);
        LutEqualizeScript.destroy();

        output.copyTo(imagebitmap);

        input.destroy();
        output.destroy();
        rs.destroy();
    }

    /**
     * Function to apply histogram equalization to a color image (RGB)
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void egalisationRGBRS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imagebitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());
        Allocation output_gray = Allocation.createTyped(rs,input.getType());

        ScriptC_togray ToGray = new ScriptC_togray(rs);
        ToGray.forEach_toGray(input,output_gray);
        ToGray.destroy();

        ScriptC_histogramme HistogramScript = new ScriptC_histogramme(rs);
        HistogramScript.set_size(imagebitmap.getHeight()*imagebitmap.getWidth());
        int[] histo_cum = HistogramScript.reduce_calculate_histogram_CUM(output_gray).get();

        ScriptC_lutinit_and_equalize_RGB LutEqualizeScript = new ScriptC_lutinit_and_equalize_RGB(rs);

        LutEqualizeScript.set_LutTable(histo_cum);
        LutEqualizeScript.forEach_ApplyChanges_Equalize_RGB(input,output);
        LutEqualizeScript.destroy();

        output.copyTo(imagebitmap);

        input.destroy();
        output.destroy();
        rs.destroy();
    }
}
