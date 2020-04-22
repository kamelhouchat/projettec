package com.projetTec.imageStudio.controller.bottomDialogFragment.listenerInterface;

/**
 * <p>
 * The interface that will be implemented in the class where the Emoji {@link com.projetTec.imageStudio.controller.bottomDialogFragment.EmojiBottomDialogFragment}
 * will be instantiated and used.
 * </p>
 *
 * @author Kamel.H
 * @see com.projetTec.imageStudio.controller.bottomDialogFragment.EmojiBottomDialogFragment
 * @see com.projetTec.imageStudio.controller.adapters.EmojiRecyclerViewAdapter
 * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
 */
public interface OnEmojiOptionsChange {

    /**
     * <p>The listener which will be called when a emoji is selected in the {@link com.projetTec.imageStudio.controller.bottomDialogFragment.EmojiBottomDialogFragment} class.</p>
     *
     * @param emojiCode The emoji code
     * @see com.projetTec.imageStudio.controller.bottomDialogFragment.EmojiBottomDialogFragment
     * @see com.projetTec.imageStudio.controller.adapters.EmojiRecyclerViewAdapter
     * @see ja.burhanrashid52.photoeditor.PhotoEditor
     */
    void onEmojiOptionsChange(String emojiCode);

}
