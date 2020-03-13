package com.projettec.imageStudio.Controller.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.projettec.imageStudio.Controller.BarChart;
import com.projettec.imageStudio.Controller.StudioActivity;
import com.projettec.imageStudio.R;

import java.io.IOException;
import java.io.OutputStream;


public class Plus_fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plus, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvFragSecond);
        tv.setText(getArguments().getString("msg"));

        final String image_path = getArguments().getString("image_path");

        Button barchart_botton = (Button) v.findViewById(R.id.barchart_botton);
        barchart_botton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(StudioActivity.getContextOfApplication(), BarChart.class);
                intent.putExtra("image_path", image_path);
                startActivity(intent);
            }
        });


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