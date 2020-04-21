package com.projetTec.imageStudio.controller.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.projetTec.imageStudio.controller.adapters.EditingToolRecyclerViewAdapter;

import com.projetTec.imageStudio.controller.adapters.FilterRecyclerViewAdapter;
import com.projetTec.imageStudio.controller.StudioActivity;
import com.projetTec.imageStudio.controller.bottomDialogFragment.BrushBottomDialogFragment;
import com.projetTec.imageStudio.controller.bottomDialogFragment.EmojiBottomDialogFragment;
import com.projetTec.imageStudio.controller.bottomDialogFragment.listenerInterface.OnBrushOptionsChange;
import com.projetTec.imageStudio.controller.dialogFragment.TextDialogFragment;
import com.projetTec.imageStudio.model.animation.ViewAnimation;
import com.projetTec.imageStudio.model.editingImage.additionalFilters.AdditionalFilters;
import com.projetTec.imageStudio.model.editingImage.additionalFilters.FaceDetection;
import com.projetTec.imageStudio.model.editingImage.basicFilters.Conversion;
import com.projetTec.imageStudio.model.editingImage.basicFilters.Convolution;
import com.projetTec.imageStudio.model.editingImage.basicFilters.DynamicExtension;
import com.projetTec.imageStudio.model.editingImage.basicFilters.Equalization;
import com.projetTec.imageStudio.model.editingImage.basicFilters.Filters;
import com.projetTec.imageStudio.R;
import com.projetTec.imageStudio.model.filters.FilterType;
import com.projetTec.imageStudio.model.filters.OnItemFilterSelected;
import com.projetTec.imageStudio.model.imageAdaptation.BitmapImageAdaptation;
import com.projetTec.imageStudio.model.tools.OnItemToolSelected;
import com.projetTec.imageStudio.model.tools.ToolType;
import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.takusemba.cropme.CropLayout;
import com.takusemba.cropme.OnCropListener;
import com.tapadoo.alerter.Alerter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Studio fragment is a class that extends from {@link Fragment}, it is used for the management and display of the layout studio_fragment.
 * Studio Fragment is the main fragment and it manages:
 *     <ul>
 *         <li>Initialization and the display of all views</li>
 *         <li>Initialization of the filter recycle view</li>
 *         <li>Initialization of the tool recycle view</li>
 *         <li>Listeners of tools</li>
 *         <li>Listeners of filters</li>
 *         <li>Listeners of filters</li>
 *         <li>Listeners of all views in studio fragment</li>
 *         <li>Save image</li>
 *         <li>Rotate image</li>
 *         <li>Crop image</li>
 *         <li>Animation management and display of views corresponding to the selected action</li>
 *     </ul>
 * </p>
 *
 * @author Kamel.H
 * @see Fragment
 * @see Filters
 * @see DynamicExtension
 * @see Equalization
 * @see Convolution
 * @see FilterRecyclerViewAdapter
 * @see EditingToolRecyclerViewAdapter
 * @see OnItemFilterSelected
 * @see OnItemToolSelected
 * @see ViewAnimation
 */

@SuppressWarnings("FieldCanBeLocal")
public class Studio_fragment extends Fragment implements OnItemToolSelected, OnItemFilterSelected, View.OnClickListener, OnPhotoEditorListener, OnBrushOptionsChange {

    //The bitmap passed by MainActivity, used to reset to main state
    private static Bitmap captImage;

    //Used to reset before applying filter
    private Bitmap loadedToRestore;

    //Used for applying changes
    private Bitmap loadedToChange;

    //Class instances that contain filters
    private Filters filters;
    private DynamicExtension dynamicExtension;
    private Equalization equalization;
    private Convolution convolution;
    private AdditionalFilters additionalFilters;

    //The activity context
    private Context applicationContext;

    //The view if layout
    private View v;

    //Filter an tool recyclers views
    private RecyclerView filterRecyclerView, editingToolRecyclerView;

    //The Crop image layout
    private CropLayout cropLayout;

    //The photo view layout (Support zoom)
    private PhotoView photoView;

    //The photo editor layout (Support Text, Emoji, Sticker, Brush, Eraser)
    private PhotoEditorView photoEditorView;

    //The photo editor to use the image editing feature
    private PhotoEditor photoEditor;

