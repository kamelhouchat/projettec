package com.projetTec.imageStudio.controller.bottomDialogFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.projetTec.imageStudio.R;
import com.projetTec.imageStudio.controller.adapters.EmojiRecyclerViewAdapter;

public class EmojiBottomDialogFragment extends BottomSheetDialogFragment {

/*    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };*/

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_dialog_fragment_emoji, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

/*        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }*/

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        RecyclerView rvEmoji = contentView.findViewById(R.id.emoji_list_recycler_view_emoji_bottom_sheet_fragment);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        rvEmoji.setLayoutManager(gridLayoutManager);
        EmojiRecyclerViewAdapter emojiAdapter = new EmojiRecyclerViewAdapter();
        rvEmoji.setAdapter(emojiAdapter);
    }

}
