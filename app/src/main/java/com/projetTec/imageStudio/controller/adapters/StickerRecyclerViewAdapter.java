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

    private OnStickerClickListener onStickerClickListener;

    public StickerRecyclerViewAdapter() {
        stickerList = new int[]{R.drawable.hat};
    }

    public interface OnStickerClickListener {
        void onStickerClickListener(Bitmap sticker);
    }

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView stickerImage;

        public ViewHolder(@NonNull View itemView) {
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
