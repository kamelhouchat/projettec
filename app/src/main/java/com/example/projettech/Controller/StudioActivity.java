package com.example.projettech.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projettech.Model.AuxiliaryFunction;
import com.example.projettech.Model.Convolution;
import com.example.projettech.Model.DynamicExtension;
import com.example.projettech.Model.Equalization;
import com.example.projettech.Model.Filter;
import com.example.projettech.R;
import com.gauravk.bubblebarsample.adapters.ScreenSlidePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.tapadoo.alerter.Alerter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.util.ArrayList;

import com.example.projettech.Controller.Utilities.Fragment.ScreenSlidePageFragment;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;



public class StudioActivity extends AppCompatActivity {

    ImageView image ;
    Toolbar toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studio_final_test);
        /**
         * Implement toolbar layout after including it in studio_activity layout
         */

        /*toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /**
         * Load a Bitmap Image (Passed by mainactivity)
         */
        Intent intent = getIntent();
        String image_path = intent.getStringExtra("imagePath");
        Uri image_uri = Uri.parse(image_path);
        Bitmap captImage = null;
        try {
            captImage = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), image_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alerter.create(this)
                .setText("Chargement terminé")
                .setBackgroundColorInt(Color.parseColor("#233ED8"))
                .show();

        int height = captImage.getHeight();
        int width = captImage.getWidth();

        ArrayList<ScreenSlidePageFragment> fragList = new ArrayList<>();
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.home), R.color.blue_grey_inactive));
        fragList.add(ScreenSlidePageFragment.newInstance(getString(R.string.search), R.color.blue_inactive));
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(fragList, getSupportFragmentManager());

        final BubbleNavigationLinearView bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setTypeface(Typeface.createFromAsset(getAssets(), "rubik.ttf"));

        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                bubbleNavigationLinearView.setCurrentActiveItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                viewPager.setCurrentItem(position, true);
            }
        });

        image = findViewById(R.id.imageView);
        image.setImageBitmap(captImage);

        /*BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);

        bnve.enableAnimation(true);
        bnve.enableItemShiftingMode(true);
        bnve.enableShiftingMode(true);*/


    }//Remove this to restore last version

        /*final ImageView img1 = findViewById(R.id.image1);
        TextView texte1 = findViewById(R.id.texte1);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inMutable = true ;
        o.inScaled = false ;

        final Bitmap imagebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tig, o);
        final Bitmap imagebitmap_copy = Bitmap.createBitmap(imagebitmap);
        img1.setImageBitmap(captImage);



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
    }*/
}

