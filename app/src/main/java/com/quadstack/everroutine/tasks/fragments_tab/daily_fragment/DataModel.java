package com.quadstack.everroutine.tasks.fragments_tab.daily_fragment;

import com.quadstack.everroutine.Utility;

/**
 * Created by ISHRAK on 1/14/17.
 */

public class DataModel
{
    String taskName, date, time;
    int recursiveDayID, dateRef;
    int startHour, startMinute, startAmPm, stopHour, stopMinute, stopAmPm, startTwentyFour, stopTwentyFour;
    int tickImgResource, taskCategory, taskPriority, taskAlarm;
    int tasksPoolRef;
    int taskId;

    public DataModel(int taskId, String taskName, int recursiveDayID, int dateRef, int startHour, int startMinute, int startAmPm, int stopHour, int stopMinute, int stopAmPm, int startTwentyFour, int stopTwentyFour, int tickImgResource, int taskCategory, int taskPriority, int taskAlarm, int tasksPoolRef)
    {
        this.taskId = taskId;
        this.taskName = taskName;
        this.recursiveDayID = recursiveDayID;
        this.dateRef = dateRef;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.startAmPm = startAmPm;
        this.stopHour = stopHour;
        this.stopMinute = stopMinute;
        this.stopAmPm = stopAmPm;
        this.startTwentyFour = startTwentyFour;
        this.stopTwentyFour = stopTwentyFour;
        this.tickImgResource = tickImgResource;
        this.taskCategory = taskCategory;
        this.taskPriority = taskPriority;
        this.taskAlarm = taskAlarm;
        this.tasksPoolRef = tasksPoolRef;

        date = (String)Utility.dateRefCache.get(dateRef);
        String startTextHour = (startHour<10?"0"+Integer.toString(startHour):Integer.toString(startHour));
        String startTextMinute = (startMinute<10?"0"+Integer.toString(startMinute):Integer.toString(startMinute));
        String stopTextHour = (stopHour<10?"0"+Integer.toString(stopHour):Integer.toString(stopHour));
        String stopTextMinute = (stopMinute<10?"0"+Integer.toString(stopMinute):Integer.toString(stopMinute));
        time = (startTextHour + ":" + startTextMinute + " " + (startAmPm==Utility.am?"AM":"PM") + " - " + stopTextHour + ":" + stopTextMinute + " " + (stopAmPm==Utility.am?"AM":"PM"));
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public int getTaskPriority()
    {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority)
    {
        this.taskPriority = taskPriority;
    }

    public int getTickImgResource()
    {
        return tickImgResource;
    }

    public void setTickImgResource(int tickImgResource)
    {
        this.tickImgResource = tickImgResource;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRecursiveDayID() {
        return recursiveDayID;
    }

    public void setRecursiveDayID(int recursiveDayID) {
        this.recursiveDayID = recursiveDayID;
    }

    public int getDateRef() {
        return dateRef;
    }

    public void setDateRef(int dateRef) {
        this.dateRef = dateRef;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getStartAmPm() {
        return startAmPm;
    }

    public void setStartAmPm(int startAmPm) {
        this.startAmPm = startAmPm;
    }

    public int getStopHour() {
        return stopHour;
    }

    public void setStopHour(int stopHour) {
        this.stopHour = stopHour;
    }

    public int getStopMinute() {
        return stopMinute;
    }

    public void setStopMinute(int stopMinute) {
        this.stopMinute = stopMinute;
    }

    public int getStopAmPm() {
        return stopAmPm;
    }

    public void setStopAmPm(int stopAmPm) {
        this.stopAmPm = stopAmPm;
    }

    public int getStartTwentyFour() {
        return startTwentyFour;
    }

    public void setStartTwentyFour(int startTwentyFour) {
        this.startTwentyFour = startTwentyFour;
    }

    public int getStopTwentyFour() {
        return stopTwentyFour;
    }

    public void setStopTwentyFour(int stopTwentyFour) {
        this.stopTwentyFour = stopTwentyFour;
    }

    public int getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(int taskCategory) {
        this.taskCategory = taskCategory;
    }

    public int getTaskAlarm() {
        return taskAlarm;
    }

    public void setTaskAlarm(int taskAlarm) {
        this.taskAlarm = taskAlarm;
    }

    public int getTasksPoolRef()
    {
        return tasksPoolRef;
    }

    public void setTasksPoolRef(int tasksPoolRef)
    {
        this.tasksPoolRef = tasksPoolRef;
    }
}
