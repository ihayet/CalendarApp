package com.quadstack.everroutine.tasks_pool;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.days_to_remember.DaysToRememberAddEdit;
import com.quadstack.everroutine.double_drawer.DoubleDrawerActivity;

import java.util.ArrayList;

public class TasksPoolAddEdit extends DoubleDrawerActivity {

    private View tasksPoolAddEditView;

    private ImageView tasksPoolAddEditTitle, tasksPoolAddEditNameLabel, tasksPoolAddEditCategoryLabel, tasksPoolAddEditPriorityLabel, tasksPoolAddEditAlarmLabel;
    private EditText tasksPoolAddEditName, tasksPoolAddEditCategory;
    private RelativeLayout tasksPoolAddEditPriorityLayout, tasksPoolAddEditAlarmLayout;
    private TextView tasksPoolAddEditPriorityLow, tasksPoolAddEditPriorityMedium, tasksPoolAddEditPriorityHigh, tasksPoolAddEditAlarmTextView;
    private SeekBar tasksPoolAddEditPriority;
    private Switch tasksPoolAddEditAlarm;
    private ImageView tasksPoolAddEditSaveReturnButton, tasksPoolAddEditSaveMoreButton, tasksPoolAddEditSaveButton, tasksPoolAddEditDeleteButton;

    private ListView tasksPoolAddEditCategoryPopupListView;
    private PopupWindow tasksPoolAddEditCategoryPopupWindow;

    private int tasksPoolAddEditTaskID;

    private ArrayList<String> tasksPoolAddEditCategoryPopupListViewArray = new ArrayList<String>(){{add("Official"); add("Academic"); add("Miscellaneous");}};

    private ArrayAdapter<String> tasksPoolAddEditCategoryPopupListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tasksPoolAddEditView = super.setContentLayout(R.layout.activity_tasks_pool_add_edit, R.layout.custom_actionbar_layout);

        tasksPoolAddEditTitle = (ImageView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditTitle);
        tasksPoolAddEditNameLabel = (ImageView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditNameLabel);
        tasksPoolAddEditCategoryLabel = (ImageView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditCategoryLabel);
        tasksPoolAddEditPriorityLabel = (ImageView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditPriorityLabel);
        tasksPoolAddEditAlarmLabel = (ImageView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditAlarmLabel);

