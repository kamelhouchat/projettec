package com.example.projettech.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projettech.Model.AuxiliaryFunction;
import com.example.projettech.Model.Convolution;
import com.example.projettech.Model.DynamicExtension;
import com.example.projettech.Model.Equalization;
import com.example.projettech.Model.Filter;
import com.example.projettech.R;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.graphics.Color.HSVToColor;


public class StudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);

        Intent intent = getIntent();
        Bitmap captImage = (Bitmap) intent.getExtras().get("data");

        final ImageView img1 = findViewById(R.id.image1);
        TextView texte1 = findViewById(R.id.texte1);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inMutable = true ;
        o.inScaled = false ;

        final Bitmap imagebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tig, o);
        final Bitmap imagebitmap_copy = Bitmap.createBitmap(imagebitmap);
        img1.setImageBitmap(captImage);

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

        final Bitmap imagebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tig, o);
        final Bitmap imagebitmap_copy = Bitmap.createBitmap(imagebitmap);

        img1.setImageBitmap(imagebitmap_copy);

        Filter filter = new Filter(imagebitmap_copy,this);
        DynamicExtension dynamicExtension = new DynamicExtension(imagebitmap_copy,this);
        Equalization equalization = new Equalization(imagebitmap_copy,this);
        Convolution convolution = new Convolution(imagebitmap_copy,this);

        switch (item.getItemId()){
            case R.id.refresh :
                AuxiliaryFunction.copy_image(imagebitmap,imagebitmap_copy);
                Toast.makeText(this,"Refreshing ...",Toast.LENGTH_LONG).show();
                break ;
            case R.id.togray:
                //toGrays(imagebitmap_copy);
                filter.tograyRS(imagebitmap_copy);
                Toast.makeText(this,"To gray ...",Toast.LENGTH_LONG).show();
                break ;
            case R.id.colorize:
                filter.colorizeRS(imagebitmap_copy,90);
                Toast.makeText(this,"Colorize...",Toast.LENGTH_LONG).show();
                break ;
            case R.id.keepcolor :
                //keepcolor(imagebitmap_copy, Color.rgb(250,12,55), 40);
                filter.KeepColorRS(imagebitmap_copy,25);
                Toast.makeText(this, "Keep Color ...", Toast.LENGTH_LONG).show();
                break ;
            case R.id.contrasteplus :
                dynamicExtension.contrastePlusGrayRS(imagebitmap_copy);
                //contrastePlusGrayLut(imagebitmap_copy);
                Toast.makeText(this, "Cotraste+ ...", Toast.LENGTH_LONG).show();
                break ;
            case R.id.contrastemoins :
                dynamicExtension.contrasteFewerGrayRS(imagebitmap_copy);
                //contrasteFewerGray(imagebitmap_copy);
                Toast.makeText(this, "Cotraste- ...", Toast.LENGTH_LONG).show();
                break;
            case R.id.contrastecouleurplus :
                dynamicExtension.contrastePlusRGB_RS(imagebitmap_copy);
                //contrastePlusCouleurRGB(imagebitmap_copy);
                Toast.makeText(this,"Contraste+ (RGB)...",Toast.LENGTH_LONG).show();
                break;
            case R.id.contrastecouleurplusHSV :
                dynamicExtension.contrastePlusHSV_RS(imagebitmap_copy);
                //contrastePlusCouleurHSV(imagebitmap_copy);
                Toast.makeText(this,"Contraste+ (HSV)...",Toast.LENGTH_LONG).show();
                break;
            case R.id.egalisationNB :
                //egalisationNB(imagebitmap_copy);
                equalization.egalisationGrayRS(imagebitmap_copy);
                Toast.makeText(this, "Egalisation NB+ ...", Toast.LENGTH_LONG).show();
                break;
            case R.id.egalisationcouleur :
                double start = System.currentTimeMillis();
                equalization.egalisationRGBRS(imagebitmap_copy);
                //egalisationcouleur(imagebitmap_copy);
                double end = System.currentTimeMillis() - start ;
                Log.e("Render Scri0pt Time -> ",String.valueOf(end/1000));
                Toast.makeText(this,"Egalisation couleur ...",Toast.LENGTH_LONG).show();
                break;
            case R.id.average_convulve :
                int [][] average_filter = new int [5][5];
                for (int i=0 ;i < 5; i++)
                    for (int j=0 ;j < 5; j++)
                        average_filter [i][j] = 1 ;
                convolution.convolution(imagebitmap_copy, average_filter);
                Toast.makeText(this,"Filtre moyenneur ...", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

