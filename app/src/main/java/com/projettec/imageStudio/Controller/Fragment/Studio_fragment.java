package com.projettec.imageStudio.Controller.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.projettec.imageStudio.Controller.Adapter.RecyvlerViewAdapter;
import com.projettec.imageStudio.Controller.StudioActivity;
import com.projettec.imageStudio.Model.Filter;
import com.projettec.imageStudio.R;

import java.io.IOException;
import java.util.ArrayList;

public class Studio_fragment extends Fragment{

    Context applicationContext ;
    View v ;
    Bitmap captImage ;
    PhotoViewAttacher photoView;
    PhotoView photo_view ;
    ArrayList<String> filterName = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_studio, container, false);

        applicationContext = StudioActivity.getContextOfApplication();

        String image_path = getArguments().getString("image");
        Uri image_uri = Uri.parse(image_path);
        captImage = null;
        try {
            captImage = MediaStore.Images.Media.getBitmap(
                    applicationContext.getContentResolver(), image_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //captImage = BitmapFactory.decodeResource(getResources(), R.drawable.pipimage);

        int height = captImage.getHeight();
        int width = captImage.getWidth();

        photo_view = (PhotoView) v.findViewById(R.id.photo_view);
        Glide.with(applicationContext).load(captImage).override(width,height).into(photo_view);
        //photo_view.setImageBitmap(captImage);
        //ImageView image_view = (ImageView) v.findViewById(R.id.image_view);
        //Glide.with(applicationContext).load(captImage).override(width,height).into(image_view);


        //image_view.setImageBitmap(captImage);

        initFilterName();

        return v;
    }

    public static Studio_fragment newInstance(String image_path) {

        Studio_fragment f = new Studio_fragment();
        Bundle b = new Bundle();

        b.putString("image", image_path);

        f.setArguments(b);

        return f;
    }

    public void initFilterName(){
        filterName.add("Gray");
        filterName.add("Colorize");
        filterName.add("KeepColor");
        filterName.add("Cont + Gray");
        filterName.add("Cont + RGB");
        filterName.add("Cont + HSV");
        filterName.add("Cont - Gray");
        filterName.add("Equa Gray");
        filterName.add("Equa RGB");


        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        RecyvlerViewAdapter adapter = new RecyvlerViewAdapter(filterName, captImage ,applicationContext);
        recyclerView.setAdapter(adapter);
    }
}
