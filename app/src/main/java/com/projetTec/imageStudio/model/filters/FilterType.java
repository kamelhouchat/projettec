package com.projetTec.imageStudio.model.filters;

/**
 * <p>
 * Enumeration which contains the filters which
 * are used for the initialization of a {@link FilterModel}
 * </p>
 *
 * @see FilterModel
 */

public enum FilterType {

    TO_GRAY,
    COLORIZE,
    KEEP_COLOR,
    CONTRAST_PLUS_GRAY,
    CONTRAST_PLUS_RGB,
    CONTRAST_PLUS_HSV,
    CONTRAST_FEWER_GRAY,
    EQUALIZATION_GRAY,
    EQUALIZATION_RGB,
    CONVOLUTION_MOY,
    CONVOLUTION_GAUS,
    CONTOUR,

}
