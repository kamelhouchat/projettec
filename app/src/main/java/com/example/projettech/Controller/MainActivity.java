package com.example.projettech.Controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.projettech.R;

public class MainActivity extends AppCompatActivity {

    ImageView background , clover , logoUniversite;
    LinearLayout logo , hometext , menus ;
    Animation frombuttom ,logoAnimation;
    static final int REQUEST_CODE = 123 ;
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

        checkPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100){
            Bitmap captImage = (Bitmap) data.getExtras().get("data");
            Intent studioIntent = new Intent(this,StudioActivity.class);
            studioIntent.putExtras(data);
            startActivity(studioIntent);
            overridePendingTransition(R.anim.slidein,R.anim.slideout);
        }
    }

    public void LoadImage(View view){
        this.finish();
    }

    public void TakeImage(View view){
        /*Intent intent = new Intent(this,StudioActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein,R.anim.slideout);*/
        checkPermission();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,100);

    }

    public boolean isGranted(){
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    public void checkPermission(){
        if (!isGranted()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Permission d'accès");
            builder.setMessage("Nous avons besoin de votre permission pour accéder a la caméra et la galerie");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },
                            REQUEST_CODE
                    );
                }
            });
            builder.setNegativeButton("Annuler",null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

}
