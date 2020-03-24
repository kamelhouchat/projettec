package com.projettec.imageStudio.model.rotateimage;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class RotateImageView {

    private static void rotateImage(float degree, Bitmap loadedToChange) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedBitmap = Bitmap.createBitmap(loadedToChange, 0, 0, loadedToChange.getWidth(), loadedToChange.getHeight(), matrix, true);
        loadedToChange = rotatedBitmap.copy(rotatedBitmap.getConfig(), true);
    }

    /**
     * TODO -- fixing auto rotating when we load image from gellery or camera
     */

}