    //The images present in the layout
    private ImageView undoImage, saveImage, restoreImage, rotateLeft, rotateRight;

    //The top center text
    private TextView centerText;

    //The colored seek bar (Used to apply colorize filter)
    private ColorSeekBar colorSeekBar;

    //The seek bar is used to apply brightness filter
    private SeekBar seekBar;

    //The rotation linear layout (It contain two button 'Left and Right')
    private LinearLayout rotationButtonLayout;

    //An instance of brush bottom dialog fragment
    private final BrushBottomDialogFragment brushBottomDialogFragment = new BrushBottomDialogFragment();

    //Booleans to find out which action is selected at a given time
    private boolean isFilter = false;
    private boolean isColorizeOrShading = false;
    private boolean isCropImage = false;
    private boolean isRotate = false;
    private boolean isBrightness = false;
    private boolean isSaturation = false;
    private boolean isText = false;
    private boolean isBrush = false;

    //Boolean to choose if we want to execute the methods in java or render script
    private static boolean isRenderScript = true;

    //Boolean to choose if we want to execute the brightness method using HSV or RGB
    private static boolean isBrightnessRGB = true;

    //The image path
    private String imagePath;

    //The image's uri
    private Uri imageUri;

    private static final String TAG = "Studio_fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_studio, container, false);

        initView();

        isBrightnessRGB = true;
        isRenderScript = true;

        brushBottomDialogFragment.setOnBrushOptionsChange(this);

        applicationContext = StudioActivity.getContextOfApplication();

