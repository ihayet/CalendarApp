package com.quadstack.everroutine.tasks.fragments_tab.calendar_fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.tasks.TaskAddEdit;
import com.quadstack.everroutine.tasks.fragments_tab.daily_fragment.Adapter;
import com.quadstack.everroutine.tasks.fragments_tab.daily_fragment.DataModel;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sakib on 10/4/2016.
 */

public class CalendarFragment extends Fragment
{
    private View calendarFragmentView;

    private CalendarView calendarView;
    private static ListView calendarListView;
    private static TextView listEmpty;

    private static  Adapter listAdapter;

    private Context context;

    private int taskViewType;

    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        calendarFragmentView = inflater.inflate(R.layout.fragment_calendar_task_view, container ,false );

        calendarView = (CalendarView)calendarFragmentView.findViewById(R.id.calendarView);
        calendarListView = (ListView)calendarFragmentView.findViewById(R.id.calendarListView);
        listEmpty = (TextView)calendarFragmentView.findViewById(R.id.calendarTaskListEmpty);

        listAdapter = new Adapter(context, R.layout.task_row);

        calendarListView.setAdapter(listAdapter);

        calendar = Calendar.getInstance();

        addData(Utility.dateFormat.format(new Date()));

        setDynamicLayout();
        addListeners();

        return  calendarFragmentView;
    }

    public void initFragment(Context context, int taskViewType)
    {
        this.context = context;
        this.taskViewType = taskViewType;
    }

    private void addData(String date)
    {
        listAdapter.clear();

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

    public static void checkEmptyList()
    {
        if(listAdapter.getCount() == 0)
        {
            listEmpty.setVisibility(View.VISIBLE);
            calendarListView.setVisibility(View.INVISIBLE);
        }
        else
        {
            listEmpty.setVisibility(View.INVISIBLE);
            calendarListView.setVisibility(View.VISIBLE);
        }
    }

    private void setDynamicLayout()
    {
        calendarView.getLayoutParams().height = Utility.calendarWidth;

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        calendarListView.getLayoutParams().height = Utility.screenHeight - (int)(1.5*Utility.titleHeight) - Utility.calendarWidth - (Utility.taskRowHeight/2);

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        listEmpty.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        listEmpty.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        listEmpty.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditNameHeight)));
    }

    private void addListeners()
    {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                String date = Utility.dateFormat.format(calendar.getTime());

                Log.e("Date selected", date);

                addData(date);
            }
        });

        calendarListView.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        });
    }
}
