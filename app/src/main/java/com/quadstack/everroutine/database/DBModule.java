package com.quadstack.everroutine.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.tasks.fragments_tab.daily_fragment.DataModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ISHRAK on 12/27/16.
 */

public class DBModule extends SQLiteOpenHelper
{
    private Context context = null;

    public DBModule(Context context)
    {
        super(context, DBMeta.database_name, null, DBMeta.newVersion);

        this.context = context;

        Log.e("DATABASE ACTIVITY: ", DBMeta.database_name + " database CREATED/OPENED");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //String UserMetaCreation = "create table " + DBMeta.UserMeta.table_name + " (" + ;
        String DateRefCreation = "CREATE TABLE " + DBMeta.DateRef.table_name + " (" + DBMeta.DateRef.id + " INTEGER PRIMARY KEY AUTOINCREMENT," + DBMeta.DateRef.date + " TEXT," + DBMeta.DateRef.date_hash + " INTEGER);";
        String DaysToRememberCreation = "CREATE TABLE " + DBMeta.DaysToRemember.table_name  + " (" + DBMeta.DaysToRemember.id + " INTEGER PRIMARY KEY AUTOINCREMENT," + DBMeta.DaysToRemember.event_name + " TEXT," + DBMeta.DaysToRemember.event_date_ref + " INTEGER," + DBMeta.DaysToRemember.event_category + " INTEGER," + DBMeta.DaysToRemember.event_details + " TEXT," + " FOREIGN KEY (" + DBMeta.DaysToRemember.event_date_ref + ") REFERENCES " + DBMeta.DateRef.table_name + "(" + DBMeta.DateRef.id + "));";
        String TasksPoolCreation = "CREATE TABLE " + DBMeta.TasksPool.table_name + " (" + DBMeta.TasksPool.id + " INTEGER PRIMARY KEY AUTOINCREMENT," + DBMeta.TasksPool.task_name + " TEXT," + DBMeta.TasksPool.task_priority + " INTEGER," + DBMeta.TasksPool.task_category + " INTEGER," + DBMeta.TasksPool.task_alarm + " INTEGER," + DBMeta.TasksPool.task_pool_visibility + " INTEGER);";
        String TasksCreation = "CREATE TABLE " + DBMeta.Tasks.table_name + " (" + DBMeta.Tasks.id + " INTEGER PRIMARY KEY AUTOINCREMENT," + DBMeta.Tasks.tasks_pool_ref + " INTEGER," + DBMeta.Tasks.recursive_day_id + " INTEGER," + DBMeta.Tasks.task_date_ref + " INTEGER," + DBMeta.Tasks.start_hour + " INTEGER," + DBMeta.Tasks.start_minute + " INTEGER," + DBMeta.Tasks.start_am_pm + " INTEGER," + DBMeta.Tasks.stop_hour + " INTEGER," + DBMeta.Tasks.stop_minute + " INTEGER," + DBMeta.Tasks.stop_am_pm + " INTEGER," + DBMeta.Tasks.start_twentyfour + " INTEGER," + DBMeta.Tasks.stop_twentyfour + " INTEGER);";

        //db.execSQL(UserMetaCreation);
        //Log.v("Database Activity", "UserMeta Created/Opened");

        db.execSQL(DateRefCreation);
        Log.e("Database Activity", "DateRef Created");

        db.execSQL(DaysToRememberCreation);
        Log.e("Database Activity", "DaysToRemember Created");

        db.execSQL(TasksPoolCreation);
        Log.e("Database Activity", "TasksPool Created");

        db.execSQL(TasksCreation);
        Log.e("Database Activity", "Tasks Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DBMeta.UserMeta.table_name);
        db.execSQL("DROP TABLE IF EXISTS " + DBMeta.DateRef.table_name);
        db.execSQL("DROP TABLE IF EXISTS " + DBMeta.DaysToRemember.table_name);
        db.execSQL("DROP TABLE IF EXISTS " + DBMeta.TasksPool.table_name);
        db.execSQL("DROP TABLE IF EXISTS " + DBMeta.Tasks.table_name);

        onCreate(db);
    }

    //Common ----------------------------------------------------------------------------------------------------------
    private int getHighestID(String tableName, String idColumnName)
    {
        int id = -1;

        SQLiteDatabase dbRead = getReadableDatabase();
        String query = "SELECT MAX(" + idColumnName + ") FROM " + tableName;
        Cursor dateRefCursor = dbRead.rawQuery(query, null);

        if(dateRefCursor.moveToFirst())
        {
            id = dateRefCursor.getInt(0);
        }

        return id;
    }

    public void removeItem(String tableName, String idColumnName, int id)
    {
        SQLiteDatabase dbWrite = getWritableDatabase();

        dbWrite.execSQL("DELETE FROM " + tableName + " WHERE " + idColumnName + "=?", new String[]{String.valueOf(id)});
    }

    public void removeMultipleItems(String tableName, String idColumnName, List eventIDList)
    {
        SQLiteDatabase dbWrite = getWritableDatabase();

        String args = TextUtils.join(", ", eventIDList);

        dbWrite.execSQL(String.format("DELETE FROM " + tableName + " WHERE " + idColumnName + " IN (%s);", args));
    }

    public void removeAllItems(String tableName)
    {
        SQLiteDatabase dbWrite = getWritableDatabase();

        dbWrite.execSQL("DELETE FROM " + tableName);
    }
    //Common ----------------------------------------------------------------------------------------------------------



    //Date Ref - ----------------------------------------------------------------------------------------------------
    public Cursor getDateRef()
    {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBMeta.DateRef.table_name;

        Cursor dateRefCursor = dbRead.rawQuery(query, null);

        return dateRefCursor;
    }

    public int addDateRef(String date)                                          //returns the dateRef id corresponding to date
    {
        if(date.equals(""))
        {
            return -1;
        }

        Set<Map.Entry<Integer, String>> dateRefEntries = Utility.dateRefCache.entrySet();

        for(Map.Entry<Integer, String> entry : dateRefEntries)
        {
            if(entry.getValue().equals(date))
            {
                Log.e("Existent: ", "True");

                return entry.getKey();
            }
        }

        try
        {
            int dateHash = Utility.calculateDateHash(date);

            SQLiteDatabase dbWrite = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(DBMeta.DateRef.date, date);
            contentValues.put(DBMeta.DateRef.date_hash, dateHash);

            dbWrite.insert(DBMeta.DateRef.table_name, null, contentValues);             //inserting into database

            int id = getHighestID(DBMeta.DateRef.table_name, DBMeta.DateRef.id);

            Utility.dateRefCache.put(id, date);                                          //inserting into the cache

            //showDateRefCache();

            Log.e("Existent: ", "False");

            return id;
        }
        catch(Exception ex)
        {
            Log.v("Exception: ", ex.getMessage());
        }

        return -1;
    }

    public void showDateRefCache()
    {
        for(Object key : Utility.dateRefCache.keySet())
        {
            Log.e("Date Ref - " + String.valueOf(key), String.valueOf(Utility.dateRefCache.get(key)));
        }
    }
    //Date Ref - ----------------------------------------------------------------------------------------------------



    //Days To Remember ----------------------------------------------------------------------------------------------
    public Cursor getDaysToRemember()
    {
        SQLiteDatabase dbRead = this.getReadableDatabase();

        String query = "SELECT " + DBMeta.DaysToRemember.table_name + "." + DBMeta.DaysToRemember.id + ","
                            + DBMeta.DaysToRemember.table_name + "." + DBMeta.DaysToRemember.event_date_ref + ","
                            + DBMeta.DaysToRemember.table_name + "." + DBMeta.DaysToRemember.event_category + ","
                            + DBMeta.DaysToRemember.table_name + "." + DBMeta.DaysToRemember.event_name + ","
                            + DBMeta.DaysToRemember.table_name + "." + DBMeta.DaysToRemember.event_details + ","
                            + DBMeta.DateRef.table_name + "." + DBMeta.DateRef.date_hash
                            + " FROM " + DBMeta.DaysToRemember.table_name + "," + DBMeta.DateRef.table_name
                            + " WHERE " + DBMeta.DaysToRemember.event_date_ref + "=" + DBMeta.DateRef.id
                            + " ORDER BY " + DBMeta.DateRef.date_hash + " ASC";

        Cursor daysToRememberCursor = dbRead.rawQuery(query, null);

        return daysToRememberCursor;
    }

    public int addDaysToRemember(String eventName, String date, int eventCategory, String eventDetails)
    {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBMeta.DaysToRemember.event_name, eventName);
        contentValues.put(DBMeta.DaysToRemember.event_date_ref, new DBModule(context).addDateRef(date));
        contentValues.put(DBMeta.DaysToRemember.event_category, eventCategory);
        contentValues.put(DBMeta.DaysToRemember.event_details, eventDetails);

        dbWrite.insert(DBMeta.DaysToRemember.table_name, null, contentValues);

        int id = getHighestID(DBMeta.DaysToRemember.table_name, DBMeta.DaysToRemember.id);

        return id;
    }

