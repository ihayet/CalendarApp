package com.quadstack.everroutine.tasks_pool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.days_to_remember_tasks_pool.DaysToRemember_TasksPool_Adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ISHRAK on 11/29/2016.
 */
public class Adapter extends DaysToRemember_TasksPool_Adapter
{
    private LayoutHandler layoutHandler;

    public Adapter(Context context, int resource) {
        super(context, resource);

        this.context = context;
    }

    static class LayoutHandler
    {
        RelativeLayout row;
        TextView taskName, taskAlarm, taskPriorityText;
        SeekBar taskPriority;
        ImageView taskCategoryImageView, tickImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowLayout = convertView;

        //holder-pattern start

        if(rowLayout == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowLayout = layoutInflater.inflate(R.layout.tasks_pool_row, parent, false);

            layoutHandler = new LayoutHandler();

            layoutHandler.row = (RelativeLayout)rowLayout.findViewById(R.id.tasksPoolRow);
            layoutHandler.taskName = (TextView)rowLayout.findViewById(R.id.tasksPoolRowName);
            layoutHandler.taskPriority = (SeekBar)rowLayout.findViewById(R.id.tasksPoolRowPriority);
            layoutHandler.taskPriorityText = (TextView)rowLayout.findViewById(R.id.tasksPoolRowPriorityText);
            layoutHandler.taskAlarm = (TextView)rowLayout.findViewById(R.id.tasksPoolRowAlarm);
            layoutHandler.taskCategoryImageView = (ImageView)rowLayout.findViewById(R.id.tasksPoolRowImage);
            layoutHandler.tickImageView = (ImageView)rowLayout.findViewById(R.id.tasksPoolRowTick);

            setRowDynamicLayout();

            rowLayout.setTag(layoutHandler);
        }
        else
        {
            layoutHandler = (LayoutHandler)rowLayout.getTag();
        }
        //holder-pattern end

        final DataModel task = (DataModel)this.getItem(position);

        layoutHandler.taskName.setText(task.getItemName());
        layoutHandler.taskPriority.setProgress(task.getTaskPriority());
        layoutHandler.taskAlarm.setText("Alarm: " + (task.isTaskAlarm() ? "On" : "Off"));
        layoutHandler.taskCategoryImageView.setImageBitmap(Utility.decodeSampledBitmapFromResource(context.getResources(), task.getImgResource(), Utility.eventImageWidth, Utility.eventImageHeight));
        layoutHandler.tickImageView.setImageResource(task.getTickImgResource());

        layoutHandler.taskPriority.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rowLayout;
    }

    @Override
    protected void setRowDynamicLayout()
    {
        layoutHandler.row.getLayoutParams().width = Utility.rowWidth;
        layoutHandler.row.getLayoutParams().height = Utility.rowHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.row.getLayoutParams()).setMargins(Utility.rowLeft, Utility.rowTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.taskCategoryImageView.getLayoutParams().width = Utility.eventImageWidth;
        layoutHandler.taskCategoryImageView.getLayoutParams().height = Utility.eventImageHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.taskCategoryImageView.getLayoutParams()).setMargins(Utility.eventImageLeft, Utility.eventImageTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.tickImageView.getLayoutParams().width = Utility.tickImageWidth;
        layoutHandler.tickImageView.getLayoutParams().height = Utility.tickImageHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.tickImageView.getLayoutParams()).setMargins(Utility.tickImageLeft, Utility.tickImageTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.taskName.getLayoutParams().width = Utility.eventNameWidth;
        layoutHandler.taskName.getLayoutParams().height = Utility.eventNameHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.taskName.getLayoutParams()).setMargins(Utility.eventNameLeft, Utility.eventNameTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.taskPriorityText.getLayoutParams().width = (int)(0.22*Utility.eventNameWidth);
        layoutHandler.taskPriorityText.getLayoutParams().height = Utility.eventDateHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.taskPriorityText.getLayoutParams()).setMargins(Utility.eventDateLeft, Utility.eventDateTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.taskPriority.getLayoutParams().width = Utility.eventNameWidth - layoutHandler.taskPriorityText.getLayoutParams().width;
        layoutHandler.taskPriority.getLayoutParams().height = Utility.eventDateHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.taskPriority.getLayoutParams()).setMargins(Utility.eventDateLeft + layoutHandler.taskPriorityText.getLayoutParams().width, Utility.eventDateTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.taskAlarm.getLayoutParams().width = Utility.eventDateWidth;
        layoutHandler.taskAlarm.getLayoutParams().height = Utility.eventDateHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.taskAlarm.getLayoutParams()).setMargins(Utility.eventDetailsLeft, Utility.eventDetailsTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~
    }
}
