package com.quadstack.everroutine.database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by ISHRAK on 12/27/16.
 */

public class DBMeta
{
    public static String database_name = "EverRoutine";
    public static int newVersion = 16;

    //----------------------------------------------------------------------------------------------------------------------------
    public static class UserMeta
    {
        public static String table_name = "UserMeta";

        public static String user_name = "UserMetaUserName";
        public static String notification_status = "UserMetaNotificationStatus";        //boolean
        public static String alarm_tone = "";
        public static String about = "";
    }
    //----------------------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------------------------------
    public static class DateRef
    {
        public static String table_name = "DateRef";

        public static String id = "DateRefID";                                      //integer
        public static String date = "DateRefDate";                                  //text
        public static String date_hash = "DateRefDateHash";                         //integer
    }
    //----------------------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------------------------------
    public static class DaysToRemember
    {
        public static String table_name = "DaysToRemember";

        public static String id = "DaysToRememberID";                               //integer
        public static String event_name = "DaysToRememberEventName";                //text
        public static String event_date_ref = "DaysToRememberEventDate";            //integer //foreign key
        public static String event_category = "DaysToRememberEventCategory";        //integer
        public static String event_details = "DaysToRememberEventDetails";          //text
    }
    //----------------------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------------------------------
    public static class TasksPool
    {
        public static String table_name = "TasksPool";

        public static String id = "TaskPoolID";
        public static String task_name = "TaskPoolName";
        public static String task_priority = "TaskPriority";
        public static String task_category = "TaskCategory";
        public static String task_alarm = "TaskAlarm";
        public static String task_pool_visibility = "TaskPoolVisibility";
    }
    //----------------------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------------------------------
    public static class Tasks
    {
        public static String table_name = "Tasks";

        public static String id = "TasksID";
        public static String tasks_pool_ref = "TasksPoolRef";                                        //foreign key
        public static String recursive_day_id = "TaskRecursiveDayID";
        public static String task_date_ref = "TaskNonRecursiveDate";
        public static String start_hour = "TaskStartHour";
        public static String start_minute = "TaskStartMinute";
        public static String start_am_pm = "TasksStartAMPM";
        public static String stop_hour = "TaskStopHour";
        public static String stop_minute = "TaskStopMinute";
        public static String stop_am_pm = "TasksStopAMPM";
        public static String start_twentyfour = "TasksStartTwentyFour";
        public static String stop_twentyfour = "TasksStopTwentyFour";
    }
    //----------------------------------------------------------------------------------------------------------------------------
}