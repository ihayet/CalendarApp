package com.quadstack.everroutine.tasks.fragments_tab.weekly_fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.tasks.TaskAddEdit;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sakib on 10/4/2016.
 */

public class WeeklyFragment extends Fragment
{
    private View weeklyFragmentView;
    
    Context context;

    TextView sun, mon, tue, wed, thu, fri, sat;
    ListView list_sun,list_mon,list_tues,list_wed,list_thurs,list_fri,list_sat;
    TextView week;
    Button ButtonLeft,ButtonRight;
    String formattedDate;
    
    int taskViewType;

    Calendar calendar = Calendar.getInstance();
    Adapter[] listAdapters;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        weeklyFragmentView = inflater.inflate(R.layout.fragment_weekly_task_view, container ,false );

        sun = (TextView)weeklyFragmentView.findViewById(R.id.sunTextView);
        mon = (TextView)weeklyFragmentView.findViewById(R.id.monTextView);
        tue = (TextView)weeklyFragmentView.findViewById(R.id.tueTextView);
        wed = (TextView)weeklyFragmentView.findViewById(R.id.wedTextView);
        thu = (TextView)weeklyFragmentView.findViewById(R.id.thuTextView);
        fri = (TextView)weeklyFragmentView.findViewById(R.id.friTextView);
        sat = (TextView)weeklyFragmentView.findViewById(R.id.satTextView);

        list_sun = (ListView) weeklyFragmentView.findViewById(R.id.listsunday);
        list_mon = (ListView) weeklyFragmentView.findViewById(R.id.listmonday);
        list_tues= (ListView) weeklyFragmentView.findViewById(R.id.listtuesday);
        list_wed = (ListView) weeklyFragmentView.findViewById(R.id.listwednesday);
        list_sat = (ListView) weeklyFragmentView.findViewById(R.id.listsaturday);
        list_thurs = (ListView) weeklyFragmentView.findViewById(R.id.listthrusday);
        list_fri = (ListView) weeklyFragmentView.findViewById(R.id.listfriday);

        week = (TextView) weeklyFragmentView.findViewById(R.id.week);
        ButtonLeft = (Button) weeklyFragmentView.findViewById(R.id.ButtonLeft);
        ButtonRight = (Button) weeklyFragmentView.findViewById(R.id.ButtonRight);

        calendar.setTime(new Date());
        formattedDate = Utility.dateFormat.format(calendar.getTime());

        listAdapters = new Adapter[7];
        setDynamicLayout();
        addListeners();
        setListAdapters();
        updateView(0);
        
