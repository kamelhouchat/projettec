package com.projettec.imageStudio.controller.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projettec.imageStudio.controller.fragments.Studio_fragment;
import com.projettec.imageStudio.model.editingImage.DynamicExtension;
import com.projettec.imageStudio.model.editingImage.Equalization;
import com.projettec.imageStudio.model.editingImage.Filter;
import com.projettec.imageStudio.R;
import com.projettec.imageStudio.model.filters.FilterModel;
import com.projettec.imageStudio.model.filters.FilterType;
import com.projettec.imageStudio.model.filters.OnItemFilterSelected;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<FilterRecyclerViewAdapter.ViewHolder>{

    private ArrayList<FilterModel> filterModels = new ArrayList<FilterModel>();
    private Bitmap loadedImage ;
    private Bitmap loadedToRecycle ;
    private Context mContext;
    private OnItemFilterSelected onItemFilterSelected ;
    private Filter filter ;
    private DynamicExtension dynamicExtension ;
    private Equalization equalization ;

    private static final String TAG = "RecyvlerViewAdapter";

    public FilterRecyclerViewAdapter(ArrayList<FilterModel> filterModels, Bitmap loadedImage, Context mContext, OnItemFilterSelected onItemFilterSelected) {
        this.filterModels = filterModels;
        this.loadedImage = loadedImage;
        this.loadedToRecycle = Bitmap.createScaledBitmap(this.loadedImage,
                100,
                100,
                true);
        this.mContext = mContext;
        this.onItemFilterSelected = onItemFilterSelected;
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
        FilterModel filterModel = filterModels.get(position);

        Bitmap loadedToRecycle = Bitmap.createBitmap(chargeImage(filterModel.getFilterType()));

        Glide.with(mContext)
                .load(loadedToRecycle)
                .into(holder.filter_image);

        holder.filter_name.setText(filterModel.getFilterName());
    }

    @Override
    public int getItemCount() {
        return filterModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView filter_image ;
        TextView filter_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //filter_image = (ImageView) itemView.findViewById(R.id.image_view_filter);
            filter_image = (CircleImageView) itemView.findViewById(R.id.image_view_filter);
            filter_name = (TextView) itemView.findViewById(R.id.text_view_filter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemFilterSelected.onFilterSelected(filterModels.get(getLayoutPosition()).getFilterType());
                }
            });
        }
    }

    public Bitmap chargeImage(FilterType filterType){
        /*Bitmap loadedToRecycle = Bitmap.createScaledBitmap(this.loadedImage,
                100,
                100,
                true);*/
        Bitmap loadedToRecycle = Bitmap.createBitmap(this.loadedToRecycle);
        switch (filterType){
            case TOGRAY:
                filter.tograyRS(loadedToRecycle);
                break ;
            case COLORIZE:
                filter.colorizeRS(loadedToRecycle, 200);
                break ;
            case KEEPCOLOR:
                filter.KeepColorRS(loadedToRecycle, 90);
                break ;
            case CONTRASTPLUSGRAY:
                dynamicExtension.contrastePlusGrayRS(loadedToRecycle);
                break ;
            case CONTRASTPLUSRGB:
                dynamicExtension.contrastePlusRGB_RS(loadedToRecycle);
                break ;
            case CONTRASTPLUSHSV:
                dynamicExtension.contrastePlusHSV_RS(loadedToRecycle);
                break ;
            case CONTRASTFEWERGRAY:
                dynamicExtension.contrasteFewerGrayRS(loadedToRecycle);
                break ;
            case EQUALIZATIONGRAY:
                equalization.egalisationGrayRS(loadedToRecycle);
                break ;
            case EQUALIZATIONRGB:
                equalization.egalisationRGBRS(loadedToRecycle);
                break ;
        }
        return loadedToRecycle;
    }

}
