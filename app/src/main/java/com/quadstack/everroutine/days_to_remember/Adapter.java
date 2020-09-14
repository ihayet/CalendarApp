package com.quadstack.everroutine.days_to_remember;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.days_to_remember_tasks_pool.DaysToRemember_TasksPool_Adapter;

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
        TextView eventNameTextView, eventDateTextView, eventDetailsTextView;
        ImageView eventIconImageView, tickImageView;
    }

    //------------------------------------------------------------------------------------------------------------------------

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowLayout = convertView;

        //holder-pattern start

        if(rowLayout == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowLayout = layoutInflater.inflate(R.layout.days_to_remember_row, parent, false);

            layoutHandler = new LayoutHandler();

            layoutHandler.row = (RelativeLayout)rowLayout.findViewById(R.id.daysToRememberRow);
            layoutHandler.eventNameTextView = (TextView)rowLayout.findViewById(R.id.daysToRememberRowName);
            layoutHandler.eventDateTextView = (TextView)rowLayout.findViewById(R.id.daysToRememberRowDate);
            layoutHandler.eventDetailsTextView = (TextView)rowLayout.findViewById(R.id.daysToRememberRowDescription);
            layoutHandler.eventIconImageView = (ImageView)rowLayout.findViewById(R.id.daysToRememberRowImage);
            layoutHandler.tickImageView = (ImageView)rowLayout.findViewById(R.id.daysToRememberRowTick);

            setRowDynamicLayout();

            rowLayout.setTag(layoutHandler);
        }
        else
        {
            layoutHandler = (LayoutHandler)rowLayout.getTag();
        }
        //holder-pattern end

        DataModel event = (DataModel)this.getItem(position);

        layoutHandler.eventNameTextView.setText(event.getItemName());
        layoutHandler.eventDateTextView.setText(event.getEventDate());
        layoutHandler.eventDetailsTextView.setText(event.getEventDetails());
        layoutHandler.eventIconImageView.setImageBitmap(Utility.decodeSampledBitmapFromResource(context.getResources(), event.getImgResource(), Utility.eventImageWidth, Utility.eventImageHeight));
        layoutHandler.tickImageView.setImageResource(event.getTickImgResource());

        return rowLayout;
    }

    @Override
    protected void setRowDynamicLayout()
    {
        RelativeLayout.LayoutParams rowParams = new RelativeLayout.LayoutParams(Utility.rowWidth, Utility.rowHeight);
        rowParams.setMargins(Utility.rowLeft, Utility.rowTop, 0, 0);
        layoutHandler.row.setLayoutParams(rowParams);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams rowImageViewParams = new RelativeLayout.LayoutParams(Utility.eventImageWidth, Utility.eventImageHeight);
        rowImageViewParams.setMargins(Utility.eventImageLeft, Utility.eventImageTop, 0, 0);
        layoutHandler.eventIconImageView.setLayoutParams(rowImageViewParams);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams rowTickImageViewParams = new RelativeLayout.LayoutParams(Utility.tickImageWidth, Utility.tickImageHeight);
        rowTickImageViewParams.setMargins(Utility.tickImageLeft, Utility.tickImageTop, 0, 0);
        layoutHandler.tickImageView.setLayoutParams(rowTickImageViewParams);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams rowNameViewParams = new RelativeLayout.LayoutParams(Utility.eventNameWidth, Utility.eventNameHeight);
        rowNameViewParams.setMargins(Utility.eventNameLeft, Utility.eventNameTop, 0, 0);
        layoutHandler.eventNameTextView.setLayoutParams(rowNameViewParams);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams rowDateViewParams = new RelativeLayout.LayoutParams(Utility.eventDateWidth, Utility.eventDateHeight);
        rowDateViewParams.setMargins(Utility.eventDateLeft, Utility.eventDateTop, 0, 0);
        layoutHandler.eventDateTextView.setLayoutParams(rowDateViewParams);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams rowDetailsViewParams = new RelativeLayout.LayoutParams(Utility.eventDetailsWidth, Utility.eventDetailsHeight);
        rowDetailsViewParams.setMargins(Utility.eventDetailsLeft, Utility.eventDetailsTop, 0, 0);
        layoutHandler.eventDetailsTextView.setLayoutParams(rowDetailsViewParams);
    }

    //------------------------------------------------------------------------------------------------------------------------

    public int addSorted(Object object)
    {
        int i;

        if(list.size() == 0)
        {
            list.add(object);
            notifyDataSetChanged();
            return 0;
        }

        if(((DataModel)object).getEventDateHash() <= ((DataModel)list.get(0)).getEventDateHash())
        {
            list.add(0, object);
            notifyDataSetChanged();
            return 0;
        }

        if(((DataModel)object).getEventDateHash() >= ((DataModel)list.get(list.size()-1)).getEventDateHash())
        {
            list.add(object);
            notifyDataSetChanged();
            return list.size()-1;
        }

        for(i=1;i<list.size();i++)
        {
            if(((DataModel)object).getEventDateHash() <= ((DataModel)list.get(i)).getEventDateHash())
            {
                list.add(i, object);
                notifyDataSetChanged();
                return i;
            }
        }

        return 0;
    }

    public int editSorted(int id, Object object)
    {
        int i;

        removeItemWithID(id);

        if(list.size() == 0)
        {
            list.add(object);
            notifyDataSetChanged();
            return 0;
        }

        if(((DataModel)object).getEventDateHash() <= ((DataModel)list.get(0)).getEventDateHash())
        {
            list.add(0, object);
            notifyDataSetChanged();
            return 0;
        }

        if(((DataModel)object).getEventDateHash() >= ((DataModel)list.get(list.size()-1)).getEventDateHash())
        {
            list.add(object);
            notifyDataSetChanged();
            return list.size()-1;
        }

        for(i=1;i<list.size();i++)
        {
            if(((DataModel)object).getEventDateHash() <= ((DataModel)list.get(i)).getEventDateHash())
            {
                list.add(i, object);
                notifyDataSetChanged();
                return i;
            }
        }

        return 0;
    }
}
