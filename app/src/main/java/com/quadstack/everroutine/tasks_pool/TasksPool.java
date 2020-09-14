package com.quadstack.everroutine.tasks_pool;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.days_to_remember.*;
import com.quadstack.everroutine.days_to_remember_tasks_pool.DaysToRemember_TasksPool_Activity;

public class TasksPool extends DaysToRemember_TasksPool_Activity
{

    private View tasksPoolView;
    private static Adapter tasksPoolAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tasksPoolAdapter = new Adapter(TasksPool.this, R.layout.tasks_pool_row);

        tasksPoolView = super.setContextAndListAdapter(TasksPool.this, "TasksPool", tasksPoolAdapter);

        addData();
        //addDummyData();

        setSpecificDynamicLayout();
        addSpecificListeners(); //Common listeners (deleteButton, deleteAllButton) are added from the super class
    }

    private void setSpecificDynamicLayout()
    {
        title.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_title, Utility.titleWidth, Utility.titleHeight));
    }

    private void addSpecificListeners()
    {
        addButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    addButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_add_selected, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    addButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_add, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

                    Intent addEditIntent = new Intent(TasksPool.this, TasksPoolAddEdit.class);

                    addEditIntent.putExtra("activity_type", "add");

                    startActivity(addEditIntent);

                    return true;
                }

                return false;
            }
        });

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(longPressSelectMode == true)
                {
                    showTick(position);
                }
                else
                {
                    DataModel task = (DataModel)tasksPoolAdapter.getItem(position);

                    Intent addEditIntent = new Intent(TasksPool.this, TasksPoolAddEdit.class);

                    addEditIntent.putExtra("activity_type", "edit");
                    addEditIntent.putExtra("task_id", task.getItemID());
                    addEditIntent.putExtra("task_name", task.getItemName());
                    addEditIntent.putExtra("task_category", task.getItemCategory());
                    addEditIntent.putExtra("task_priority", task.getTaskPriority());
                    addEditIntent.putExtra("task_alarm", task.isTaskAlarm());

                    startActivity(addEditIntent);
                }
            }
        });

        listView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                longPressSelectMode = true;

                showTick(position);

                vibrator.vibrate(500);

                return true;
            }
        });
    }

    //-----------------------------------------------------------------------------------------------------------------------

    private void addData()
    {
        int id, taskCategory, taskPriority, taskAlarm;
        String taskName;

        Cursor cursor = new DBModule(getApplicationContext()).getTasksPool();

        if(cursor.moveToFirst())
        {
            do
            {
                id = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.id));
                taskCategory = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.task_category));
                taskPriority = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.task_priority));
                taskAlarm = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.task_alarm));
                taskName = cursor.getString(cursor.getColumnIndex(DBMeta.TasksPool.task_name));

                tasksPoolAdapter.add(new DataModel(id, taskName, taskCategory, taskPriority, (taskAlarm==1?true:false), getImgResource(taskCategory)));
            }
            while(cursor.moveToNext());
        }
    }

    private void addDummyData()
    {
        tasksPoolAdapter.add(new DataModel(1, "Task 1", 1, 1, false, R.drawable.official));
        tasksPoolAdapter.add(new DataModel(2, "Task 2", 2, 2, true, R.drawable.academic));
        tasksPoolAdapter.add(new DataModel(3, "Task 3", 0, 0, false, R.drawable.miscellaneous));
        tasksPoolAdapter.add(new DataModel(4, "Task 4", 1, 1, false, R.drawable.official));
        tasksPoolAdapter.add(new DataModel(5, "Task 5", 2, 2, true, R.drawable.academic));
        tasksPoolAdapter.add(new DataModel(6, "Task 6", 0, 0, false, R.drawable.miscellaneous));
        tasksPoolAdapter.add(new DataModel(7, "Task 7", 1, 1, false, R.drawable.official));
        tasksPoolAdapter.add(new DataModel(8, "Task 8", 2, 2, true, R.drawable.academic));
        tasksPoolAdapter.add(new DataModel(9, "Task 9", 0, 0, false, R.drawable.miscellaneous));
        tasksPoolAdapter.add(new DataModel(10, "Task 10", 1, 1, false, R.drawable.official));
        tasksPoolAdapter.add(new DataModel(11, "Task 11", 2, 2, true, R.drawable.academic));
        tasksPoolAdapter.add(new DataModel(12, "Task 12", 0, 0, false, R.drawable.miscellaneous));
        tasksPoolAdapter.add(new DataModel(13, "Task 13", 1, 1, false, R.drawable.official));
        tasksPoolAdapter.add(new DataModel(14, "Task 14", 2, 2, true, R.drawable.academic));
        tasksPoolAdapter.add(new DataModel(15, "Task 15", 0, 0, false, R.drawable.miscellaneous));
        tasksPoolAdapter.add(new DataModel(16, "Task 16", 1, 1, false, R.drawable.official));
        tasksPoolAdapter.add(new DataModel(17, "Task 17", 2, 2, true, R.drawable.academic));
        tasksPoolAdapter.add(new DataModel(18, "Task 18", 0, 0, false, R.drawable.miscellaneous));
    }

    private static int getImgResource(int taskCategory)
    {
        switch(taskCategory)
        {
            case 0:
                return R.drawable.official;
            case 1:
                return R.drawable.academic;
            case 2:
                return R.drawable.miscellaneous;
            default:
                break;
        }

        return 0;
    }

    //-----------------------------------------------------------------------------------------------------------------------

    public static void addToAdapter(int id, String taskName, int taskCategory, int taskPriority, boolean taskAlarm)
    {
        final int taskID = id;

        tasksPoolAdapter.add(new DataModel(id, taskName, taskCategory, taskPriority, taskAlarm, getImgResource(taskCategory)));

        listView.post(new Runnable()
        {
            @Override
            public void run() {
                listView.smoothScrollToPosition(tasksPoolAdapter.getPosition(taskID));
            }
        });
    }

    public static void editInAdapter(int id, String taskName, int taskCategory, int taskPriority, boolean taskAlarm)
    {
        final int position = tasksPoolAdapter.getPosition(id);

        tasksPoolAdapter.removeItemWithID(id);

        tasksPoolAdapter.insert(new DataModel(id, taskName, taskCategory, taskPriority, taskAlarm, getImgResource(taskCategory)), position);

        listView.post(new Runnable()
        {
            @Override
            public void run() {
                listView.smoothScrollToPosition(position);
            }
        });
    }

    public static void removeFromAdapter(int id)
    {
        tasksPoolAdapter.removeItemWithID(id);
    }
}
