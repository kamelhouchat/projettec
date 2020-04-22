package com.projetTec.imageStudio.controller.bottomDialogFragment.listenerInterface;

import android.graphics.Bitmap;

/**
 * <p>
 * The interface that will be implemented in the class where the Emoji {@link com.projetTec.imageStudio.controller.bottomDialogFragment.StickerBottomDialogFragment}
 * will be instantiated and used.
 * </p>
 *
 * @author Kamel.H
 * @see com.projetTec.imageStudio.controller.bottomDialogFragment.StickerBottomDialogFragment
 * @see com.projetTec.imageStudio.controller.adapters.StickerRecyclerViewAdapter
 * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
 */
public interface OnStickerOptionsChange {

    /**
     * <p>The listener which will be called when a sticker is selected in the {@link com.projetTec.imageStudio.controller.bottomDialogFragment.EmojiBottomDialogFragment} class.</p>
     *
     * @param stickerImage The sticker converted into bitmap
     * @see com.projetTec.imageStudio.controller.bottomDialogFragment.StickerBottomDialogFragment
     * @see com.projetTec.imageStudio.controller.adapters.StickerRecyclerViewAdapter
     * @see ja.burhanrashid52.photoeditor.PhotoEditor
     */
    void onStickerOptionsChange(Bitmap stickerImage);

}
