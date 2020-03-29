package com.projetTec.imageStudio.controller.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.polyak.iconswitch.IconSwitch;
import com.projetTec.imageStudio.controller.HistogramActivity;
import com.projetTec.imageStudio.controller.StudioActivity;
import com.projetTec.imageStudio.R;
import com.tapadoo.alerter.Alerter;

import java.util.Objects;

/**
 * <p>
 * Plus fragment is a class that extends from {@link Fragment}, it is used for the management and display of the layout plus_fragment
 * </p>
 *
 * @author Kamel.H
 * @see HistogramActivity
 * @see Fragment
 */

public class Plus_fragment extends Fragment implements View.OnClickListener{

    // The layout view
    private View v;

    //The button which will display the histogram
    private ImageView buttonHistog;

    //The TextView on which the width of the image will be displayed
    private TextView imageWidth;

    //The TextView on which the height of the image will be displayed
    private TextView imageHeight;

    //The button which redirects to the GITHUB repository
    private TextView githubButton;

    //The toggle which allows you to choose between java and renderscript
    private IconSwitch iconSwitchRenderscriptJava;

    //The toggle which allows you to choose between brightness_RGB and brightness_HSV
    private IconSwitch iconSwitchBrightnessRGB_HSV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_plus, container, false);

        final String image_path = Objects.requireNonNull(getArguments()).getString("image_path");

        initView();

        imageWidth.setText(String.valueOf(Studio_fragment.getCaptImage().getWidth()));
        imageHeight.setText(String.valueOf(Studio_fragment.getCaptImage().getHeight()));

        buttonHistog.setOnClickListener(new View.OnClickListener() {
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

    /**
     * <p>
     * A static method which allows you to instantiate an object of the plus_fragment class without going through a constructor.
     * The elements necessary for the functioning of Plus_fragment are passed by a Bundle
     * </p>
     *
     * @param image_path The path of the image
     * @return An instance of the Plus_fragment class
     * @see Plus_fragment
     * @see Bundle
     */
    public static Plus_fragment newInstance(String image_path) {
        Plus_fragment f = new Plus_fragment();
        Bundle b = new Bundle();
        b.putString("image_path", image_path);

        f.setArguments(b);

        return f;
    }

    /**
     * <p>Method that initializes views
     */
    private void initView() {
        buttonHistog = v.findViewById(R.id.histogram_button);

        imageWidth = v.findViewById(R.id.fragment_plus_image_width);

        imageHeight = v.findViewById(R.id.fragment_plus_image_height);

        githubButton = v.findViewById(R.id.fragment_plus_github_button);
        githubButton.setOnClickListener(this);

        iconSwitchRenderscriptJava = v.findViewById(R.id.toggle_renderscript_java);
        iconSwitchRenderscriptJava.setCheckedChangeListener(new IconSwitch.CheckedChangeListener() {
            @Override
            public void onCheckChanged(IconSwitch.Checked current) {
                switch (current){
                    case LEFT:
                        Studio_fragment.setIsRenderScript(true);
                        showAlerter("Choix : Render script");
                        break;
                    case RIGHT:
                        Studio_fragment.setIsRenderScript(false);
                        showAlerter("Choix : Java");
                        break;
                }
            }
        });

        iconSwitchBrightnessRGB_HSV = v.findViewById(R.id.toggle_brightness_hsv_rgb);
        iconSwitchBrightnessRGB_HSV.setCheckedChangeListener(new IconSwitch.CheckedChangeListener() {
            @Override
            public void onCheckChanged(IconSwitch.Checked current) {
                switch (current){
                    case LEFT:
                        Studio_fragment.setIsBrightnessRGB(true);
                        showAlerter("Choix : RGB");
                        break;
                    case RIGHT:
                        Studio_fragment.setIsBrightnessRGB(false);
                        showAlerter("Choix : HSV");
                        break;
                }
            }
        });
    }

    /**
     * <p>Method for managing listener of other view of the plus_fragment layout.
     *
     * @param v The view we just clicked on
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_plus_github_button:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kamelhouchat/projettec"));
                startActivity(browserIntent);
                break;
        }
    }

    /**
     * <p>Method for showing alerter
     *
     * @param string Text to show
     */
    private void showAlerter(String string) {
        Alerter.create(Objects.requireNonNull(getActivity()))
                .setText(string)
                .setBackgroundColorRes(R.color.blue_active)
                .setDuration(300)
                .show();
    }
}