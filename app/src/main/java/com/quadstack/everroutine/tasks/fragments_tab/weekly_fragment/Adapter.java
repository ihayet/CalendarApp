package com.quadstack.everroutine.tasks.fragments_tab.weekly_fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quadstack.everroutine.R;

import java.util.ArrayList;

/**
 * Created by Sakib on 1/13/2017.
 */

public class Adapter extends ArrayAdapter
{
    Context context;

    private ArrayList list;

    private static LayoutInflater inflater=null;

    public Adapter(Context context, int resource)
    {
        super(context, resource);

        this.context = context;

        list = new ArrayList();
    }

    public class Holder
    {
        TextView task;
        TextView start_time;
        TextView end_time;
    }

    @Override
    public void add(Object object)
    {
        list.add(object);
        notifyDataSetChanged();
    }

    @Override
    public void insert(Object object, int index)
    {
        list.add(index, object);
        notifyDataSetChanged();
    }

    @Override
    public void remove(Object object)
    {
        list.remove(object);
        notifyDataSetChanged();
    }

    @Override
    public void clear()
    {
        list.removeAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generaed method stub

        Holder holder;
        View cellView = convertView;

        if(cellView == null)
        {
            inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            holder = new Holder();

            cellView = inflater.inflate(R.layout.weekly_list_cells, null);
            holder.task=(TextView) cellView.findViewById(R.id.task);
            holder.start_time=(TextView) cellView.findViewById(R.id.start_time);
            holder.end_time=(TextView) cellView.findViewById(R.id.end_time);

            cellView.setTag(holder);
        }
        else
        {
            holder = (Holder)cellView.getTag();
        }

        DataModel task = (DataModel) list.get(position);

        int taskPriority = task.getTaskPriority();
        setDynamicLayout(holder, taskPriority);

        holder.task.setText(task.getTaskName());
        holder.start_time.setText(task.getStartTime());
        holder.end_time.setText(task.getStopTime());

        return cellView;
    }

    private void setDynamicLayout(Holder holder, int priority)
    {
        int color = 0;

        switch(priority)
        {
            case 0:
                color = Color.parseColor("#99ff66");
                break;
            case 1:
                color = Color.parseColor("#33ccff");
                break;
            case 2:
                color = Color.parseColor("#ff6666");
                break;
            default:
                break;
        }
        holder.task.setBackgroundColor(color);
        holder.start_time.setBackgroundColor(color);
        holder.end_time.setBackgroundColor(color);
    }
}