        imagePath = Objects.requireNonNull(getArguments()).getString("image");
        imageUri = Uri.parse(imagePath);
        captImage = null;
        try {
            captImage = MediaStore.Images.Media.getBitmap(
                    applicationContext.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //captImage = BitmapFactory.decodeResource(getResources(), R.drawable.tom);

        captImage = BitmapImageAdaptation.getReducedBitmap(captImage);

        try {
            BitmapImageAdaptation.fixAutoRotate(captImage, applicationContext.getContentResolver().openInputStream(imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadedToRestore = captImage.copy(captImage.getConfig(), true);
        loadedToChange = captImage.copy(captImage.getConfig(), true);

        //Load filters
        filters = new Filters(applicationContext);
        dynamicExtension = new DynamicExtension(applicationContext);
        equalization = new Equalization(applicationContext);
        convolution = new Convolution(applicationContext);
        additionalFilters = new AdditionalFilters(applicationContext);

        int height = captImage.getHeight();
        int width = captImage.getWidth();

        Glide.with(applicationContext).load(captImage).override(width, height).into(photoView);


        //Glide.with(applicationContext).load(captImage).centerInside().into(photo_view);
        //photo_view.setImageBitmap(captImage);
        //Glide.with(applicationContext).load(captImage).override(width,height).into(image_view);


        initFilterRecyclerView();
        initEditingToolRecyclerView();

        return v;
    }

    /**
     * <p>
     * A static method which allows you to instantiate an object of the studio_fragment class without going through a constructor.
     * The elements necessary for the functioning of Plus_fragment are passed by a Bundle.
     * </p>
     *
     * @param image_path The path of the image
     * @return An instance of the Studio_fragment class
     * @see Studio_fragment
     * @see Bundle
     */
    public static Studio_fragment newInstance(String image_path) {

        Studio_fragment f = new Studio_fragment();
        Bundle b = new Bundle();

        b.putString("image", image_path);

        f.setArguments(b);

        return f;
    }

    /**
     * <p>Method that initializes views.
     */
    private void initView() {
        colorSeekBar = v.findViewById(R.id.seek);
        colorSeekBar.setVisibility(View.INVISIBLE);

        undoImage = v.findViewById(R.id.fragment_studio_undo_parent);
        undoImage.setOnClickListener(this);

        saveImage = v.findViewById(R.id.fragment_studio_save);
        saveImage.setOnClickListener(this);

        restoreImage = v.findViewById(R.id.fragment_studio_restore);
        restoreImage.setOnClickListener(this);

        centerText = v.findViewById(R.id.fragment_studio_center_text);

        photoView = v.findViewById(R.id.photo_view);

        photoEditorView = v.findViewById(R.id.photo_editor);
        photoEditorView.setVisibility(View.INVISIBLE);

        cropLayout = v.findViewById(R.id.crop_view);
        cropLayout.setVisibility(View.INVISIBLE);

        rotationButtonLayout = v.findViewById(R.id.rotation_button_menu);
        rotationButtonLayout.setVisibility(View.INVISIBLE);

        rotateLeft = v.findViewById(R.id.rotate_left);
        rotateLeft.setOnClickListener(this);

        rotateRight = v.findViewById(R.id.rotate_right);
        rotateRight.setOnClickListener(this);

        seekBar = v.findViewById(R.id.brightness_seek_bar);
        seekBar.setVisibility(View.INVISIBLE);

        photoEditor = new PhotoEditor.Builder(getContext(), photoEditorView).build();
        photoEditor.setOnPhotoEditorListener(this);
    }

    /**
     * <p>Method which initializes the filter recycler view.
     */
    private void initFilterRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false);
        filterRecyclerView = v.findViewById(R.id.filter_recyclerview);
        filterRecyclerView.setLayoutManager(layoutManager);
        FilterRecyclerViewAdapter adapter = new FilterRecyclerViewAdapter(captImage, applicationContext, this);
        filterRecyclerView.setAdapter(adapter);
        filterRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * <p>Method which initializes the filter recycler view.
     */
    private void initEditingToolRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false);
        editingToolRecyclerView = v.findViewById(R.id.editing_tool_recyclerview);
        editingToolRecyclerView.setLayoutManager(layoutManager);
        EditingToolRecyclerViewAdapter adapter = new EditingToolRecyclerViewAdapter(this);
        editingToolRecyclerView.setAdapter(adapter);
    }

    /**
     * <p>Bitmap getter passed by main activity.
     *
     * @return The bitmap passed by MainActivity
     */
    public static Bitmap getCaptImage() {
        return captImage;
    }

    /**
     * <p>Setter for isRenderScript boolean.
     *
     * @param isRenderScript true if in render script, else false
     * @see Filters
     * @see DynamicExtension
     * @see Equalization
     * @see Convolution
     */
    public static void setIsRenderScript(boolean isRenderScript) {
        Studio_fragment.isRenderScript = isRenderScript;
    }

    /**
     * <p>Setter for isBrightnessRGB boolean.
     *
     * @param isBrightnessRGB true if in RGB, else false
     * @see Filters
     */
    public static void setIsBrightnessRGB(boolean isBrightnessRGB) {
        Studio_fragment.isBrightnessRGB = isBrightnessRGB;
    }

    /**
     * <p>Method that displays an alert : in progress.
     */
    private void incoming() {
        Alerter.create(Objects.requireNonNull(getActivity()))
                .enableProgress(true)
                .setProgressColorRes(R.color.blue_grey_active)
                .setDuration(2000)
                .setBackgroundColorRes(R.color.blue_grey_active)
                .setText("En cours ...")
                .show();
    }

    /**
     * <p>Method for managing listeners of modification tools (Editing tool recycler view).
     *
     * @param toolType The tool Type
     * @see EditingToolRecyclerViewAdapter
     * @see ToolType
     * @see com.projetTec.imageStudio.model.tools.ToolModel
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case EDIT:
                centerText.setText("Recadrer");
                cropImage();
                break;
            case ROTATE:
                centerText.setText("Rotation");
                isRotate = true;
                ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_close_black_24dp);
                ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, editingToolRecyclerView, rotationButtonLayout,
                        0, 200, 200);
                break;
            case BRIGHTNESS:
                centerText.setText("Lumino");
                brightness();
                break;
            case SATURATION:
                centerText.setText("Satura");
                saturation();
                break;
            case FILTER:
                centerText.setText("Filtre");
                isFilter = true;
                ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, editingToolRecyclerView, filterRecyclerView,
                        0, 200, 200);

                ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_close_black_24dp);
                break;
            case TEXT:
                centerText.setText("Texte");
                writeText();
                break;
            case BRUSH:
                centerText.setText("Pinceau");
                //incoming();
                brush();
                break;
            case EMOJI:
                centerText.setText("Emoji");
                //incoming();
                EmojiBottomDialogFragment emojiBottomDialogFragment = new EmojiBottomDialogFragment();
                emojiBottomDialogFragment.show(Objects.requireNonNull(getFragmentManager()),emojiBottomDialogFragment.getTag());
                break;
            case STICKER:
                centerText.setText("Sticker");
                incoming();
                break;
            case FACE:
                centerText.setText("Face");
                FaceDetection faceDetection = new FaceDetection(applicationContext);
                faceDetection.detectFace(loadedToChange, photoView);
                break;
        }
    }

    /**
     * <p>Method for managing listeners of filters (Filter recycler view).
     *
     * @param filterType The filter Type
     * @see FilterRecyclerViewAdapter
     * @see FilterType
     * @see com.projetTec.imageStudio.model.filters.FilterModel
     */
    @SuppressWarnings({"ConstantConditions"})
    @Override
    public void onFilterSelected(FilterType filterType) {
        //Bitmap loadedToChange = Bitmap.createBitmap(this.loadedImage);
        /*final Bitmap loadedToChange = Bitmap.createScaledBitmap(this.loadedImage,
                50,
                50,
                true);*/

        //final Bitmap loadedToChange = captImage.copy(captImage.getConfig(), true);

        loadedToChange = loadedToRestore.copy(loadedToRestore.getConfig(), true);

        switch (filterType) {
            case TO_GRAY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                if (isRenderScript)
                    filters.tograyRS(loadedToChange);
                else if (!isRenderScript)
                    filters.toGrays(loadedToChange);
                break;
            case COLORIZE:
                ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, filterRecyclerView, colorSeekBar,
                        0, 200, 200);
                isColorizeOrShading = true;
                isFilter = false;
                colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    @Override
                    public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                        float[] hsv = new float[3];
                        Conversion.RGBToHSV_new(Color.red(color), Color.green(color), Color.blue(color), hsv);
                        if (isRenderScript)
                            filters.colorizeRS(loadedToChange, hsv[0]);
                        else if (!isRenderScript)
                            filters.colorize(loadedToChange, hsv[0]);
                        photoView.setImageBitmap(loadedToChange);
                    }
                });
                break;
            case KEEP_COLOR:
                colorSeekBar.setVisibility(View.INVISIBLE);
                if (isRenderScript)
                    filters.KeepColorRS(loadedToChange, 90);
                else if (!isRenderScript)
                    filters.keepColor(loadedToChange, Color.rgb(0, 0, 255), 15);
                break;
            case CONTRAST_PLUS_GRAY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                if (isRenderScript)
                    dynamicExtension.contrastPlusGrayRS(loadedToChange);
                else if (!isRenderScript)
                    dynamicExtension.contrastPlusGrayLut(loadedToChange);
                break;
            case CONTRAST_PLUS_RGB:
                colorSeekBar.setVisibility(View.INVISIBLE);
                if (isRenderScript)
                    dynamicExtension.contrastPlusRGB_RS(loadedToChange);
                else if (!isRenderScript)
                    dynamicExtension.contrastPlusCouleurRGB(loadedToChange);
                break;
            case CONTRAST_PLUS_HSV:
                colorSeekBar.setVisibility(View.INVISIBLE);
                if (isRenderScript)
                    dynamicExtension.contrastPlusHSV_RS(loadedToChange);
                else if (!isRenderScript)
                    dynamicExtension.contrastPlusCouleurHSV(loadedToChange);
                break;
            case CONTRAST_FEWER_GRAY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                if (isRenderScript)
                    dynamicExtension.contrastFewerGrayRS(loadedToChange);
                else if (!isRenderScript)
                    dynamicExtension.contrastFewerGray(loadedToChange);
                break;
            case EQUALIZATION_GRAY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                if (isRenderScript)
                    equalization.equalizationGrayRS(loadedToChange);
                else if (!isRenderScript)
                    equalization.equalizationGray(loadedToChange);
                break;
            case EQUALIZATION_RGB:
                colorSeekBar.setVisibility(View.INVISIBLE);
                if (isRenderScript)
                    equalization.equalizationRGB_RS(loadedToChange);
                else if (!isRenderScript)
                    equalization.equalizationCouleur(loadedToChange);
                break;
            case CONVOLUTION_MOY:
                colorSeekBar.setVisibility(View.INVISIBLE);
                int size = 7;
                /*if (!isRenderScript){
                    int[][] filterMoy = new int[size][size];
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            filterMoy[i][j] = 1;
                        }
                    }
                    convolution.convolutions(loadedToChange, filterMoy);
                } else { */
                    int[] filterMoy = new int[size];
                    for (int i = 0; i < size; i++)
                        filterMoy[i] = 1;
                    convolution.convolutionAverageFilterRS(loadedToChange, filterMoy);
                //}
                break;
            case CONVOLUTION_GAUS:
                colorSeekBar.setVisibility(View.INVISIBLE);
                //if (!isRenderScript){
                    int[][] filterGaus = {
                            {1, 2, 3, 2, 1},
                            {2, 6, 8, 6, 2},
                            {3, 8, 10, 8, 3},
                            {2, 6, 8, 6, 2},
                            {1, 2, 3, 2, 1}};
                    convolution.convolutions(loadedToChange, filterGaus);
                /*}else {
                    int[] filterGaus = {1, 2, 3, 2, 1,
                                        2, 6, 8, 6, 2,
                                        3, 8, 10, 8, 3,
                                        2, 6, 8, 6, 2,
                                        1, 2, 3, 2, 1};
                    convolution.convolutionAverageFilterRS(loadedToChange, filterGaus);
                }*/

                break;
            case CONTOUR:
                colorSeekBar.setVisibility(View.INVISIBLE);
                filters.tograyRS(loadedToChange);
                /*if (!isRenderScript){
                    int[][] gx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
                    int[][] gy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
                    Convolution.contours(loadedToChange, gx, gy);
                }
                else {*/
                    int[] gx =  {-1,0,1,-2,0,2,-1,0,1};
                    int[] gy =  {-1,-2,-1,0,0,0,1,2,1};
                    convolution.contoursFilterRS(loadedToChange,gx,gy);
                //}

                break;
            case SNOW_EFFECT:
                if (isRenderScript)
                    additionalFilters.snowAndBlackEffectRS(loadedToChange, true);
                else if (!isRenderScript)
                    additionalFilters.snowAndBlackEffect(loadedToChange, true);
                break;
            case BLACK_EFFECT:
                if (isRenderScript)
                    additionalFilters.snowAndBlackEffectRS(loadedToChange, false);
                else if (!isRenderScript)
                    additionalFilters.snowAndBlackEffect(loadedToChange, false);
                break;
            case NOISE_EFFECT:
                if (isRenderScript)
                    additionalFilters.noiseEffectRS(loadedToChange);
                else if (!isRenderScript)
                    additionalFilters.noiseEffect(loadedToChange);
                break;
            case INVERT_EFFECT:
                if (isRenderScript)
                    additionalFilters.invertEffectRS(loadedToChange);
                else if (!isRenderScript) {
                    additionalFilters.invertEffect(loadedToChange);
                }
                break;
            case SHADING_EFFECT:
                ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, filterRecyclerView, colorSeekBar,
                        0, 200, 200);
                isColorizeOrShading = true;
                isFilter = false;
                colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    @Override
                    public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                        if (isRenderScript)
                            loadedToChange = additionalFilters.shadingFilterRS(loadedToRestore, color);
                        else if (!isRenderScript)
                            loadedToChange = additionalFilters.shadingFilter(loadedToRestore, color);
                        photoView.setImageBitmap(loadedToChange);
                    }
                });
                break;
            case EQUALIZATION_YUV:
                additionalFilters.equalizationYuvY(loadedToChange);
                break;
            case MIX_EQUALIZATION_RGB_YUV:
                additionalFilters.mixEqualizationRgbYuv(loadedToChange);
                break;

        }
        //Glide.with(mContext).load(this.loadedImage).into(photoView);
        photoView.setImageBitmap(loadedToChange);
    }

    /**
     * <p>Method for managing listener of other view of the studio_fragment layout.
     *
     * @param v The view we just clicked on
     * @see ViewAnimation
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_studio_undo_parent:
                goBack();
                break;
            case R.id.fragment_studio_save:
                if (isBrush) {
                    photoEditor.setBrushDrawingMode(true);
                    brushBottomDialogFragment.show(Objects.requireNonNull(getFragmentManager()),brushBottomDialogFragment.getTag());
                }
                else {
                    final SweetAlertDialog saveAlerter = new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.PROGRESS_TYPE);
                    saveAlerter.setTitleText("Sauvgarde ...").show();
                    final Handler saveHandler = new Handler();
                    saveHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            saveImage(loadedToChange);
                            saveAlerter.hide();
                            new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Sauvegardé avec succès!")
                                    .show();
                        }
                    }, 1000);
                }
                break;
            case R.id.fragment_studio_restore:
                if (isBrush) {
                    photoEditor.brushEraser();
                }
                else {
                    final SweetAlertDialog restoreAlerter = new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.PROGRESS_TYPE);
                    restoreAlerter.setTitleText("Réstauration...").show();
                    final Handler restoreHandler = new Handler();
                    restoreHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadedToChange = captImage.copy(captImage.getConfig(), true);
                            loadedToRestore = captImage.copy(captImage.getConfig(), true);
                            Glide.with(applicationContext).load(captImage).override(captImage.getWidth(), captImage.getHeight()).into(photoView);
                            restoreAlerter.hide();
                        }
                    }, 1000);
                }
                break;
            case R.id.rotate_left:
                rotateImage(270);
                break;
            case R.id.rotate_right:
                rotateImage(90);
                break;
        }
    }

    /**
     * <p>Method which manages the back button, which takes a defined role for each context.
     *
     * @see ViewAnimation
     */
    @SuppressLint("SetTextI18n")
    private void goBack() {
        if (isCropImage) {
            cropLayout.isOffFrame();
            cropLayout.crop();
            centerText.setText("Studio");
            if (isCropImage) {
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, R.anim.frombuttom, editingToolRecyclerView, 0, 200, true);
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_in, saveImage, 0, 200, true);
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_in, restoreImage, 0, 200, true);
                ViewAnimation.viewAnimatedChange(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out, cropLayout, photoView,
                        0, 200, 200);
                photoView.setImageBitmap(loadedToChange);
                isCropImage = false;
            }
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_arrow_left_black_24dp);
        } else if (isFilter) {
            ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, filterRecyclerView, editingToolRecyclerView,
                    0, 200, 200);
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_arrow_left_black_24dp);
            centerText.setText("Studio");
            isFilter = false;
        } else if (isColorizeOrShading) {
            ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, colorSeekBar, filterRecyclerView,
                    0, 200, 200);
            isColorizeOrShading = false;
            isFilter = true;
        } else if (isRotate) {
            isRotate = false;
            centerText.setText("Studio");
            ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, rotationButtonLayout, editingToolRecyclerView,
                    0, 200, 200);
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_arrow_left_black_24dp);
        } else if (isBrightness) {
            isBrightness = false;
            centerText.setText("Studio");
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_arrow_left_black_24dp);
            ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, seekBar, editingToolRecyclerView,
                    0, 200, 200);
            loadedToChange = loadedToRestore.copy(loadedToRestore.getConfig(), true);
        } else if (isSaturation) {
            isSaturation = false;
            centerText.setText("Studio");
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_arrow_left_black_24dp);
            ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, seekBar, editingToolRecyclerView,
                    0, 200, 200);
            loadedToChange = loadedToRestore.copy(loadedToRestore.getConfig(), true);
        } else if (isText) {
            isText = false;
            centerText.setText("Studio");
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_arrow_left_black_24dp);
            ViewAnimation.viewAnimatedHideOrShow(applicationContext, R.anim.frombuttom, editingToolRecyclerView, 0, 200, true);
            ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_in, saveImage, 0, 200, true);
            ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_in, restoreImage, 0, 200, true);
            ViewAnimation.viewAnimatedChange(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out, photoEditorView, photoView,
                    0, 200, 200);
            saveImageAfterChangesPhotoEditor();
        } else if (isBrush) {
            isBrush = false;
            photoEditor.setBrushDrawingMode(false);
            centerText.setText("Studio");
            ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_arrow_left_black_24dp);
            ViewAnimation.viewAnimatedHideOrShow(applicationContext, R.anim.frombuttom, editingToolRecyclerView, 0, 200, true);
            ViewAnimation.imageViewAnimatedChange(applicationContext, saveImage, R.drawable.ic_save_black_24dp);
            ViewAnimation.imageViewAnimatedChange(applicationContext, restoreImage, R.drawable.ic_restore_black_24dp);
            ViewAnimation.viewAnimatedChange(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out, photoEditorView, photoView,
                    0, 200, 200);
            saveImageAfterChangesPhotoEditor();
        } else {
            if (captImage.sameAs(loadedToChange))
                Objects.requireNonNull(getActivity()).finish();
            else {
                new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Êtes-vous sûr?")
                        .setContentText("Vous perdrez vos modifications! Sauvgardez avant")
                        .setConfirmText("Oui, je suis sûr!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog
                                        .setTitleText("Retour!")
                                        .setContentText("Retour a la page principal!")
                                        .hideConfirmButton()
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Objects.requireNonNull(getActivity()).finish();
                                    }
                                }, 1000);
                            }
                        })
                        .show();
            }
        }
    }

    /**
     * <p>Method which allows you to save an image (bitmap) in the gallery and to refresh it.
     *
     * @param finalBitmap The bitmap we want to save
     * @see File
     * @see FileOutputStream
     * @see MediaScannerConnection
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/Image_Studio");
        myDir.mkdirs();

        String fileName = "Image-" + System.currentTimeMillis() + ".jpg";
        File file = new File(myDir, fileName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Tell the media scanner about the new file so that it is immediately available to the user.
        MediaScannerConnection.scanFile(applicationContext, new String[]{file.toString()}, null, null);
    }

    /**
     * <p>Method which allows to use a {@link CropLayout} in order to crop an image and save the result in a Bitmap.
     *
     * @see CropLayout
     * @see ViewAnimation
     */
    private void cropImage() {
        ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_close_black_24dp);
        ViewAnimation.viewAnimatedHideOrShow(applicationContext, R.anim.tobuttom, editingToolRecyclerView, 0, 200, false);
        ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_out, saveImage, 0, 200, false);
        ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_out, restoreImage, 0, 200, false);
        ViewAnimation.viewAnimatedChange(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out, photoView, cropLayout,
                0, 200, 200);
        cropLayout.addOnCropListener(new OnCropListener() {
            @Override
            public void onSuccess(@NotNull Bitmap bitmap) {
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, R.anim.frombuttom, editingToolRecyclerView, 0, 200, true);
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_in, saveImage, 0, 200, true);
                ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_in, restoreImage, 0, 200, true);
                ViewAnimation.viewAnimatedChange(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out, cropLayout, photoView,
                        0, 200, 200);
                loadedToRestore = bitmap.copy(bitmap.getConfig(), true);
                loadedToChange = bitmap.copy(bitmap.getConfig(), true);
                photoView.setImageBitmap(loadedToChange);
                isCropImage = false;
            }

            @Override
            public void onFailure(@NotNull Exception e) {

            }
        });

        cropLayout.setBitmap(loadedToChange);
        isCropImage = true;
    }

    /**
     * <p>Method which makes it possible to call on the TextDialogFragment, which makes it possible to write text on the bitmap image.
     *
     * @see PhotoEditor
     * @see ToolType
     * @see TextDialogFragment
     * @see ViewAnimation
     */
    private void writeText() {
        ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_close_black_24dp);
        ViewAnimation.viewAnimatedHideOrShow(applicationContext, R.anim.tobuttom, editingToolRecyclerView, 0, 200, false);
        ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_out, saveImage, 0, 200, false);
        ViewAnimation.viewAnimatedHideOrShow(applicationContext, android.R.anim.fade_out, restoreImage, 0, 200, false);

        photoEditorView.getSource().setImageBitmap(loadedToChange);

        ViewAnimation.viewAnimatedChange(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out, photoView, photoEditorView,
                0, 200, 200);

        isText = true;

        TextDialogFragment textEditorDialogFragment = TextDialogFragment.show(getActivity());
        textEditorDialogFragment.setOnTextEditorListener(new TextDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);

                photoEditor.addText(inputText, styleBuilder);
            }
        });
    }

    /**
     * <p>Method which allows the user, the use of a brush to draw on a bitmap image, and the use of the eraser.
     *
     * @see PhotoEditor
     * @see ToolType
     * @see ViewAnimation
     */
    private void brush() {
        ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_close_black_24dp);
        ViewAnimation.viewAnimatedHideOrShow(applicationContext, R.anim.tobuttom, editingToolRecyclerView, 0, 200, false);
        ViewAnimation.imageViewAnimatedChange(applicationContext, saveImage, R.drawable.ic_brush_black_24dp);
        ViewAnimation.imageViewAnimatedChange(applicationContext, restoreImage, R.drawable.ic_eraser_black);

        photoEditorView.getSource().setImageBitmap(loadedToChange);

        ViewAnimation.viewAnimatedChange(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out, photoView, photoEditorView,
                0, 200, 200);

        isBrush = true;

        brushBottomDialogFragment.show(Objects.requireNonNull(getFragmentManager()),brushBottomDialogFragment.getTag());
        photoEditor.setBrushDrawingMode(true);
    }

    /**
     * <p>Method which allows to rotate the image to a degree passed in parameter.
     *
     * @param degree The degree of rotation
     * @see Matrix
     */
    private void rotateImage(float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedBitmapLadedToRestore = Bitmap.createBitmap(loadedToRestore, 0, 0, loadedToRestore.getWidth(), loadedToRestore.getHeight(), matrix, true);
        Bitmap rotatedBitmapLoadedToChange = Bitmap.createBitmap(loadedToChange, 0, 0, loadedToChange.getWidth(), loadedToChange.getHeight(), matrix, true);
        loadedToRestore = rotatedBitmapLadedToRestore.copy(rotatedBitmapLadedToRestore.getConfig(), true);
        loadedToChange = rotatedBitmapLoadedToChange.copy(rotatedBitmapLoadedToChange.getConfig(), true);
        photoView.setImageBitmap(loadedToChange);
    }

    /**
     * <p>Method which increases or decreases the brightness of a bitmap.
     *
     * @see ViewAnimation
     * @see Filters
     */
    private void brightness() {
        isBrightness = true;
        ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_close_black_24dp);
        ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, editingToolRecyclerView, seekBar,
                0, 200, 200);
        seekBar.setProgress(256);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progressValue = seekBar.getProgress() - 256;
