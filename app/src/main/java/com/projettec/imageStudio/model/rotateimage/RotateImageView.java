package com.projettec.imageStudio.model.rotateimage;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class RotateImageView {

    @SuppressWarnings("UnusedAssignment")
    public static void rotateImage(float degree, Bitmap loadedToRestore, Bitmap loadedToChange) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedBitmapLadedToRestore = Bitmap.createBitmap(loadedToRestore, 0, 0, loadedToRestore.getWidth(), loadedToRestore.getHeight(), matrix, true);
        Bitmap rotatedBitmapLoadedToChange = Bitmap.createBitmap(loadedToChange, 0, 0, loadedToChange.getWidth(), loadedToChange.getHeight(), matrix, true);
        loadedToRestore = rotatedBitmapLadedToRestore.copy(rotatedBitmapLadedToRestore.getConfig(), true);
        loadedToChange = rotatedBitmapLoadedToChange.copy(rotatedBitmapLoadedToChange.getConfig(), true);
        //photo_view.setImageBitmap(loadedToChange);
    }

    /*
      TODO -- fixing auto rotating when we load image from gallery or camera
     */

}
