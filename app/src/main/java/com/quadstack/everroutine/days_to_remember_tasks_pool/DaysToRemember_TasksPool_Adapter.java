package com.quadstack.everroutine.days_to_remember_tasks_pool;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.quadstack.everroutine.days_to_remember.DataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ISHRAK on 11/29/2016.
 */
public abstract class DaysToRemember_TasksPool_Adapter extends ArrayAdapter
{
    protected Context context;
    protected List list = new ArrayList();

    public DaysToRemember_TasksPool_Adapter(Context context, int resource) {
        super(context, resource);

        this.context = context;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public void remove(Object object) {

        list.remove(object);
    }

    @Override
    public void insert(Object object, int index) {
        list.add(index, object);
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

    public void removeAll()
    {
        list.removeAll(list);

        notifyDataSetChanged();
    }

    public void removeItemWithID(int id)
    {
        for(Object item : list)
        {
            if(((DaysToRemember_TasksPool_DataModel)item).getItemID() == id)
            {
                list.remove(item);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public int getPosition(int id)
    {
        for(Object item : list)
        {
            if(((DaysToRemember_TasksPool_DataModel)item).getItemID() == id)
            {
                return list.indexOf(item);
            }
        }

        return -1;
    }

    protected abstract void setRowDynamicLayout();
}
