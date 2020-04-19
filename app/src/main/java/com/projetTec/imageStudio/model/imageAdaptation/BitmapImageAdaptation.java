package com.projetTec.imageStudio.model.imageAdaptation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import androidx.exifinterface.media.ExifInterface;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * Class which contains all the static methods which allow the adaptation and the adjustment of the problems of the images before loading.
 * </p>
 *
 * @author Kamel.H
 * @see ExifInterface
 * @see Matrix
 * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
 */
public class BitmapImageAdaptation {

    /**
     * <p>
     * Method which allows to calculate a new height and a new width to reduce the size of the image passed in parameter.
     * </p>
     *
     * @param imageBitmap The bitmap image
     * @return An integer array containing the new height and the new width (reduced).
     */
    public static Bitmap getReducedBitmap(Bitmap imageBitmap) {
        int height = imageBitmap.getHeight();
        int width = imageBitmap.getWidth();

        while (height > 1500 && width > 1500) {
            height *= 0.9;
            width *= 0.9;
        }
        return  Bitmap.createScaledBitmap(imageBitmap, width, height, true);
    }

    /**
     * <p>
     * Method which makes it possible to check if the rotation of the image is correct and to correct it if it is not the case.
     * </p>
     *
     * @param imageBitmap      The bitmap image
     * @param imageInputStream The image input stream
     * @throws IOException
     * @see com.projetTec.imageStudio.controller.fragments.Studio_fragment
     * @see ExifInterface
     * @see Matrix
     */
    public static void fixAutoRotate(Bitmap imageBitmap, InputStream imageInputStream) throws IOException {
        ExifInterface exifInterface = new ExifInterface(imageInputStream);

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.preScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.preScale(1, -1);
                break;
        }
        imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
    }

}

