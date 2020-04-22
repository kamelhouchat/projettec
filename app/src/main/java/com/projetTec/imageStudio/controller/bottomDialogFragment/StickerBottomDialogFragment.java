package com.projetTec.imageStudio.controller.bottomDialogFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.projetTec.imageStudio.R;
import com.projetTec.imageStudio.controller.adapters.EmojiRecyclerViewAdapter;
import com.projetTec.imageStudio.controller.adapters.StickerRecyclerViewAdapter;
import com.projetTec.imageStudio.controller.bottomDialogFragment.listenerInterface.OnEmojiOptionsChange;
import com.projetTec.imageStudio.controller.bottomDialogFragment.listenerInterface.OnStickerOptionsChange;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * The StickerBottomSheetDialogFragment class which extends from BottomSheetDialogFragment which allows
 * to implement a Dialog Fragment which contains different sticker which will be selected by user.
 * </p>
 *
 * @author Kamel.H
 * @see ja.burhanrashid52.photoeditor.PhotoEditor
 * @see StickerRecyclerViewAdapter
 * @see OnStickerOptionsChange
 * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
 */
public class StickerBottomDialogFragment extends BottomSheetDialogFragment {

    //The onStickerOptionsChange method which facilitates the management of listener
    private OnStickerOptionsChange onStickerOptionsChange;

    /**
     * <p>
     * The setter of onStickerOptionsChange
     * </p>
     *
     * @param onStickerOptionsChange The new listener.
     */
    public void setOnStickerOptionsChange(OnStickerOptionsChange onStickerOptionsChange) {
        this.onStickerOptionsChange = onStickerOptionsChange;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NotNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_dialog_fragment_sticker, null);
        dialog.setContentView(contentView);

        //noinspection deprecation
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        RecyclerView emojiRecyclerView = contentView.findViewById(R.id.sticker_list_recycler_view_emoji_bottom_sheet_fragment);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        emojiRecyclerView.setLayoutManager(gridLayoutManager);
        StickerRecyclerViewAdapter stickerAdapter = new StickerRecyclerViewAdapter();
        emojiRecyclerView.setAdapter(stickerAdapter);

        stickerAdapter.setOnStickerClickListener(new StickerRecyclerViewAdapter.OnStickerClickListener() {
            @Override
            public void onStickerClickListener(Bitmap sticker) {
                onStickerOptionsChange.onStickerOptionsChange(sticker);
                dismiss();
            }
        });
    }
}
