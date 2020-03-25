package com.projettec.imageStudio.model.tools;

/**
 * <p>
 * ToolModel is a type of tool that contains :
 *     <ul>
 *         <li>Tool name</li>
 *         <li>Tool icon</li>
 *         <li>Tool type</li>
 *     </ul>
 * </p>
 *
 * @author Kamel.H
 * @see ToolType
 * @see com.projettec.imageStudio.controller.adapters.EditingToolRecyclerViewAdapter
 */

public class ToolModel {

    //The name of the tool that will be displayed on the menu
    private String ToolName;

    //The icon of the tool that will be displayed on the menu
    private int ToolIcon;

    //The type of the tool which will be essential for the management of listener and adapter
    private ToolType ToolType;

    /**
     * <p>The ToolModel constructor which takes the name, icon and type as parameters</p>
     *
     * @param ToolName The name of the tool
     * @param ToolIcon The icon of the tool
     * @param ToolType The type of the tool
     * @see ToolType
     */
    public ToolModel(String ToolName, int ToolIcon, ToolType ToolType) {
        this.ToolName = ToolName;
        this.ToolIcon = ToolIcon;
        this.ToolType = ToolType;
    }

    /**
     * return the tool type
     *
     * @return ToolTpe -- the type of the tool
     * @see ToolType
     */
    public ToolType getToolType() {
        return ToolType;
    }

    /**
     * return the tool name
     *
     * @return ToolName -- the name of the tool
     */
    public String getToolName() {
        return ToolName;
    }

    /**
     * return the tool icon
     *
     * @return ToolIcon -- the icon of the tool
     */
    public int getToolIcon() {
        return ToolIcon;
    }
}
