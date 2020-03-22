package com.projettec.imageStudio.controller.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SweepGradient;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
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
import com.projettec.imageStudio.model.filters.FilterModel;
import com.projettec.imageStudio.model.filters.FilterType;
import com.projettec.imageStudio.model.filters.OnItemFilterSelected;
import com.projettec.imageStudio.model.tools.OnItemToolSelected;
import com.projettec.imageStudio.model.tools.ToolType;
import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.tapadoo.alerter.Alerter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;

public class Studio_fragment extends Fragment implements OnItemToolSelected, OnItemFilterSelected, View.OnClickListener {

    public static Bitmap captImage ;

    private Bitmap loadedToChange ;

    private Filter filter ;
    private DynamicExtension dynamicExtension ;
    private Equalization equalization ;
    private Convolution convolution ;

    private Context applicationContext ;
    private View v ;

    private RecyclerView filterRecyclerView, editingToolRecyclerView ;
    private PhotoView photo_view ;
    private ImageView undoImage, saveImage, restoreImage;
    private TextView centerText;
    private ColorSeekBar colorSeekBar;

    private boolean isFilter = false ;

    private ConstraintSet mConstraintSet = new ConstraintSet();

    private static final String TAG = "Studio_fragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_studio, container, false);

        initView();

        applicationContext = StudioActivity.getContextOfApplication();

        String image_path = getArguments().getString("image");
        Uri image_uri = Uri.parse(image_path);
        captImage = null;
        try {
            captImage = MediaStore.Images.Media.getBitmap(
                    applicationContext.getContentResolver(), image_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadedToChange = captImage.copy(captImage.getConfig(), true);

        //Load filters
        filter = new Filter(captImage, applicationContext);
        dynamicExtension = new DynamicExtension(captImage, applicationContext);
        equalization = new Equalization(captImage, applicationContext);

        //captImage = BitmapFactory.decodeResource(getResources(), R.drawable.background_org);

        int height = captImage.getHeight();
        int width = captImage.getWidth();

        Glide.with(applicationContext).load(captImage).override(width,height).into(photo_view);
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

        loadedToChange = captImage.copy(captImage.getConfig(), true);

        switch (filterType){
            case TOGRAY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                filter.tograyRS(loadedToChange);
                break ;
            case COLORIZE:
                colorSeekBar.setVisibility(View.VISIBLE);
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
                        Glide.with(applicationContext).load(captImage).override(captImage.getWidth(), captImage.getHeight()).into(photo_view);
                        restoreAlerter.hide();
                    }
                }, 1000);
                break;
        }
    }

    public void goBack(){
        if (isFilter){
            ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, filterRecyclerView, editingToolRecyclerView,
                    0, 200, 200);
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_keyboard_arrow_left_black_24dp);
            centerText.setText("Studio");
            isFilter = false ;
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

}
