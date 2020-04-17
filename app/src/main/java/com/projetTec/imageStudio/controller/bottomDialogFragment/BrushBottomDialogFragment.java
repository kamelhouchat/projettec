package com.projetTec.imageStudio.controller.bottomDialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.projetTec.imageStudio.R;
import com.projetTec.imageStudio.controller.adapters.ColorPickerAdapter;
import com.projetTec.imageStudio.controller.bottomDialogFragment.listenerInterface.OnBrushOptionsChange;

/**
 * <p>
 * The BrushBottomSheetDialogFragment class which extends from BottomSheetDialogFragment which allows
 * to implement a Dialog Fragment which contains a ColorPicker which will allow us to change brush color.
 * </p>
 *
 * @author Kamel.H
 * @see ja.burhanrashid52.photoeditor.PhotoEditor
 * @see ColorPickerAdapter
 * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
 */
public class BrushBottomDialogFragment extends BottomSheetDialogFragment {

    //The onBrushOptionsChange method which facilitates the management of listener
    private OnBrushOptionsChange onBrushOptionsChange;

    /**
     * The constructor is empty.
     */
    public BrushBottomDialogFragment() {

    }

    /**
     * <p>
     * The setter of onBrushOptionsChange
     * </p>
     *
     * @param onBrushOptionsChange The new listener.
     */
    public void setOnBrushOptionsChange(OnBrushOptionsChange onBrushOptionsChange) {
        this.onBrushOptionsChange = onBrushOptionsChange;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_dialog_fragment_brush, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView colorPickerRecyclerView = view.findViewById(R.id.color_picker_recycler_view_brush_bottom_sheet_fragment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        colorPickerRecyclerView.setLayoutManager(linearLayoutManager);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter();
        colorPickerRecyclerView.setAdapter(colorPickerAdapter);
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                if (onBrushOptionsChange != null) {
                    dismiss();
                    onBrushOptionsChange.onBrushColorChanged(colorCode);
                }
            }
        });
    }
}
