package com.quadstack.everroutine.days_to_remember_tasks_pool;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;

import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.double_drawer.DoubleDrawerActivity;
import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;

import java.util.ArrayList;
import java.util.List;

public abstract class DaysToRemember_TasksPool_Activity extends DoubleDrawerActivity
{
    protected RelativeLayout layout;
    protected View view;

    protected Context context;

    protected ImageView title, addButton, addButtonLabel, deleteButton, deleteButtonLabel, deleteAllButton, deleteAllButtonLabel;
    protected TextView listEmpty;

    protected static ListView listView;
    protected DaysToRemember_TasksPool_Adapter listAdapter;

    protected static boolean changeOccurred;
    protected static List selectedIDList;

    protected boolean longPressSelectMode;

    protected Vibrator vibrator;

    private String activityName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = super.setContentLayout(R.layout.activity_days_to_remember_tasks_pool, R.layout.custom_actionbar_layout);

        layout = (RelativeLayout)view.findViewById(R.id.daysToRememberTasksPoolLayout);

        title = (ImageView)view.findViewById(R.id.daysToRememberTasksPoolTitle);

        listView = (ListView)view.findViewById(R.id.daysToRememberTasksPoolListView);

        addButton = (ImageView)view.findViewById(R.id.daysToRememberTasksPoolAdd);
        addButtonLabel = (ImageView)view.findViewById(R.id.daysToRememberTasksPoolAddLabel);
        deleteButton = (ImageView)view.findViewById(R.id.daysToRememberTasksPoolDelete);
        deleteButtonLabel = (ImageView)view.findViewById(R.id.daysToRememberTasksPoolDeleteLabel);
        deleteAllButton = (ImageView)view.findViewById(R.id.daysToRememberTasksPoolDeleteAll);
        deleteAllButtonLabel = (ImageView)view.findViewById(R.id.daysToRememberTasksPoolDeleteAllLabel);

        listEmpty = (TextView)view.findViewById(R.id.daysToRememberTasksPoolListEmpty);
        //list adapter will be set from the subclass

        setDynamicLayout();

        addListeners();

        selectedIDList = new ArrayList();

        longPressSelectMode = false;

        vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    protected View setContextAndListAdapter(Context context, String activityName, DaysToRemember_TasksPool_Adapter listAdapter)
    {
        this.context = context;

        this.activityName = activityName;

        this.listAdapter = listAdapter;
        listView.setAdapter(this.listAdapter);

        checkEmptyList();

        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkEmptyList();
    }

    @Override
    public void onBackPressed()
    {

        if(longPressSelectMode == false)
        {
            if(changeOccurred == true)
            {
                //delete/update/add event entries from the database corresponding to the selectedIDList
            }

            super.onBackPressed();
        }
        else
        {
            for(Object i : selectedIDList)
            {
                ((DaysToRemember_TasksPool_DataModel)listAdapter.getItem((int)i)).setTickImgResource(0);
            }

            selectedIDList.removeAll(selectedIDList);

            listAdapter.notifyDataSetChanged();

            longPressSelectMode = false;
        }
    }

