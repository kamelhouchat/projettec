package com.projettec.imageStudio.model.tools;

/**
 * <p>
 *     The interface which will be implemented by the class where the
 *     {@link com.projettec.imageStudio.controller.adapters.EditingToolRecyclerViewAdapter} will be instantiated
 * </p>
 *
 * @see ToolModel
 * @see ToolType
 */

public interface OnItemToolSelected {

    /**
     * <p>
     *     The method in which the listener will be placed according to the {@link ToolType}
     * </p>
     * @param toolType The tool Type
     *
     * @see ToolType
     * @see ToolModel
     */
    void onToolSelected(ToolType toolType);

}
