package com.projettec.imageStudio.Controller.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.projettec.imageStudio.Controller.Fragment.Studio_fragment;
import com.projettec.imageStudio.Model.AuxiliaryFunction;
import com.projettec.imageStudio.Model.DynamicExtension;
import com.projettec.imageStudio.Model.Equalization;
import com.projettec.imageStudio.Model.Filter;
import com.projettec.imageStudio.R;
import com.rtugeek.android.colorseekbar.ColorSeekBar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import com.projettec.imageStudio.Model.Conversion;


public class RecyvlerViewAdapter extends RecyclerView.Adapter<RecyvlerViewAdapter.ViewHolder>{

    private ArrayList<String> filterName = new ArrayList<String>();
    private Bitmap loadedImage ;
    private Bitmap loadedToRecycle ;
    private PhotoView photoView ;
    private Context mContext ;
    private Filter filter ;
    private DynamicExtension dynamicExtension ;
    private Equalization equalization ;
    private ColorSeekBar colorSeekBar;
    private static final String TAG = "RecyvlerViewAdapter";

    public RecyvlerViewAdapter(ArrayList<String> filterName, Bitmap loadedImage, Context mContext, PhotoView photoView, ColorSeekBar colorSeekBar) {
        this.filterName = filterName;
        this.loadedImage = loadedImage;
        this.loadedToRecycle = Bitmap.createScaledBitmap(this.loadedImage,
                100,
                100,
                true);
        this.photoView = photoView;
        this.mContext = mContext;
        this.filter = new Filter(loadedImage, mContext);
        this.dynamicExtension = new DynamicExtension(loadedImage, mContext);
        this.equalization = new Equalization(loadedImage, mContext);
        this.colorSeekBar = colorSeekBar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Bitmap loadedToRecycle = Bitmap.createBitmap(chargeImage(position));

        Glide.with(mContext)
                .load(loadedToRecycle)
                .into(holder.filter_image);

        holder.filter_name.setText(filterName.get(position));

        holder.filter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, filterName.get(position), Toast.LENGTH_SHORT).show();
                applyChanges(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //ImageView filter_image ;
        CircleImageView filter_image ;
        TextView filter_name ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //filter_image = (ImageView) itemView.findViewById(R.id.image_view_filter);
            filter_image = (CircleImageView) itemView.findViewById(R.id.image_view_filter);
            filter_name = (TextView) itemView.findViewById(R.id.text_view_filter);
        }
    }

    public Bitmap chargeImage(int position){
        /*Bitmap loadedToRecycle = Bitmap.createScaledBitmap(this.loadedImage,
                100,
                100,
                true);*/
        Bitmap loadedToRecycle = Bitmap.createBitmap(this.loadedToRecycle);
        switch (position){
            case 0:
                filter.tograyRS(loadedToRecycle);
                break ;
            case 1:
                filter.colorizeRS(loadedToRecycle, 200);
                break ;
            case 2:
                filter.KeepColorRS(loadedToRecycle, 90);
                break ;
            case 3:
                dynamicExtension.contrastePlusGrayRS(loadedToRecycle);
                break ;
            case 4:
                dynamicExtension.contrastePlusRGB_RS(loadedToRecycle);
                break ;
            case 5:
                dynamicExtension.contrastePlusHSV_RS(loadedToRecycle);
                break ;
            case 6:
                dynamicExtension.contrasteFewerGrayRS(loadedToRecycle);
                break ;
            case 7:
                equalization.egalisationGrayRS(loadedToRecycle);
                break ;
            case 8:
                equalization.egalisationRGBRS(loadedToRecycle);
                break ;
        }
        return loadedToRecycle;
    }

    public void applyChanges(int position){
        //Bitmap loadedToChange = Bitmap.createBitmap(this.loadedImage);
        /*final Bitmap loadedToChange = Bitmap.createScaledBitmap(this.loadedImage,
                50,
                50,
                true);*/
        final Bitmap loadedToChange = this.loadedImage.copy(this.loadedImage.getConfig(), true);

        switch (position){
            case 0:
                filter.tograyRS(loadedToChange);
                break ;
            case 1:
                /*colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    int color ; float hue;
                    @Override
                    public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                        float hsv[] = new float[3];
                        color = colorSeekBar.getColor();
                        Conversion.RGBToHSV_new(Color.red(color), Color.green(color), Color.blue(color), hsv);
                        //hue = hsv[0];
                        //if (!AuxiliaryFunction.Is_like(hue, hsv[0], 5)){
                            filter.colorizeRS(loadedToChange, hsv[0]);
                            photoView.setImageBitmap(loadedToChange);
                            hue = hsv[0];
                        //}
                    }
                });*/
                break ;
            case 2:
                //filter.KeepColorRS(loadedToChange, 90);
                colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    int color ; float hue;
                    @Override
                    public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                        float hsv[] = new float[3];
                        final Bitmap loadedToChange = loadedImage.copy(loadedImage.getConfig(), true);

                        color = colorSeekBar.getColor();
                        Log.i("Color ----------->" , color +" ");
                        Conversion.RGBToHSV_new(Color.red(color), Color.green(color), Color.blue(color), hsv);
                        Log.i(TAG, "onColorChangeListener:  ------> "+ hsv[0]);
                        //hue = hsv[0];
                        //if (!AuxiliaryFunction.Is_like(hue, hsv[0], 5)){
                        //filter.KeepColorRS(loadedToChange, hsv[0]);
                        //filter.keepcolor(loadedToChange, color, 15);
                        filter.saveColors(loadedToChange,color);
                        photoView.setImageBitmap(loadedToChange);
                        //hue = hsv[0];
                        //}
                    }
                });
                break ;
            case 3:
                dynamicExtension.contrastePlusGrayRS(loadedToChange);
                break ;
            case 4:
                dynamicExtension.contrastePlusRGB_RS(loadedToChange);
                break ;
            case 5:
                dynamicExtension.contrastePlusHSV_RS(loadedToChange);
                break ;
            case 6:
                dynamicExtension.contrasteFewerGrayRS(loadedToChange);
                break ;
            case 7:
                equalization.egalisationGrayRS(loadedToChange);
                break ;
            case 8:
                equalization.egalisationRGBRS(loadedToChange);
                break ;
        }
        //Glide.with(mContext).load(this.loadedImage).into(photoView);
        photoView.setImageBitmap(loadedToChange);
    }



}
