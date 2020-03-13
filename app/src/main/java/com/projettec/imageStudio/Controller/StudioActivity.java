package com.projettec.imageStudio.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.projettec.imageStudio.Controller.Fragment.Studio_fragment;
import com.projettec.imageStudio.Controller.Fragment.Plus_fragment;
import com.projettec.imageStudio.R;
import com.tapadoo.alerter.Alerter;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;



public class StudioActivity extends AppCompatActivity {

    ImageView image ;
    Toolbar toolbar ;
    String image_path;
    Uri image_uri ;
    Bitmap captImage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studio_final_test);
        contextOfApplication = getApplicationContext();
        /**
         * Implement toolbar layout after including it in studio_activity layout
         */

        /*toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /**
         * Load a Bitmap Image (Passed by mainactivity)
         */
        Intent intent = getIntent();
        image_path = intent.getStringExtra("imagePath");
        image_uri = Uri.parse(image_path);
        captImage = null;
        try {
            captImage = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), image_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alerter.create(this)
                .setText("Chargement terminé")
                .setBackgroundColorInt(Color.parseColor("#233ED8"))
                .setDuration(300)
                .show();

        int height = captImage.getHeight();
        int width = captImage.getWidth();

        final BubbleNavigationLinearView bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setTypeface(Typeface.createFromAsset(getAssets(), "rubik.ttf"));

        final ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                pager.setCurrentItem(position, true);
            }
        });


    }//Remove this to restore last version

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 0: return Studio_fragment.newInstance(image_path);
                case 1: return Plus_fragment.newInstance("SecondFragment, Instance 1",image_path);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

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

