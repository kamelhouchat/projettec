package com.projetTec.imageStudio.model.imageAdaptation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapImageAdaptation {

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

    /**
     * <p>This function allows allows resizing a bitmap image</p>
     *
     * @param imageBitmap image which we want to resize
     * @param newWidth    new width
     * @param newHeight   new height
     * @return new image resized
     */
    public static Bitmap resizeBitmap(Bitmap imageBitmap, int newWidth, int newHeight) {
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    /**
     * <p>
     * Method which allows to calculate a new height and a new width to reduce the size of the image passed in parameter.
     * </p>
     *
     * @param imageBitmap The bitmap image
     * @return An integer array containing the new height and the new width (reduced).
     */
    public static int[] getReducedHeightWidth(Bitmap imageBitmap) {
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();

        while (height > 1500 && width > 1500) {
            height *= 0.9;
            width *= 0.9;
        }
        return new int[]{height, width};
    }


}
