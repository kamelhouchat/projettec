package com.example.projettech.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.projettech.R;

public class MainActivity extends AppCompatActivity {

    ImageView background , clover , logoUniversite;
    LinearLayout logo , hometext , menus ;
    Animation frombuttom ,logoAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frombuttom = AnimationUtils.loadAnimation(this,R.anim.frombuttom);
        logoAnimation = AnimationUtils.loadAnimation(this,R.anim.logoanimation);

        background = (ImageView) findViewById(R.id.background_main);
        clover = (ImageView) findViewById(R.id.clover);
        logoUniversite = (ImageView) findViewById(R.id.universite);
        logo = (LinearLayout) findViewById(R.id.logo);
        hometext = (LinearLayout) findViewById(R.id.hometext);
        menus = (LinearLayout) findViewById(R.id.menus);

        background.animate().translationY(-1900).setDuration(800).setStartDelay(1000);
        clover.animate().alpha(0).setDuration(800).setStartDelay(700);
        logo.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(1000);
        hometext.startAnimation(frombuttom);
        menus.startAnimation(frombuttom);
        logoUniversite.startAnimation(logoAnimation);
    }
}
