package com.projetTec.imageStudio.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.projetTec.imageStudio.R;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

/**
 * <p>This class is used to display the histogram of an image whose path has passed through a bundle. </p>
 *
 * @author Fall.E-P
 * @see com.projetTec.imageStudio.controller.fragments.Plus_fragment
 * @see com.github.mikephil.charting.charts.BarChart
 */

@SuppressWarnings("FieldCanBeLocal")
public class HistogramActivity extends AppCompatActivity {

    //ArrayList that will contain the green color
    private ArrayList colorG;

    //ArrayList that will contain the red color
    private ArrayList colorR;

    //ArrayList that will contain the blue color
    private ArrayList colorB;

    //The array index
    private ArrayList arrayIndex;

    //The image path
    private String imagePath;

    //The loaded image (with uses of imagePath)
    private Bitmap captImage;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra("image_path");
        Uri image_uri = Uri.parse(imagePath);
        captImage = null;
        try {
            captImage = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), image_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        com.github.mikephil.charting.charts.BarChart barChart = findViewById(R.id.bar_chart);

        arrayIndex = new ArrayList();
        for (int i = 0; i < 256; i++)
            arrayIndex.add(Integer.toString(i));

        colorR = new ArrayList();
        colorG = new ArrayList();
        colorB = new ArrayList();

        setBarEntry();

        BarDataSet barDataSetR = new BarDataSet(colorR, "Color.red");
        BarDataSet barDataSetG = new BarDataSet(colorG, "Color.green");
        BarDataSet barDataSetB = new BarDataSet(colorB, "Color.blue");
        barDataSetR.setColor(ColorTemplate.rgb("#FF0000"));
        barDataSetG.setColor(ColorTemplate.rgb("#00FF00"));
        barDataSetB.setColor(ColorTemplate.rgb("#0000FF"));

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSetR);
        dataSets.add(barDataSetG);
        dataSets.add(barDataSetB);

        BarData data = new BarData(arrayIndex, dataSets);

        data.setValueFormatter(new LargeValueFormatter());

        barChart.setData(data);
        barChart.invalidate(); // refresh

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.animateXY(2000, 2000);
        barChart.animateY(1500);
        barChart.setDoubleTapToZoomEnabled(true);
        barChart.setPinchZoom(false); // scaling can now only be done on x- and y-axis separately
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        barChart.setDescription("Histogramme : Niveau de gris");
    }

    /**
     * <p>
     * This method allows to calculate the three channel's histograms of loaded image.
     * </p>
     *
     * @return Matrix that contains the R G  and B histogram's.
     * @see com.github.mikephil.charting.charts.BarChart
     */
    private int[][] histoOfThreeColors() {
        int height = captImage.getHeight();
        int width = captImage.getWidth();
        int[] pixels = new int[height * width];
        captImage.getPixels(pixels, 0, width, 0, 0, width, height);

        int[][] tabHisto = new int[3][256];
        for (int i = 0; i < height * width; i++) {
            tabHisto[0][Color.red(pixels[i])]++;
            tabHisto[1][Color.green(pixels[i])]++;
            tabHisto[2][Color.blue(pixels[i])]++;
        }
        return tabHisto;
    }

    /**
     * <p>
     * This method allows to set different {@link com.github.mikephil.charting.charts.BarChart} entries(R,G,B).
     * </p>
     *
     * @see com.github.mikephil.charting.charts.BarChart
     */
    @SuppressWarnings("unchecked")
    private void setBarEntry() {
        int[][] tabHisto = histoOfThreeColors();
        for (int i = 0; i < 256; i++) {
            colorR.add(new BarEntry(tabHisto[0][i], i));
            colorG.add(new BarEntry(tabHisto[1][i], i));
            colorB.add(new BarEntry(tabHisto[2][i], i));
        }
    }
}