        tasksPoolAddEditName = (EditText)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditName);
        tasksPoolAddEditCategory = (EditText)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditCategory);

        tasksPoolAddEditAlarmLayout = (RelativeLayout)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditAlarmLayout);
        tasksPoolAddEditPriorityLayout = (RelativeLayout)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditPriorityLayout);

        tasksPoolAddEditPriorityLow = (TextView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditPriorityLow);
        tasksPoolAddEditPriorityMedium = (TextView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditPriorityMedium);
        tasksPoolAddEditPriorityHigh = (TextView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditPriorityHigh);
        tasksPoolAddEditAlarmTextView = (TextView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditAlarmTextView);

        tasksPoolAddEditPriority = (SeekBar)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditPriority);
        tasksPoolAddEditAlarm = (Switch)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditAlarm);

        tasksPoolAddEditSaveReturnButton = (ImageView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditSaveReturnButton);
        tasksPoolAddEditSaveMoreButton = (ImageView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditSaveMoreButton);
        tasksPoolAddEditSaveButton = (ImageView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditSaveButton);
        tasksPoolAddEditDeleteButton = (ImageView)tasksPoolAddEditView.findViewById(R.id.tasksPoolAddEditDeleteButton);

        setDynamicLayout();
        addListeners();

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
    protected void addListeners()
    {
        tasksPoolAddEditCategory.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tasksPoolAddEditCategory.setInputType(InputType.TYPE_NULL);

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    initCategoryPopupWindow();

                    return true;
                }

                return false;
            }
        });

        tasksPoolAddEditPriorityLow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tasksPoolAddEditPriority.setProgress(0);
            }
        });

        tasksPoolAddEditPriorityMedium.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tasksPoolAddEditPriority.setProgress(1);
            }
        });

        tasksPoolAddEditPriorityHigh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tasksPoolAddEditPriority.setProgress(2);
            }
        });

        tasksPoolAddEditPriority.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch(progress)
                {
                    case 0:
                        tasksPoolAddEditPriorityLow.setTextSize(22);
                        tasksPoolAddEditPriorityMedium.setTextSize(15);
                        tasksPoolAddEditPriorityHigh.setTextSize(15);

                        ((RelativeLayout.LayoutParams)tasksPoolAddEditPriority.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.tasksPoolAddEditPriorityLow);
                        break;
                    case 1:
                        tasksPoolAddEditPriorityMedium.setTextSize(22);
                        tasksPoolAddEditPriorityLow.setTextSize(15);
                        tasksPoolAddEditPriorityHigh.setTextSize(15);

                        ((RelativeLayout.LayoutParams)tasksPoolAddEditPriority.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.tasksPoolAddEditPriorityMedium);
                        break;
                    case 2:
                        tasksPoolAddEditPriorityHigh.setTextSize(22);
                        tasksPoolAddEditPriorityMedium.setTextSize(15);
                        tasksPoolAddEditPriorityLow.setTextSize(15);

                        ((RelativeLayout.LayoutParams)tasksPoolAddEditPriority.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.tasksPoolAddEditPriorityHigh);
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

        tasksPoolAddEditAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    tasksPoolAddEditAlarmTextView.setText("ON");
                }
                else
                {
                    tasksPoolAddEditAlarmTextView.setText("OFF");
                }
            }
        });

        tasksPoolAddEditSaveReturnButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    tasksPoolAddEditSaveReturnButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_return_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    tasksPoolAddEditSaveReturnButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_return, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    if(addTasksPool())
                    {
                        onBackPressed();
                    }
                }

                return true;
            }
        });

        tasksPoolAddEditSaveMoreButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    tasksPoolAddEditSaveMoreButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_more_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    tasksPoolAddEditSaveMoreButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_more, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    if(addTasksPool())
                    {
                        tasksPoolAddEditName.setText("");
                        tasksPoolAddEditCategory.setText("");
                        tasksPoolAddEditPriority.setProgress(1);
                        tasksPoolAddEditAlarm.setChecked(false);

                        tasksPoolAddEditName.requestFocus();
                    }
                }

                return true;
            }
        });

        tasksPoolAddEditSaveButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    tasksPoolAddEditSaveButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    tasksPoolAddEditSaveButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    if(editTasksPool())
                    {
                        onBackPressed();
                    }
                }

                return true;
            }
        });

        tasksPoolAddEditDeleteButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    tasksPoolAddEditDeleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_delete_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    tasksPoolAddEditDeleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_delete, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    removeTasksPool();
                    onBackPressed();
                }

                return true;
            }
        });
    }

    @Override
    protected void setDynamicLayout()
    {
        tasksPoolAddEditTitle.getLayoutParams().width = Utility.titleWidth;
        tasksPoolAddEditTitle.getLayoutParams().height = Utility.titleHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditTitle.getLayoutParams()).setMargins(0, 0, 0, 0);

        tasksPoolAddEditTitle.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_title, Utility.titleWidth, Utility.titleHeight));

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        tasksPoolAddEditNameLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        tasksPoolAddEditNameLabel.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditNameLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditNameLabelTop, 0, 0);

        tasksPoolAddEditNameLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_name_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        tasksPoolAddEditName.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        tasksPoolAddEditName.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditName.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditNameTop, 0, 0);
        tasksPoolAddEditName.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        tasksPoolAddEditName.setTextSize(Utility.fontSize);

        tasksPoolAddEditName.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditNameHeight)));

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        tasksPoolAddEditCategoryLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        tasksPoolAddEditCategoryLabel.getLayoutParams().height =Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditCategoryLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditDateLabelTop, 0, 0);

        tasksPoolAddEditCategoryLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_category_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        tasksPoolAddEditCategory.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        tasksPoolAddEditCategory.getLayoutParams().height = Utility.daysToRememberAddEditCategoryHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditCategory.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditDateTop, 0, 0);
        tasksPoolAddEditCategory.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        tasksPoolAddEditCategory.setTextSize(Utility.fontSize);

        tasksPoolAddEditCategory.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDateHeight)));

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        tasksPoolAddEditPriorityLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        tasksPoolAddEditPriorityLabel.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditPriorityLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditCategoryLabelTop, 0, 0);

        tasksPoolAddEditPriorityLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_priority_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        tasksPoolAddEditPriorityLayout.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        tasksPoolAddEditPriorityLayout.getLayoutParams().height = Utility.daysToRememberAddEditCategoryHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditPriorityLayout.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditCategoryTop, 0, 0);
        tasksPoolAddEditPriority.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);

        tasksPoolAddEditPriorityLayout.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditCategoryHeight)));

        ((RelativeLayout.LayoutParams)tasksPoolAddEditPriorityLow.getLayoutParams()).topMargin = Utility.daysToRememberAddEditLabelHeight;
        tasksPoolAddEditPriorityLow.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, 0, 0);
        tasksPoolAddEditPriorityHigh.setPadding(0, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);

        tasksPoolAddEditPriorityLow.setTextSize(15);
        tasksPoolAddEditPriorityMedium.setTextSize(22);
        tasksPoolAddEditPriorityHigh.setTextSize(15);

        tasksPoolAddEditPriority.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        tasksPoolAddEditAlarmLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        tasksPoolAddEditAlarmLabel.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditAlarmLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditDetailsLabelTop, 0, 0);

        tasksPoolAddEditAlarmLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_alarm_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        tasksPoolAddEditAlarmLayout.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        tasksPoolAddEditAlarmLayout.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditAlarmLayout.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditDetailsTop, 0, 0);
        tasksPoolAddEditAlarmLayout.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);

        tasksPoolAddEditAlarmLayout.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDetailsHeight)));

        tasksPoolAddEditAlarmTextView.setPadding((Utility.daysToRememberAddEditTornPaperPaddingSide/3), 0, 0, 0);
        tasksPoolAddEditAlarmTextView.setTextSize(22);

        ((RelativeLayout.LayoutParams)tasksPoolAddEditAlarmTextView.getLayoutParams()).topMargin = (Utility.daysToRememberAddEditNameHeight/2) - 30;

        tasksPoolAddEditAlarm.setRight(Utility.daysToRememberAddEditTornPaperPaddingSide);
        tasksPoolAddEditAlarm.setPadding(0, 0, (Utility.daysToRememberAddEditTornPaperPaddingSide/3), 0);
    }

    //-------------------------------------------------------------------------------------------------------------------------

    private void setAddDynamicLayout()
    {
        tasksPoolAddEditSaveReturnButton.getLayoutParams().width = Utility.daysToRememberAddEditButtonWidth;
        tasksPoolAddEditSaveReturnButton.getLayoutParams().height = Utility.daysToRememberAddEditButtonHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditSaveReturnButton.getLayoutParams()).setMargins(Utility.daysToRememberAddEditSaveButtonLeft, Utility.tasksPoolAddEditButtonTop, 0, 100);

        tasksPoolAddEditSaveReturnButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_return, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        tasksPoolAddEditSaveReturnButton.setActivated(true);
        tasksPoolAddEditSaveReturnButton.setVisibility(View.VISIBLE);

        tasksPoolAddEditSaveButton.setActivated(false);
        tasksPoolAddEditSaveButton.setVisibility(View.INVISIBLE);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        tasksPoolAddEditSaveMoreButton.getLayoutParams().width = Utility.daysToRememberAddEditButtonWidth;
        tasksPoolAddEditSaveMoreButton.getLayoutParams().height = Utility.daysToRememberAddEditButtonHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditSaveMoreButton.getLayoutParams()).setMargins(Utility.daysToRememberAddEditDeleteButtonLeft, Utility.tasksPoolAddEditButtonTop, 0, 100);

        tasksPoolAddEditSaveMoreButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_more, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        tasksPoolAddEditSaveMoreButton.setActivated(true);
        tasksPoolAddEditSaveMoreButton.setVisibility(View.VISIBLE);

        tasksPoolAddEditDeleteButton.setActivated(false);
        tasksPoolAddEditDeleteButton.setVisibility(View.INVISIBLE);
    }

    private void setEditDynamicLayout(Bundle extras)
    {
        tasksPoolAddEditName.setText(extras.getString("task_name"));
        tasksPoolAddEditCategory.setText(tasksPoolAddEditCategoryPopupListViewArray.get(extras.getInt("task_category")));
        tasksPoolAddEditPriority.setProgress(extras.getInt("task_priority"));
        tasksPoolAddEditAlarm.setChecked(extras.getBoolean("task_alarm"));
        tasksPoolAddEditTaskID = extras.getInt("task_id");

        tasksPoolAddEditSaveButton.getLayoutParams().width = Utility.daysToRememberAddEditButtonWidth;
        tasksPoolAddEditSaveButton.getLayoutParams().height = Utility.daysToRememberAddEditButtonHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditSaveButton.getLayoutParams()).setMargins(Utility.daysToRememberAddEditSaveButtonLeft, Utility.tasksPoolAddEditButtonTop, 0, 100);

        tasksPoolAddEditSaveButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        tasksPoolAddEditSaveButton.setActivated(true);
        tasksPoolAddEditSaveButton.setVisibility(View.VISIBLE);

        tasksPoolAddEditSaveReturnButton.setActivated(false);
        tasksPoolAddEditSaveReturnButton.setVisibility(View.INVISIBLE);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        tasksPoolAddEditDeleteButton.getLayoutParams().width = Utility.daysToRememberAddEditButtonWidth;
        tasksPoolAddEditDeleteButton.getLayoutParams().height = Utility.daysToRememberAddEditButtonHeight;
        ((RelativeLayout.LayoutParams)tasksPoolAddEditDeleteButton.getLayoutParams()).setMargins(Utility.daysToRememberAddEditDeleteButtonLeft, Utility.tasksPoolAddEditButtonTop, 0, 100);

        tasksPoolAddEditDeleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_delete, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        tasksPoolAddEditDeleteButton.setActivated(true);
        tasksPoolAddEditDeleteButton.setVisibility(View.VISIBLE);

        tasksPoolAddEditSaveMoreButton.setActivated(false);
        tasksPoolAddEditSaveMoreButton.setVisibility(View.INVISIBLE);
    }

    //-------------------------------------------------------------------------------------------------------------------------

    private void initCategoryPopupWindow()
    {
        int[] loc_int = new int[2];
        Rect location = new Rect();

        if(tasksPoolAddEditCategory != null)
        {
            tasksPoolAddEditCategory.getLocationOnScreen(loc_int);

            location.left = loc_int[0];
            location.top = loc_int[1];
            location.right = loc_int[0] + tasksPoolAddEditCategory.getWidth();
            location.bottom = loc_int[1] + tasksPoolAddEditCategory.getLayoutParams().height;
        }

        LayoutInflater inflater = (LayoutInflater)TasksPoolAddEdit.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.days_to_remember_event_category_popup, (ViewGroup)findViewById(R.id.eventCategoryPopupElement));

        tasksPoolAddEditCategoryPopupListView = (ListView)popupView.findViewById(R.id.daysToRememberAddEditCategoryPopupListView);

        tasksPoolAddEditCategoryPopupListViewAdapter = new ArrayAdapter(TasksPoolAddEdit.this, R.layout.days_to_remember_event_category_popup_list_item, tasksPoolAddEditCategoryPopupListViewArray);
        tasksPoolAddEditCategoryPopupListView.setAdapter(tasksPoolAddEditCategoryPopupListViewAdapter);

        tasksPoolAddEditCategoryPopupWindow = new PopupWindow(popupView, Utility.daysToRememberAddEditTornPaperWidth, 2*Utility.daysToRememberAddEditCategoryHeight, true);
        tasksPoolAddEditCategoryPopupWindow.showAtLocation(popupView, Gravity.TOP | Gravity.LEFT, location.left, location.bottom);

        tasksPoolAddEditCategoryPopupListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tasksPoolAddEditCategory.setText(tasksPoolAddEditCategoryPopupListViewAdapter.getItem(position));
                tasksPoolAddEditCategoryPopupWindow.dismiss();
            }
        });
    }

    //-------------------------------------------------------------------------------------------------------------------------

    private boolean addTasksPool()
    {
        if(tasksPoolAddEditName.getText().toString().equals(""))
        {
            Toast.makeText(TasksPoolAddEdit.this, "Task Name cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(tasksPoolAddEditCategory.getText().toString().equals(""))
        {
            Toast.makeText(TasksPoolAddEdit.this, "Task Category cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        }

        int id = new DBModule(getApplicationContext()).addTasksPool(tasksPoolAddEditName.getText().toString(), tasksPoolAddEditCategoryPopupListViewArray.indexOf(tasksPoolAddEditCategory.getText().toString()), tasksPoolAddEditPriority.getProgress(), (tasksPoolAddEditAlarm.isChecked()?1:0), 1);

        TasksPool.addToAdapter(id, tasksPoolAddEditName.getText().toString(), tasksPoolAddEditCategoryPopupListViewArray.indexOf(tasksPoolAddEditCategory.getText().toString()), tasksPoolAddEditPriority.getProgress(), tasksPoolAddEditAlarm.isChecked());

        return true;
    }

    private boolean editTasksPool()
    {
        if(tasksPoolAddEditName.getText().toString().equals(""))
        {
            Toast.makeText(TasksPoolAddEdit.this, "Task Name cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(tasksPoolAddEditCategory.getText().toString().equals(""))
        {
            Toast.makeText(TasksPoolAddEdit.this, "Task Category cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        }

        int id = new DBModule(getApplicationContext()).editTasksPool(tasksPoolAddEditTaskID, tasksPoolAddEditName.getText().toString(), tasksPoolAddEditCategoryPopupListViewArray.indexOf(tasksPoolAddEditCategory.getText().toString()), tasksPoolAddEditPriority.getProgress(), (tasksPoolAddEditAlarm.isChecked()?1:0), 1);

        TasksPool.editInAdapter(id, tasksPoolAddEditName.getText().toString(), tasksPoolAddEditCategoryPopupListViewArray.indexOf(tasksPoolAddEditCategory.getText().toString()), tasksPoolAddEditPriority.getProgress(), tasksPoolAddEditAlarm.isChecked());

        return true;
    }

    private void removeTasksPool()
    {
        new DBModule(getApplicationContext()).removeItem(DBMeta.TasksPool.table_name, DBMeta.TasksPool.id, tasksPoolAddEditTaskID);

        TasksPool.removeFromAdapter(tasksPoolAddEditTaskID);
    }
}
