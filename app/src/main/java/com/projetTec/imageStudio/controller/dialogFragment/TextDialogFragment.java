package com.projetTec.imageStudio.controller.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projetTec.imageStudio.R;
import com.projetTec.imageStudio.controller.adapters.ColorPickerAdapter;

/**
 * <p>
 * The class extends from DialogFragment, it allows to implement and display a fragment dialog in which
 * the user can choose the color of the text and enter a text then validate it.
 * </p>
 *
 * @author Kamel.H
 * @see ja.burhanrashid52.photoeditor.PhotoEditor
 */

public class TextDialogFragment extends DialogFragment {

    private static final String TAG = "TextDialogFragment";
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String EXTRA_COLOR_CODE = "extra_color_code";

    //The place where we will enter text
    private EditText addTextEditText;

    //The done button
    private TextView addTextDoneTextView;

    //An InputMethodManager which will allow us to manage the keyboard display
    private InputMethodManager inputMethodManager;

    //A color that will contain the color chosen by the user
    private int colorCode;

    //The textEditor method which facilitates the management of listener
    private TextEditor textEditor;

    /**
     * <p>
     * The interface which contains the method which manages the listener of the done button.
     * </p>
     *
     * @author Kamel.H
     * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
     * @see TextDialogFragment
     */
    public interface TextEditor {

        /**
         * <p>
         * The listener which will be called when the user presses the done button.
         * </p>
         *
         * @param inputText The text written by the user
         * @param colorCode The color chosen by the user
         * @see TextDialogFragment
         * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
         */
        void onDone(String inputText, int colorCode);

    }

    /**
     * <p>
     * The listener of textEditor, which allows to make a callback to listener if user is done with text editing.
     * </p>
     *
     * @param textEditor The new listener
     */
    public void setOnTextEditorListener(TextEditor textEditor) {
        this.textEditor = textEditor;
    }

    //Show dialog with provide text and text color
    public static TextDialogFragment show(FragmentActivity appCompatActivity, String inputText, int colorCode) {
        Bundle args = new Bundle();
        args.putString(EXTRA_INPUT_TEXT, inputText);
        args.putInt(EXTRA_COLOR_CODE, colorCode);
        TextDialogFragment fragment = new TextDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    //Show dialog with default text input as empty and text color white
    public static TextDialogFragment show(FragmentActivity appCompatActivity) {
        return show(appCompatActivity,
                "", ContextCompat.getColor(appCompatActivity, R.color.white));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //Make dialog full screen with transparent background
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_add_text, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addTextDoneTextView = view.findViewById(R.id.done_add_text_dialog_fragment);
        addTextEditText = view.findViewById(R.id.edit_text_add_text_dialog_fragment);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        //Setup the color picker for text color
        RecyclerView addTextColorPickerRecyclerView = view.findViewById(R.id.color_picker_recycler_view_add_text_dialog_fragment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter();
        //This listener will change the text color when clicked on any color from picker
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                TextDialogFragment.this.colorCode = colorCode;
                addTextEditText.setTextColor(colorCode);
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        addTextEditText.setText(getArguments().getString(EXTRA_INPUT_TEXT));
        colorCode = getArguments().getInt(EXTRA_COLOR_CODE);
        addTextEditText.setTextColor(colorCode);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //Make a callback on activity when user is done with text editing
        addTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dismiss();
                String inputText = addTextEditText.getText().toString();
                if (!TextUtils.isEmpty(inputText) && textEditor != null) {
                    textEditor.onDone(inputText, colorCode);
                }
            }
        });

    }

}