    @Override
    protected void addListeners()
    {
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
                        Toast.makeText(getApplicationContext(), "No items selected", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        new AlertDialog.Builder(context)
                                .setTitle("Confirm Deletion").setMessage("Are you sure you want to delete " + selectedIDList.size() + " items?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(activityName.contains("DaysToRemember"))
                                        {
                                            new DBModule(context).removeMultipleItems(DBMeta.DaysToRemember.table_name, DBMeta.DaysToRemember.id, getEventIDFromListID());
                                        }
                                        else if(activityName.contains("TasksPool"))
                                        {
                                            new DBModule(context).removeMultipleItems(DBMeta.TasksPool.table_name, DBMeta.TasksPool.id, getEventIDFromListID());
                                        }

                                        listAdapter.removeMultipleElements(selectedIDList);

                                        longPressSelectMode = false;

                                        checkEmptyList();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {

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
                        Toast.makeText(getApplicationContext(), "The list is empty", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        new AlertDialog.Builder(context)
                                .setTitle("Confirm Deletion").setMessage("Are you sure you want to delete all the items?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(activityName.contains("DaysToRemember"))
                                        {
                                            new DBModule(context).removeAllItems(DBMeta.DaysToRemember.table_name);
                                        }
                                        else if(activityName.contains("TasksPool"))
                                        {
                                            new DBModule(context).removeAllItems(DBMeta.TasksPool.table_name);
                                        }

                                        listAdapter.removeAll();

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

    @Override
    protected void setDynamicLayout()
    {
        title.getLayoutParams().width = Utility.titleWidth;
        title.getLayoutParams().height = Utility.titleHeight;
        ((RelativeLayout.LayoutParams)title.getLayoutParams()).setMargins(0, 0, 0, 0);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        listView.getLayoutParams().width = Utility.screenWidth;
        listView.getLayoutParams().height = Utility.daysToRememberListViewHeight;
        ((RelativeLayout.LayoutParams)listView.getLayoutParams()).setMargins(0, Utility.daysToRememberListViewTop, 0, Utility.daysToRememberListViewBottom);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        addButton.getLayoutParams().width = Utility.daysToRememberButtonDiameter;
        addButton.getLayoutParams().height = Utility.daysToRememberButtonDiameter;
        ((RelativeLayout.LayoutParams)addButton.getLayoutParams()).setMargins(Utility.daysToRememberAddLeft, Utility.daysToRememberAddTop, 0, 2);

        addButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_add, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

        addButtonLabel.getLayoutParams().width = Utility.daysToRememberButtonLabelWidth;
        addButtonLabel.getLayoutParams().height = Utility.daysToRememberButtonLabelHeight;
        ((RelativeLayout.LayoutParams)addButtonLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddLabelLeft, Utility.daysToRememberAddLabelTop, 0, 0);

        addButtonLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_add_label, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        deleteButton.getLayoutParams().width = Utility.daysToRememberButtonDiameter;
        deleteButton.getLayoutParams().height = Utility.daysToRememberButtonDiameter;
        ((RelativeLayout.LayoutParams)deleteButton.getLayoutParams()).setMargins(Utility.daysToRememberDeleteLeft, Utility.daysToRememberAddTop, 0, 2);

        deleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

        deleteButtonLabel.getLayoutParams().width = Utility.daysToRememberButtonLabelWidth;
        deleteButtonLabel.getLayoutParams().height = Utility.daysToRememberButtonLabelHeight;
        ((RelativeLayout.LayoutParams)deleteButtonLabel.getLayoutParams()).setMargins(Utility.daysToRememberDeleteLabelLeft, Utility.daysToRememberAddLabelTop, 0, 0);

        deleteButtonLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete_label, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        deleteAllButton.getLayoutParams().width = Utility.daysToRememberButtonDiameter;
        deleteAllButton.getLayoutParams().height = Utility.daysToRememberButtonDiameter;
        ((RelativeLayout.LayoutParams)deleteAllButton.getLayoutParams()).setMargins(Utility.daysToRememberDeleteAllLeft, Utility.daysToRememberAddTop, 0, 2);

        deleteAllButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete_all, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

        deleteAllButtonLabel.getLayoutParams().width = Utility.daysToRememberButtonLabelWidth;
        deleteAllButtonLabel.getLayoutParams().height = Utility.daysToRememberButtonLabelHeight;
        ((RelativeLayout.LayoutParams)deleteAllButtonLabel.getLayoutParams()).setMargins(Utility.daysToRememberDeleteAllLabelLeft, Utility.daysToRememberAddLabelTop, 0, 0);

        deleteAllButtonLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_delete_all_label, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        listEmpty.getLayoutParams().width = Utility.rowWidth;
        listEmpty.getLayoutParams().height = Utility.rowHeight;
        ((RelativeLayout.LayoutParams)listEmpty.getLayoutParams()).setMargins(Utility.rowLeft, ((Utility.screenHeight/2) - Utility.rowHeight), 0, 0);

        listEmpty.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.rowWidth, Utility.rowHeight)));
        listEmpty.setPadding(100, 0, 100, 0);
        listEmpty.setTextSize(Utility.fontSize);
    }

    protected void showTick(int position)
    {
        if(((DaysToRemember_TasksPool_DataModel)listAdapter.getItem(position)).getTickImgResource() == 0)
        {
            ((DaysToRemember_TasksPool_DataModel)listAdapter.getItem(position)).setTickImgResource(R.drawable.green_tick);
            selectedIDList.add(position);
        }
        else
        {
            ((DaysToRemember_TasksPool_DataModel)listAdapter.getItem(position)).setTickImgResource(0);
            selectedIDList.remove((Object)position);

            if(selectedIDList.isEmpty())
            {
                longPressSelectMode = false;
            }
        }

        listAdapter.notifyDataSetChanged();
    }

    protected void checkEmptyList()
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

    protected static void setChangeOccurred(boolean change)
    {
        changeOccurred = change;
    }
    protected static boolean getChangeOccurred()
    {
        return changeOccurred;
    }

    protected static void addToSelectedIDList(int eventID)
    {
        selectedIDList.add(eventID);
    }
    protected static List getSelectedIDList()
    {
        return selectedIDList;
    }

    //-----------------------------------------------------------------------------------------------------------------------

    private List getEventIDFromListID()                             //For both events and tasks
    {
        List eventIDList = new ArrayList();

        for(Object itemID : selectedIDList)
        {
            eventIDList.add(((DaysToRemember_TasksPool_DataModel)listAdapter.getItem((int)itemID)).getItemID());
        }

        return eventIDList;
    }
}
