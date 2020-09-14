package com.quadstack.everroutine.days_to_remember;

import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.days_to_remember_tasks_pool.DaysToRemember_TasksPool_DataModel;

/**
 * Created by ISHRAK on 11/29/2016.
 */
public class DataModel extends DaysToRemember_TasksPool_DataModel
{
    private String eventDate;
    private String eventDetails;
    private int eventDateHash;

    public DataModel(int eventID, String eventName, int eventCategory, String eventDate, int eventDateHash, String eventDetails, int imgResource)
    {
        super(eventID, eventName, eventCategory, imgResource);

        this.eventDate = eventDate;
        this.eventDetails = eventDetails;
        this.eventDateHash = eventDateHash;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public int getEventDateHash() {
        return eventDateHash;
    }

    public void setEventDateHash(int eventDateHash) {
        this.eventDateHash = eventDateHash;
    }
}
