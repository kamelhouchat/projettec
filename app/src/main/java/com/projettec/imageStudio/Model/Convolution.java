package com.projettec.imageStudio.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

public class Convolution {

    private Bitmap imagebitmap;
    private Context context;

    public Convolution(Bitmap imagebitmap, Context context) {
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
     * Allows to convolve the values of the image
     * @param bmp image to convolve
     * @param filter filter to apply on the image
     */
    public void convolution(Bitmap bmp, int [][] filter){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int [] pixel = new int[width * height];
        int red = 0, green = 0, bleu = 0;
        int sizeFilter = filter.length;
        int n = sizeFilter /2;
        bmp.getPixels(pixel,0, width , 0, 0,width ,height);
        int moy = moyenne(filter);
        for (int y = n; y < height-n; y++){
            for (int x = n ; x < width - n; x++){
                red = green = bleu = 0;
                for(int u = -n; u <= n; u++){
                    for (int v = -n; v <= n; v++ ){
                        int color = bmp.getPixel(x + u,  y + v);
                        //int color= pixel[(width * i + x) + (j + y)];
                        int r = Color.red(color);
                        int g = Color.green(color);
                        int b = Color.blue(color);

                        red = red + (r * filter[u +n][v +n]);
                        green = green + (g * filter[u +n][v +n]);
                        bleu = bleu + (b * filter[u +n][v +n]);
                    }
                }
                bmp.setPixel(x,y, Color.rgb(red / moy, green / moy, bleu / moy ));
                //pixel[(width * i) + j ] = Color.rgb(red,green,bleu);
            }
        }

    }

    /**
     * This function calculates the weighted average of a filter
     * @param pixel the table containing the weighted average
     * @return sum of the values in the array
     */
    public int moyenne(int [][]pixel) {
        int moy = 0;
        int n = pixel.length;
        for (int i = 0; i < n; i++){
            for (int j = 0 ; j < pixel[i].length; j++)
                moy += pixel[i][j];
        }
        return moy;
    }

    /*--------------------------------------Render Sctipt---------------------------------------*/

    /*public void ConvulveAverageFilterRS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(this);
        ScriptC_average_convulve ConvulveScript = new ScriptC_average_convulve(rs);

        int width = imagebitmap.getWidth();
        int height = imagebitmap.getHeight();
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);

        Allocation pixels_rs = Allocation.createSized(rs, Element.I32(rs),pixels.length);
        pixels_rs.copyFrom(pixels);
        ConvulveScript.bind_pixels(pixels_rs);

    }*/

}
