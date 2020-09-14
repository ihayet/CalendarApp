package com.quadstack.everroutine.double_drawer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.quadstack.everroutine.EverRoutine;
import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.about_us.AboutUsActivity;
import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.days_to_remember.DaysToRemember;
import com.quadstack.everroutine.statistics.activity_tracker;
import com.quadstack.everroutine.tasks.import_from_tasks_pool.ImportFromTasksPool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public abstract class DoubleDrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private LayoutInflater layoutInflater;

//    private String[] basicTools = {"Home", "Search", "Settings", "About"};
//    private String[] superTools = {"Power Find", "Stress Tracker", "Reshuffle"};

    private RelativeLayout contentLayout;
    private ListView drawerListViewBasicTools, drawerListViewSuperTools;

    private Adapter basicAdapter, superAdapter;

    private ImageButton navigationBtn;
    private ImageButton superToolsBtn;

    private PopupWindow settingsPopupWindow;
    private PopupWindow searchPopupWindow;
    private PopupWindow powerFindPopupWindow;
    private PopupWindow reshufflePopupWindow;
    private Button alarmToneBtn;
    private ImageButton searchBtn;
    private ImageButton aboutUsBtn;
    private String chosenAlarmTone;
    private RadioGroup radioCategoryGroup;
    private RadioButton radioCategoryButton;
    private String searchCategory;
    private String searchText;
    private EditText searchEditText;
    private TextView resultTextView;
    private EditText fromDateEditText,toDateEditText,fromTimeEditText,toTimeEditText;
    private Button searchClose, settingsClose, powerFindClose, reshuffleClose;
    private ImageButton importButton;

    private Utility.Time startTime,stopTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_drawer);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contentLayout = (RelativeLayout)findViewById(R.id.contentPlaceholder);
        drawerListViewBasicTools = (ListView)findViewById(R.id.leftDrawer);
        drawerListViewSuperTools = (ListView)findViewById(R.id.rightDrawer);

        basicAdapter = new Adapter(getApplicationContext(), R.layout.drawer_row);
        superAdapter = new Adapter(getApplicationContext(), R.layout.drawer_row);

        drawerListViewBasicTools.setAdapter(basicAdapter);
        drawerListViewSuperTools.setAdapter(superAdapter);

        drawerLayout.setScrimColor(Color.parseColor("#B7000000"));
    }

    private void setDrawerLayout()
    {
        drawerListViewBasicTools.getLayoutParams().width = Utility.drawerWidth;
        drawerListViewBasicTools.getLayoutParams().height = Utility.drawerHeight;
        ((DrawerLayout.LayoutParams)drawerListViewBasicTools.getLayoutParams()).topMargin = Utility.drawerTop;

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        drawerListViewSuperTools.getLayoutParams().width = Utility.drawerWidth;
        drawerListViewSuperTools.getLayoutParams().height = Utility.drawerHeight;
        ((DrawerLayout.LayoutParams)drawerListViewSuperTools.getLayoutParams()).topMargin = Utility.drawerTop;
    }

    private void addDrawerItems()
    {
        basicAdapter.add(new DataModel("Home", R.drawable.wooden_shelf, R.drawable.home, R.drawable.official_selected));
        basicAdapter.add(new DataModel("Search", R.drawable.wooden_shelf, R.drawable.search, R.drawable.official_selected));
        basicAdapter.add(new DataModel("Settings", R.drawable.wooden_shelf, R.drawable.settings, R.drawable.official_selected));
        basicAdapter.add(new DataModel("About", R.drawable.wooden_shelf, R.drawable.about, R.drawable.official_selected));

        superAdapter.add(new DataModel("Super Tools", R.drawable.metal_shelf, R.drawable.super_tools, R.drawable.official_selected));
        superAdapter.add(new DataModel("Power Find", R.drawable.metal_shelf, R.drawable.power_find, R.drawable.official_selected));
        superAdapter.add(new DataModel("Statistics", R.drawable.metal_shelf, R.drawable.statistics, R.drawable.official_selected));
        superAdapter.add(new DataModel("Reshuffle", R.drawable.metal_shelf, R.drawable.shuffle, R.drawable.official_selected));
    }

    private void addDrawerListener()
    {
        drawerListViewBasicTools.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(DoubleDrawerActivity.this, EverRoutine.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    case 1:
                        drawerLayout.closeDrawers();
                        showSearchPopupWindow();
                        break;
                    case 2:
                        drawerLayout.closeDrawers();
                        showSettingsPopupWindow();
                        break;
                    case 3:
                        Intent aboutIntent = new Intent(DoubleDrawerActivity.this, AboutUsActivity.class);
                        startActivity(aboutIntent);
                        drawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
            }
        });

        drawerListViewSuperTools.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        drawerLayout.closeDrawers();
                        showPowerFindPopupWindow();
                        break;
                    case 2:
                        Intent statistics_intent = new Intent(DoubleDrawerActivity.this, activity_tracker.class);
                        startActivity(statistics_intent);
                        drawerLayout.closeDrawers();
                        break;
                    case 3:
                        drawerLayout.closeDrawers();
                        showReshufflePopupWindow();
                    default:
                        break;
                }
            }
        });
    }

    protected View setContentLayout(int layoutResourceID, int customActionBarResourceID)
    {
        contentLayout.removeAllViews();   //to eliminate the memory retention issue

        View contentView = layoutInflater.inflate(layoutResourceID, contentLayout, false);

        contentLayout.addView(contentView);

        /*Custom ActionBar Initialization and Button Listeners*/

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(customActionBarResourceID);

        navigationBtn = (ImageButton)findViewById(R.id.navigationBtn);
        superToolsBtn = (ImageButton)findViewById(R.id.superToolsBtn);

        if(customActionBarResourceID == R.layout.custom_actionbar_layout_with_import)
        {
            importButton = (ImageButton)findViewById(R.id.importBtn);

            importButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(DoubleDrawerActivity.this, ImportFromTasksPool.class);
                    startActivity(intent);
                }
            });
        }

        setDrawerLayout();
        addDrawerItems();
        addDrawerListener();
        navigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDrawer(0);
            }
        });

        superToolsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDrawer(1);
            }
        });

        getSupportActionBar().getCustomView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout()
            {
                Utility.setDrawerTop(getApplicationContext(), getSupportActionBar().getHeight());
                setDrawerLayout();
            }
        });

        /*-------------------------------------------------------------------------------------------*/

        return contentView;
    }

    /*-----------------Settings Popup Window--------------------------------*/

    private void showSettingsPopupWindow()
    {
        try
        {
            //get instance of layout inflater
            LayoutInflater inflater = (LayoutInflater)DoubleDrawerActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.settings_popup_window,(ViewGroup)findViewById(R.id.settingsPopup));
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels-((int)(displaymetrics.heightPixels/4));
            int width = displaymetrics.widthPixels-100;
            settingsPopupWindow = new PopupWindow(layout,width,height,true);
            settingsPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

            settingsClose = (Button)layout.findViewById(R.id.settingsClose);
            settingsClose.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    settingsPopupWindow.dismiss();
                }
            });

            alarmToneBtn = (Button)layout.findViewById(R.id.alarmToneBtn);
            alarmToneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAlarmTone();
                }
            });

            aboutUsBtn = (ImageButton)layout.findViewById(R.id.aboutUsBtn);
            aboutUsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settingsPopupWindow.dismiss();
                    Intent aboutUs = new Intent(DoubleDrawerActivity.this, AboutUsActivity.class);
                    startActivity(aboutUs);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setAlarmTone()
    {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        //this.startActivityForResult(intent, 5);
        startActivityForResult(intent,5);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            String title = ringtone.getTitle(this);
            if (uri != null)
            {
                chosenAlarmTone = uri.toString();
                if(chosenAlarmTone!=null)
                {
                    Toast.makeText(getApplicationContext(),"Alarm Tone Changed to: "+title,Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                chosenAlarmTone = null;
            }
        }
    }

    /*---------------------------------------------------------------------------------------------*/

    /*-------------------------Search Popup Window--------------------------------------------------*/

    private void showSearchPopupWindow()
    {
        try
        {
            //get instance of layout inflater
            LayoutInflater inflater = (LayoutInflater)DoubleDrawerActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.search_popup_window,(ViewGroup)findViewById(R.id.searchPopup));
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels-((int)(displaymetrics.heightPixels/6));
            int width = displaymetrics.widthPixels-100;
            searchPopupWindow = new PopupWindow(layout,width,height,true);
            searchPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

            final ListView listView = (ListView)layout.findViewById(R.id.daysToRememberTasksPoolSearchListView);
            final com.quadstack.everroutine.days_to_remember.Adapter daysToRememberSearchAdapter = new com.quadstack.everroutine.days_to_remember.Adapter(getApplicationContext(), R.layout.days_to_remember_row);
            final com.quadstack.everroutine.tasks_pool.Adapter tasksPoolSearchAdapter = new com.quadstack.everroutine.tasks_pool.Adapter(getApplicationContext(), R.layout.tasks_pool_row);

            searchClose = (Button)layout.findViewById(R.id.searchClose);
            searchClose.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    searchPopupWindow.dismiss();
                }
            });
            searchBtn = (ImageButton)layout.findViewById(R.id.searchBtn);
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daysToRememberSearchAdapter.removeAll();
                    tasksPoolSearchAdapter.removeAll();

                    radioCategoryGroup = (RadioGroup)layout.findViewById(R.id.radioCategory);
                    int selectedId = radioCategoryGroup.getCheckedRadioButtonId();
                    radioCategoryButton = (RadioButton)layout.findViewById(selectedId);
                    searchCategory = radioCategoryButton.getText().toString();
                    searchEditText = (EditText)layout.findViewById(R.id.searchEditText);
                    searchText = searchEditText.getText().toString();
                    resultTextView = (TextView)layout.findViewById(R.id.noResultTextView);

                    if(searchCategory.equals("Days To Remember")){
                        searchCategory = "DaysToRemember";
                    }
                    else {
                        searchCategory = "Tasks";
                    }

                    DBModule dbModule = new DBModule(getApplicationContext());

                    Cursor cursor = dbModule.searchDB(searchText,searchCategory);

                    if(cursor.getCount()==0){
                        resultTextView.setVisibility(View.VISIBLE);
                        resultTextView.setText("No Results Found");
                        /*daysToRememberSearchAdapter.clear();
                        tasksPoolSearchAdapter.clear();*/
                    }

                    if(searchCategory.equals("DaysToRemember")){
                        listView.setAdapter(daysToRememberSearchAdapter);
                        int id, dateRef, dateHash, eventCategory, eventImgResource;
                        String eventName, eventDetails;
                        if(cursor.moveToFirst())
                        {
                            resultTextView.setVisibility(View.INVISIBLE);
                            resultTextView.setText("");
                            do
                            {
                                id = cursor.getInt(cursor.getColumnIndex(DBMeta.DaysToRemember.id));
                                dateRef  = cursor.getInt(cursor.getColumnIndex(DBMeta.DaysToRemember.event_date_ref));
                                eventCategory = cursor.getInt(cursor.getColumnIndex(DBMeta.DaysToRemember.event_category));
                                eventName = cursor.getString(cursor.getColumnIndex(DBMeta.DaysToRemember.event_name));
                                eventDetails = cursor.getString(cursor.getColumnIndex(DBMeta.DaysToRemember.event_details));
                                dateHash = cursor.getInt(cursor.getColumnIndex(DBMeta.DateRef.date_hash));

                                eventImgResource = Utility.getImgResource(eventCategory);

                                daysToRememberSearchAdapter.add(new com.quadstack.everroutine.days_to_remember.DataModel(id, eventName, eventCategory, (String)Utility.dateRefCache.get(dateRef), dateHash, eventDetails, eventImgResource));
                            }
                            while(cursor.moveToNext());
                        }
                    }
                    else {
                        listView.setAdapter(tasksPoolSearchAdapter);
                        int id, category, priority, alarm, taskImgResource;
                        String task;
                        if(cursor.moveToFirst())
                        {
                            resultTextView.setVisibility(View.INVISIBLE);
                            resultTextView.setText("");
                            do
                            {
                                id = cursor.getInt(cursor.getColumnIndex(DBMeta.Tasks.id));
                                task  = cursor.getString(cursor.getColumnIndex(DBMeta.TasksPool.task_name));
                                category = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.task_category));
                                priority = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.task_priority));
                                alarm = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.task_alarm));

                                taskImgResource = Utility.getImgResource(category);

                                tasksPoolSearchAdapter.add(new com.quadstack.everroutine.tasks_pool.DataModel(id, task, category, priority, (alarm==0?false:true), taskImgResource));
                            }
                            while(cursor.moveToNext());
                        }
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*---------------------------------------------------------------------------------------------------*/

    /*----------------------------------Power Find Popup Window------------------------------------------*/

    private void showReshufflePopupWindow(){
        try {
            //get instance of layout inflater
            LayoutInflater inflater = (LayoutInflater)DoubleDrawerActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.reshuffle_popup_window,(ViewGroup)findViewById(R.id.reshufflePopup));
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels-((int)(displaymetrics.heightPixels/2));
            int width = displaymetrics.widthPixels-100;
            reshufflePopupWindow = new PopupWindow(layout,width,height,true);
            reshufflePopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

            reshuffleClose = (Button)layout.findViewById(R.id.reshuffleClose);
            reshuffleClose.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    reshufflePopupWindow.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*---------------------------------------------------------------------------------------------------*/

    private void showPowerFindPopupWindow(){
        try{
            //get instance of layout inflater
            LayoutInflater inflater = (LayoutInflater)DoubleDrawerActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.power_find_popup_window,(ViewGroup)findViewById(R.id.powerFindPopup));
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels-((int)(displaymetrics.heightPixels/8));
            int width = displaymetrics.widthPixels-100;
            powerFindPopupWindow = new PopupWindow(layout,width,height,true);
            powerFindPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

            powerFindClose = (Button)layout.findViewById(R.id.powerFindClose);
            powerFindClose.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    powerFindPopupWindow.dismiss();
                }
            });

            fromDateEditText = (EditText)layout.findViewById(R.id.fromDateET);
            toDateEditText = (EditText)layout.findViewById(R.id.toDateET);
            fromTimeEditText = (EditText)layout.findViewById(R.id.fromTimeET);
            toTimeEditText = (EditText)layout.findViewById(R.id.toTimeET);

            fromDateEditText.setInputType(InputType.TYPE_NULL);
            fromDateEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar myCalendar = Calendar.getInstance();

                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
                    {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String myDateFormat = "dd-MMM-yyyy";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myDateFormat, Locale.US);

                            fromDateEditText.setText(simpleDateFormat.format(myCalendar.getTime()));
                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(DoubleDrawerActivity.this, dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });

            toDateEditText.setInputType(InputType.TYPE_NULL);
            toDateEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar myCalendar = Calendar.getInstance();

                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
                    {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String myDateFormat = "dd-MMM-yyyy";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myDateFormat, Locale.US);

                            toDateEditText.setText(simpleDateFormat.format(myCalendar.getTime()));
                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(DoubleDrawerActivity.this, dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });

            fromTimeEditText.setInputType(InputType.TYPE_NULL);
            fromTimeEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initTimeDialog(Utility.start_time);
                }
            });

            toTimeEditText.setInputType(InputType.TYPE_NULL);
            toTimeEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initTimeDialog(Utility.stop_time);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initTimeDialog(int startStopTime)
    {
        final int startOrStop = startStopTime;

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(DoubleDrawerActivity.this, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                if(hourOfDay < 12)
                {
                    if(startOrStop == Utility.start_time)
                    {
                        //startTime.setTime((hourOfDay==0?12:hourOfDay), minute, Utility.am);
                        fromTimeEditText.setText((Integer.toString(hourOfDay==0?12:hourOfDay)+":"+Integer.toString(minute)+" AM"));
                    }
                    else
                    {
                        //stopTime.setTime(hourOfDay, minute, Utility.am);
                        toTimeEditText.setText((Integer.toString(hourOfDay==0?12:hourOfDay)+":"+Integer.toString(minute)+" AM"));
                    }
                }
                else
                {
                    if(startOrStop == Utility.start_time)
                    {
                        //startTime.setTime((hourOfDay==12?hourOfDay:hourOfDay-12), minute, Utility.pm);
                        fromTimeEditText.setText((Integer.toString(hourOfDay==12?hourOfDay:hourOfDay-12)+":"+Integer.toString(minute)+" PM"));
                    }
                    else
                    {
                        //stopTime.setTime((hourOfDay==12?hourOfDay:hourOfDay-12), minute, Utility.pm);
                        toTimeEditText.setText((Integer.toString(hourOfDay==12?hourOfDay:hourOfDay-12)+":"+Integer.toString(minute)+" PM"));
                    }
                }
            }
        }, hour, minute, false);

        timePicker.setTitle((startStopTime==Utility.start_time?"Select starting time":"Select stopping time"));
        timePicker.show();
    }

    /*--------------------------Drawer Slider on ActionBar Button Click----------------------------------*/

    private void showDrawer(int i){
        if(i==0){
            drawerLayout.openDrawer(Gravity.LEFT);
        }
        else if(i==1){
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    /*------------------------------------------------------------------------------------------------------*/

    protected abstract void setDynamicLayout();
    protected abstract void addListeners();

}
