package com.projettec.imageStudio.model.filters;

/**
 * <p>
 * FilterModel is a type of filter that contains :
 *     <ul>
 *         <li>Filter name</li>
 *         <li>Filter type</li>
 *     </ul>
 * </p>
 *
 * @author Kamel.H
 * @see FilterType
 * @see com.projettec.imageStudio.controller.adapters.FilterRecyclerViewAdapter
 */

public class FilterModel {

    //The name of the filter that will be displayed on the menu
    private String filterName;

    //The type of the filter which will be essential for the management of listener and adapter
    private FilterType filterType;

    /**
     * <p>The FilterModel constructor which takes the name and the filter type as parameters</p>
     *
     * @param filterName The name of the filter
     * @param filterType The type of the filter
     * @see FilterType
     */
    public FilterModel(String filterName, FilterType filterType) {
        this.filterName = filterName;
        this.filterType = filterType;
    }

    /**
     * return the filter name
     *
     * @return FilterName -- the name of the filter
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * return the filter type
     *
     * @return FilterTpe -- the type of the filter
     * @see FilterType
     */
    public FilterType getFilterType() {
        return filterType;
    }

}
