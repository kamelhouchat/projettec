package com.projettec.imageStudio.controller.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.projettec.imageStudio.controller.fragment.Studio_fragment;
import com.projettec.imageStudio.model.DynamicExtension;
import com.projettec.imageStudio.model.Equalization;
import com.projettec.imageStudio.model.Filter;
import com.projettec.imageStudio.R;
import com.rtugeek.android.colorseekbar.ColorSeekBar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import com.projettec.imageStudio.model.Conversion;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> filterName = new ArrayList<String>();
    private Bitmap loadedImage ;
    private Bitmap loadedToRecycle ;
    private Context mContext ;
    private Filter filter ;
    private DynamicExtension dynamicExtension ;
    private Equalization equalization ;

    private static final String TAG = "RecyvlerViewAdapter";

    public RecyclerViewAdapter(ArrayList<String> filterName, Bitmap loadedImage, Context mContext) {
        this.filterName = filterName;
        this.loadedImage = loadedImage;
        this.loadedToRecycle = Bitmap.createScaledBitmap(this.loadedImage,
                100,
                100,
                true);
        this.mContext = mContext;
        this.filter = new Filter(loadedImage, mContext);
        this.dynamicExtension = new DynamicExtension(loadedImage, mContext);
        this.equalization = new Equalization(loadedImage, mContext);
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
                Studio_fragment.applyChanges(position);
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

}
