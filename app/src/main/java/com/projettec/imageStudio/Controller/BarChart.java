package com.projettec.imageStudio.Controller;

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
import com.projettec.imageStudio.R;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class BarChart extends AppCompatActivity {

    private ArrayList ColorG;
    private ArrayList ColorR;
    private ArrayList ColorB;
    private ArrayList ArrayIndex;
    private String image_path ;
    private Bitmap captImage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);

        Intent intent = getIntent();
        image_path = intent.getStringExtra("image_path");
        Uri image_uri = Uri.parse(image_path);
        captImage = null;
        try {
            captImage = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), image_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        com.github.mikephil.charting.charts.BarChart barChart = findViewById(R.id.barchart);

        ArrayIndex = new ArrayList();
        for (int i = 0; i<256; i++)
            ArrayIndex.add(Integer.toString(i));

        ColorR = new ArrayList();
        ColorG = new ArrayList();
        ColorB = new ArrayList();


        setBarEntry();


        /*-----------------------*/
        BarDataSet barDataSetR = new BarDataSet(ColorR,"Color.red");
        BarDataSet barDataSetG = new BarDataSet(ColorG,"Color.green");
        BarDataSet barDataSetB = new BarDataSet(ColorB,"Color.blue");
        barDataSetR.setColor(ColorTemplate.rgb("#FF0000"));
        barDataSetG.setColor(ColorTemplate.rgb("#00FF00"));
        barDataSetB.setColor(ColorTemplate.rgb("#0000FF"));

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSetR);
        dataSets.add(barDataSetG);
        dataSets.add(barDataSetB);

        BarData data = new BarData(ArrayIndex,dataSets);
        //data.setGroupSpace(80f);

        data.setValueFormatter(new LargeValueFormatter());
        //data.setValueTypeface(tf);

        barChart.setData(data);
        barChart.invalidate(); // refresh

        /*------------------------*/


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

    public int[][] histoOfThreeColors(){
        int height = captImage.getHeight();
        int width = captImage.getWidth();
        int[] pixels = new int[height*width];
        captImage.getPixels(pixels,0,width,0,0,width,height);

        int[][] tabHisto = new int[3][256];
        for (int i=0; i<height*width; i++){
            tabHisto[0][Color.red(pixels[i])]++;
            tabHisto[1][Color.green(pixels[i])]++;
            tabHisto[2][Color.blue(pixels[i])]++;
        }
        return tabHisto;
    }

    public void setBarEntry(){
        int[][] tabHisto = histoOfThreeColors();
        for (int i = 0; i<256; i++){
            ColorR.add(new BarEntry(tabHisto[0][i],i));
            ColorG.add(new BarEntry(tabHisto[1][i],i));
            ColorB.add(new BarEntry(tabHisto[2][i],i));
        }
    }

}
