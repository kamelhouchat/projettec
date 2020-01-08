package com.example.projettech;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rssample.ScriptC_findMinMax;
import com.android.rssample.ScriptC_findMinMaxHSV;
import com.android.rssample.ScriptC_findMinMaxRGB;
import com.android.rssample.ScriptC_histogramme;
import com.android.rssample.ScriptC_keepcolor;
import com.android.rssample.ScriptC_lutinit_and_contrast_HSV;
import com.android.rssample.ScriptC_lutinit_and_contrast_gray;
import com.android.rssample.ScriptC_lutinit_and_contrast_RGB;
import com.android.rssample.ScriptC_lutinit_and_equalize_gray;
import com.android.rssample.ScriptC_lutinit_and_equalize_RGB;
import com.android.rssample.ScriptC_togray;
import com.android.rssample.ScriptC_colorize;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.renderscript.Allocation;
import androidx.renderscript.Int2;
import androidx.renderscript.RenderScript;

import static android.graphics.Color.HSVToColor;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView img1 = findViewById(R.id.image1);
        TextView texte1 = findViewById(R.id.texte1);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inMutable = true ;
        o.inScaled = false ;

        final Bitmap imagebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plage, o);
        final Bitmap imagebitmap_copy = Bitmap.createBitmap(imagebitmap);
        img1.setImageBitmap(imagebitmap_copy);

        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();

        texte1.setTextSize(20);
        texte1.setText(height+" Px , "+width+" Px");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.contraste || item.getItemId() == R.id.contrastecouleur
            || item.getItemId() == R.id.egalisation){return super.onOptionsItemSelected(item);}
        final ImageView img1 = findViewById(R.id.image1);
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inMutable = true ;
        o.inScaled = false ;

        final Bitmap imagebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plage, o);
        final Bitmap imagebitmap_copy = Bitmap.createBitmap(imagebitmap);

        img1.setImageBitmap(imagebitmap_copy);

        switch (item.getItemId()){
            case R.id.refresh :
                copy_image(imagebitmap,imagebitmap_copy);
                Toast.makeText(this,"Refreshing ...",Toast.LENGTH_LONG).show();
                break ;
            case R.id.togray:
                //toGrays(imagebitmap_copy);
                tograyRS(imagebitmap_copy);
                Toast.makeText(this,"To gray ...",Toast.LENGTH_LONG).show();
                break ;
            case R.id.colorize:
                colorizeRS(imagebitmap_copy,90);
                Toast.makeText(this,"Colorize...",Toast.LENGTH_LONG).show();
                break ;
            case R.id.keepcolor :
                //keepcolor(imagebitmap_copy, Color.rgb(250,12,55), 40);
                KeepColorRS(imagebitmap_copy,25);
                Toast.makeText(this, "Keep Color ...", Toast.LENGTH_LONG).show();
                break ;
            case R.id.contrasteplus :
                contrastePlusGrayRS(imagebitmap_copy);
                //contrastePlusGrayLut(imagebitmap_copy);
                Toast.makeText(this, "Cotraste+ ...", Toast.LENGTH_LONG).show();
                break ;
            case R.id.contrastemoins :
                contrasteFewerGrayRS(imagebitmap_copy);
                //contrasteFewerGray(imagebitmap_copy);
                Toast.makeText(this, "Cotraste- ...", Toast.LENGTH_LONG).show();
                break;
            case R.id.contrastecouleurplus :
                contrastePlusRGB_RS(imagebitmap_copy);
                //contrastePlusCouleurRGB(imagebitmap_copy);
                Toast.makeText(this,"Contraste+ (RGB)...",Toast.LENGTH_LONG).show();
                break;
            case R.id.contrastecouleurplusHSV :
                contrastePlusHSV_RS(imagebitmap_copy);
                //contrastePlusCouleurHSV(imagebitmap_copy);
                Toast.makeText(this,"Contraste+ (HSV)...",Toast.LENGTH_LONG).show();
                break;
            case R.id.egalisationNB :
                //egalisationNB(imagebitmap_copy);
                egalisationGrayRS(imagebitmap_copy);
                Toast.makeText(this, "Egalisation NB+ ...", Toast.LENGTH_LONG).show();
                break;
            case R.id.egalisationcouleur :
                double start = System.currentTimeMillis();
                egalisationRGBRS(imagebitmap_copy);
                //egalisationcouleur(imagebitmap_copy);
                double end = System.currentTimeMillis() - start ;
                Log.e("Render Scri0pt Time -> ",String.valueOf(end/1000));
                Toast.makeText(this,"Egalisation couleur ...",Toast.LENGTH_LONG).show();
                break;
            case R.id.average_convulve :
                average_filter(imagebitmap_copy);
                Toast.makeText(this,"Filtre moyenneur ...", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /*---------------------------------------Fonction Auxiliaire--------------------------------------*/

    /**
     * Function to copy a bitmap image to another
     * @param old -> The image we want to copy
     * @param new_image -> The destination image
     */
    public void copy_image(Bitmap old , Bitmap new_image){
        int[] pixels = new int[old.getHeight() * old.getWidth()];
        old.getPixels(pixels,0,old.getWidth(),0,0,old.getWidth(),old.getHeight());
        new_image.setPixels(pixels,0,new_image.getWidth(),0,0,new_image.getWidth(),new_image.getHeight());
    }

    /**
     * Function that separates an RGB color into three components (R,G and B)
     * @param rgb -> Color coded in RGB
     * @return an array that contains r, g and b separately
     */
    public int[] RGBtoR_G_B(int rgb){
        int r = Color.red(rgb);
        int g = Color.green(rgb);
        int b = Color.blue(rgb);
        int[] r_g_b = new int[]{r, g, b};
        return  r_g_b;
    }

    /**
     * function that returns True if the hue we pass in parameter is close to the hue located, else False
     * @param reper the hue we spot
     * @param val the hue we want to compare
     * @param radius the margin that is accepted
     * @return
     */
    public boolean Is_like(float reper , float val, int radius){
        final int RADIUS = radius ;
        float min_accept;
        float max_accept;
        max_accept = (reper + RADIUS)%360 ;
        min_accept = reper - RADIUS;
        if (min_accept < 0)
            min_accept = 360 + min_accept;

        return (( 0 < val && val < max_accept) || ( min_accept < val && val < 360 ));
    }

    /**
     * Function that converts from RGB to HSV
     * @param redi the red value
     * @param greeni the green value
     * @param bluei the blue value
     * @param hsv a floating array of 3 empty boxes
     */
    public void RGBToHSV_new(int redi , int greeni , int bluei , float[] hsv){
        float cmax = 0, cmin = 0, delta, saturation, value, hue = 0;
        float red = (float) (redi / 255.0);
        float green = (float) (greeni / 255.0);
        float blue = (float) (bluei / 255.0);
        cmin = Math.min(Math.min(red,green),blue);
        cmax = Math.max(Math.max(red,green),blue);
        delta = cmax - cmin;
        if (cmax == 0) {
            for (int i = 0; i < 3; i++) {
                hsv[i] = 0;
            }
            return;
        }
        else if (cmax == red)
            hue = (float) (60.0 * (((green - blue) / delta)));
        else if (cmax == green)
            hue = (float) (60.0 * (((blue - red) / delta) + 2));
        else if (cmax == blue)
            hue = (float) (60.0 * (((red - green) / delta) + 4));
        hsv[0] = hue;
        saturation = delta / cmax;
        value = (cmax);
        hsv[1] = saturation;
        hsv[2] = value;
    }

    /**
     * Function that converts from HSV to RGB
     * @param hsvColor An array of float that contains the three HSV values of the color
     * @return Color in RGB
     */
    public int HSVToColor_new(float[] hsvColor){
        float r, g, b, k;
        k = (float) (5 + (hsvColor[0] / 60.0)) % 6;
        r = (int) ((hsvColor[2] - hsvColor[2] * hsvColor[1] * Math.max(Math.min(Math.min(k, 4 - k), 1), 0)) * 255.0);
        k = (float) (3 + (hsvColor[0] / 60.0)) % 6;
        g = (int) ((hsvColor[2] - hsvColor[2] * hsvColor[1] * Math.max(Math.min(Math.min(k, 4 - k), 1), 0)) * 255.0);
        k = (float) (1 + (hsvColor[0] / 60.0)) % 6;
        b = (int) ((hsvColor[2] - hsvColor[2] * hsvColor[1] * Math.max(Math.min(Math.min(k, 4 - k), 1), 0)) * 255.0);
        int color  = Color.rgb((int)r,(int)g,(int)b);
        return color;
    }

    /**
     * Function that calculates the histogram from an array that contains the pixels of the a gray image
     * @param pixels the table which contains the pixels of the image
     * @return the image histogram (table of 256 boxes)
     */
    public int[] histogramme(int[] pixels){
        int[] histogramme = new int[256];
        for (int i = 0 ; i < pixels.length ; i++){
            int color = Color.red(pixels[i]);
            histogramme[color] +=1;
        }
        return histogramme ;
    }

    /**
     * Function that calculates the histogram from an array that contains the pixels of the a color image
     * in HSV mode (we take the biggest value between h, s and v)
     * @param pixels the table which contains the pixels of the image
     * @return the image histogram (table of 256 boxes)
     */
    public int[] histogrammeHSV(int[] pixels){
        int[] histogramme = new int[256];
        int r, g, b, max;
        for (int i = 0; i < pixels.length; i++) {
            r = Color.red(pixels[i]);
            g = Color.green(pixels[i]);
            b = Color.blue(pixels[i]);
            max = r > g ? r : g;
            max = max > b ? max : b;
            histogramme[max]++;
        }
        return histogramme;
    }

    /**
     * Function that calculates the min and max of a histogram of an image (in color or black and white)
     * from its pixel array
     * @param pixels the table which contains the pixels of the image
     * @param hsv boolean to say if we want the histogram in hsv or in gray
     * @return an array of 2 boxes which contains the min and the max
     */
    public int[] min_max_histo(int [] pixels, boolean hsv){
        int min = 0 ; boolean min_t = false ;
        int max = 0 ; int[] histo = new int[256];
        if (hsv){ histo = histogrammeHSV(pixels);}
        else { histo = histogramme(pixels);}
        for (int i = 0 ; i < 256 ; i++){
            if (histo[i] > 0){
                if (!min_t){
                    min = i ;
                    min_t = true ;
                }
                max = i ;
            }
        }
        int[] min_max = new int[2];
        min_max = new int[]{min, max};
        return min_max ;
    }

    /**
     * Function that calculates the min and max from a histogram
     * @param histo the histogram
     * @return an array of 2 boxes which contains the min and the max
     */
    public int[] minmax(int[] histo){
        int min=0 , max=0 ; boolean min_t = false ;
        for (int i = 0 ; i < 256; i++){
            if (histo[i] > 0){
                if (!min_t){
                    min = i ;
                    min_t = true ;
                }
                max = i ;
            }
        }
        return new int[]{min, max};
    }

    /**
     * Function which initializes the LUT table using the current view formula
     * and the reverse formula for decrement
     * @param minmax an array of 2 boxes which contains the min and the max
     * @param increase boolean to say if we want to increase or decrease the contrast of the image
     * @return the LUT table
     */
    public int[] LUT_Init(int [] minmax, boolean increase) {
        int[] LUT = new int[256];
        for (int ng = 0; ng < 256; ng++){
            if (increase)
                LUT[ng] = (256 * (ng - minmax[0])) / (minmax[1] - minmax[0]);
            else
                LUT[ng] = ((ng * (minmax[1] - minmax[0])) / 255) + minmax[0];
        }
        return LUT;
    }

    /*------------------------------------------------------------------------------------------------*/

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
            r_g_b = RGBtoR_G_B(pixels[i]);
            RGBToHSV_new(r_g_b[0],r_g_b[1],r_g_b[2],h);
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

        int[] r_g_b_reper = RGBtoR_G_B(rgb);
        float[] hh = new float[3];
        RGBToHSV_new(r_g_b_reper[0],r_g_b_reper[1],r_g_b_reper[2],hh);
        float reper = hh[0];  // Le H de la couleur passer en paramétre

        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        for (int i = 0 ; i < height * width ; i++){
            int[] r_g_b_image = RGBtoR_G_B(pixels[i]);

            RGBToHSV_new(r_g_b_image[0],r_g_b_image[1],r_g_b_image[2],h);
            if (!Is_like(reper , h[0], radius)){
                int new_color = (int) (r_g_b_image[0]*0.3 + r_g_b_image[1]*0.59 + r_g_b_image[2]*0.11);
                pixels[i]= Color.rgb(new_color,new_color,new_color);
            }
        }
        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function that increases the contrast of a gray image (without using the lut table)
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void  contrasteplus(Bitmap imagebitmap){
        tograyRS(imagebitmap);
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] min_max = min_max_histo(pixels, false);
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
        tograyRS(imagebitmap);
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int[] pixels = new int[height * width];
        imagebitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] min_max = min_max_histo(pixels, false);
        if (min_max[0] == min_max[1]) return;
        int[] LUT = new int[256];
        // INITIALISATION DE LA LUT
        LUT = LUT_Init(min_max, true);
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
        tograyRS(imagebitmap);
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] min_max = min_max_histo(pixels, false);
        int diff = min_max[1]-min_max[0];
        int perc = (diff*5)/100;
        min_max[0] = min_max[0]+perc ;
        min_max[1] = min_max[1]-perc ;

        if (min_max[0] < 0 || min_max[1] > 255) return;
        int[] LUT = new int[256];
        // INITIALISATION DE LA LUT
        LUT = LUT_Init(min_max,false);
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
        int[] min_max_R = minmax(histoR);
        int minR = min_max_R[0] ; int maxR = min_max_R[1] ;
        int[] min_max_G = minmax(histoG);
        int minG = min_max_G[0]; int maxG = min_max_G[1];
        int[] min_max_B = minmax(histoB);
        int minB = min_max_B[0]; int maxB = min_max_B[0] ;

        int[] LUTR = LUT_Init(min_max_R, true);
        int[] LUTG = LUT_Init(min_max_G, true);
        int[] LUTB = LUT_Init(min_max_B, true);

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

        int[] min_max= min_max_histo(pixels, true);
        if (min_max[0] == min_max[1]) return;

        int[] LUT = LUT_Init(min_max,true);
        for (int i = 0; i < height * width; i++) {
            RGBToHSV_new(Color.red(pixels[i]), Color.green(pixels[i]), Color.blue(pixels[i]), hsv);
            color = LUT[ (int) (hsv[2] * 255)];
            hsv[2] = color / 255.0f ;
            pixels[i] = HSVToColor_new(hsv);
        }
        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * Function to apply histogram equalization to a gray image
     * (In JAVA)
     * @param imagebitmap a Bitmap image
     */
    public void egalisationNB(Bitmap imagebitmap){
        tograyRS(imagebitmap);
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int accumulator = 0;
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] histo = histogramme(pixels);
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

    /**
     * Function which returns the neighbors of the pixel pass in parameter (3x3)
     * @param pixels the pixel array of the image
     * @param pos the current position
     * @param width the width of the image
     * @return a 3x3 array which contains the neighbors of the desired pixel
     */
    public int[] Convulve_3x3(int[] pixels, int pos, int width){
        int[] convulve = new int[9];
        convulve[0] = pixels[pos-width-1];
        convulve[1] = pixels[pos-width];
        convulve[2] = pixels[pos-width+1];
        convulve[3] = pixels[pos-1];
        convulve[4] = pixels[pos];
        convulve[5] = pixels[pos+1];
        convulve[6] = pixels[pos+width-1];
        convulve[7] = pixels[pos+width];
        convulve[8] = pixels[pos+width+1];
        return convulve;
    }

    /**
     * Function that tests whether a pixel is in a border
     * @param pixels the pixel array of the image
     * @param pos the current position
     * @param width the width of the image
     * @param height the height of the image
     * @return true if the pixel is in a border else false
     */
    public boolean in_border(int[] pixels, int pos, int width, int height){
        return  ( pos%width == 0 || pos < width || (pos+1)%width == 0 || pos >= width*(height-1) );
    }

    /**
     * Function which applies the averaging filter to the image passed in parameter
     * @param imagebitmap a Bitmap image
     */
    public void average_filter(Bitmap imagebitmap){
        int height = imagebitmap.getHeight();
        int width = imagebitmap.getWidth();
        int[] pixels = new int[height*width];
        imagebitmap.getPixels(pixels,0,width,0,0,width,height);
        int[] new_pixels = new int[height*width];
        for (int i = width; i < height*width; i++){
            if (!in_border(pixels, i, width, height)) {
                int averageR = 0, averageG = 0, averageB = 0 ;
                int[] convulve = Convulve_3x3(pixels, i, width);
                for (int j = 0; j<convulve.length; j++){
                    averageR += Color.red(convulve[j]);
                    averageG += Color.green(convulve[j]);
                    averageB += Color.blue(convulve[j]);
                }
                pixels[i] = Color.rgb(averageR/9,averageG/9,averageB/9);
            }
        }
        imagebitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    /*------------------------------------------------------------------------------------------------*/

    /**
     * Function which converts the image pass into parameter in gray
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void tograyRS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(this);

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
        RenderScript rs = RenderScript.create(this);

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
        RenderScript rs = RenderScript.create(this);

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

    /**
     * Function that increases the contrast of a gray image
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void contrastePlusGrayRS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(this);

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
        RenderScript rs = RenderScript.create(this);

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
        RenderScript rs = RenderScript.create(this);

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
        RenderScript rs = RenderScript.create(this);

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

    /**
     * Function to apply histogram equalization to a gray image
     * (Using RenderScript)
     * @param imagebitmap a Bitmap image
     */
    public void egalisationGrayRS(Bitmap imagebitmap){
        RenderScript rs = RenderScript.create(this);

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
        RenderScript rs = RenderScript.create(this);

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

