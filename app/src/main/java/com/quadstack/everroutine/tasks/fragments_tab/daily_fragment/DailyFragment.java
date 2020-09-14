package com.quadstack.everroutine.tasks.fragments_tab.daily_fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.days_to_remember_tasks_pool.DaysToRemember_TasksPool_Adapter;
import com.quadstack.everroutine.days_to_remember_tasks_pool.DaysToRemember_TasksPool_DataModel;
import com.quadstack.everroutine.tasks.TaskAddEdit;
import com.quadstack.everroutine.tasks_pool.TasksPool;
import com.quadstack.everroutine.tasks_pool.TasksPoolAddEdit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sakib on 10/4/2016.
 */

public class DailyFragment extends Fragment
{
    View dailyFragmentView;

    Context context = null;

    private TextView dateText;
    private ImageView leftArrow, rightArrow;
    private ImageView addButton, addButtonLabel, deleteButton, deleteButtonLabel, deleteAllButton, deleteAllButtonLabel;
    private static TextView listEmpty;

    public static List selectedIDList;
    public static boolean longPressSelectMode;
    public Vibrator vibrator;

    public static ListView listView;
    public static Adapter listAdapter;

    private int taskViewType;

    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        dailyFragmentView = inflater.inflate(R.layout.fragment_daily_task_view, container, false);

        listView = (ListView)dailyFragmentView.findViewById(R.id.dailyTaskListView);

        listAdapter = new Adapter(context, R.layout.task_row);

        listView.setAdapter(listAdapter);

        dateText = (TextView)dailyFragmentView.findViewById(R.id.dailyTaskDate);
        leftArrow = (ImageView)dailyFragmentView.findViewById(R.id.dailyTaskLeftArrow);
        rightArrow = (ImageView)dailyFragmentView.findViewById(R.id.dailyTaskRightArrow);

        addButton = (ImageView)dailyFragmentView.findViewById(R.id.dailyTaskAdd);
        addButtonLabel = (ImageView)dailyFragmentView.findViewById(R.id.dailyTaskAddLabel);
        deleteButton = (ImageView)dailyFragmentView.findViewById(R.id.dailyTaskDelete);
        deleteButtonLabel = (ImageView)dailyFragmentView.findViewById(R.id.dailyTaskDeleteLabel);
        deleteAllButton = (ImageView)dailyFragmentView.findViewById(R.id.dailyTaskDeleteAll);
        deleteAllButtonLabel = (ImageView)dailyFragmentView.findViewById(R.id.dailyTaskDeleteAllLabel);

        listEmpty = (TextView)dailyFragmentView.findViewById(R.id.dailyTaskListEmpty);

        calendar = Calendar.getInstance();

        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(new Date());

