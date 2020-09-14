package com.quadstack.everroutine.days_to_remember_tasks_pool;

/**
 * Created by ISHRAK on 11/29/2016.
 */
public abstract class DaysToRemember_TasksPool_DataModel
{
    protected int itemID;
    protected String itemName;
    protected int itemCategory;
    protected int imgResource;
    protected int tickImgResource;

    public DaysToRemember_TasksPool_DataModel(int itemID, String itemName, int itemCategory, int imgResource)
    {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemCategory = itemCategory;

        this.imgResource = imgResource;
        this.tickImgResource = 0;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int eventID) {
        this.itemID = eventID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(int itemCategory) {
        this.itemCategory = itemCategory;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public int getTickImgResource()
    {
        return tickImgResource;
    }

    public void setTickImgResource(int tickImgResource)
    {
        this.tickImgResource = tickImgResource;
    }
}
