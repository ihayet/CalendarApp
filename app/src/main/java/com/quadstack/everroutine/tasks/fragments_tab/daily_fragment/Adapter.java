package com.quadstack.everroutine.tasks.fragments_tab.daily_fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ISHRAK on 1/14/17.
 */

public class Adapter extends ArrayAdapter
{
    private Context context;
    private ArrayList list;
    private LayoutHandler layoutHandler;

    public Adapter(Context context, int resource) {
        super(context, resource);

        this.context = context;

        list = new ArrayList();
    }

    static class LayoutHandler
    {
        RelativeLayout taskRow;
        TextView taskRowName, taskRowTime, taskRowPriorityTextView;
        SeekBar taskRowPriority;
        ImageView tickImageView;
    }

    @Override
    public void add(Object object)
    {
        list.add(object);
    }

    @Override
    public void insert(Object object, int index)
    {
        list.add(index, object);
    }

    @Override
    public void remove(Object object)
    {
        list.remove(object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public void clear()
    {
        list.removeAll(list);
        notifyDataSetChanged();
    }

    public void removeMultipleElements(List elementList)
    {
        Collections.sort(elementList);

        for(int i = 0; i < elementList.size(); i++)
        {
            int pos = (int)elementList.get(i) - i;
            list.remove(pos);
        }

        notifyDataSetChanged();

        elementList.removeAll(elementList);
    }

    public ArrayList getList()
    {
        return list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowLayout = convertView;

        //holder-pattern start
        if(rowLayout == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowLayout = inflater.inflate(R.layout.task_row, parent, false);

            layoutHandler = new LayoutHandler();

            layoutHandler.taskRow = (RelativeLayout)rowLayout.findViewById(R.id.taskRow);
            layoutHandler.taskRowName = (TextView)rowLayout.findViewById(R.id.taskRowName);
            layoutHandler.taskRowTime = (TextView)rowLayout.findViewById(R.id.taskRowTimeText);
            layoutHandler.taskRowPriorityTextView = (TextView)rowLayout.findViewById(R.id.taskRowPriorityText);
            layoutHandler.taskRowPriority = (SeekBar)rowLayout.findViewById(R.id.taskRowPriority);
            layoutHandler.tickImageView = (ImageView)rowLayout.findViewById(R.id.taskRowTick);

            setRowDynamicLayout();

            rowLayout.setTag(layoutHandler);
        }
        else
        {
            layoutHandler = (LayoutHandler)rowLayout.getTag();
        }
        //holder-pattern end

        DataModel taskData = (DataModel)list.get(position);

        layoutHandler.taskRowName.setText(taskData.getTaskName());
        layoutHandler.taskRowTime.setText(taskData.getTime());
        layoutHandler.taskRowPriority.setProgress(taskData.getTaskPriority());
        layoutHandler.tickImageView.setImageResource(taskData.getTickImgResource());

        layoutHandler.taskRowPriority.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rowLayout;
    }

    private void setRowDynamicLayout()
    {
        layoutHandler.taskRow.getLayoutParams().width = Utility.rowWidth;
        layoutHandler.taskRow.getLayoutParams().height = Utility.taskRowHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.taskRow.getLayoutParams()).setMargins(Utility.rowLeft, Utility.rowTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.tickImageView.getLayoutParams().width = Utility.tickImageWidth;
        layoutHandler.tickImageView.getLayoutParams().height = Utility.tickImageHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.tickImageView.getLayoutParams()).setMargins(Utility.tickImageLeft, Utility.tickImageTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.taskRowName.getLayoutParams().width = Utility.eventNameWidth;
        layoutHandler.taskRowName.getLayoutParams().height = Utility.taskNameHeight;
        //((RelativeLayout.LayoutParams)layoutHandler.taskRowName.getLayoutParams()).setMargins(Utility.eventImageLeft, Utility.eventImageTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.taskRowPriorityTextView.getLayoutParams().width = (int)(0.22*Utility.eventNameWidth);
        layoutHandler.taskRowPriorityTextView.getLayoutParams().height = Utility.taskNameHeight;
        //((RelativeLayout.LayoutParams)layoutHandler.taskRowPriorityTextView.getLayoutParams()).setMargins(Utility.eventImageLeft, Utility.eventDateTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.taskRowPriority.getLayoutParams().width = Utility.taskPriorityWidth;
        layoutHandler.taskRowPriority.getLayoutParams().height = Utility.taskNameHeight;
        //((RelativeLayout.LayoutParams)layoutHandler.taskRowPriority.getLayoutParams()).setMargins(Utility.eventImageLeft + layoutHandler.taskRowPriorityTextView.getLayoutParams().width, Utility.eventDateTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        layoutHandler.taskRowTime.getLayoutParams().width = Utility.eventDateWidth;
        layoutHandler.taskRowTime.getLayoutParams().height = Utility.eventDateHeight;
        //((RelativeLayout.LayoutParams)layoutHandler.taskRowTime.getLayoutParams()).setMargins(Utility.eventImageLeft, Utility.eventDetailsTop, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~
    }
}
