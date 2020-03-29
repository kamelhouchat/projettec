package com.projettec.imageStudio.controller;

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
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/**
 * <p>
 * This is the base activity, the activity manages:
 *     <ol>
 *         <li>Application launch animations</li>
 *         <li>Granting access permissions</li>
 *         <li>Loading the image from the gallery and the camera</li>
 *     </ol>
 * </p>
 *
 * @author Kamel.H
 * @see StudioActivity
 * @see R.anim
 */

public class MainActivity extends AppCompatActivity {

    //ImageView loaded from the layout
    private ImageView background, clover, logoUniversity;

    //LinearLayout loaded from the layout
    private LinearLayout logo, homeText, menus;

    //Animation loaded from R.anim
    private Animation fromBottom, logoAnimation;

    //The uri of the loaded image from camera
    private Uri imageUri;

    //Random code that identifies the result of the picker
    private static final int PICKER_REQUEST_CODE = 1;
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_CAMERA = 100;

    //List that will contain the selected photos
    private List<Uri> mSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromBottom = AnimationUtils.loadAnimation(this, R.anim.frombuttom);
        logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logoanimation);

        initView();

        background.animate().translationY(-1900).setDuration(800).setStartDelay(1000);
        clover.animate().alpha(0).setDuration(800).setStartDelay(700);
        logo.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(1000);
        homeText.startAnimation(fromBottom);
        menus.startAnimation(fromBottom);
        logoUniversity.startAnimation(logoAnimation);

        checkPermission();
    }

    /**
     * The method that initializes all views
     *
     * @see LinearLayout
     * @see ImageView
     */
    private void initView() {
        background = findViewById(R.id.background_main);
        clover = findViewById(R.id.clover);
        logoUniversity = findViewById(R.id.university);
        logo = findViewById(R.id.logo);
        homeText = findViewById(R.id.home_text);
        menus = findViewById(R.id.menus);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Intent studioIntent = new Intent(this, StudioActivity.class);
            try {
                studioIntent.putExtra("imagePath", imageUri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(studioIntent);
            overridePendingTransition(R.anim.slidein, R.anim.slideout);
        }
        if (requestCode == PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(Objects.requireNonNull(data));
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

    /**
     * Method using the matisse library to load the image from the gallery
     *
     * @param view The view that corresponds to the gallery button
     */
    public void loadImage(View view) {
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

    /**
     * Method that opens the camera intent and take new picture
     *
     * @param view the view that corresponds to the camera button
     */
    public void takeImage(View view) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    /**
     * Method that checks if permissions are granted
     *
     * @return true if yes, false if not
     */
    private boolean isGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Method that asks the user to grant permissions if they are not granted
     */
    private void checkPermission() {
        if (!isGranted()) {
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
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length >= 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(this, "Autorisation Accordée !", Toast.LENGTH_LONG).show();
            } else
                Toasty.error(this, "Veuillez autoriser l'accès", Toast.LENGTH_LONG).show();
            checkPermission();
        }
    }

}
