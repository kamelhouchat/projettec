package com.projetTec.imageStudio.model.editingImage.additionalFilters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.projetTec.imageStudio.R;

/**
 * <p>
 * This function allows to detect a face on an image and to add flowers to the hair.
 * </p>
 *
 * @author Kamel.H
 * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
 * @see com.projetTec.imageStudio.model.tools.ToolType
 * @see com.projetTec.imageStudio.model.tools.ToolModel
 */
public class FaceDetection {

    private Canvas canvas;
    private Bitmap flowerLine;

    private final Paint rectPaint = new Paint();

    private final Context context;

    public FaceDetection(Context context) {
        this.context = context;
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawEyePatchBitmap(int landmarkType, float cx, float cy) {

//        if (landmarkType == Landmark.LEFT_EYE) {
//            // TODO: Optimize so that this calculation is not done for every face
//            int scaledWidth = eyePatchBitmap.getScaledWidth(canvas);
//            int scaledHeight = eyePatchBitmap.getScaledHeight(canvas);
//            canvas.drawBitmap(eyePatchBitmap, cx - (scaledWidth / 2)+20, cy - (scaledHeight / 2), null);
//        }

        if (landmarkType == Landmark.NOSE_BASE) {
            int scaledWidth = flowerLine.getScaledWidth(canvas);
            int scaledHeight = flowerLine.getScaledHeight(canvas);
            canvas.drawBitmap(flowerLine, cx - (scaledWidth / 2), cy - (scaledHeight * 2), null);
        }

    }

    private void detectLandmarks(Face face) {
        Log.i("FACE DETECTOR", "detectLandmarks: " + face.getLandmarks().size());
        for (Landmark landmark : face.getLandmarks()) {

            int cx = (int) (landmark.getPosition().x);
            int cy = (int) (landmark.getPosition().y);

            drawEyePatchBitmap(landmark.getType(), cx, cy);
        }
    }

    public void detectFace(Bitmap imageBitmap, PhotoView photoView) {

        flowerLine = BitmapFactory.decodeResource(context.getResources(), R.drawable.flower);

        rectPaint.setStrokeWidth(5);
        rectPaint.setColor(Color.WHITE);
        rectPaint.setStyle(Paint.Style.STROKE);

        final Bitmap tempBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.RGB_565);
        canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(imageBitmap, 0, 0, null);

        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        if (!faceDetector.isOperational()) {
            Toast.makeText(context, "Face Detector could not be set up on your device", Toast.LENGTH_SHORT).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);

        for (int i = 0; i < sparseArray.size(); i++) {
            Face face = sparseArray.valueAt(i);

            detectLandmarks(face);
        }

        photoView.setImageDrawable(new BitmapDrawable(context.getResources(), tempBitmap));
    }

}
