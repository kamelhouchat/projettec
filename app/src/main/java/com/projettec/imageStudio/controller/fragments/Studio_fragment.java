package com.projettec.imageStudio.controller.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.projettec.imageStudio.controller.adapters.EditingToolRecyclerViewAdapter;

import com.projettec.imageStudio.controller.adapters.FilterRecyclerViewAdapter;
import com.projettec.imageStudio.controller.StudioActivity;
import com.projettec.imageStudio.model.editingImage.Conversion;
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

import java.io.IOException;
import java.util.ArrayList;

public class Studio_fragment extends Fragment implements OnItemToolSelected, OnItemFilterSelected {

    public static Bitmap captImage ;

    private Filter filter ;
    private DynamicExtension dynamicExtension ;
    private Equalization equalization ;
    private PhotoView photo_view ;
    private ColorSeekBar colorSeekBar;
    private Context applicationContext ;
    private View v ;
    private PhotoViewAttacher photoView;
    //private ArrayList<FilterModel> filterModels = new ArrayList<FilterModel>();
    private ArrayList<FilterModel> filterModels = new ArrayList<FilterModel>();
    private RecyclerView filterRecyclerView, editingToolRecyclerView ;

    private static final String TAG = "Studio_fragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_studio, container, false);

        colorSeekBar = (ColorSeekBar) v.findViewById(R.id.seek);
        colorSeekBar.setVisibility(View.INVISIBLE);

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

        //Load filters
        filter = new Filter(captImage, applicationContext);
        dynamicExtension = new DynamicExtension(captImage, applicationContext);
        equalization = new Equalization(captImage, applicationContext);

        //captImage = BitmapFactory.decodeResource(getResources(), R.drawable.background_org);

        int height = captImage.getHeight();
        int width = captImage.getWidth();

        photo_view = (PhotoView) v.findViewById(R.id.photo_view);
        Glide.with(applicationContext).load(captImage).override(width,height).into(photo_view);
        //photo_view.setImageBitmap(captImage);
        //ImageView image_view = (ImageView) v.findViewById(R.id.image_view);
        //Glide.with(applicationContext).load(captImage).override(width,height).into(image_view);


        //image_view.setImageBitmap(captImage);

        initFilterName();
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

    public void initFilterName(){
        filterModels.add(new FilterModel("Gray", FilterType.TOGRAY));
        filterModels.add(new FilterModel("Colorize", FilterType.COLORIZE));
        filterModels.add(new FilterModel("KeepColor", FilterType.KEEPCOLOR));
        filterModels.add(new FilterModel("Cont + Gray", FilterType.CONTRASTPLUSGRAY));
        filterModels.add(new FilterModel("Cont + RGB", FilterType.CONTRASTPLUSRGB));
        filterModels.add(new FilterModel("Cont + HSV", FilterType.CONTRASTPLUSHSV));
        filterModels.add(new FilterModel("Cont - Gray", FilterType.CONTRASTFEWERGRAY));
        filterModels.add(new FilterModel("Equa Gray", FilterType.EQUALIZATIONGRAY));
        filterModels.add(new FilterModel("Equa RGB", FilterType.EQUALIZATIONRGB));


        initFilterRecyclerView();
    }

    private void initFilterRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false);
        filterRecyclerView = (RecyclerView) v.findViewById(R.id.filter_recyclerview);
        filterRecyclerView.setLayoutManager(layoutManager);
        FilterRecyclerViewAdapter adapter = new FilterRecyclerViewAdapter(filterModels, captImage, applicationContext, this);
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

    /*public static void applyChanges(int position){
        //Bitmap loadedToChange = Bitmap.createBitmap(this.loadedImage);
        *//*final Bitmap loadedToChange = Bitmap.createScaledBitmap(this.loadedImage,
                50,
                50,
                true);*//*
        final Bitmap loadedToChange = captImage.copy(captImage.getConfig(), true);

        switch (position){
            case 0:
                colorSeekBar.setVisibility(View.INVISIBLE);
                filter.tograyRS(loadedToChange);
                break ;
            case 1:
                colorSeekBar.setVisibility(View.VISIBLE);
                colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    int color ; float hue;
                    @Override
                    public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                        float hsv[] = new float[3];
                        color = colorSeekBar.getColor();
                        Conversion.RGBToHSV_new(Color.red(color), Color.green(color), Color.blue(color), hsv);
                        //hue = hsv[0];
                        //if (!AuxiliaryFunction.Is_like(hue, hsv[0], 5)){
                        filter.colorizeRS(loadedToChange, hsv[0]);
                        photo_view.setImageBitmap(loadedToChange);
                        hue = hsv[0];
                        //}
                    }
                });
                break ;
            case 2:
                colorSeekBar.setVisibility(View.INVISIBLE);
                filter.KeepColorRS(loadedToChange, 90);
                break ;
            case 3:
                colorSeekBar.setVisibility(View.INVISIBLE);
                dynamicExtension.contrastePlusGrayRS(loadedToChange);
                break ;
            case 4:
                colorSeekBar.setVisibility(View.INVISIBLE);
                dynamicExtension.contrastePlusRGB_RS(loadedToChange);
                break ;
            case 5:
                colorSeekBar.setVisibility(View.INVISIBLE);
                dynamicExtension.contrastePlusHSV_RS(loadedToChange);
                break ;
            case 6:
                colorSeekBar.setVisibility(View.INVISIBLE);
                dynamicExtension.contrasteFewerGrayRS(loadedToChange);
                break ;
            case 7:
                colorSeekBar.setVisibility(View.INVISIBLE);
                equalization.egalisationGrayRS(loadedToChange);
                break ;
            case 8:
                colorSeekBar.setVisibility(View.INVISIBLE);
                equalization.egalisationRGBRS(loadedToChange);
                break ;
        }
        //Glide.with(mContext).load(this.loadedImage).into(photoView);
        photo_view.setImageBitmap(loadedToChange);
    }*/

    public static Bitmap getCaptImage() {
        return captImage;
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case FILTER:
                //showFilter(true);
                Log.i(TAG, "onToolSelected: Filter Selected");
                editingToolRecyclerView.setVisibility(View.INVISIBLE);
                filterRecyclerView.setVisibility(View.VISIBLE);
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
        final Bitmap loadedToChange = captImage.copy(captImage.getConfig(), true);

        switch (filterType){
            case TOGRAY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                filter.tograyRS(loadedToChange);
                break ;
            case COLORIZE:
                colorSeekBar.setVisibility(View.VISIBLE);
                colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    int color ; float hue;
                    @Override
                    public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                        float hsv[] = new float[3];
                        color = colorSeekBar.getColor();
                        Conversion.RGBToHSV_new(Color.red(color), Color.green(color), Color.blue(color), hsv);
                        //hue = hsv[0];
                        //if (!AuxiliaryFunction.Is_like(hue, hsv[0], 5)){
                        filter.colorizeRS(loadedToChange, hsv[0]);
                        photo_view.setImageBitmap(loadedToChange);
                        hue = hsv[0];
                        //}
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
        }
        //Glide.with(mContext).load(this.loadedImage).into(photoView);
        photo_view.setImageBitmap(loadedToChange);
    }
}
