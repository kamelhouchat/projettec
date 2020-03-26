package com.projettec.imageStudio.controller.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projettec.imageStudio.R;
import com.projettec.imageStudio.model.tools.OnItemToolSelected;
import com.projettec.imageStudio.model.tools.ToolModel;
import com.projettec.imageStudio.model.tools.ToolType;

import java.util.ArrayList;

/**
 * <p>
 * the class is an adapter of the EditingToolsRecyclerView, it allows you to generate
 * all the tools necessary for image management by specifying the icon and the name of
 * the tool, it allows to classify them side by side and create more copied from a tool copy
 * </p>
 *
 * @author Kamel.H
 * @see ToolModel
 * @see ToolType
 * @see OnItemToolSelected
 */

public class EditingToolRecyclerViewAdapter extends RecyclerView.Adapter<EditingToolRecyclerViewAdapter.ViewHolder> {

    //ArrayList which contains the ToolModels
    private ArrayList<ToolModel> toolList = new ArrayList<ToolModel>();

    //The onItemToolSelected method which facilitates the management of listener
    private OnItemToolSelected onItemToolSelected;

    /**
     * <p>
     * The constructor of the class which takes in parameter the method which manages the listeners which will be override in the class where an object
     * of this class will be instantiated.
     * In this constructor, we add all the necessary tools that we want to see in the recyclerView to the ArrayList
     * </p>
     *
     * @param onItemToolSelected The method that manages listeners which will be override in the class where an object of this class will be instantiated
     * @see ToolType
     * @see ToolModel
     */
    public EditingToolRecyclerViewAdapter(OnItemToolSelected onItemToolSelected) {
        this.onItemToolSelected = onItemToolSelected;
        toolList.add(new ToolModel("Recadrer", R.drawable.ic_crop_black_24dp, ToolType.EDIT));
        toolList.add(new ToolModel("Rotation", R.drawable.ic_rotate_90_degrees_ccw_black_24dp, ToolType.ROTATE));
        toolList.add(new ToolModel("Luminosité", R.drawable.ic_brightness_6_black_24dp, ToolType.BRIGHTNESS));
        toolList.add(new ToolModel("Filtre", R.drawable.ic_filter_black_24dp, ToolType.FILTER));
        toolList.add(new ToolModel("Texte", R.drawable.ic_text_fields_black_24dp, ToolType.TEXT));
        toolList.add(new ToolModel("Pinceau", R.drawable.ic_brush_black_24dp, ToolType.BRUSH));
        toolList.add(new ToolModel("Gomme", R.drawable.ic_eraser, ToolType.ERASER));
        toolList.add(new ToolModel("Emoji", R.drawable.ic_insert_emoticon_black_24dp, ToolType.EMOJI));
        toolList.add(new ToolModel("Autocollant", R.drawable.ic_sticker, ToolType.STICKER));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editing_tool_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToolModel toolModel = toolList.get(position);
        holder.textView.setText(toolModel.getToolName());
        holder.imageView.setImageResource(toolModel.getToolIcon());
    }

    @Override
    public int getItemCount() {
        return toolList.size();
    }

    /**
     * <p>The class allows us to extract the elements of the exemplary layout</p>
     *
     * @author Kamel.h
     * @see OnItemToolSelected
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        //ImageView on which we put the tool icon each time
        ImageView imageView;

        //TextView on which we put the tool name each time
        TextView textView;

        /**
         * <p>Constructor in which the views and listener were initialized
         *
         * @param itemView The layout view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgToolIcon);
            textView = itemView.findViewById(R.id.txtTool);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemToolSelected.onToolSelected(toolList.get(getLayoutPosition()).getToolType());
                }
            });
        }
    }
}
