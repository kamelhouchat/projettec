package com.projetTec.imageStudio.controller.bottomDialogFragment.listenerInterface;

/**
 * <p>
 * The interface that will be implemented in the class where the Brush {@link com.google.android.material.bottomsheet.BottomSheetDialogFragment}
 * will be instantiated and used.
 * </p>
 *
 * @author Kamel.H
 * @see com.projetTec.imageStudio.controller.bottomDialogFragment.BrushBottomDialogFragment
 * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
 */
public interface OnBrushOptionsChange {

    /**
     * <p>The listener which will be called when a color is selected in the BrushBottomSheetDialogFragment class.</p>
     *
     * @param colorCode The color code
     * @see com.projetTec.imageStudio.controller.bottomDialogFragment.BrushBottomDialogFragment
     * @see ja.burhanrashid52.photoeditor.PhotoEditor
     */
    void onBrushColorChanged(int colorCode);

}
