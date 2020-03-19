package com.projettec.imageStudio.model.other;

public class ToolModel {

    private String ToolName;
    private int ToolIcon;
    private ToolType ToolType;

    public ToolModel(String ToolName, int ToolIcon, ToolType ToolType) {
        this.ToolName = ToolName;
        this.ToolIcon = ToolIcon;
        this.ToolType = ToolType;
    }

    public ToolType getToolType() {
        return ToolType;
    }

    public String getToolName() {
        return ToolName;
    }

    public int getToolIcon() {
        return ToolIcon;
    }
}
