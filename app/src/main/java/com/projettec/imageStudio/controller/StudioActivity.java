package com.projettec.imageStudio.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.projettec.imageStudio.controller.fragments.Studio_fragment;
import com.projettec.imageStudio.controller.fragments.Plus_fragment;
import com.projettec.imageStudio.R;
import com.tapadoo.alerter.Alerter;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

/**
 * <p>
 * The activity manages:
 *     <ol>
 *         <li>Loading the image sent by activity main</li>
 *         <li>Loading the navigation bar</li>
 *         <li>Management of the navigation bar using ViewPager</li>
 *         <li>Hide system bars</li>
 *     </ol>
 * </p>
 *
 * @author Kamel.H
 * @see Studio_fragment
 * @see Plus_fragment
 * @see com.gauravk.bubblenavigation.IBubbleNavigation
 * @see ViewPager
 */

public class StudioActivity extends AppCompatActivity {

    //The context of the activity
    public static Context contextOfApplication;

    //The image path
    private String image_path;

    //The activity decor view
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studio_final_test);

        contextOfApplication = getApplicationContext();

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        //Load a Bitmap Image (Passed by MainActivity)
        Intent intent = getIntent();
        image_path = intent.getStringExtra("imagePath");

        Alerter.create(this)
                .setText("Chargement terminé")
                .setBackgroundColorRes(R.color.blue_grey_inactive)
                .setDuration(300)
                .show();

        final BubbleNavigationLinearView bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setTypeface(Typeface.createFromAsset(getAssets(), "rubik.ttf"));

        final ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                bubbleNavigationLinearView.setCurrentActiveItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                pager.setCurrentItem(position, true);
            }
        });

    }

    /**
     * <p>The class is an adapter of the view pager
     *
     * @author Kamel.H
     * @see Studio_fragment
     * @see Plus_fragment
     * @see FragmentPagerAdapter
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return Studio_fragment.newInstance(image_path);
                case 1:
                    return Plus_fragment.newInstance(image_path);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    /**
     * <p>Static method which returns the context of the activity
     * @return
     */
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    /**
     * <p>Method which hide system bars
     *
     * @return a single flag combination
     * @see View
     */
    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

}

