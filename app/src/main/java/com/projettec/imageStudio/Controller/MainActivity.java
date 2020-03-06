package com.projettec.imageStudio.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.projettec.imageStudio.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;


import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    ImageView background , clover , logoUniversite;
    LinearLayout logo , hometext , menus ;
    Animation frombuttom ,logoAnimation;

    Uri image_uri ;

    // Random code that identifies the result of the picker
    static final int PICKER_REQUEST_CODE = 1;

    // List that will contain the selected files/videos
    List<Uri> mSelected;

    static final int REQUEST_CODE = 123 ;
    static final int REQUEST_CAMERA = 100;

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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Intent studioIntent = new Intent(this, StudioActivity.class);
            try {
                studioIntent.putExtra("imagePath", image_uri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(studioIntent);
            overridePendingTransition(R.anim.slidein, R.anim.slideout);
        }
        if (requestCode == PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Intent studioIntent = new Intent(this, StudioActivity.class);
            try {
                studioIntent.putExtra("imagePath", mSelected.get(0).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(studioIntent);
            overridePendingTransition(R.anim.slidein, R.anim.slideout);
        }
    }

    public void LoadImage(View view){
        Matisse.from(MainActivity.this)
                .choose(MimeType.ofImage())
                .countable(false)
                .maxSelectable(1)
                .showSingleMediaType(true)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .maxOriginalSize(4)
                .autoHideToolbarOnSingleTap(true)
                .forResult(PICKER_REQUEST_CODE);
    }

    public void TakeImage(View view){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent,REQUEST_CAMERA);
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
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length >= 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                    Toasty.success(this,"Autorisation Accordée !",Toast.LENGTH_LONG).show();
                }
                else
                    Toasty.error(this,"Veuillez autoriser l'accès",Toast.LENGTH_LONG).show();
                    checkPermission();
                break ;
        }
    }

}
