package com.projetTec.imageStudio.controller.bottomDialogFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.projetTec.imageStudio.R;
import com.projetTec.imageStudio.controller.adapters.EmojiRecyclerViewAdapter;
import com.projetTec.imageStudio.controller.bottomDialogFragment.listenerInterface.OnEmojiOptionsChange;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * The EmojiBottomSheetDialogFragment class which extends from BottomSheetDialogFragment which allows
 * to implement a Dialog Fragment which contains different emoji which will be selected by user.
 * </p>
 *
 * @author Kamel.H
 * @see ja.burhanrashid52.photoeditor.PhotoEditor
 * @see EmojiRecyclerViewAdapter
 * @see OnEmojiOptionsChange
 * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
 */
public class EmojiBottomDialogFragment extends BottomSheetDialogFragment {

    //The onEmojiOptionsChange method which facilitates the management of listener
    private OnEmojiOptionsChange onEmojiOptionsChange;

    /**
     * <p>
     * The setter of onEmojiOptionsChange
     * </p>
     *
     * @param onEmojiOptionsChange The new listener.
     */
    public void setOnEmojiOptionsChange(OnEmojiOptionsChange onEmojiOptionsChange) {
        this.onEmojiOptionsChange = onEmojiOptionsChange;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NotNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_dialog_fragment_emoji, null);
        dialog.setContentView(contentView);

        //noinspection deprecation
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        RecyclerView emojiRecyclerView = contentView.findViewById(R.id.emoji_list_recycler_view_emoji_bottom_sheet_fragment);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        emojiRecyclerView.setLayoutManager(gridLayoutManager);
        EmojiRecyclerViewAdapter emojiAdapter = new EmojiRecyclerViewAdapter();
        emojiRecyclerView.setAdapter(emojiAdapter);

        emojiAdapter.setOnEmojiClickListener(new EmojiRecyclerViewAdapter.OnEmojiClickListener() {
            @Override
            public void onEmojiClickListener(String emojiCode) {
                onEmojiOptionsChange.onEmojiOptionsChange(emojiCode);
                dismiss();
            }
        });
    }
}