        return  weeklyFragmentView;
    }
    
    public void initFragment(Context context, int taskViewType)
    {
        this.context = context;
        this.taskViewType = taskViewType;
    }

    private void setListAdapters()
    {
        for(int i=0; i<7; i++)
        {
            listAdapters[i] = new Adapter(context, R.layout.weekly_list_cells);
        }
        list_sun.setAdapter(listAdapters[0]);
        list_mon.setAdapter(listAdapters[1]);
        list_tues.setAdapter(listAdapters[2]);
        list_wed.setAdapter(listAdapters[3]);
        list_thurs.setAdapter(listAdapters[4]);
        list_fri.setAdapter(listAdapters[5]);
        list_sat.setAdapter(listAdapters[6]);
    }

    private void updateView(int anchorShift)
    {
        int decrement, increment;


        calendar.add(Calendar.DATE, anchorShift);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        decrement = dayOfWeek;
        increment = 6 - dayOfWeek;


        calendar.add(Calendar.DATE, -decrement);
        String up = Utility.dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, decrement + increment);
        String down = Utility.dateFormat.format(calendar.getTime());
        week.setText(up + " - " + down);
        calendar.add(Calendar.DATE, -increment);


        addData(Utility.dateFormat.format(calendar.getTime()));
        for(int i=dayOfWeek-1; i>=0; i--)
        {
            calendar.add(Calendar.DATE, -1);
            addData(Utility.dateFormat.format(calendar.getTime()));
        }
        calendar.add(Calendar.DATE, dayOfWeek);
        for(int i=dayOfWeek + 1; i<7; i++)
        {
            calendar.add(Calendar.DATE, 1);
            addData(Utility.dateFormat.format(calendar.getTime()));
        }
        calendar.add(Calendar.DATE, dayOfWeek - 6);
    }
    public void addData(String date)
    {
        try
        {
            calendar.setTime(Utility.dateFormat.parse(date));
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            listAdapters[dayOfWeek].clear();

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

                    (listAdapters[dayOfWeek]).add(new DataModel(taskId, taskName, recursiveDayId, taskDateRef, startHour, startMinute, startAmPm, stopHour, stopMinute, stopAmPm, startTwentyFour, stopTwentyFour, 0, taskCategory, taskPriority, taskAlarm, tasksPoolRef));
                }
                while(taskCursor.moveToNext());
            }
        }
        catch(Exception e)
        {
            Log.e("Exception:", e.getMessage());
        }
    }

    public void setDynamicLayout()
    {
        Log.e("Screen Width:", Integer.toString(Utility.screenWidth));
        Log.e("Weekly List Width:", Integer.toString(Utility.weeklyListWidth));

        sun.getLayoutParams().width = Utility.weeklyListWidth;
        mon.getLayoutParams().width = Utility.weeklyListWidth;
        tue.getLayoutParams().width = Utility.weeklyListWidth;
        wed.getLayoutParams().width = Utility.weeklyListWidth;
        thu.getLayoutParams().width = Utility.weeklyListWidth;
        fri.getLayoutParams().width = Utility.weeklyListWidth;
        sat.getLayoutParams().width = Utility.weeklyListWidth;

        sun.getLayoutParams().height = Utility.weeklyListLabelHeight;
        mon.getLayoutParams().height = Utility.weeklyListLabelHeight;
        tue.getLayoutParams().height = Utility.weeklyListLabelHeight;
        wed.getLayoutParams().height = Utility.weeklyListLabelHeight;
        thu.getLayoutParams().height = Utility.weeklyListLabelHeight;
        fri.getLayoutParams().height = Utility.weeklyListLabelHeight;
        sat.getLayoutParams().height = Utility.weeklyListLabelHeight;

        //------------------------------------------------------------------------------------------------------------------

        list_sun.getLayoutParams().width = Utility.weeklyListWidth;
        list_mon.getLayoutParams().width = Utility.weeklyListWidth;
        list_tues.getLayoutParams().width = Utility.weeklyListWidth;
        list_wed.getLayoutParams().width = Utility.weeklyListWidth;
        list_thurs.getLayoutParams().width = Utility.weeklyListWidth;
        list_fri.getLayoutParams().width = Utility.weeklyListWidth;
        list_sat.getLayoutParams().width = Utility.weeklyListWidth;

        //------------------------------------------------------------------------------------------------------------------

        ((RelativeLayout.LayoutParams)week.getLayoutParams()).width = Utility.centerTextViewWidth;
        ((RelativeLayout.LayoutParams)week.getLayoutParams()).height = Utility.centerTextViewHeight;
        week.setPadding(10, 0, 10, 5);

        //------------------------------------------------------------------------------------------------------------------
    }

    public void addListeners()
    {
        ButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("Left Button clicked",  Utility.dateFormat.format(calendar.getTime()));
                updateView(-7);
            }
        });
        ButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("Right Button clicked",  Utility.dateFormat.format(calendar.getTime()));
                updateView(7);
            }
        });

        list_sun.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weeklyListItemClickActivity(0, position);
            }
        });
        list_mon.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weeklyListItemClickActivity(1, position);
            }
        });
        list_tues.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weeklyListItemClickActivity(2, position);
            }
        });
        list_wed.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weeklyListItemClickActivity(3, position);
            }
        });
        list_thurs.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weeklyListItemClickActivity(4, position);
            }
        });
        list_fri.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weeklyListItemClickActivity(5, position);
            }
        });
        list_sat.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weeklyListItemClickActivity(6, position);
            }
        });
    }

    private void weeklyListItemClickActivity(int day, int position)
    {
        DataModel task = (DataModel)listAdapters[day].getItem(position);

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