    public int editDaysToRemember(int id, String eventName, String date, int eventCategory, String eventDetails)
    {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBMeta.DaysToRemember.event_name, eventName);
        contentValues.put(DBMeta.DaysToRemember.event_date_ref, new DBModule(context).addDateRef(date));
        contentValues.put(DBMeta.DaysToRemember.event_category, eventCategory);
        contentValues.put(DBMeta.DaysToRemember.event_details, eventDetails);

        String whereClause = DBMeta.DaysToRemember.id + "=?";

        dbWrite.update(DBMeta.DaysToRemember.table_name, contentValues, whereClause, new String[]{Integer.toString(id)});

        return id;
    }
    //Days To Remember ----------------------------------------------------------------------------------------------



    //Tasks Pool ----------------------------------------------------------------------------------------------------
    public Cursor getTasksPool()
    {
        SQLiteDatabase dbRead = this.getReadableDatabase();

        String query = "SELECT " + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.id + ","
                        + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_name + ","
                        + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_category + ","
                        + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_priority + ","
                        + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_alarm
                        + " FROM " + DBMeta.TasksPool.table_name + " WHERE "
                        + DBMeta.TasksPool.task_pool_visibility + " = ?";

        Cursor tasksPoolCursor = dbRead.rawQuery(query, new String[]{"1"});

        return tasksPoolCursor;
    }

    //to populate the "import from tasks pool list" in the task adding activity
    public Cursor getTasksPool(int taskCategory)
    {
        SQLiteDatabase dbRead = this.getReadableDatabase();

        String query = "SELECT " + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.id + ","
                + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_name + ","
                + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_category + ","
                + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_priority + ","
                + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_alarm
                + " FROM " + DBMeta.TasksPool.table_name + " WHERE ("
                + DBMeta.TasksPool.task_pool_visibility + "=? AND " + DBMeta.TasksPool.task_category + "=?)";

        Cursor tasksPoolCursor = dbRead.rawQuery(query, new String[]{"1", Integer.toString(taskCategory)});

        return tasksPoolCursor;
    }

    public int addTasksPool(String taskName, int taskCategory, int taskPriority, int taskAlarm, int taskPoolVisibility)
    {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBMeta.TasksPool.task_name, taskName);
        contentValues.put(DBMeta.TasksPool.task_category, taskCategory);
        contentValues.put(DBMeta.TasksPool.task_priority, taskPriority);
        contentValues.put(DBMeta.TasksPool.task_alarm, taskAlarm);
        contentValues.put(DBMeta.TasksPool.task_pool_visibility, taskPoolVisibility);

        dbWrite.insert(DBMeta.TasksPool.table_name, null, contentValues);

        int id = getHighestID(DBMeta.TasksPool.table_name, DBMeta.TasksPool.id);

        return id;
    }

    public int editTasksPool(int id, String taskName, int taskCategory, int taskPriority, int taskAlarm, int taskPoolVisibility)
    {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBMeta.TasksPool.task_name, taskName);
        contentValues.put(DBMeta.TasksPool.task_category, taskCategory);
        contentValues.put(DBMeta.TasksPool.task_priority, taskPriority);
        contentValues.put(DBMeta.TasksPool.task_alarm, taskAlarm);
        contentValues.put(DBMeta.TasksPool.task_pool_visibility, taskPoolVisibility);

        String whereClause = DBMeta.TasksPool.id + "=?";

        dbWrite.update(DBMeta.TasksPool.table_name, contentValues, whereClause, new String[]{Integer.toString(id)});

        return id;
    }
    //Tasks Pool ----------------------------------------------------------------------------------------------------


    //Tasks -------------------------------------------------------------------------------------------------------------
    public int getDayIDFromDate(String date)                                        //Sunday - 0, Saturday - 6
    {
        int dayOfWeek = -1;

        try
        {
            Date d = Utility.dateFormat.parse(date);

            Calendar c = Calendar.getInstance();
            c.setTime(d);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        }
        catch(Exception e)
        {
            Log.v("Exception", e.getMessage());
        }

        return dayOfWeek-1;
    }

    public int getTwentyFourTime(int hour, int minute, int amPm)
    {
        int twentyFour = 0;

        if(hour == 12)
        {
            hour = 0;
        }

        if(amPm == Utility.am)
        {
            twentyFour = (hour*100) + minute;
        }
        else if(amPm == Utility.pm)
        {
            twentyFour = ((hour+12)*100) + minute;
        }

        return twentyFour;
    }

    public int detectConflict(int recursiveDayID, String date, int startTwentyFour, int stopTwentyFour)
    {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        String queryCheck, queryCheckDay;
        Cursor countCursor, countCursorDay;
        int count = 0;
        if(!date.equals(""))
        {
            queryCheck = "SELECT * FROM " + DBMeta.Tasks.table_name + " WHERE ((?>=" + DBMeta.Tasks.start_twentyfour + " AND ?<=" + DBMeta.Tasks.stop_twentyfour + ") OR (?>=" + DBMeta.Tasks.start_twentyfour + " AND ?<=" + DBMeta.Tasks.stop_twentyfour + ") OR (?<=" + DBMeta.Tasks.start_twentyfour + " AND ?>=" + DBMeta.Tasks.stop_twentyfour + ")) AND (" + DBMeta.Tasks.task_date_ref + "=?)";
            countCursor = dbWrite.rawQuery(queryCheck, new String[]{Integer.toString(startTwentyFour), Integer.toString(startTwentyFour), Integer.toString(stopTwentyFour), Integer.toString(stopTwentyFour), Integer.toString(startTwentyFour), Integer.toString(stopTwentyFour), Integer.toString(addDateRef(date))});
            queryCheckDay = "SELECT * FROM " + DBMeta.Tasks.table_name + " WHERE ((?>=" + DBMeta.Tasks.start_twentyfour + " AND ?<=" + DBMeta.Tasks.stop_twentyfour + ") OR (?>=" + DBMeta.Tasks.start_twentyfour + " AND ?<=" + DBMeta.Tasks.stop_twentyfour + ") OR (?<=" + DBMeta.Tasks.start_twentyfour + " AND ?>=" + DBMeta.Tasks.stop_twentyfour + ")) AND (" + DBMeta.Tasks.recursive_day_id + "=?)";
            countCursorDay = dbWrite.rawQuery(queryCheck, new String[]{Integer.toString(startTwentyFour), Integer.toString(startTwentyFour), Integer.toString(stopTwentyFour), Integer.toString(stopTwentyFour), Integer.toString(startTwentyFour), Integer.toString(stopTwentyFour), Integer.toString(recursiveDayID)});

            count = countCursor.getCount() + countCursorDay.getCount();
        }
        else
        {
            queryCheck = "SELECT * FROM " + DBMeta.Tasks.table_name + " WHERE ((?>=" + DBMeta.Tasks.start_twentyfour + " AND ?<=" + DBMeta.Tasks.stop_twentyfour + ") OR (?>=" + DBMeta.Tasks.start_twentyfour + " AND ?<=" + DBMeta.Tasks.stop_twentyfour + ") OR (?<=" + DBMeta.Tasks.start_twentyfour + " AND ?>=" + DBMeta.Tasks.stop_twentyfour + ")) AND (" + DBMeta.Tasks.recursive_day_id + "=?)";
            countCursor = dbWrite.rawQuery(queryCheck, new String[]{Integer.toString(startTwentyFour), Integer.toString(startTwentyFour), Integer.toString(stopTwentyFour), Integer.toString(stopTwentyFour), Integer.toString(startTwentyFour), Integer.toString(stopTwentyFour), Integer.toString(recursiveDayID)});

            count = countCursor.getCount();
        }

        if(count != 0)
        {
            countCursor.moveToFirst();
            return countCursor.getInt(countCursor.getColumnIndex(DBMeta.Tasks.id));
        }
        else
        {
            return 0;
        }
    }

    public Cursor getTasks(int taskCategory, String date)
    {
        int dayOfWeek = getDayIDFromDate(date);

        SQLiteDatabase dbRead = this.getReadableDatabase();

        String query = "SELECT " + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.id + ","
                        + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_name + ","
                        + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_category + ","
                        + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_priority + ","
                        + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_alarm + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.id + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.tasks_pool_ref + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.recursive_day_id + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.task_date_ref + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.start_hour + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.start_minute + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.start_am_pm + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.stop_hour + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.stop_minute + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.stop_am_pm + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.start_twentyfour + ","
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.stop_twentyfour + " FROM "
                        + DBMeta.TasksPool.table_name + "," + DBMeta.Tasks.table_name + " WHERE ("
                        + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.id + "="
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.tasks_pool_ref + ") AND ("
                        + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_category + "=?) AND (("
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.task_date_ref + "=?) OR (("
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.task_date_ref + "=-1) AND ("
                        + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.recursive_day_id + "=?)))"
                        + " ORDER BY " + DBMeta.Tasks.start_twentyfour + " ASC";

        Cursor cursor = dbRead.rawQuery(query, new String[]{Integer.toString(taskCategory), Integer.toString(addDateRef(date)), Integer.toString(dayOfWeek)});

        return cursor;
    }

    public Cursor getAllTasks(String date)
    {
        int dayOfWeek = getDayIDFromDate(date);

        SQLiteDatabase dbRead = this.getReadableDatabase();

        String query = "SELECT " + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.id + ","
                + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_name + ","
                + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_category + ","
                + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_priority + ","
                + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_alarm + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.id + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.tasks_pool_ref + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.recursive_day_id + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.task_date_ref + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.start_hour + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.start_minute + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.start_am_pm + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.stop_hour + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.stop_minute + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.stop_am_pm + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.start_twentyfour + ","
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.stop_twentyfour + " FROM "
                + DBMeta.TasksPool.table_name + "," + DBMeta.Tasks.table_name + " WHERE ("
                + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.id + "="
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.tasks_pool_ref + ") AND (("
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.task_date_ref + "=?) OR (("
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.task_date_ref + "=-1) AND ("
                + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.recursive_day_id + "=?)))"
                + " ORDER BY " + DBMeta.Tasks.start_twentyfour + " ASC";

        Cursor cursor = dbRead.rawQuery(query, new String[]{Integer.toString(addDateRef(date)), Integer.toString(dayOfWeek)});

        return cursor;
    }

    //check if tasks pool data have been altered
    public int addTask(boolean addAnyway, int tasksPoolRef, int recursiveDayID, String date, int startHour, int startMinute, int startAmPm, int stopHour, int stopMinute, int stopAmPm)
    {
        int startTwentyFour = getTwentyFourTime(startHour, startMinute, startAmPm);
        int stopTwentyFour = getTwentyFourTime(stopHour, stopMinute, stopAmPm);

        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if(!addAnyway)
        {
            if(detectConflict(recursiveDayID, date, startTwentyFour, stopTwentyFour) > 0)
            {
                return -1;
            }
        }

        contentValues.put(DBMeta.Tasks.tasks_pool_ref, tasksPoolRef);
        contentValues.put(DBMeta.Tasks.recursive_day_id, recursiveDayID);
        contentValues.put(DBMeta.Tasks.task_date_ref, addDateRef(date));
        contentValues.put(DBMeta.Tasks.start_hour, startHour);
        contentValues.put(DBMeta.Tasks.start_minute, startMinute);
        contentValues.put(DBMeta.Tasks.start_am_pm, startAmPm);
        contentValues.put(DBMeta.Tasks.stop_hour, stopHour);
        contentValues.put(DBMeta.Tasks.stop_minute, stopMinute);
        contentValues.put(DBMeta.Tasks.stop_am_pm, stopAmPm);
        contentValues.put(DBMeta.Tasks.start_twentyfour, startTwentyFour);
        contentValues.put(DBMeta.Tasks.stop_twentyfour, stopTwentyFour);

        dbWrite.insert(DBMeta.Tasks.table_name, null, contentValues);

        int id = getHighestID(DBMeta.Tasks.table_name, DBMeta.Tasks.id);

        return id;
    }

    //check if tasks pool data have been altered
    public int editTask(boolean addAnyway, int id, int tasksPoolRef, int recursiveDayID, String date, int startHour, int startMinute, int startAmPm, int stopHour, int stopMinute, int stopAmPm)
    {
        int startTwentyFour = getTwentyFourTime(startHour, startMinute, startAmPm);
        int stopTwentyFour = getTwentyFourTime(stopHour, stopMinute, stopAmPm);

        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int conflictDeterminant = detectConflict(recursiveDayID, date, startTwentyFour, stopTwentyFour);

        if(!addAnyway)
        {
            if(conflictDeterminant != id && conflictDeterminant > 0)
            {
                return -1;
            }
        }

        contentValues.put(DBMeta.Tasks.tasks_pool_ref, tasksPoolRef);
        contentValues.put(DBMeta.Tasks.recursive_day_id, recursiveDayID);
        contentValues.put(DBMeta.Tasks.task_date_ref, addDateRef(date));
        contentValues.put(DBMeta.Tasks.start_hour, startHour);
        contentValues.put(DBMeta.Tasks.start_minute, startMinute);
        contentValues.put(DBMeta.Tasks.start_am_pm, startAmPm);
        contentValues.put(DBMeta.Tasks.stop_hour, stopHour);
        contentValues.put(DBMeta.Tasks.stop_minute, stopMinute);
        contentValues.put(DBMeta.Tasks.stop_am_pm, stopAmPm);
        contentValues.put(DBMeta.Tasks.start_twentyfour, startTwentyFour);
        contentValues.put(DBMeta.Tasks.stop_twentyfour, stopTwentyFour);

        String whereClause = DBMeta.Tasks.id + "=?";

        dbWrite.update(DBMeta.Tasks.table_name, contentValues, whereClause, new String[]{Integer.toString(id)});

        return id;
    }

    public void removeMultipleTasksOfSpecificCategory(String tableName, int taskCategory, String date, List taskDeletionList)
    {
        SQLiteDatabase dbWrite = getWritableDatabase();

        String args = TextUtils.join(", ", taskDeletionList);

        //dbWrite.execSQL(String.format("DELETE FROM " + tableName + " WHERE " + DBMeta.Tasks.task_date_ref + "=" + Integer.toString(addDateRef(date)) + " AND " + DBMeta.Tasks.+ DBMeta.Tasks.id + " IN (%s);", args));
    }

    public void removeAllTasksOfSpecificCategory(String tableName, int taskCategory, String date)
    {
        SQLiteDatabase dbWrite = getWritableDatabase();

        dbWrite.execSQL(String.format("DELETE FROM " + tableName + " WHERE " + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.id + " IN (SELECT " + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.id + " FROM " + DBMeta.Tasks.table_name + "," + DBMeta.TasksPool.table_name + " WHERE " + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.tasks_pool_ref + "=" + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.id + " AND " + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_category + "=(%s) AND " + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.task_date_ref + "=(%s))", String.valueOf(taskCategory), String.valueOf(addDateRef(date))));
    }
    //Tasks -------------------------------------------------------------------------------------------------------------

    //Search ----------------------------------------------------------------------------------------------------
    public Cursor searchDB(String searchText,String tableName)
    {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        Cursor cursor;
        String sql;

        if(tableName.equals("DaysToRemember")){
            sql = "SELECT " + DBMeta.DaysToRemember.table_name + "." + DBMeta.DaysToRemember.id + ","
                    + DBMeta.DaysToRemember.table_name + "." + DBMeta.DaysToRemember.event_date_ref + ","
                    + DBMeta.DaysToRemember.table_name + "." + DBMeta.DaysToRemember.event_category + ","
                    + DBMeta.DaysToRemember.table_name + "." + DBMeta.DaysToRemember.event_name + ","
                    + DBMeta.DaysToRemember.table_name + "." + DBMeta.DaysToRemember.event_details + ","
                    + DBMeta.DateRef.table_name + "." + DBMeta.DateRef.date_hash
                    + " FROM " + DBMeta.DaysToRemember.table_name + "," + DBMeta.DateRef.table_name
                    + " WHERE " + DBMeta.DaysToRemember.event_date_ref + "=" + DBMeta.DateRef.id
                    + " AND " + DBMeta.DaysToRemember.event_name + " LIKE '" +searchText + "%'"
                    + " ORDER BY " + DBMeta.DateRef.date_hash + " ASC";
            //sql = "SELECT * FROM "+ DBMeta.DaysToRemember.table_name + " WHERE " +DBMeta.DaysToRemember.event_name + " LIKE  '"+searchText+"%'";
        }
        else {
            sql = "SELECT * FROM "+ DBMeta.Tasks.table_name + "," + DBMeta.TasksPool.table_name + " WHERE " + DBMeta.Tasks.table_name + "." + DBMeta.Tasks.tasks_pool_ref + "=" + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.id + " AND " + DBMeta.TasksPool.table_name + "." + DBMeta.TasksPool.task_name + " LIKE  '"+searchText+"%'";
        }

        cursor=dbRead.rawQuery(sql,null);
        return  cursor;
    }
    //Search ----------------------------------------------------------------------------------------------------

}
