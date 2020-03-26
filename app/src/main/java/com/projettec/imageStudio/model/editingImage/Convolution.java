package com.projettec.imageStudio.model.editingImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import com.projettec.imageStudio.model.editingImage.Filter;

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
        int som = somme(filter);
        for (int y = n; y < height-n; y++){
            for (int x = n ; x < width - n; x++){
                red = green = bleu = 0;
                for(int u = -n; u <= n; u++){
                    for (int v = -n; v <= n; v++ ){
                        int color = bmp.getPixel(x + u,  y + v);
                        int r = Color.red(color);
                        int g = Color.green(color);
                        int b = Color.blue(color);

                        red = red + (r * filter[u +n][v +n]);
                        green = green + (g * filter[u +n][v +n]);
                        bleu = bleu + (b * filter[u +n][v +n]);
                    }
                }
                bmp.setPixel(x,y, Color.rgb(red / som, green / som, bleu / som ));
            }
        }

    }

    /**
     * Permet de convoluer les valeurs de l'image
     * @param bmp image à convoluer
     * @param filter filtre à appliquer sur l'image
     */
    public static void convolutions(Bitmap bmp, int [][] filter){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int [] pixel = new int[width * height];
        int red = 0, green = 0, bleu = 0;
        int sizeFilter = filter.length;
        int n = sizeFilter /2;

        int som = somme(filter);

        bmp.getPixels(pixel,0, width , 0, 0,width ,height);
        for (int y = n; y < height-n; y++){
            for (int x = n ; x < width - n; x++){
                red = green = bleu = 0;
                for(int u = -n; u <= n; u++){
                    for (int v = -n; v <= n; v++ ){
                        int color= pixel[((y + u) * width + (x + v))];
                        int r = Color.red(color);
                        int g = Color.green(color);
                        int b = Color.blue(color);

                        red = red + (r * filter[u +n][v +n]);
                        green = green + (g * filter[u +n][v +n]);
                        bleu = bleu + (b * filter[u +n][v +n]);
                    }
                }
                pixel[(width * y) + x] = Color.rgb(red / som, green / som, bleu / som );
            }
        }
        bmp.setPixels(pixel,0, width , 0, 0,width ,height);
    }

    /**
     * This function calculates the weighted average of a filter
     * @param pixel the table containing the weighted average
     * @return sum of the values in the array
     */
    public static int somme(int [][]pixel) {
        int som = 0;
        int n = pixel.length;
        for (int i = 0; i < n; i++){
            for (int j = 0 ; j < pixel[i].length; j++)
                som += pixel[i][j];
        }
        return som;
    }

    //utilisation du contours
    int gx[][] =  {{1,0,1},{-2,0,2},{-1,0,1}};
    int gy[][] =  {{-1,-2,-1},{0,0,0},{1,2,1}};
    /**
     *
     * @param bmp
     * @param gx
     * @param gy
     */
    public void contours(Bitmap bmp, int gx[][] ,int gy[][]) {
        Filter filter = new Filter(bmp,context);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] newpixels = new int[width * height];
        int red = 0, red1 = 0;
        int sizeGx = gx.length;

        int n = sizeGx / 2;

        filter.tograyRS(bmp);
        bmp.getPixels(pixels,0, width , 0, 0,width ,height);

        for (int y = n; y < height - n; y++) {
            for (int x = n; x < width - n; x++) {
                red = 0;
                red1 = 0;
                for (int u = -n; u <= n; u++) {
                    for (int v = -n; v <= n; v++) {
                        int color = pixels[((y + u) * width + (x + v))];
                        int r = Color.red(color);

                        red = red + (r * gx[u + n][v + n]);
                        red1 = red1 + (r * gy[u + n][v + n]);
                    }
                }
                int modR = (int) Math.sqrt((red * red) + (red1 * red1));
                if (modR > 255) modR = 255;

                newpixels[(width * y) + x ] = Color.rgb(modR, modR, modR);
            }
        }
        bmp.setPixels(newpixels,0, width , 0, 0,width ,height);
    }


    /*--------------------------------------RenderScript---------------------------------------*/

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
