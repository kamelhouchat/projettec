package com.projettec.imageStudio.model.filters;

/**
 * <p>
 * The interface which will be implemented by the class where the
 * {@link com.projettec.imageStudio.controller.adapters.FilterRecyclerViewAdapter} will be instantiated
 * </p>
 *
 * @see FilterModel
 * @see FilterType
 */

public interface OnItemFilterSelected {

    /**
     * <p>
     * The method in which the listener will be placed according to the {@link FilterType}
     * </p>
     *
     * @param filterType The filter Type
     * @see FilterType
     * @see FilterModel
     */
    void onFilterSelected(FilterType filterType);

}
