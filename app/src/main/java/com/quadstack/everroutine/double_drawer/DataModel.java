package com.quadstack.everroutine.double_drawer;

/**
 * Created by ISHRAK on 11/29/2016.
 */
public class DataModel
{
    private String itemName;
    private int shelfImageResource, buttonImageResource, buttonSelectedImageResource;

    public DataModel(String itemName, int shelfImageResource, int buttonImageResource, int buttonSelectedImageResource) {

        this.itemName = itemName;
        this.shelfImageResource = shelfImageResource;
        this.buttonImageResource = buttonImageResource;
        this.buttonSelectedImageResource = buttonSelectedImageResource;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getShelfImageResource() {
        return shelfImageResource;
    }

    public void setShelfImageResource(int shelfImageResource) {
        this.shelfImageResource = shelfImageResource;
    }

    public int getButtonImageResource() {
        return buttonImageResource;
    }

    public void setButtonImageResource(int buttonImageResource) {
        this.buttonImageResource = buttonImageResource;
    }

    public int getButtonSelectedImageResource() {
        return buttonSelectedImageResource;
    }

    public void setButtonSelectedImageResource(int buttonSelectedImageResource) {
        this.buttonSelectedImageResource = buttonSelectedImageResource;
    }
}
