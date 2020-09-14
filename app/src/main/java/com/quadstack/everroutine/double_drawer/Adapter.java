package com.quadstack.everroutine.double_drawer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.days_to_remember_tasks_pool.DaysToRemember_TasksPool_Adapter;

import java.util.ArrayList;

/**
 * Created by ISHRAK on 11/29/2016.
 */
public class Adapter extends ArrayAdapter
{
    private LayoutHandler layoutHandler;
    private Context context;
    private ArrayList drawerItemList = new ArrayList();

    public Adapter(Context context, int resource) {
        super(context, resource);

        this.context = context;
    }

    static class LayoutHandler
    {
        RelativeLayout drawerContainer;
        ImageView shelfImageView, buttonImageView;
    }

    @Override
    public void add(Object object) {
        drawerItemList.add(object);
    }

    @Override
    public int getCount() {
        return drawerItemList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return drawerItemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowLayout = convertView;

        //holder-pattern start

        if(rowLayout == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowLayout = layoutInflater.inflate(R.layout.drawer_row, parent, false);

            layoutHandler = new LayoutHandler();

            layoutHandler.drawerContainer = (RelativeLayout)rowLayout.findViewById(R.id.drawerContainer);
            layoutHandler.buttonImageView = (ImageView)rowLayout.findViewById(R.id.drawerButtonImageView);
            layoutHandler.shelfImageView = (ImageView)rowLayout.findViewById(R.id.drawerShelfImageView);

            setRowDynamicLayout();

            rowLayout.setTag(layoutHandler);
        }
        else
        {
            layoutHandler = (LayoutHandler)rowLayout.getTag();
        }
        //holder-pattern end

        final DataModel drawerItem = (DataModel)this.getItem(position);

        layoutHandler.shelfImageView.setImageBitmap(Utility.decodeSampledBitmapFromResource(context.getResources(), drawerItem.getShelfImageResource(), Utility.shelfWidth, Utility.shelfHeight));
        layoutHandler.buttonImageView.setImageBitmap(Utility.decodeSampledBitmapFromResource(context.getResources(), drawerItem.getButtonImageResource(), Utility.drawerButtonWidth, Utility.drawerButtonHeight));

        return rowLayout;
    }

    private void setRowDynamicLayout()
    {
        layoutHandler.buttonImageView.getLayoutParams().width = Utility.drawerButtonWidth;
        layoutHandler.buttonImageView.getLayoutParams().height = Utility.drawerButtonHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.buttonImageView.getLayoutParams()).setMargins(Utility.drawerButtonLeft, Utility.drawerButtonTop, 0, 0);

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        layoutHandler.shelfImageView.getLayoutParams().width = Utility.shelfWidth;
        layoutHandler.shelfImageView.getLayoutParams().height = Utility.shelfHeight;
        ((RelativeLayout.LayoutParams)layoutHandler.shelfImageView.getLayoutParams()).setMargins(Utility.shelfLeft, Utility.shelfTop, 0, 0);
    }
}
