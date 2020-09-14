package com.quadstack.everroutine.statistics;

/**
 * Created by Sakib on 2/10/2017.
 */
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.double_drawer.DoubleDrawerActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class activity_tracker extends DoubleDrawerActivity
{
    View statistics_view;
    private static String TAG = "ActivityTracker";
    ImageView statisticsTitle;
    Button ButtonLeft,ButtonRight;
    PieChart pieChart;
    TextView Week;
    String formattedDate;
    Context context;

    private float[] yData = {0.0f , 0.0f ,0.0f};
    private String[] xData = {"Official", "Academic", "Misc"};


    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        statistics_view = super.setContentLayout(R.layout.activity_tracker, R.layout.custom_actionbar_layout);

        statisticsTitle = (ImageView)statistics_view.findViewById(R.id.statisticsTitle);
        pieChart = (PieChart) statistics_view.findViewById(R.id.idPieChart);
        ButtonLeft = (Button) statistics_view.findViewById(R.id.ButtonLeft);
        ButtonRight = (Button) statistics_view.findViewById(R.id.ButtonRight);
        Week = (TextView) statistics_view.findViewById(R.id.week);

        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Hours Spend");
        pieChart.setCenterTextSize(10);

        setDynamicLayout();

        addDataSet();
        addListeners();
        updateView(0);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
               /* Log.d(TAG,"onValueSelected:Value selsct from chart");
                Log.d(TAG,"onValueSelected" + e.toString() );
                Log.d(TAG,"onValueSelected" + h.toString() );*/
                int pos1 = e.toString().indexOf("y: ");
                String hours = e.toString().substring(pos1 + 3);

                int i;
                for (i = 0; i < yData.length; i++)
                {
                    if (yData[i] == Float.parseFloat(hours))
                    {
                        pos1 = i;
                        break;
                    }
                }
                String hoursDetails = xData[pos1];
                Toast.makeText(activity_tracker.this, "Hours Spend  " + hours + "\n" + "Activity Type " + hoursDetails, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    @Override
    protected void setDynamicLayout()
    {
        ((RelativeLayout.LayoutParams)statisticsTitle.getLayoutParams()).width = Utility.titleWidth;
        ((RelativeLayout.LayoutParams)statisticsTitle.getLayoutParams()).height = Utility.titleHeight;
        statisticsTitle.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.statistics_title, Utility.titleWidth, Utility.titleHeight));

        //------------------------------------------------------------------------------------------------------------------------------------------------------------

        ((RelativeLayout.LayoutParams)Week.getLayoutParams()).width = Utility.centerTextViewWidth;
        ((RelativeLayout.LayoutParams)Week.getLayoutParams()).height = Utility.centerTextViewHeight;
        Week.setPadding(5, 0, 0, 5);
    }

    @Override
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
    }

    private void addDataSet() {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String>  xEntrys = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);

        for(int i = 0; i< yData.length ; i++)
        {
            yEntrys.add(new PieEntry(yData[i], i));
        }
        for (int i = 1 ; i < xData.length ;  i++)
        {
            xEntrys.add(xData[i] );
        }
        PieDataSet pieDataSet = new PieDataSet(yEntrys , "Activity Tracker");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(25);
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

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
        Week.setText(up + " - " + down);
        calendar.add(Calendar.DATE, -increment);

        yData[Utility.official_task] = 0;
        yData[Utility.academic_task] = 0;
        yData[Utility.miscellaneous_task] = 0;

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

        addDataSet();

        Log.e("Official: ", Double.toString(yData[Utility.official_task]));
        Log.e("Academic: ", Double.toString(yData[Utility.academic_task]));
        Log.e("Miscellaneous: ", Double.toString(yData[Utility.miscellaneous_task]));
    }

    public void addData(String date)
    {
        try
        {
            calendar.setTime(Utility.dateFormat.parse(date));
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            for (int tasktypes = 0; tasktypes < 3; tasktypes++)
            {
                Cursor taskCursor = (new DBModule(getApplicationContext())).getTasks(tasktypes, date);

                if (taskCursor.moveToFirst())
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

                        int difference =stopTwentyFour - startTwentyFour;
                        float hour = difference / 100 ;
                        float minute = difference % 100 ;

                        yData[tasktypes] += (hour * 6 + minute )/6;
                    }
                    while (taskCursor.moveToNext());
                }
            }
        }
        catch(Exception e)
        {
            Log.e("Exception:", e.getMessage());
        }

    }
}