//                float val = (float) progressValue / 256;
//                Bitmap returnBitmap = filter.brightnessAndSaturationHSV_RS(loadedToChange, val);
//                loadedToRestore = filter.brightnessAndSaturationHSV(loadedToChange, val, true);

                if (isBrightnessRGB)
                    loadedToRestore = filters.brightnessRGB(loadedToChange, progressValue);
                else if (!isBrightnessRGB)
                    loadedToRestore = filters.brightnessAndSaturationHSV(loadedToChange, (float) progressValue / 256, true);
                photoView.setImageBitmap(loadedToRestore);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * <p>Method which increases or decreases saturation of a bitmap.
     *
     * @see ViewAnimation
     * @see Filters
     */
    private void saturation() {
        isSaturation = true;
        ViewAnimation.imageViewAnimatedChange(applicationContext, undoImage, R.drawable.ic_close_black_24dp);
        ViewAnimation.viewAnimatedChange(applicationContext, R.anim.frombuttom, R.anim.tobuttom, editingToolRecyclerView, seekBar,
                0, 200, 200);
        seekBar.setProgress(256);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progressSaturation = seekBar.getProgress() - 256;
                float val = (float) progressSaturation / 256;
                loadedToRestore = filters.brightnessAndSaturationHSV(loadedToChange, val, false);
                photoView.setImageBitmap(loadedToRestore);
            }
        });
    }

    /**
     * <p>Method which allows to apply the changes on the bitmap after using the text tool or the brush tool
     *
     * @see PhotoEditor
     * @see TextDialogFragment
     * @see BrushBottomDialogFragment
     */
    private void saveImageAfterChangesPhotoEditor() {
        photoEditor.saveAsBitmap(new OnSaveBitmap() {
            @Override
            public void onBitmapReady(Bitmap saveBitmap) {
                loadedToRestore = saveBitmap.copy(saveBitmap.getConfig(), true);
                loadedToChange = saveBitmap.copy(saveBitmap.getConfig(), true);
                photoView.setImageBitmap(loadedToChange);
            }

            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "onFailure: Failed to save bitmap after adding text");
            }
        });
    }

    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
        TextDialogFragment textEditorDialogFragment =
                TextDialogFragment.show(Objects.requireNonNull(getActivity()), text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener(new TextDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);

                photoEditor.editText(rootView, inputText, styleBuilder);
            }
        });
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onBrushColorChanged(int colorCode) {
        photoEditor.setBrushColor(colorCode);
    }
}
