package com.quadstack.everroutine.tasks_pool;

import com.quadstack.everroutine.days_to_remember_tasks_pool.DaysToRemember_TasksPool_DataModel;

/**
 * Created by ISHRAK on 11/29/2016.
 */
public class DataModel extends DaysToRemember_TasksPool_DataModel
{
    private int taskPriority;
    private boolean taskAlarm;

    public DataModel(int taskID, String taskName, int taskCategory, int taskPriority, boolean taskAlarm, int imgResource)
    {
        super(taskID, taskName, taskCategory, imgResource);

        this.taskPriority = taskPriority;
        this.taskAlarm = taskAlarm;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public boolean isTaskAlarm() {
        return taskAlarm;
    }

    public void setTaskAlarm(boolean taskAlarm) {
        this.taskAlarm = taskAlarm;
    }
}
