package com.projettec.imageStudio.controller.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.projettec.imageStudio.controller.HistogramActivity;
import com.projettec.imageStudio.controller.StudioActivity;
import com.projettec.imageStudio.R;

/**
 * @author Kamel.H
 * @see HistogramActivity
 * @see Fragment
 */

public class Plus_fragment extends Fragment {

    private View v ;

    private ImageView buttonBarchart;
    private TextView imageWidth;
    private TextView imageHeight;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_plus, container, false);

        final String image_path = getArguments().getString("image_path");

        initView();

        imageWidth.setText(String.valueOf(Studio_fragment.getCaptImage().getWidth()));
        imageHeight.setText(String.valueOf(Studio_fragment.getCaptImage().getHeight()));

        buttonBarchart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudioActivity.getContextOfApplication(), HistogramActivity.class);
                intent.putExtra("image_path", image_path);
                startActivity(intent);
            }
        });

        ImageView Image_edited = v.findViewById(R.id.fragment_plus_image);
        Image_edited.setImageBitmap(Studio_fragment.getCaptImage());

        return v;
    }

    public static Plus_fragment newInstance(String text, String image_path) {

        Plus_fragment f = new Plus_fragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        b.putString("image_path", image_path);

        f.setArguments(b);

        return f;
    }

    public void initView() {
        buttonBarchart = v.findViewById(R.id.barchart_button);
        imageWidth = v.findViewById(R.id.fragment_plus_image_width);
        imageHeight = v.findViewById(R.id.fragment_plus_image_height);
    }
}