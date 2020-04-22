package com.projetTec.imageStudio.controller.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projetTec.imageStudio.R;
import com.projetTec.imageStudio.controller.StudioActivity;

/**
 * <p>
 * The class is an adapter of the StickerRecyclerView, it generates a list of sticker that will be displayed and sets up a listener.
 * </p>
 *
 * @author Kamel.H
 * @see com.projetTec.imageStudio.controller.bottomDialogFragment.StickerBottomDialogFragment
 */
public class StickerRecyclerViewAdapter extends RecyclerView.Adapter<StickerRecyclerViewAdapter.ViewHolder>{

    //The array which will contain the different sticker
    private final int[] stickerList;

    //The onStickerClickListener method which facilitates the management of listener
    private OnStickerClickListener onStickerClickListener;

    /**
     * <p>
     *     The constructor takes nothing in parameter, ie fills the list with the different sticker.
     * </p>
     *
     * @see com.projetTec.imageStudio.controller.bottomDialogFragment.StickerBottomDialogFragment
     */
    public StickerRecyclerViewAdapter() {
        stickerList = new int[]{R.drawable.hat};
    }

    /**
     * <p>
     * The interface that will be implemented in the class where the {@link StickerRecyclerViewAdapter}
     * will be instantiated and used.
     * </p>
     *
     * @author Kamel.H
     * @see com.projetTec.imageStudio.controller.bottomDialogFragment.StickerBottomDialogFragment
     */
    public interface OnStickerClickListener {

        /**
         * <p>
         * The listener which will be called when a sticker is selected in the Sticker RecyclerView class.
         * </p>
         *
         * @param sticker The Emoji code
         */
        void onStickerClickListener(Bitmap sticker);

    }

    /**
     * <p>
     * The setter of onStickerClickListener.
     * </p>
     *
     * @param onStickerClickListener The new listener
     */
    public void setOnStickerClickListener(OnStickerClickListener onStickerClickListener) {
        this.onStickerClickListener = onStickerClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(StudioActivity.getContextOfApplication()).inflate(R.layout.sticker_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.stickerImage.setImageResource(stickerList[position]);
    }

    @Override
    public int getItemCount() {
        return stickerList.length;
    }

    /**
     * <p>
     * The class allows us to extract the elements of the exemplary layout.
     * </p>
     *
     * @author Kamel.H
     * @see RecyclerView.ViewHolder
     * @see StickerRecyclerViewAdapter.OnStickerClickListener
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        //ImageView on which we put the sticker each time
        final ImageView stickerImage;

        /**
         * <p>
         * Constructor in which the views and listener were initialized.
         * </p>
         *
         * @param itemView The layout view
         */
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            stickerImage = itemView.findViewById(R.id.image_view_sticker_row);

            stickerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStickerClickListener.onStickerClickListener(
                            BitmapFactory.decodeResource(StudioActivity.getContextOfApplication().getResources(), stickerList[getLayoutPosition()])
                    );
                }
            });
        }
    }
}
