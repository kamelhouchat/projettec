package com.projettec.imageStudio.controller.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.projettec.imageStudio.controller.adapters.EditingToolRecyclerViewAdapter;

import com.projettec.imageStudio.controller.adapters.FilterRecyclerViewAdapter;
import com.projettec.imageStudio.controller.StudioActivity;
import com.projettec.imageStudio.model.animation.ViewAnimation;
import com.projettec.imageStudio.model.editingImage.Conversion;
import com.projettec.imageStudio.model.editingImage.Convolution;
import com.projettec.imageStudio.model.editingImage.DynamicExtension;
import com.projettec.imageStudio.model.editingImage.Equalization;
import com.projettec.imageStudio.model.editingImage.Filter;
import com.projettec.imageStudio.R;
import com.projettec.imageStudio.model.filters.FilterType;
import com.projettec.imageStudio.model.filters.OnItemFilterSelected;
import com.projettec.imageStudio.model.tools.OnItemToolSelected;
import com.projettec.imageStudio.model.tools.ToolType;
import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.takusemba.cropme.CropLayout;
import com.takusemba.cropme.OnCropListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class Studio_fragment extends Fragment implements OnItemToolSelected, OnItemFilterSelected, View.OnClickListener {

    public static Bitmap captImage ;

    private Bitmap loadedToRestore ;
    private Bitmap loadedToChange ;

    private Filter filter ;
    private DynamicExtension dynamicExtension ;
    private Equalization equalization ;
    private Convolution convolution ;

    private Context applicationContext ;
    private View v ;

    private RecyclerView filterRecyclerView, editingToolRecyclerView ;

    private CropLayout cropLayout ;

    private PhotoView photo_view ;
    private ImageView undoImage, saveImage, restoreImage, rotateLeft, rotateRight;
    private TextView centerText;
    private ColorSeekBar colorSeekBar;
    private LinearLayout rotationButtonLayout;
    //private ArcSeekBar arcSeekBar;

    private boolean isFilter = false ;
    private boolean isColorize = false ;
    private boolean isCropImage = false ;
    private boolean isRotate = false ;

    private static final String TAG = "Studio_fragment";

    private String image_path ;
    private Uri image_uri ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_studio, container, false);

        initView();

        applicationContext = StudioActivity.getContextOfApplication();

        image_path = getArguments().getString("image");
        image_uri = Uri.parse(image_path);
        captImage = null;
        try {
            captImage = MediaStore.Images.Media.getBitmap(
                    applicationContext.getContentResolver(), image_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadedToRestore = captImage.copy(captImage.getConfig(), true);
        loadedToChange = captImage.copy(captImage.getConfig(), true);

        //Load filters
        filter = new Filter(captImage, applicationContext);
        dynamicExtension = new DynamicExtension(captImage, applicationContext);
        equalization = new Equalization(captImage, applicationContext);
        convolution = new Convolution(captImage, applicationContext);

        //captImage = BitmapFactory.decodeResource(getResources(), R.drawable.background_org);

        int height = captImage.getHeight();
        int width = captImage.getWidth();

        Glide.with(applicationContext).load(captImage).override(width, height).into(photo_view);


        //Glide.with(applicationContext).load(captImage).centerInside().into(photo_view);
        //photo_view.setImageBitmap(captImage);
        //Glide.with(applicationContext).load(captImage).override(width,height).into(image_view);


        initFilterRecyclerView();
        initEditingToolRecyclerView();

        return v;
    }

    public static Studio_fragment newInstance(String image_path) {

        Studio_fragment f = new Studio_fragment();
        Bundle b = new Bundle();

        b.putString("image", image_path);

        f.setArguments(b);

        return f;
    }

    public void initView() {
        colorSeekBar = (ColorSeekBar) v.findViewById(R.id.seek);
        colorSeekBar.setVisibility(View.INVISIBLE);

        undoImage = (ImageView) v.findViewById(R.id.fragment_studio_undo_parent);
        undoImage.setOnClickListener(this);

        saveImage = (ImageView) v.findViewById(R.id.fragment_studio_save);
        saveImage.setOnClickListener(this);

        restoreImage = (ImageView) v.findViewById(R.id.fragment_studio_restore);
        restoreImage.setOnClickListener(this);

        centerText = (TextView) v.findViewById(R.id.fragment_studio_center_text);

        photo_view = (PhotoView) v.findViewById(R.id.photo_view);

        cropLayout = (CropLayout) v.findViewById(R.id.crop_view);
        cropLayout.setVisibility(View.INVISIBLE);

        rotationButtonLayout = (LinearLayout) v.findViewById(R.id.degree_seekbar);
        rotationButtonLayout.setVisibility(View.INVISIBLE);

        rotateLeft = (ImageView) v.findViewById(R.id.rotate_left);
        rotateLeft.setOnClickListener(this);

        rotateRight = (ImageView) v.findViewById(R.id.rotate_right);
        rotateRight.setOnClickListener(this);
    }

    private void initFilterRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false);
        filterRecyclerView = (RecyclerView) v.findViewById(R.id.filter_recyclerview);
        filterRecyclerView.setLayoutManager(layoutManager);
        FilterRecyclerViewAdapter adapter = new FilterRecyclerViewAdapter(captImage, applicationContext, this);
        filterRecyclerView.setAdapter(adapter);
        filterRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void initEditingToolRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false);
        editingToolRecyclerView = (RecyclerView) v.findViewById(R.id.editing_tool_recyclerview);
        editingToolRecyclerView.setLayoutManager(layoutManager);
        EditingToolRecyclerViewAdapter adapter = new EditingToolRecyclerViewAdapter(this);
        editingToolRecyclerView.setAdapter(adapter);
    }

    public static Bitmap getCaptImage() {
        return captImage;
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case EDIT:
                centerText.setText("Recadrer");
                cropImage();
                break;
            case ROTATE:
                centerText.setText("Rotation");
                isRotate = true ;
                ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_close_black_24dp);
                ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, editingToolRecyclerView, rotationButtonLayout,
                        0, 200, 200);
                break;
            case FILTER:
                ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, editingToolRecyclerView, filterRecyclerView,
                        0, 200, 200);

                ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_close_black_24dp);
                centerText.setText("Filtre");
                isFilter = true;
                break;
            case TEXT:
                centerText.setText("Texte");
                break;
            case BRUSH:
                centerText.setText("Pinceau");
                break;
            case ERASER:
                centerText.setText("Gomme");
                break;
            case EMOJI:
                centerText.setText("Emoji");
                break;
            case STICKER:
                centerText.setText("Sticker");
                break;
        }
    }

    @Override
    public void onFilterSelected(FilterType filterType) {
        //Bitmap loadedToChange = Bitmap.createBitmap(this.loadedImage);
        /*final Bitmap loadedToChange = Bitmap.createScaledBitmap(this.loadedImage,
                50,
                50,
                true);*/

        //final Bitmap loadedToChange = captImage.copy(captImage.getConfig(), true);

        loadedToChange = loadedToRestore.copy(loadedToRestore.getConfig(), true);

        switch (filterType){
            case TOGRAY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                filter.tograyRS(loadedToChange);
                break ;
            case COLORIZE:
                //colorSeekBar.setVisibility(View.VISIBLE);
                ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, filterRecyclerView, colorSeekBar,
                        0, 200, 200);
                isColorize = true ;
                isFilter = false ;
                colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    @Override
                    public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                        float hsv[] = new float[3];
                        Conversion.RGBToHSV_new(Color.red(color), Color.green(color), Color.blue(color), hsv);
                        filter.colorizeRS(loadedToChange, hsv[0]);
                        photo_view.setImageBitmap(loadedToChange);
                    }
                });
                break ;
            case KEEPCOLOR:
                colorSeekBar.setVisibility(View.INVISIBLE);
                filter.KeepColorRS(loadedToChange, 90);
                break ;
            case CONTRASTPLUSGRAY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                dynamicExtension.contrastePlusGrayRS(loadedToChange);
                break ;
            case CONTRASTPLUSRGB:
                colorSeekBar.setVisibility(View.INVISIBLE);
                dynamicExtension.contrastePlusRGB_RS(loadedToChange);
                break ;
            case CONTRASTPLUSHSV:
                colorSeekBar.setVisibility(View.INVISIBLE);
                dynamicExtension.contrastePlusHSV_RS(loadedToChange);
                break ;
            case CONTRASTFEWERGRAY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                dynamicExtension.contrasteFewerGrayRS(loadedToChange);
                break ;
            case EQUALIZATIONGRAY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                equalization.egalisationGrayRS(loadedToChange);
                break ;
            case EQUALIZATIONRGB:
                colorSeekBar.setVisibility(View.INVISIBLE);
                equalization.egalisationRGBRS(loadedToChange);
                break ;
            case CONVOLUTIONMOY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                int size = 3;
                int filter[][] = new int[size][size];
                for(int i = 0; i < size; i++){
                    for(int j = 0; j < size; j++){
                        filter[i][j] = 1;
                    }
                }
                convolution.convolutions(loadedToChange,filter);
                break;
            case CONVOLUTIONGAUS:
                int filterGaus[][] = {{1,2,3,2,1},
                        {2,6,8,6,2},
                        {3,8,10,8,3},
                        {2,6,8,6,2},
                        {1,2,3,2,1}};
                convolution.convolutions(loadedToChange,filterGaus);
                break;
        }
        //Glide.with(mContext).load(this.loadedImage).into(photoView);
        photo_view.setImageBitmap(loadedToChange);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_studio_undo_parent :
                goBack();
                break;
            case R.id.fragment_studio_save:
                final SweetAlertDialog saveAlerter = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                saveAlerter.setTitleText("Sauvgarde ...").show();
                final Handler saveHandler = new Handler();
                saveHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saveImage(loadedToChange);
                        saveAlerter.hide();
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Sauvegardé avec succès!")
                                .show();
                    }
                }, 1000);
                break;
            case R.id.fragment_studio_restore :
                final SweetAlertDialog restoreAlerter = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                restoreAlerter.setTitleText("Réstauration...").show();
                final Handler restoreHandler = new Handler();
                restoreHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadedToChange = captImage.copy(captImage.getConfig(), true);
                        loadedToRestore = captImage.copy(captImage.getConfig(), true);
                        Glide.with(applicationContext).load(captImage).override(captImage.getWidth(), captImage.getHeight()).into(photo_view);
                        restoreAlerter.hide();
                    }
                }, 1000);
                break;
            case R.id.rotate_left:
                rotateImage(270);
                break;
            case R.id.rotate_right:
                rotateImage(90);
                break;
        }
    }

    public void goBack() {
        if (isCropImage){
            cropLayout.isOffFrame();
            cropLayout.crop();
            centerText.setText("Studio");
            if (isCropImage) {
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, R.anim.frombuttom, editingToolRecyclerView, 0, 200, true);
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_in, saveImage, 0, 200, true);
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_in, restoreImage, 0, 200, true);
                ViewAnimation.viewAnimatedChange(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out, cropLayout, photo_view,
                        0, 200, 200);
                photo_view.setImageBitmap(loadedToChange);
                isCropImage = false ;
            }
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_keyboard_arrow_left_black_24dp);
        }
        else if (isFilter) {
            ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, filterRecyclerView, editingToolRecyclerView,
                    0, 200, 200);
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_keyboard_arrow_left_black_24dp);
            centerText.setText("Studio");
            isFilter = false;
        }
        else if (isColorize) {
            ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, colorSeekBar, filterRecyclerView,
                    0, 200, 200);
            isColorize = false;
            isFilter = true;
        }
        else if (isRotate) {
            isRotate = false ;
            centerText.setText("Studio");
            ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, rotationButtonLayout, editingToolRecyclerView,
                    0, 200, 200);
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_keyboard_arrow_left_black_24dp);
        }
        else {
            if (captImage.sameAs(loadedToChange))
                getActivity().finish();
            else {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Êtes-vous sûr?")
                        .setContentText("Vous perdrez vos modifications! Sauvgardez avant")
                        .setConfirmText("Oui, je suis sûr!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog
                                        .setTitleText("Retour!")
                                        .setContentText("Retour a la page principal!")
                                        .hideConfirmButton()
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().finish();
                                    }
                                }, 1000);
                            }
                        })
                        .show();
            }
        }
    }

    public void saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/Image_Studio");
        myDir.mkdirs();

        String fname = "Image-"+ System.currentTimeMillis() +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Tell the media scanner about the new file so that it is immediately available to the user.
        MediaScannerConnection.scanFile(applicationContext, new String[]{file.toString()}, null, null);
    }

    public void cropImage() {
        ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_close_black_24dp);
        ViewAnimation.viewAnimatedHideOrShow(applicationContext, R.anim.tobuttom, editingToolRecyclerView, 0, 200, false);
        ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_out, saveImage, 0, 200, false);
        ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_out, restoreImage, 0, 200, false);
        ViewAnimation.viewAnimatedChange(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out, photo_view, cropLayout,
                0, 200, 200);
        cropLayout.addOnCropListener(new OnCropListener() {
            @Override
            public void onSuccess(@NotNull Bitmap bitmap) {
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, R.anim.frombuttom, editingToolRecyclerView, 0, 200, true);
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_in, saveImage, 0, 200, true);
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_in, restoreImage, 0, 200, true);
                ViewAnimation.viewAnimatedChange(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out, cropLayout, photo_view,
                        0, 200, 200);
                loadedToRestore = bitmap.copy(bitmap.getConfig(), true);
                loadedToChange = bitmap.copy(bitmap.getConfig(), true);
                photo_view.setImageBitmap(loadedToChange);
                isCropImage = false ;
            }

            @Override
            public void onFailure(@NotNull Exception e) {
                Toast.makeText(applicationContext, "NOOOOOO", Toast.LENGTH_LONG).show();
            }
        });

        cropLayout.setBitmap(loadedToChange);
        isCropImage = true ;
    }

    public void rotateImage(float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedBitmapLadedToRestore = Bitmap.createBitmap(loadedToRestore, 0, 0, loadedToRestore.getWidth(), loadedToRestore.getHeight(), matrix, true);
        Bitmap rotatedBitmapLoadedToChange = Bitmap.createBitmap(loadedToChange, 0, 0, loadedToChange.getWidth(), loadedToChange.getHeight(), matrix, true);
        loadedToRestore = rotatedBitmapLadedToRestore.copy(rotatedBitmapLadedToRestore.getConfig(), true);
        loadedToChange = rotatedBitmapLoadedToChange.copy(rotatedBitmapLoadedToChange.getConfig(), true);
        photo_view.setImageBitmap(loadedToChange);
    }
}