        dateText.setText(Utility.dateFormat.format(new Date()) + "    " + Utility.daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)-1]);

        setDynamicLayout();
        addListeners();

        addData(Utility.dateFormat.format(new Date()));

        checkEmptyList();

        return dailyFragmentView;
    }

    public void initFragment(Context context, int taskViewType)
    {
        this.context = context;

        selectedIDList = new ArrayList();

        longPressSelectMode = false;

        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        this.taskViewType = taskViewType;
    }

    public void addData(String date)
    {
        //Clearing ticks, when list is refreshed with a different date -----------------------------
        for(Object i : selectedIDList)
        {
            ((DataModel)listAdapter.getItem((int)i)).setTickImgResource(0);
        }

        selectedIDList.removeAll(DailyFragment.selectedIDList);
        listAdapter.notifyDataSetChanged();
        longPressSelectMode = false;
        //Clearing ticks, when list is refreshed with a different date -----------------------------

        //Resetting --------------------------------------------------------------------------------
        listAdapter.clear();
        //Resetting --------------------------------------------------------------------------------

        Cursor taskCursor;

        if(taskViewType!=Utility.combined_task)
        {
            taskCursor = (new DBModule(context)).getTasks(taskViewType, date);
        }
        else
        {
            taskCursor = (new DBModule(context)).getAllTasks(date);
        }

        if(taskCursor.moveToFirst())
        {
            do
            {
                int tasksPoolId = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.TasksPool.id));
                String taskName = taskCursor.getString(taskCursor.getColumnIndex(DBMeta.TasksPool.task_name));
                int taskCategory = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.TasksPool.task_category));
                int taskPriority = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.TasksPool.task_priority));
                int taskAlarm = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.TasksPool.task_alarm));

                int taskId = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.id));
                int tasksPoolRef = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.tasks_pool_ref));
                int recursiveDayId = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.recursive_day_id));
                int taskDateRef = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.task_date_ref));
                int startHour = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.start_hour));
                int startMinute = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.start_minute));
                int startAmPm = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.start_am_pm));
                int stopHour = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.stop_hour));
                int stopMinute = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.stop_minute));
                int stopAmPm = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.stop_am_pm));
                int startTwentyFour = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.start_twentyfour));
                int stopTwentyFour = taskCursor.getInt(taskCursor.getColumnIndex(DBMeta.Tasks.stop_twentyfour));

                listAdapter.add(new DataModel(taskId, taskName, recursiveDayId, taskDateRef, startHour, startMinute, startAmPm, stopHour, stopMinute, stopAmPm, startTwentyFour, stopTwentyFour, 0, taskCategory, taskPriority, taskAlarm, tasksPoolRef));
            }
            while(taskCursor.moveToNext());
        }

        checkEmptyList();
    }

    public void showTick(int position)
    {
        if(((DataModel)listAdapter.getItem(position)).getTickImgResource() == 0)
        {
            ((DataModel)listAdapter.getItem(position)).setTickImgResource(R.drawable.green_tick);
            selectedIDList.add(position);
        }
        else
        {
            ((DataModel)listAdapter.getItem(position)).setTickImgResource(0);
            selectedIDList.remove((Object)position);

            if(selectedIDList.isEmpty())
            {
                longPressSelectMode = false;
            }
        }

        listAdapter.notifyDataSetChanged();
    }

    public void setDynamicLayout()
    {
        leftArrow.getLayoutParams().width = Utility.taskDateButtonWidth;
        leftArrow.getLayoutParams().height = Utility.taskDateButtonHeight;
        ((RelativeLayout.LayoutParams)leftArrow.getLayoutParams()).setMargins(Utility.taskArrowLeft, Utility.taskDateButtonTop, 0, 0);

        leftArrow.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.left_arrow, Utility.taskDateButtonWidth, Utility.taskDateButtonHeight));

        dateText.getLayoutParams().width = Utility.taskDateButtonWidth;
        dateText.getLayoutParams().height = Utility.taskDateButtonHeight;
        ((RelativeLayout.LayoutParams)dateText.getLayoutParams()).setMargins(Utility.taskDateButtonLeft, Utility.taskDateButtonTop, 0, 0);

        rightArrow.getLayoutParams().width = Utility.taskDateButtonWidth;
        rightArrow.getLayoutParams().height = Utility.taskDateButtonHeight;
        ((RelativeLayout.LayoutParams)rightArrow.getLayoutParams()).setMargins(0, Utility.taskDateButtonTop, Utility.taskArrowLeft, 0);

        rightArrow.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.right_arrow, Utility.taskDateButtonWidth, Utility.taskDateButtonHeight));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        listView.getLayoutParams().width = Utility.screenWidth;
        listView.getLayoutParams().height = Utility.taskListViewHeight;
        ((RelativeLayout.LayoutParams)listView.getLayoutParams()).setMargins(0, Utility.taskListViewTop, 0, Utility.taskListViewBottom);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        addButton.getLayoutParams().width = Utility.taskButtonDiameter;
        addButton.getLayoutParams().height = Utility.taskButtonDiameter;
        ((RelativeLayout.LayoutParams)addButton.getLayoutParams()).setMargins(Utility.taskAddLeft, Utility.taskAddTop, 0, 2);

        addButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_add, Utility.taskButtonDiameter, Utility.taskButtonDiameter));

        addButtonLabel.getLayoutParams().width = Utility.taskButtonLabelWidth;
        addButtonLabel.getLayoutParams().height = Utility.taskButtonLabelHeight;
        ((RelativeLayout.LayoutParams)addButtonLabel.getLayoutParams()).setMargins(Utility.taskAddLabelLeft, Utility.taskAddLabelTop, 0, 0);

        addButtonLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_add_label, Utility.taskButtonDiameter, Utility.taskButtonDiameter));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        deleteButton.getLayoutParams().width = Utility.taskButtonDiameter;
        deleteButton.getLayoutParams().height = Utility.taskButtonDiameter;
        ((RelativeLayout.LayoutParams)deleteButton.getLayoutParams()).setMargins(Utility.taskDeleteLeft, Utility.taskAddTop, 0, 2);

        deleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete, Utility.taskButtonDiameter, Utility.taskButtonDiameter));

        deleteButtonLabel.getLayoutParams().width = Utility.taskButtonLabelWidth;
        deleteButtonLabel.getLayoutParams().height = Utility.taskButtonLabelHeight;
        ((RelativeLayout.LayoutParams)deleteButtonLabel.getLayoutParams()).setMargins(Utility.taskDeleteLabelLeft, Utility.taskAddLabelTop, 0, 0);

        deleteButtonLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete_label, Utility.taskButtonDiameter, Utility.taskButtonDiameter));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        deleteAllButton.getLayoutParams().width = Utility.taskButtonDiameter;
        deleteAllButton.getLayoutParams().height = Utility.taskButtonDiameter;
        ((RelativeLayout.LayoutParams)deleteAllButton.getLayoutParams()).setMargins(Utility.taskDeleteAllLeft, Utility.taskAddTop, 0, 2);

        deleteAllButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete_all, Utility.taskButtonDiameter, Utility.taskButtonDiameter));

        deleteAllButtonLabel.getLayoutParams().width = Utility.taskButtonLabelWidth;
        deleteAllButtonLabel.getLayoutParams().height = Utility.taskButtonLabelHeight;
        ((RelativeLayout.LayoutParams)deleteAllButtonLabel.getLayoutParams()).setMargins(Utility.taskDeleteAllLabelLeft, Utility.taskAddLabelTop, 0, 0);

        deleteAllButtonLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete_all_label, Utility.taskButtonDiameter, Utility.taskButtonDiameter));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        listEmpty.getLayoutParams().width = Utility.rowWidth;
        listEmpty.getLayoutParams().height = Utility.rowHeight;
        ((RelativeLayout.LayoutParams)listEmpty.getLayoutParams()).setMargins(Utility.rowLeft, ((Utility.screenHeight/2) - Utility.rowHeight - Utility.titleHeight), 0, 0);

        listEmpty.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.rowWidth, Utility.rowHeight)));
        listEmpty.setPadding(100, 0, 100, 0);
        listEmpty.setTextSize(Utility.fontSize);
    }

    public void addListeners()
    {
        leftArrow.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {

                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    calendar.add(Calendar.DATE, -1);
                    String date = Utility.dateFormat.format(calendar.getTime());

                    dateText.setText(Utility.dateFormat.format(calendar.getTime()) + "    " + Utility.daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)-1]);

                    addData(date);
                }

                return true;
            }
        });

        rightArrow.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {

                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    calendar.add(Calendar.DATE, 1);
                    String date = Utility.dateFormat.format(calendar.getTime());

                    dateText.setText(Utility.dateFormat.format(calendar.getTime()) + "    " + Utility.daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)-1]);

                    addData(date);
                }

                return true;
            }
        });

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

                    Intent addEditIntent = new Intent(context, TaskAddEdit.class);

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
                    DataModel task = (DataModel)listAdapter.getItem(position);

                    Intent addEditIntent = new Intent(context, TaskAddEdit.class);

                    addEditIntent.putExtra("activity_type", "edit");
                    addEditIntent.putExtra("task_id", task.getTaskId());
                    addEditIntent.putExtra("task_tasksPoolRef", task.getTasksPoolRef());
                    addEditIntent.putExtra("task_name", task.getTaskName());
                    addEditIntent.putExtra("task_category", task.getTaskCategory());
                    addEditIntent.putExtra("task_priority", task.getTaskPriority());
                    addEditIntent.putExtra("task_alarm", (task.getTaskAlarm()==1?true:false));
                    addEditIntent.putExtra("task_recursive", (task.getDateRef()==-1?true:false));
                    addEditIntent.putExtra("task_date", (task.getDateRef()==-1?"":task.getDate()));
                    addEditIntent.putExtra("task_day", task.getRecursiveDayID());
                    addEditIntent.putExtra("task_startHour", task.getStartHour());
                    addEditIntent.putExtra("task_startMinute", task.getStartMinute());
                    addEditIntent.putExtra("task_startAmPm", task.getStartAmPm());
                    addEditIntent.putExtra("task_stopHour", task.getStopHour());
                    addEditIntent.putExtra("task_stopMinute", task.getStopMinute());
                    addEditIntent.putExtra("task_stopAmPm", task.getStopAmPm());

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

        deleteButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    deleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete_selected, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    deleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

                    if(selectedIDList.isEmpty())
                    {
                        Toast.makeText(context, "No items selected", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        new AlertDialog.Builder(context)
                                .setTitle("Confirm Deletion").setMessage("Are you sure you want to delete " + selectedIDList.size() + " items?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        listAdapter.removeMultipleElements(selectedIDList);

                                        longPressSelectMode = false;

                                        checkEmptyList();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }

                    return true;
                }

                return false;
            }
        });

        deleteAllButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    deleteAllButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete_all_selected, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    deleteAllButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete_all, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

                    if(listAdapter.getCount() == 0)
                    {
                        Toast.makeText(context, "The list is empty", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        new AlertDialog.Builder(context)
                                .setTitle("Confirm Deletion").setMessage("Are you sure you want to delete all the items?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if(taskViewType == Utility.combined_task)
                                        {
                                            (new DBModule(context)).removeAllItems(DBMeta.Tasks.table_name);
                                        }
                                        else
                                        {
                                            (new DBModule(context)).removeAllTasksOfSpecificCategory(DBMeta.Tasks.table_name, taskViewType, Utility.dateFormat.format(calendar.getTime()));
                                        }

                                        listAdapter.clear();

                                        longPressSelectMode = false;

                                        checkEmptyList();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }

                    return true;
                }

                return false;
            }
        });
    }

    public static void checkEmptyList()
    {
        if(listAdapter.getCount() == 0)
        {
            listEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
        else
        {
            listEmpty.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }
}