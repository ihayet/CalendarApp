package com.quadstack.everroutine.tasks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.days_to_remember.DaysToRememberAddEdit;
import com.quadstack.everroutine.double_drawer.DoubleDrawerActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskAddEdit extends DoubleDrawerActivity
{
    private View taskAddEditView;

    private ImageView taskAddEditTitle, taskAddEditNameLabel, taskAddEditCategoryLabel, taskAddEditPriorityLabel, taskAddEditAlarmLabel;
    private static EditText taskAddEditName, taskAddEditCategory;
    private RelativeLayout taskAddEditPriorityLayout, taskAddEditAlarmLayout;
    private TextView taskAddEditPriorityLow, taskAddEditPriorityMedium, taskAddEditPriorityHigh, taskAddEditAlarmTextView;
    private static SeekBar taskAddEditPriority;
    private static Switch taskAddEditAlarm;

    private RelativeLayout taskAddEditRecursiveLayout;
    private TextView taskAddEditRecursiveText;
    private Switch taskAddEditRecursive;
    private EditText taskAddEditDate, taskAddEditDay, taskAddEditStartTime, taskAddEditStopTime;

    private ImageView taskAddEditRecursiveLabel, taskAddEditDayDateLabel, taskAddEditStartTimeLabel, taskAddEditStopTimeLabel;

    private ImageView taskAddEditSaveReturnButton, taskAddEditSaveMoreButton, taskAddEditSaveButton, taskAddEditDeleteButton;

    private ListView taskAddEditCategoryPopupListView, taskAddEditDayPopupListView;
    private PopupWindow taskAddEditCategoryPopupWindow, taskAddEditDayPopupWindow;

    private static ArrayList<String> taskAddEditCategoryPopupListViewArray = new ArrayList<String>(){{add("Official"); add("Academic"); add("Miscellaneous");}};
    private ArrayList<String> taskAddEditDayPopupListViewArray = new ArrayList<String>(){{add("Sun"); add("Mon"); add("Tue"); add("Wed"); add("Thu"); add("Fri"); add("Sat");}};

    private ArrayAdapter<String> taskAddEditCategoryPopupListViewAdapter, taskAddEditDayPopupListViewAdapter;

    private Calendar calendar;

    private Utility.Time startTime, stopTime;

    private int taskAddEditTaskID;
    private int taskAddEditTasksPoolRef;

    private String editTaskName, editTaskCategory;
    private int editTaskPriority;
    private boolean editTaskAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        taskAddEditView = super.setContentLayout(R.layout.activity_task_add_edit, R.layout.custom_actionbar_layout_with_import);

        taskAddEditTitle = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditTitle);
        taskAddEditNameLabel = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditNameLabel);
        taskAddEditCategoryLabel = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditCategoryLabel);
        taskAddEditPriorityLabel = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditPriorityLabel);
        taskAddEditAlarmLabel = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditAlarmLabel);

        taskAddEditName = (EditText)taskAddEditView.findViewById(R.id.taskAddEditName);
        taskAddEditCategory = (EditText)taskAddEditView.findViewById(R.id.taskAddEditCategory);

        taskAddEditAlarmLayout = (RelativeLayout)taskAddEditView.findViewById(R.id.taskAddEditAlarmLayout);
        taskAddEditPriorityLayout = (RelativeLayout)taskAddEditView.findViewById(R.id.taskAddEditPriorityLayout);

        taskAddEditPriorityLow = (TextView)taskAddEditView.findViewById(R.id.taskAddEditPriorityLow);
        taskAddEditPriorityMedium = (TextView)taskAddEditView.findViewById(R.id.taskAddEditPriorityMedium);
        taskAddEditPriorityHigh = (TextView)taskAddEditView.findViewById(R.id.taskAddEditPriorityHigh);
        taskAddEditAlarmTextView = (TextView)taskAddEditView.findViewById(R.id.taskAddEditAlarmTextView);

        taskAddEditPriority = (SeekBar)taskAddEditView.findViewById(R.id.taskAddEditPriority);
        taskAddEditAlarm = (Switch)taskAddEditView.findViewById(R.id.taskAddEditAlarm);

        taskAddEditRecursiveLayout = (RelativeLayout)taskAddEditView.findViewById(R.id.taskAddEditRecursiveLayout);
        taskAddEditRecursiveText = (TextView)taskAddEditView.findViewById(R.id.taskAddEditRecursiveTextView);
        taskAddEditRecursive = (Switch)taskAddEditView.findViewById(R.id.taskAddEditRecursive);

        taskAddEditDate = (EditText)taskAddEditView.findViewById(R.id.taskAddEditDate);
        taskAddEditDay = (EditText)taskAddEditView.findViewById(R.id.taskAddEditDay);
        taskAddEditStartTime = (EditText)taskAddEditView.findViewById(R.id.taskAddEditStartTime);
        taskAddEditStopTime = (EditText)taskAddEditView.findViewById(R.id.taskAddEditStopTime);

        taskAddEditRecursiveLabel = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditRecursiveLabel);
        taskAddEditDayDateLabel = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditDayDateLabel);
        taskAddEditStartTimeLabel = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditStartTimeLabel);
        taskAddEditStopTimeLabel = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditStopTimeLabel);

        taskAddEditSaveReturnButton = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditSaveReturnButton);
        taskAddEditSaveMoreButton = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditSaveMoreButton);
        taskAddEditSaveButton = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditSaveButton);
        taskAddEditDeleteButton = (ImageView)taskAddEditView.findViewById(R.id.taskAddEditDeleteButton);

        setDynamicLayout();
        addListeners();

        calendar = Calendar.getInstance();

        startTime = (new Utility.Time());
        stopTime = (new Utility.Time());

        Bundle extras = getIntent().getExtras();

        if(extras.getString("activity_type").contains("add"))
        {
            setAddDynamicLayout();
        }
        else
        {
            setEditDynamicLayout(extras);
        }
    }

    @Override
    protected void setDynamicLayout()
    {
        taskAddEditTitle.getLayoutParams().width = Utility.titleWidth;
        taskAddEditTitle.getLayoutParams().height = Utility.titleHeight;
        ((RelativeLayout.LayoutParams)taskAddEditTitle.getLayoutParams()).setMargins(0, 0, 0, 0);

        taskAddEditTitle.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), TabActivity.titleResource, Utility.titleWidth, Utility.titleHeight));

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        taskAddEditNameLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        taskAddEditNameLabel.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)taskAddEditNameLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditNameLabelTop, 0, 0);

        taskAddEditNameLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_name_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        taskAddEditName.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        taskAddEditName.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)taskAddEditName.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditNameTop, 0, 0);
        taskAddEditName.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        taskAddEditName.setTextSize(Utility.fontSize);

        taskAddEditName.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditNameHeight)));

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        taskAddEditCategoryLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        taskAddEditCategoryLabel.getLayoutParams().height =Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)taskAddEditCategoryLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditDateLabelTop, 0, 0);

        taskAddEditCategoryLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_category_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        taskAddEditCategory.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        taskAddEditCategory.getLayoutParams().height = Utility.daysToRememberAddEditCategoryHeight;
        ((RelativeLayout.LayoutParams)taskAddEditCategory.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditDateTop, 0, 0);
        taskAddEditCategory.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        taskAddEditCategory.setTextSize(Utility.fontSize);

        taskAddEditCategory.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDateHeight)));

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        taskAddEditPriorityLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        taskAddEditPriorityLabel.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)taskAddEditPriorityLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditCategoryLabelTop, 0, 0);

        taskAddEditPriorityLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_priority_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        taskAddEditPriorityLayout.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        taskAddEditPriorityLayout.getLayoutParams().height = Utility.daysToRememberAddEditCategoryHeight;
        ((RelativeLayout.LayoutParams)taskAddEditPriorityLayout.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditCategoryTop, 0, 0);
        taskAddEditPriority.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);

        taskAddEditPriorityLayout.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditCategoryHeight)));

        ((RelativeLayout.LayoutParams)taskAddEditPriorityLow.getLayoutParams()).topMargin = Utility.daysToRememberAddEditLabelHeight;
        taskAddEditPriorityLow.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, 0, 0);
        taskAddEditPriorityHigh.setPadding(0, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);

        taskAddEditPriorityLow.setTextSize(15);
        taskAddEditPriorityMedium.setTextSize(22);
        taskAddEditPriorityHigh.setTextSize(15);

        taskAddEditPriority.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        taskAddEditAlarmLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        taskAddEditAlarmLabel.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)taskAddEditAlarmLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditDetailsLabelTop, 0, 0);

        taskAddEditAlarmLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_alarm_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        taskAddEditAlarmLayout.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        taskAddEditAlarmLayout.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)taskAddEditAlarmLayout.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditDetailsTop, 0, 0);
        taskAddEditAlarmLayout.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);

        taskAddEditAlarmLayout.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDetailsHeight)));

        taskAddEditAlarmTextView.setPadding((Utility.daysToRememberAddEditTornPaperPaddingSide/3), 0, 0, 0);
        taskAddEditAlarmTextView.setTextSize(22);

        ((RelativeLayout.LayoutParams)taskAddEditAlarmTextView.getLayoutParams()).topMargin = (Utility.daysToRememberAddEditNameHeight/2) - 30;

        taskAddEditAlarm.setRight(Utility.daysToRememberAddEditTornPaperPaddingSide);
        taskAddEditAlarm.setPadding(0, 0, (Utility.daysToRememberAddEditTornPaperPaddingSide/3), 0);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        taskAddEditRecursiveLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        taskAddEditRecursiveLabel.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)taskAddEditRecursiveLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.taskAddEditRecursiveLabelTop, 0, 0);

        taskAddEditRecursiveLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.task_repeat, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        taskAddEditRecursiveLayout.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        taskAddEditRecursiveLayout.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)taskAddEditRecursiveLayout.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.taskAddEditRecursiveTop, 0, 0);
        taskAddEditRecursiveLayout.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);

        taskAddEditRecursiveLayout.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDetailsHeight)));

        taskAddEditRecursiveText.setPadding((Utility.daysToRememberAddEditTornPaperPaddingSide/3), 0, 0, 0);
        taskAddEditRecursiveText.setTextSize(22);

        ((RelativeLayout.LayoutParams)taskAddEditRecursiveText.getLayoutParams()).topMargin = (Utility.daysToRememberAddEditNameHeight/2) - 30;

        taskAddEditRecursive.setRight(Utility.daysToRememberAddEditTornPaperPaddingSide);
        taskAddEditRecursive.setPadding(0, 0, (Utility.daysToRememberAddEditTornPaperPaddingSide/3), 0);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        taskAddEditDayDateLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        taskAddEditDayDateLabel.getLayoutParams().height =Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)taskAddEditDayDateLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.taskAddEditDayDateLabelTop, 0, 0);

        taskAddEditDayDateLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.task_date, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        taskAddEditDate.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        taskAddEditDate.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)taskAddEditDate.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.taskAddEditDayDateTop, 0, 0);
        taskAddEditDate.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        taskAddEditDate.setTextSize(Utility.fontSize);

        taskAddEditDate.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDateHeight)));

        taskAddEditDay.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        taskAddEditDay.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)taskAddEditDay.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.taskAddEditDayDateTop, 0, 0);
        taskAddEditDay.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        taskAddEditDay.setTextSize(Utility.fontSize);

        taskAddEditDay.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDateHeight)));

        taskAddEditRecursive.setChecked(false);
        taskAddEditDay.setVisibility(View.INVISIBLE);
        taskAddEditDay.setEnabled(false);
        taskAddEditDate.setVisibility(View.VISIBLE);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        taskAddEditStartTimeLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        taskAddEditStartTimeLabel.getLayoutParams().height =Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)taskAddEditStartTimeLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.taskAddEditStartTimeLabelTop, 0, 0);

        taskAddEditStartTimeLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.start_time, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        taskAddEditStartTime.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        taskAddEditStartTime.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)taskAddEditStartTime.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.taskAddEditStartTimeTop, 0, 0);
        taskAddEditStartTime.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        taskAddEditStartTime.setTextSize(Utility.fontSize);

        taskAddEditStartTime.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDateHeight)));

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        taskAddEditStopTimeLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        taskAddEditStopTimeLabel.getLayoutParams().height =Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)taskAddEditStopTimeLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.taskAddEditStopTimeLabelTop, 0, 0);

        taskAddEditStopTimeLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.stop_time, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        taskAddEditStopTime.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        taskAddEditStopTime.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)taskAddEditStopTime.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.taskAddEditStopTimeTop, 0, 0);
        taskAddEditStopTime.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        taskAddEditStopTime.setTextSize(Utility.fontSize);

        taskAddEditStopTime.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDateHeight)));

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    }

    @Override
    protected void addListeners()
    {
        taskAddEditCategory.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                taskAddEditCategory.setInputType(InputType.TYPE_NULL);

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    initCategoryPopupWindow();

                    return true;
                }

                return false;
            }
        });

        taskAddEditPriorityLow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                taskAddEditPriority.setProgress(0);
            }
        });

        taskAddEditPriorityMedium.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                taskAddEditPriority.setProgress(1);
            }
        });

        taskAddEditPriorityHigh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                taskAddEditPriority.setProgress(2);
            }
        });

        taskAddEditPriority.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch(progress)
                {
                    case 0:
                        taskAddEditPriorityLow.setTextSize(22);
                        taskAddEditPriorityMedium.setTextSize(15);
                        taskAddEditPriorityHigh.setTextSize(15);

                        ((RelativeLayout.LayoutParams)taskAddEditPriority.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.taskAddEditPriorityLow);
                        break;
                    case 1:
                        taskAddEditPriorityMedium.setTextSize(22);
                        taskAddEditPriorityLow.setTextSize(15);
                        taskAddEditPriorityHigh.setTextSize(15);

                        ((RelativeLayout.LayoutParams)taskAddEditPriority.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.taskAddEditPriorityMedium);
                        break;
                    case 2:
                        taskAddEditPriorityHigh.setTextSize(22);
                        taskAddEditPriorityMedium.setTextSize(15);
                        taskAddEditPriorityLow.setTextSize(15);

                        ((RelativeLayout.LayoutParams)taskAddEditPriority.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.taskAddEditPriorityHigh);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        taskAddEditAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    taskAddEditAlarmTextView.setText("ON");
                }
                else
                {
                    taskAddEditAlarmTextView.setText("OFF");
                }
            }
        });

        taskAddEditRecursive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    taskAddEditRecursiveText.setText("Every Week - ON");

                    taskAddEditDayDateLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.task_day, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));
                    taskAddEditDate.setVisibility(View.INVISIBLE);
                    taskAddEditDate.setEnabled(false);
                    taskAddEditDay.setVisibility(View.VISIBLE);
                    taskAddEditDay.setEnabled(true);
                }
                else
                {
                    taskAddEditRecursiveText.setText("Every Week - OFF");

                    taskAddEditDayDateLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.task_date, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));
                    taskAddEditDate.setVisibility(View.VISIBLE);
                    taskAddEditDate.setEnabled(true);
                    taskAddEditDay.setVisibility(View.INVISIBLE);
                    taskAddEditDay.setEnabled(false);
                }
            }
        });

        taskAddEditDate.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                taskAddEditDate.setInputType(InputType.TYPE_NULL);

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
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

                            taskAddEditDate.setText(simpleDateFormat.format(myCalendar.getTime()));
                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(TaskAddEdit.this, dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }

                return true;
            }
        });

        taskAddEditDay.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                taskAddEditDay.setInputType(InputType.TYPE_NULL);

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    initDayPopupWindow();

                    return true;
                }

                return false;
            }
        });

        taskAddEditStartTime.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                taskAddEditStartTime.setInputType(InputType.TYPE_NULL);

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    initTimeDialog(Utility.start_time);

                    return true;
                }

                return false;
            }
        });

        taskAddEditStopTime.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                taskAddEditStopTime.setInputType(InputType.TYPE_NULL);

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    initTimeDialog(Utility.stop_time);

                    return true;
                }

                return false;
            }
        });

        taskAddEditSaveReturnButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    taskAddEditSaveReturnButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_return_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    taskAddEditSaveReturnButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_return, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    if(add())
                    {
                        onBackPressed();
                    }
                }

                return true;
            }
        });

        taskAddEditSaveMoreButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    taskAddEditSaveMoreButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_more_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    taskAddEditSaveMoreButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_more, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    if(add())
                    {
                        taskAddEditName.setText("");
                        taskAddEditCategory.setText("");
                        taskAddEditPriority.setProgress(1);
                        taskAddEditAlarm.setChecked(false);
                        taskAddEditRecursive.setChecked(false);
                        taskAddEditDate.setText("");
                        taskAddEditDay.setText("");
                        taskAddEditStartTime.setText("");
                        taskAddEditStopTime.setText("");

                        taskAddEditName.requestFocus();
                    }
                }

                return true;
            }
        });

        taskAddEditSaveButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    taskAddEditSaveButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    taskAddEditSaveButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    if(edit())
                    {
                        onBackPressed();
                    }
                }

                return true;
            }
        });

        taskAddEditDeleteButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    taskAddEditDeleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_delete_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    taskAddEditDeleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_delete, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    removeTask();
                    onBackPressed();
                }

                return true;
            }
        });
    }

    private void setAddDynamicLayout()
    {
        taskAddEditSaveReturnButton.getLayoutParams().width = Utility.daysToRememberAddEditButtonWidth;
        taskAddEditSaveReturnButton.getLayoutParams().height = Utility.daysToRememberAddEditButtonHeight;
        ((RelativeLayout.LayoutParams)taskAddEditSaveReturnButton.getLayoutParams()).setMargins(Utility.daysToRememberAddEditSaveButtonLeft, Utility.taskAddEditButtonTop, 0, 100);

        taskAddEditSaveReturnButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_return, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        taskAddEditSaveReturnButton.setActivated(true);
        taskAddEditSaveReturnButton.setVisibility(View.VISIBLE);

        taskAddEditSaveButton.setActivated(false);
        taskAddEditSaveButton.setVisibility(View.INVISIBLE);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskAddEditSaveMoreButton.getLayoutParams().width = Utility.daysToRememberAddEditButtonWidth;
        taskAddEditSaveMoreButton.getLayoutParams().height = Utility.daysToRememberAddEditButtonHeight;
        ((RelativeLayout.LayoutParams)taskAddEditSaveMoreButton.getLayoutParams()).setMargins(Utility.daysToRememberAddEditDeleteButtonLeft, Utility.taskAddEditButtonTop, 0, 100);

        taskAddEditSaveMoreButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_more, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        taskAddEditSaveMoreButton.setActivated(true);
        taskAddEditSaveMoreButton.setVisibility(View.VISIBLE);

        taskAddEditDeleteButton.setActivated(false);
        taskAddEditDeleteButton.setVisibility(View.INVISIBLE);
    }

    private void setEditDynamicLayout(Bundle extras)
    {
        editTaskName = extras.getString("task_name");
        editTaskCategory = taskAddEditCategoryPopupListViewArray.get(extras.getInt("task_category"));
        editTaskPriority = extras.getInt("task_priority");
        editTaskAlarm = extras.getBoolean("task_alarm");

        startTime.hour = extras.getInt("task_startHour");
        startTime.minute = extras.getInt("task_startMinute");
        startTime.amPm = extras.getInt("task_startAmPm");
        stopTime.hour = extras.getInt("task_stopHour");
        stopTime.minute = extras.getInt("task_stopMinute");
        stopTime.amPm = extras.getInt("task_stopAmPm");

        taskAddEditName.setText(editTaskName);
        taskAddEditCategory.setText(editTaskCategory);
        taskAddEditPriority.setProgress(editTaskPriority);
        taskAddEditAlarm.setChecked(editTaskAlarm);
        taskAddEditRecursive.setChecked(extras.getBoolean("task_recursive"));
        taskAddEditDate.setText(extras.getString("task_date"));
        taskAddEditDay.setText(taskAddEditDayPopupListViewArray.get(extras.getInt("task_day")));
        String startTextHour = (startTime.hour<10?"0"+Integer.toString(startTime.hour):Integer.toString(startTime.hour));
        String startTextMinute = (startTime.minute<10?"0"+Integer.toString(startTime.minute):Integer.toString(startTime.minute));
        String stopTextHour = (stopTime.hour<10?"0"+Integer.toString(stopTime.hour):Integer.toString(stopTime.hour));
        String stopTextMinute = (stopTime.minute<10?"0"+Integer.toString(stopTime.minute):Integer.toString(stopTime.minute));
        taskAddEditStartTime.setText(startTextHour + ":" + startTextMinute + " " + (startTime.amPm==Utility.am?"AM":"PM"));
        taskAddEditStopTime.setText(stopTextHour + ":" + stopTextMinute + " " + (stopTime.amPm==Utility.am?"AM":"PM"));
        taskAddEditTaskID = extras.getInt("task_id");
        taskAddEditTasksPoolRef = extras.getInt("task_tasksPoolRef");

        taskAddEditSaveButton.getLayoutParams().width = Utility.daysToRememberAddEditButtonWidth;
        taskAddEditSaveButton.getLayoutParams().height = Utility.daysToRememberAddEditButtonHeight;
        ((RelativeLayout.LayoutParams)taskAddEditSaveButton.getLayoutParams()).setMargins(Utility.daysToRememberAddEditSaveButtonLeft, Utility.taskAddEditButtonTop, 0, 100);

        taskAddEditSaveButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        taskAddEditSaveButton.setActivated(true);
        taskAddEditSaveButton.setVisibility(View.VISIBLE);

        taskAddEditSaveReturnButton.setActivated(false);
        taskAddEditSaveReturnButton.setVisibility(View.INVISIBLE);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskAddEditDeleteButton.getLayoutParams().width = Utility.daysToRememberAddEditButtonWidth;
        taskAddEditDeleteButton.getLayoutParams().height = Utility.daysToRememberAddEditButtonHeight;
        ((RelativeLayout.LayoutParams)taskAddEditDeleteButton.getLayoutParams()).setMargins(Utility.daysToRememberAddEditDeleteButtonLeft, Utility.taskAddEditButtonTop, 0, 100);

        taskAddEditDeleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_delete, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        taskAddEditDeleteButton.setActivated(true);
        taskAddEditDeleteButton.setVisibility(View.VISIBLE);

        taskAddEditSaveMoreButton.setActivated(false);
        taskAddEditSaveMoreButton.setVisibility(View.INVISIBLE);
    }

    private void initCategoryPopupWindow()
    {
        int[] loc_int = new int[2];
        Rect location = new Rect();

        if(taskAddEditCategory != null)
        {
            taskAddEditCategory.getLocationOnScreen(loc_int);

            location.left = loc_int[0];
            location.top = loc_int[1];
            location.right = loc_int[0] + taskAddEditCategory.getWidth();
            location.bottom = loc_int[1] + taskAddEditCategory.getLayoutParams().height;
        }

        LayoutInflater inflater = (LayoutInflater)TaskAddEdit.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.days_to_remember_event_category_popup, (ViewGroup)findViewById(R.id.eventCategoryPopupElement));

        taskAddEditCategoryPopupListView = (ListView)popupView.findViewById(R.id.daysToRememberAddEditCategoryPopupListView);

        taskAddEditCategoryPopupListViewAdapter = new ArrayAdapter(TaskAddEdit.this, R.layout.days_to_remember_event_category_popup_list_item, taskAddEditCategoryPopupListViewArray);
        taskAddEditCategoryPopupListView.setAdapter(taskAddEditCategoryPopupListViewAdapter);

        taskAddEditCategoryPopupWindow = new PopupWindow(popupView, Utility.daysToRememberAddEditTornPaperWidth, 2*Utility.daysToRememberAddEditCategoryHeight, true);
        taskAddEditCategoryPopupWindow.showAtLocation(popupView, Gravity.TOP | Gravity.LEFT, location.left, location.bottom);

        taskAddEditCategoryPopupListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                taskAddEditCategory.setText(taskAddEditCategoryPopupListViewAdapter.getItem(position));
                taskAddEditCategoryPopupWindow.dismiss();
            }
        });
    }

    private void initDayPopupWindow()
    {
        int[] loc_int = new int[2];
        Rect location = new Rect();

        if(taskAddEditDay != null)
        {
            taskAddEditDay.getLocationOnScreen(loc_int);

            location.left = loc_int[0];
            location.top = loc_int[1];
            location.right = loc_int[0] + taskAddEditDay.getWidth();
            location.bottom = loc_int[1] + taskAddEditDay.getLayoutParams().height;
        }

        LayoutInflater inflater = (LayoutInflater)TaskAddEdit.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.days_to_remember_event_category_popup, (ViewGroup)findViewById(R.id.eventCategoryPopupElement));

        taskAddEditDayPopupListView = (ListView)popupView.findViewById(R.id.daysToRememberAddEditCategoryPopupListView);

        taskAddEditDayPopupListViewAdapter = new ArrayAdapter(TaskAddEdit.this, R.layout.days_to_remember_event_category_popup_list_item, taskAddEditDayPopupListViewArray);
        taskAddEditDayPopupListView.setAdapter(taskAddEditDayPopupListViewAdapter);

        taskAddEditDayPopupWindow = new PopupWindow(popupView, Utility.daysToRememberAddEditTornPaperWidth, 2*Utility.daysToRememberAddEditCategoryHeight, true);
        taskAddEditDayPopupWindow.showAtLocation(popupView, Gravity.TOP | Gravity.LEFT, location.left, location.bottom);

        taskAddEditDayPopupListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                taskAddEditDay.setText(taskAddEditDayPopupListViewAdapter.getItem(position));
                taskAddEditDayPopupWindow.dismiss();
            }
        });
    }

    private void initTimeDialog(int startStopTime)
    {
        final int startOrStop = startStopTime;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(TaskAddEdit.this, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                if(hourOfDay < 12)
                {
                    if(startOrStop == Utility.start_time)
                    {
                        startTime.setTime((hourOfDay==0?12:hourOfDay), minute, Utility.am);
                        String textHour = (startTime.hour<10?("0"+Integer.toString(startTime.hour)):Integer.toString(startTime.hour));
                        String textMinute = (startTime.minute<10?"0"+Integer.toString(startTime.minute):Integer.toString(startTime.minute));
                        taskAddEditStartTime.setText(textHour + ":" + textMinute + " AM");
                    }
                    else
                    {
                        stopTime.setTime(hourOfDay, minute, Utility.am);
                        String textHour = (stopTime.hour<10?("0"+Integer.toString(stopTime.hour)):Integer.toString(stopTime.hour));
                        String textMinute = (stopTime.minute<10?"0"+Integer.toString(stopTime.minute):Integer.toString(stopTime.minute));
                        taskAddEditStopTime.setText(textHour + ":" + textMinute + " AM");
                    }
                }
                else
                {
                    if(startOrStop == Utility.start_time)
                    {
                        startTime.setTime((hourOfDay==12?hourOfDay:hourOfDay-12), minute, Utility.pm);
                        String textHour = (startTime.hour<10?("0"+Integer.toString(startTime.hour)):Integer.toString(startTime.hour));
                        String textMinute = (startTime.minute<10?"0"+Integer.toString(startTime.minute):Integer.toString(startTime.minute));
                        taskAddEditStartTime.setText(textHour + ":" + textMinute + " PM");
                        //startTime.hour<10?("0"+Integer.toString(startTime.hour)):Integer.toString(startTime.hour)
                        //Integer.toString(hourOfDay==12?hourOfDay:hourOfDay-12)
                    }
                    else
                    {
                        stopTime.setTime((hourOfDay==12?hourOfDay:hourOfDay-12), minute, Utility.pm);
                        String textHour = (stopTime.hour<10?("0"+Integer.toString(stopTime.hour)):Integer.toString(stopTime.hour));
                        String textMinute = (stopTime.minute<10?"0"+Integer.toString(stopTime.minute):Integer.toString(stopTime.minute));
                        taskAddEditStopTime.setText(textHour + ":" + textMinute + " PM");
                    }
                }
            }
        }, hour, minute, false);

        timePicker.setTitle((startStopTime==Utility.start_time?"Select starting time":"Select stopping time"));
        timePicker.show();
    }

    private boolean add()
    {
        if(taskAddEditName.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(taskAddEditCategory.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Category cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!taskAddEditRecursive.isChecked() && taskAddEditDate.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Date cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(taskAddEditRecursive.isChecked() && taskAddEditDay.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Day cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(taskAddEditStartTime.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Start Time cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(taskAddEditStopTime.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Stop Time cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        int dayOfWeekTemp = 0;

        if(!taskAddEditRecursive.isChecked())
        {
            try
            {
                Date d = Utility.dateFormat.parse(taskAddEditDate.getText().toString());
                Calendar c = Calendar.getInstance();
                c.setTime(d);

                dayOfWeekTemp = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
            catch(Exception e)
            {

            }
        }
        final int dayOfWeek = dayOfWeekTemp;

        final int tasksPoolId = ((new DBModule(getApplicationContext())).addTasksPool(taskAddEditName.getText().toString(), taskAddEditCategoryPopupListViewArray.indexOf(taskAddEditCategory.getText().toString()), taskAddEditPriority.getProgress(), (taskAddEditAlarm.isChecked()?1:0), 0));
        int taskId = (new DBModule(getApplicationContext()).addTask(false, tasksPoolId, (taskAddEditRecursive.isChecked()?taskAddEditDayPopupListViewArray.indexOf(taskAddEditDay.getText().toString()):dayOfWeek), (taskAddEditRecursive.isChecked()?"":taskAddEditDate.getText().toString()), startTime.hour, startTime.minute, startTime.amPm, stopTime.hour, stopTime.minute, stopTime.amPm));

        if(taskId == -1)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(TaskAddEdit.this);

                    dialog.setTitle("Conflict Detected")
                    .setMessage("Another task is in a conflicting state. Please select a different time slot.")
                    .setPositiveButton("Add anyway", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            int newTaskId = (new DBModule(getApplicationContext()).addTask(true, tasksPoolId, (taskAddEditRecursive.isChecked()?taskAddEditDayPopupListViewArray.indexOf(taskAddEditDay.getText().toString()):dayOfWeek), (taskAddEditRecursive.isChecked()?"":taskAddEditDate.getText().toString()), startTime.hour, startTime.minute, startTime.amPm, stopTime.hour, stopTime.minute, stopTime.amPm));
                            dialog.dismiss();
                            onBackPressed();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                    .show();

            return false;
        }

        return true;
    }

    private boolean edit()
    {
        boolean changeTasksPoolData = false;

        if(!taskAddEditName.getText().toString().equals(editTaskName) || !taskAddEditCategory.getText().toString().equals(editTaskCategory) || taskAddEditPriority.getProgress() != editTaskPriority || taskAddEditAlarm.isChecked() != editTaskAlarm)
        {
            changeTasksPoolData = true;
        }

        if(taskAddEditName.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(taskAddEditCategory.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Category cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!taskAddEditRecursive.isChecked() && taskAddEditDate.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Date cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(taskAddEditRecursive.isChecked() && taskAddEditDay.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Day cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(taskAddEditStartTime.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Start Time cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(taskAddEditStopTime.getText().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Task Stop Time cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        int dayOfWeekTemp = 0;

        if(!taskAddEditRecursive.isChecked())
        {
            try
            {
                Date d = Utility.dateFormat.parse(taskAddEditDate.getText().toString());
                Calendar c = Calendar.getInstance();
                c.setTime(d);

                dayOfWeekTemp = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
            catch(Exception e)
            {

            }
        }
        final int dayOfWeek = dayOfWeekTemp;

        int tasksPoolId = taskAddEditTasksPoolRef;
        if(changeTasksPoolData == true)
        {
            tasksPoolId = ((new DBModule(getApplicationContext())).addTasksPool(taskAddEditName.getText().toString(), taskAddEditCategoryPopupListViewArray.indexOf(taskAddEditCategory.getText().toString()), taskAddEditPriority.getProgress(), (taskAddEditAlarm.isChecked()?1:0), 0));
        }

        final int finalTasksPoolId = tasksPoolId;
        int taskId = (new DBModule(getApplicationContext()).editTask(false, taskAddEditTaskID, finalTasksPoolId, (taskAddEditRecursive.isChecked()?taskAddEditDayPopupListViewArray.indexOf(taskAddEditDay.getText().toString()):dayOfWeek), (taskAddEditRecursive.isChecked()?"":taskAddEditDate.getText().toString()), startTime.hour, startTime.minute, startTime.amPm, stopTime.hour, stopTime.minute, stopTime.amPm));

        if(taskId == -1)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(TaskAddEdit.this);
                    dialog.setTitle("Conflict Detected")
                    .setMessage("Another task is in a conflicting state. Please select a different time slot.")
                    .setPositiveButton("Edit anyway", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            int newTaskId = (new DBModule(getApplicationContext()).editTask(true, taskAddEditTaskID, finalTasksPoolId, (taskAddEditRecursive.isChecked()?taskAddEditDayPopupListViewArray.indexOf(taskAddEditDay.getText().toString()):dayOfWeek), (taskAddEditRecursive.isChecked()?"":taskAddEditDate.getText().toString()), startTime.hour, startTime.minute, startTime.amPm, stopTime.hour, stopTime.minute, stopTime.amPm));
                            dialog.dismiss();
                            onBackPressed();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                    .show();

            return false;
        }

        return true;
    }

    private void removeTask()
    {
        (new DBModule(getApplicationContext())).removeItem(DBMeta.Tasks.table_name, DBMeta.Tasks.id, taskAddEditTaskID);
    }

    public static void importFromTasksPool(String taskName, int taskCategory, int taskPriority, int taskAlarm)
    {
        taskAddEditName.setText(taskName);
        taskAddEditCategory.setText(taskAddEditCategoryPopupListViewArray.get(taskCategory).toString());
        taskAddEditPriority.setProgress(taskPriority);
        taskAddEditAlarm.setChecked((taskAlarm==1?true:false));
    }
}
