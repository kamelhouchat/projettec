package com.example.projettech.Controller.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projettech.Controller.StudioActivity;
import com.example.projettech.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;

public class Studio_fragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_studio, container, false);

        Context applicationContext = StudioActivity.getContextOfApplication();

        String image_path = getArguments().getString("image");
        Uri image_uri = Uri.parse(image_path);
        Bitmap captImage = null;
        try {
            captImage = MediaStore.Images.Media.getBitmap(
                    applicationContext.getContentResolver(), image_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int height = captImage.getHeight();
        int width = captImage.getWidth();

        ImageView image_view = (ImageView) v.findViewById(R.id.image_view);
        Glide.with(applicationContext).load(captImage).override(width,height).into(image_view);

        //image_view.setImageBitmap(captImage);

        return v;
    }

    public static Studio_fragment newInstance(String image_path) {

        Studio_fragment f = new Studio_fragment();
        Bundle b = new Bundle();

        b.putString("image", image_path);

        f.setArguments(b);

        return f;
    }
}
