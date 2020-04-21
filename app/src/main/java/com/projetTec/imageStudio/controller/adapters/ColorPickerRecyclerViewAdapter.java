package com.projetTec.imageStudio.controller.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projetTec.imageStudio.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <p>
 * The class is an adapter of the ColorPickerRecyclerView, it generates a list of colors that will be displayed and sets up a listener.
 * </p>
 *
 * @author Kamel.H
 * @see com.projetTec.imageStudio.controller.dialogFragment.TextDialogFragment
 * @see com.projetTec.imageStudio.controller.bottomDialogFragment.BrushBottomDialogFragment
 */

public class ColorPickerRecyclerViewAdapter extends RecyclerView.Adapter<ColorPickerRecyclerViewAdapter.ViewHolder> {

    //The list which will contain the different colors
    private final ArrayList<Integer> colorList = new ArrayList<>();

    //The onColorPickerClickListener method which facilitates the management of listener
    private OnColorPickerClickListener onColorPickerClickListener;

    /**
     * <p>
     *     The constructor takes nothing in parameter, ie fills the list with the different colors.
     * </p>
     *
     * @see com.projetTec.imageStudio.controller.dialogFragment.TextDialogFragment
     * @see com.projetTec.imageStudio.controller.bottomDialogFragment.BrushBottomDialogFragment
     */
    public ColorPickerRecyclerViewAdapter() {
        colorList.add(Color.BLACK);
        colorList.add(Color.WHITE);
        colorList.add(Color.BLUE);
        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        colorList.add(Color.YELLOW);
        colorList.add(Color.CYAN);
        colorList.add(Color.DKGRAY);
        colorList.add(Color.MAGENTA);
        colorList.add(Color.LTGRAY);
        colorList.add(Color.GRAY);
        colorList.add(R.color.dracula_page_bg);
    }

    /**
     * <p>
     * The interface that will be implemented in the class where the {@link ColorPickerRecyclerViewAdapter}
     * will be instantiated and used.
     * </p>
     *
     * @author Kamel.H
     * @see com.projetTec.imageStudio.controller.bottomDialogFragment.BrushBottomDialogFragment
     * @see com.projetTec.imageStudio.controller.dialogFragment.TextDialogFragment
     */
    public interface OnColorPickerClickListener {

        /**
         * <p>
         * The listener which will be called when a color is selected in the Color Picker RecyclerView class.
         * </p>
         *
         * @param colorCode The Color code
         */
        void onColorPickerClickListener(int colorCode);

    }

    /**
     * <p>
     * The setter of onColorPickerClickListener.
     * </p>
     *
     * @param onColorPickerClickListener The new listener
     */
    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener) {
        this.onColorPickerClickListener = onColorPickerClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_picker_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.circleImageView.setColorFilter(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    /**
     * <p>
     * The class allows us to extract the elements of the exemplary layout.
     * </p>
     *
     * @author Kamel.H
     * @see RecyclerView.ViewHolder
     * @see OnColorPickerClickListener
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        //CircleImageView on which we put the color each time
        final CircleImageView circleImageView;

        /**
         * <p>
         * Constructor in which the views and listener were initialized.
         * </p>
         *
         * @param itemView The layout view
         */
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.color_picker_imageColor);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onColorPickerClickListener.onColorPickerClickListener(colorList.get(getLayoutPosition()));
                }
            });
        }
    }
}
