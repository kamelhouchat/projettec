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

public class EditingToolRecyclerViewAdapter extends RecyclerView.Adapter<EditingToolRecyclerViewAdapter.ViewHolder>{

    private ArrayList<ToolModel> toolList = new ArrayList<ToolModel>();
    private OnItemToolSelected onItemToolSelected;

    public EditingToolRecyclerViewAdapter(OnItemToolSelected onItemToolSelected) {
        this.onItemToolSelected = onItemToolSelected;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        TextView textView ;

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
