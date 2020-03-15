package com.projettec.imageStudio.controller.fragment;

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

import com.projettec.imageStudio.controller.BarChart;
import com.projettec.imageStudio.controller.StudioActivity;
import com.projettec.imageStudio.R;


public class Plus_fragment extends Fragment {

    private ImageView buttonBarchart;
    private TextView imageWidth;
    private TextView imageHeight;

    private Bitmap editedImage;

    private SpannableString spannableStrings;
    private StyleSpan boldItalic;
    private String text_barchart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plus, container, false);
        final String image_path = getArguments().getString("image_path");

        //editedImage = Studio_fragment.getCaptImage().copy(Studio_fragment.getCaptImage().getConfig(), false);

        buttonBarchart = v.findViewById(R.id.barchart_button);
        imageWidth = v.findViewById(R.id.fragment_plus_image_width);
        imageHeight = v.findViewById(R.id.fragment_plus_image_height);
        imageWidth.setText(String.valueOf(Studio_fragment.getCaptImage().getWidth()));
        imageHeight.setText(String.valueOf(Studio_fragment.getCaptImage().getHeight()));

        /*text_barchart = (String) button_barchart.getText();
        spannableStrings = new SpannableString(text_barchart);
        boldItalic = new StyleSpan(Typeface.BOLD_ITALIC);
        spannableStrings.setSpan(boldItalic, 0, text_barchart.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        button_barchart.setText(spannableStrings);*/
        buttonBarchart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudioActivity.getContextOfApplication(), BarChart.class);
                intent.putExtra("image_path", image_path);
                startActivity(intent);
            }
        });

        ImageView Image_edited = v.findViewById(R.id.fragment_plus_image);
        Image_edited.setImageBitmap(Studio_fragment.getCaptImage());



        /*TextView tv = (TextView) v.findViewById(R.id.tvFragSecond);
        tv.setText(getArguments().getString("msg"));

        final String image_path = getArguments().getString("image_path");

        Button barchart_botton = (Button) v.findViewById(R.id.barchart_botton);
        barchart_botton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(StudioActivity.getContextOfApplication(), BarChart.class);
                intent.putExtra("image_path", image_path);
                startActivity(intent);
            }
        });*/




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

}