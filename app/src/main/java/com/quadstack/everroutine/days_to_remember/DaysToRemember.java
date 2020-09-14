package com.quadstack.everroutine.days_to_remember;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.days_to_remember_tasks_pool.DaysToRemember_TasksPool_Activity;

import java.util.Date;

public class DaysToRemember extends DaysToRemember_TasksPool_Activity
{
    private View daysToRememberView;
    private static Adapter daysToRememberAdapter;
    private DBModule dbModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daysToRememberAdapter = new Adapter(DaysToRemember.this, R.layout.days_to_remember_row);

        daysToRememberView = super.setContextAndListAdapter(DaysToRemember.this, "DaysToRemember", daysToRememberAdapter);

        dbModule = new DBModule(getApplicationContext());

        addData();

        setSpecificDynamicLayout();
        addSpecificListeners(); //Common listeners (deleteButton, deleteAllButton) are added from the super class

        listView.post(new Runnable()
        {
            @Override
            public void run() {
                listView.smoothScrollToPosition(getCurrentDatePosition());
            }
        });
    }

    //----------------------------------------------------------------------------------------------------------------------------

    private void addData()
    {
        int id, dateRef, dateHash, eventCategory, eventImgResource;
        String eventName, eventDetails;

        Cursor cursor = dbModule.getDaysToRemember();

        if(cursor.moveToFirst())
        {
            do
            {
                id = cursor.getInt(cursor.getColumnIndex(DBMeta.DaysToRemember.id));
                dateRef  = cursor.getInt(cursor.getColumnIndex(DBMeta.DaysToRemember.event_date_ref));
                eventCategory = cursor.getInt(cursor.getColumnIndex(DBMeta.DaysToRemember.event_category));
                eventName = cursor.getString(cursor.getColumnIndex(DBMeta.DaysToRemember.event_name));
                eventDetails = cursor.getString(cursor.getColumnIndex(DBMeta.DaysToRemember.event_details));
                dateHash = cursor.getInt(cursor.getColumnIndex(DBMeta.DateRef.date_hash));

                eventImgResource = Utility.getEventImgResource(eventCategory);

                daysToRememberAdapter.add(new DataModel(id, eventName, eventCategory, (String)Utility.dateRefCache.get(dateRef), dateHash, eventDetails, eventImgResource));
            }
            while(cursor.moveToNext());
        }
    }

    private int getCurrentDatePosition()
    {
        int i;

        String date = Utility.dateFormat.format(new Date());
        int dateHash = Utility.calculateDateHash(date);

        if(daysToRememberAdapter.getCount() != 0)
        {
            if(((DataModel)daysToRememberAdapter.getItem(daysToRememberAdapter.getCount()-1)).getEventDateHash() < dateHash)
            {
                return daysToRememberAdapter.getCount() - 1;
            }

            for(i=1;i<daysToRememberAdapter.getCount()-1;i++)
            {
                if(((DataModel)daysToRememberAdapter.getItem(i)).getEventDateHash() < dateHash && ((DataModel)daysToRememberAdapter.getItem(i+1)).getEventDateHash() > dateHash)
                {
                    return i+1;
                }
            }
        }

        return 0;
    }

    private void addDummyData()
    {
        daysToRememberAdapter.add(new DataModel(1, "1", 0, "B", 0, "C", R.drawable.official));
        daysToRememberAdapter.add(new DataModel(2, "2", 1, "E", 1, "F", R.drawable.academic));
        daysToRememberAdapter.add(new DataModel(3, "3", 2, "B", 2, "C", R.drawable.miscellaneous));
        daysToRememberAdapter.add(new DataModel(4, "4", 0, "E", 3, "F", R.drawable.combined));
        daysToRememberAdapter.add(new DataModel(5, "5", 1, "B", 4, "C", R.drawable.days_to_remember));
        daysToRememberAdapter.add(new DataModel(6, "6", 2, "E", 5, "F", R.drawable.tasks));
        daysToRememberAdapter.add(new DataModel(7, "7", 0, "B", 6, "C", R.drawable.official));
        daysToRememberAdapter.add(new DataModel(8, "8", 1, "E", 7, "F", R.drawable.academic));
        daysToRememberAdapter.add(new DataModel(9, "9", 2, "B", 8, "C", R.drawable.miscellaneous));
        daysToRememberAdapter.add(new DataModel(10, "10", 0, "E", 9, "F", R.drawable.combined));
        daysToRememberAdapter.add(new DataModel(11, "11", 1, "B", 10, "C", R.drawable.days_to_remember));
        daysToRememberAdapter.add(new DataModel(12, "12", 2, "E", 11, "F", R.drawable.tasks));
    }

    //-----------------------------------------------------------------------------------------------------------------------

    private void setSpecificDynamicLayout()
    {
        title.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_title, Utility.titleWidth, Utility.titleHeight));
    }

    private void addSpecificListeners()
    {
        addButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    addButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_add_selected, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    addButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_add, Utility.daysToRememberButtonDiameter, Utility.daysToRememberButtonDiameter));

                    Intent addEditIntent = new Intent(DaysToRemember.this, DaysToRememberAddEdit.class);

                    addEditIntent.putExtra("activity_type", "add");

                    startActivity(addEditIntent);

                    return true;
                }

                return false;
            }
        });

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(longPressSelectMode == true)
                {
                    showTick(position);
                }
                else
                {
                    DataModel event = (DataModel)daysToRememberAdapter.getItem(position);

                    Intent addEditIntent = new Intent(DaysToRemember.this, DaysToRememberAddEdit.class);

                    addEditIntent.putExtra("activity_type", "edit");
                    addEditIntent.putExtra("event_id", event.getItemID());
                    addEditIntent.putExtra("event_name", event.getItemName());
                    addEditIntent.putExtra("event_date", event.getEventDate());
                    addEditIntent.putExtra("event_category", event.getItemCategory());
                    addEditIntent.putExtra("event_details", event.getEventDetails());

                    startActivity(addEditIntent);
                }
            }
        });

        listView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                longPressSelectMode = true;

                showTick(position);

                vibrator.vibrate(500);

                return true;
            }
        });
    }

    //----------------------------------------------------------------------------------------------------------------------------

    //to be used by DaysToRememberAddEdit
    public static void addToAdapter(int id, String eventName, int eventCategory, String date, String eventDetails)
    {
        int eventImgResource = Utility.getEventImgResource(eventCategory);

        final int position = daysToRememberAdapter.addSorted(new DataModel(id, eventName, eventCategory, date, Utility.calculateDateHash(date), eventDetails, eventImgResource));

        listView.post(new Runnable()
        {
            @Override
            public void run() {
                listView.smoothScrollToPosition(position);
            }
        });
    }

    //to be used by DaysToRememberAddEdit
    public static void editInAdapter(int id, String eventName, int eventCategory, String date, String eventDetails)
    {
        int eventImgResource = Utility.getImgResource(eventCategory);

        final int position = daysToRememberAdapter.editSorted(id, new DataModel(id, eventName, eventCategory, date, Utility.calculateDateHash(date), eventDetails, eventImgResource));

        listView.post(new Runnable()
        {
            @Override
            public void run() {
                listView.smoothScrollToPosition(position);
            }
        });
    }

    //to be used by DaysToRememberAddEdit
    public static void removeFromAdapter(int id)
    {
        daysToRememberAdapter.removeItemWithID(id);
    }
}
