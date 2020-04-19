package com.projetTec.imageStudio.model.editingImage.additionalFilters;

/**
 * <p>Class that contains the auxiliary static methods used by the {@link AdditionalFilters} class.
 *
 * @author Kamel.H
 * @see AdditionalFilters
 */

class AuxiliaryFunction {

    /**
     * <p>Method which calculates the histogram of the value v of a pixel after conversion.
     *
     * @param pixels The pixel array of the image.
     * @return The histogram in the form of an integer array.
     * @see AdditionalFilters
     */
    public static int[] histogramYuv(int[] pixels) {
        int[] histogram = new int[256];
        for (int pixel : pixels) {
            float[] yuv = Conversion.rgbToYuv(pixel);
            histogram[(int) yuv[0]] += 1;
        }
        return histogram;
    }

}
