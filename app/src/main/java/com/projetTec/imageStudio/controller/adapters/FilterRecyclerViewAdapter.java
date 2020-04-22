package com.projetTec.imageStudio.controller.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projetTec.imageStudio.model.editingImage.additionalFilters.AdditionalFilters;
import com.projetTec.imageStudio.model.editingImage.basicFilters.Convolution;
import com.projetTec.imageStudio.model.editingImage.basicFilters.DynamicExtension;
import com.projetTec.imageStudio.model.editingImage.basicFilters.Equalization;
import com.projetTec.imageStudio.model.editingImage.basicFilters.Filters;
import com.projetTec.imageStudio.R;
import com.projetTec.imageStudio.model.filters.FilterModel;
import com.projetTec.imageStudio.model.filters.FilterType;
import com.projetTec.imageStudio.model.filters.OnItemFilterSelected;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <p>
 * the class is an adapter of the FilterRecyclerView, it allows you to generate
 * all the filters that will be applied to the images by specifying the name and the type of
 * the filter, it allows to classify them side by side and create more copied from a filter copy
 * </p>
 *
 * @author Kamel.H
 * @see FilterModel
 * @see FilterType
 * @see OnItemFilterSelected
 */

@SuppressWarnings("FieldCanBeLocal")
public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<FilterRecyclerViewAdapter.ViewHolder> {

    //ArrayList which contains the FilterModels
    private final ArrayList<FilterModel> filterModels = new ArrayList<>();

    //The loaded image, which will be used for initializing the filters, and applying the filters to small images to get a glimpse of the filter
    private final Bitmap loadedImage;

    //The image after compression which will be used for applying the filters in recyclerView
    private final Bitmap loadedToRecycle;

    //The activity context
    private final Context mContext;

    //The onItemFilterSelected method which facilitates the management of listener
    private final OnItemFilterSelected onItemFilterSelected;

    //Class instances that contain filters
    private final Filters filters;
    private final DynamicExtension dynamicExtension;
    private final Equalization equalization;
    private final Convolution convolution;
    private final AdditionalFilters additionalFilters;

    /**
     * <p>
     * The constructor of the class which takes in parameter :
     * <ul>
     *     <li>The method which manages the listeners which will be override in the class where an object of this class will be instantiated.</li>
     *     <li>The context</li>
     *     <li>The loaded image</li>
     * </ul>
     * <p>
     * In this constructor, we add all the filters that will be applied to the images and that we want to see in the recyclerView to the ArrayList
     * </p>
     *
     * @param loadedImage          The loaded image, which will be used for initializing the filters, and applying the filters to small images to get a glimpse of the filter
     * @param mContext             The activity context
     * @param onItemFilterSelected The method that manages listeners which will be override in the class where an object of this class will be instantiated
     * @see FilterType
     * @see FilterModel
     */
    public FilterRecyclerViewAdapter(Bitmap loadedImage, Context mContext, OnItemFilterSelected onItemFilterSelected) {
        filterModels.add(new FilterModel("Gray", FilterType.TO_GRAY));
        filterModels.add(new FilterModel("Colorize", FilterType.COLORIZE));
        filterModels.add(new FilterModel("KeepColor", FilterType.KEEP_COLOR));
        filterModels.add(new FilterModel("Cont + Gray", FilterType.CONTRAST_PLUS_GRAY));
        filterModels.add(new FilterModel("Cont + RGB", FilterType.CONTRAST_PLUS_RGB));
        filterModels.add(new FilterModel("Cont + HSV", FilterType.CONTRAST_PLUS_HSV));
        filterModels.add(new FilterModel("Cont - Gray", FilterType.CONTRAST_FEWER_GRAY));
        filterModels.add(new FilterModel("Equa Gray", FilterType.EQUALIZATION_GRAY));
        filterModels.add(new FilterModel("Equa RGB", FilterType.EQUALIZATION_RGB));
        filterModels.add(new FilterModel("Moyenneur", FilterType.CONVOLUTION_MOY));
        filterModels.add(new FilterModel("Gaussian", FilterType.CONVOLUTION_GAUS));
        filterModels.add(new FilterModel("Contours", FilterType.CONTOUR));
        filterModels.add(new FilterModel("Dessin", FilterType.SKETCH_EFFECT));
        //Additional filters
        filterModels.add(new FilterModel("Neige", FilterType.SNOW_EFFECT));
        filterModels.add(new FilterModel("Noire", FilterType.BLACK_EFFECT));
        filterModels.add(new FilterModel("Bruit", FilterType.NOISE_EFFECT));
        filterModels.add(new FilterModel("Inverser", FilterType.INVERT_EFFECT));
        filterModels.add(new FilterModel("Ombre", FilterType.SHADING_EFFECT));
        filterModels.add(new FilterModel("Equa YUV", FilterType.EQUALIZATION_YUV));
        filterModels.add(new FilterModel("Equa YUV & RGB", FilterType.MIX_EQUALIZATION_RGB_YUV));

        this.loadedImage = loadedImage;
        this.loadedToRecycle = Bitmap.createScaledBitmap(this.loadedImage,
                100,
                100,
                true);
        this.mContext = mContext;
        this.onItemFilterSelected = onItemFilterSelected;
        this.filters = new Filters(mContext);
        this.dynamicExtension = new DynamicExtension(mContext);
        this.equalization = new Equalization(mContext);
        this.convolution = new Convolution(mContext);
        this.additionalFilters = new AdditionalFilters(mContext);
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


    /**
     * <p>The class allows us to extract the elements of the exemplary layout</p>
     *
     * @author Kamel.h
     * @see OnItemFilterSelected
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        //ImageView on which we put an overview of the filter each time
        final CircleImageView filter_image;

        //TextView on which we put the filter name each time
        final TextView filter_name;

        /**
         * <p>Constructor in which the views and listener were initialized
         *
         * @param itemView The layout view
         */
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //filter_image = (ImageView) itemView.findViewById(R.id.image_view_filter);
            filter_image = itemView.findViewById(R.id.image_view_filter);
            filter_name = itemView.findViewById(R.id.text_view_filter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemFilterSelected.onFilterSelected(filterModels.get(getLayoutPosition()).getFilterType());
                }
            });
        }
    }

    /**
     * <p>
     * method which applies a type of filter to a bitmap in order to have a preview in the recycler view.
     * The bitmap size is 100px by 100px.
     * </p>
     *
     * @param filterType The type of the filter
     * @return A new bitmap on which the filter was applied
     * @see FilterType
     * @see FilterModel
     */
    private Bitmap chargeImage(FilterType filterType) {
        Bitmap loadedToRecycle = Bitmap.createBitmap(this.loadedToRecycle);
        switch (filterType) {
            case TO_GRAY:
                filters.tograyRS(loadedToRecycle);
                break;
            case COLORIZE:
                filters.colorizeRS(loadedToRecycle, 200);
                break;
            case KEEP_COLOR:
                filters.KeepColorRS(loadedToRecycle, 90);
                break;
            case CONTRAST_PLUS_GRAY:
                dynamicExtension.contrastPlusGrayRS(loadedToRecycle);
                break;
            case CONTRAST_PLUS_RGB:
                dynamicExtension.contrastPlusRGB_RS(loadedToRecycle);
                break;
            case CONTRAST_PLUS_HSV:
                dynamicExtension.contrastPlusHSV_RS(loadedToRecycle);
                break;
            case CONTRAST_FEWER_GRAY:
                dynamicExtension.contrastFewerGrayRS(loadedToRecycle);
                break;
            case EQUALIZATION_GRAY:
                equalization.equalizationGrayRS(loadedToRecycle);
                break;
            case EQUALIZATION_RGB:
                equalization.equalizationRGB_RS(loadedToRecycle);
                break;
            case CONVOLUTION_MOY:
                int size = 7;
                int[][] filterMoy = new int[size][size];
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        filterMoy[i][j] = 1;
                    }
                }
                convolution.convolutions(loadedToRecycle, filterMoy);
                break;
            case CONVOLUTION_GAUS:
                int[][] filterGaus = {{1, 2, 3, 2, 1},
                        {2, 6, 8, 6, 2},
                        {3, 8, 10, 8, 3},
                        {2, 6, 8, 6, 2},
                        {1, 2, 3, 2, 1}};
                convolution.convolutions(loadedToRecycle, filterGaus);
                break;
            case CONTOUR:
                //utilisation du contours
                int[][] gx =  {{-1,0,1},{-2,0,2},{-1,0,1}};
                int[][] gy =  {{-1,-2,-1},{0,0,0},{1,2,1}};
                filters.tograyRS(loadedToRecycle);
                convolution.contours(loadedToRecycle,gx,gy);
                break;
            case SKETCH_EFFECT:
                loadedToRecycle = additionalFilters.sketchEffect(loadedToRecycle);
                break;
            case SNOW_EFFECT:
                additionalFilters.snowAndBlackEffectRS(loadedToRecycle, true);
                break;
            case BLACK_EFFECT:
                additionalFilters.snowAndBlackEffectRS(loadedToRecycle, false);
                break;
            case NOISE_EFFECT:
                additionalFilters.noiseEffectRS(loadedToRecycle);
                break;
            case INVERT_EFFECT:
                additionalFilters.invertEffectRS(loadedToRecycle);
                break;
            case SHADING_EFFECT:
                additionalFilters.shadingFilterRS(loadedToRecycle, Color.rgb(199, 252, 236));
                break;
            case EQUALIZATION_YUV:
                additionalFilters.equalizationYuvY(loadedToRecycle);
                break;
            case MIX_EQUALIZATION_RGB_YUV:
                additionalFilters.mixEqualizationRgbYuv(loadedToRecycle);
                break;
        }
        return loadedToRecycle;
    }

}
