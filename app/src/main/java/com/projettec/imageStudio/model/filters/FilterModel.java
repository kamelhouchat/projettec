package com.projettec.imageStudio.model.filters;

public class FilterModel {

    private String filterName ;
    private FilterType filterType ;

    public FilterModel(String filterName, FilterType filterType) {
        this.filterName = filterName;
        this.filterType = filterType;
    }

    public String getFilterName() {
        return filterName;
    }

    public FilterType getFilterType() {
        return filterType;
    }

}
