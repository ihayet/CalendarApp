package com.quadstack.everroutine.days_to_remember;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.double_drawer.DoubleDrawerActivity;
import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.notification_manager.notification_activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DaysToRememberAddEdit extends DoubleDrawerActivity {

    private View daysToRememberAddEditView;

    private ImageView daysToRememberAddEditTitle, daysToRememberAddEditNameLabel, daysToRememberAddEditDateLabel, daysToRememberAddEditDetailsLabel, daysToRememberAddEditCategoryLabel, daysToRememberAddEditSaveReturnButton, daysToRememberAddEditSaveMoreButton, daysToRememberAddEditSaveButton, daysToRememberAddEditDeleteButton;
    private EditText daysToRememberAddEditName, daysToRememberAddEditDate, daysToRememberAddEditCategory, daysToRememberAddEditDetails;
    private ListView daysToRememberAddEditCategoryPopupListView;
    private PopupWindow daysToRememberAddEditCategoryPopupWindow;

    private int daysToRememberAddEditEventEditID;

    private ArrayList<String> daysToRememberAddEditCategoryPopupListViewArray = new ArrayList<String>(){{add("Birthday"); add("Anniversary"); add("National Holiday");}};

    private ArrayAdapter<String> daysToRememberAddEditCategoryPopupListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daysToRememberAddEditView = super.setContentLayout(R.layout.activity_days_to_remember_add_edit, R.layout.custom_actionbar_layout);

        daysToRememberAddEditTitle = (ImageView)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditTitle);

        daysToRememberAddEditNameLabel = (ImageView)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditNameLabel);
        daysToRememberAddEditDateLabel = (ImageView)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditDateLabel);
        daysToRememberAddEditCategoryLabel = (ImageView)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditCategoryLabel);
        daysToRememberAddEditDetailsLabel = (ImageView)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditDetailsLabel);
        daysToRememberAddEditSaveReturnButton = (ImageView)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditSaveReturnButton);
        daysToRememberAddEditSaveMoreButton = (ImageView)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditSaveMoreButton);
        daysToRememberAddEditSaveButton = (ImageView)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditSaveButton);
        daysToRememberAddEditDeleteButton = (ImageView)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditDeleteButton);

        daysToRememberAddEditName = (EditText)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditName);
        daysToRememberAddEditDate = (EditText)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditDate);
        daysToRememberAddEditCategory = (EditText)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditCategory);
        daysToRememberAddEditDetails = (EditText)daysToRememberAddEditView.findViewById(R.id.daysToRememberAddEditDetails);

        addListeners();
        setDynamicLayout();

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
        daysToRememberAddEditSaveReturnButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    daysToRememberAddEditSaveReturnButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_return_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    daysToRememberAddEditSaveReturnButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_return, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    if(addDaysToRemember())
                    {
                        onBackPressed();
                    }

                    return true;
                }

                return false;
            }
        });

        daysToRememberAddEditSaveMoreButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    daysToRememberAddEditSaveMoreButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_more_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    daysToRememberAddEditSaveMoreButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_more, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    if(addDaysToRemember())
                    {
                        daysToRememberAddEditName.setText("");
                        daysToRememberAddEditDate.setText("");
                        daysToRememberAddEditCategory.setText("");
                        daysToRememberAddEditDetails.setText("");

                        daysToRememberAddEditName.requestFocus();
                    }

                    return true;
                }

                return false;
            }
        });

        daysToRememberAddEditSaveButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    daysToRememberAddEditSaveButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    daysToRememberAddEditSaveButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    if(editDaysToRemember())
                    {
                        onBackPressed();
                    }

                    return true;
                }

                return false;
            }
        });

        daysToRememberAddEditDeleteButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    daysToRememberAddEditDeleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_delete_selected, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    daysToRememberAddEditDeleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_delete, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

                    new AlertDialog.Builder(DaysToRememberAddEdit.this)
                            .setTitle("Confirm Deletion").setMessage("Are you sure you want to delete this event?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    removeDaysToRemember();

                                    onBackPressed();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                    return true;
                }

                return false;
            }
        });

        daysToRememberAddEditDate.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                daysToRememberAddEditDate.setInputType(InputType.TYPE_NULL);

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

                            daysToRememberAddEditDate.setText(simpleDateFormat.format(myCalendar.getTime()));
                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(DaysToRememberAddEdit.this, dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }

                return true;
            }
        });

        daysToRememberAddEditCategory.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                daysToRememberAddEditCategory.setInputType(InputType.TYPE_NULL);

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    initCategoryPopupWindow();

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void setDynamicLayout()
    {
        RelativeLayout.LayoutParams daysToRememberAddEditTitleParams = new RelativeLayout.LayoutParams(Utility.titleWidth, Utility.titleHeight);
        daysToRememberAddEditTitleParams.setMargins(0, 0, 0, 0);
        daysToRememberAddEditTitle.setLayoutParams(daysToRememberAddEditTitleParams);

        daysToRememberAddEditTitle.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_title, Utility.titleWidth, Utility.titleHeight));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams daysToRememberAddEditNameLabelParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight);
        daysToRememberAddEditNameLabelParams.setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditNameLabelTop, 0, 0);
        daysToRememberAddEditNameLabel.setLayoutParams(daysToRememberAddEditNameLabelParams);

        daysToRememberAddEditNameLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_event_name_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        RelativeLayout.LayoutParams daysToRememberAddEditNameParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditNameHeight);
        daysToRememberAddEditNameParams.setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditNameTop, 0, 0);
        daysToRememberAddEditName.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        daysToRememberAddEditName.setTextSize(Utility.fontSize);
        daysToRememberAddEditName.setLayoutParams(daysToRememberAddEditNameParams);

        daysToRememberAddEditName.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditNameHeight)));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams daysToRememberAddEditDateLabelParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight);
        daysToRememberAddEditDateLabelParams.setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditDateLabelTop, 0, 0);
        daysToRememberAddEditDateLabel.setLayoutParams(daysToRememberAddEditDateLabelParams);

        daysToRememberAddEditDateLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_date_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        RelativeLayout.LayoutParams daysToRememberAddEditDateParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDateHeight);
        daysToRememberAddEditDateParams.setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditDateTop, 0, 0);
        daysToRememberAddEditDate.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        daysToRememberAddEditDate.setTextSize(Utility.fontSize);
        daysToRememberAddEditDate.setLayoutParams(daysToRememberAddEditDateParams);

        daysToRememberAddEditDate.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDateHeight)));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams daysToRememberAddEditCategoryLabelParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight);
        daysToRememberAddEditCategoryLabelParams.setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditCategoryLabelTop, 0, 0);
        daysToRememberAddEditCategoryLabel.setLayoutParams(daysToRememberAddEditCategoryLabelParams);

        daysToRememberAddEditCategoryLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_category_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        RelativeLayout.LayoutParams daysToRememberAddEditCategoryParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditCategoryHeight);
        daysToRememberAddEditCategoryParams.setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditCategoryTop, 0, 0);
        daysToRememberAddEditCategory.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        daysToRememberAddEditCategory.setTextSize(Utility.fontSize);
        daysToRememberAddEditCategory.setLayoutParams(daysToRememberAddEditCategoryParams);

        daysToRememberAddEditCategory.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditCategoryHeight)));

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams daysToRememberAddEditDetailsLabelParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight);
        daysToRememberAddEditDetailsLabelParams.setMargins(Utility.daysToRememberAddEditNameLabelLeft, Utility.daysToRememberAddEditDetailsLabelTop, 0, 0);
        daysToRememberAddEditDetailsLabel.setLayoutParams(daysToRememberAddEditDetailsLabelParams);

        daysToRememberAddEditDetailsLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_details_label, Utility.daysToRememberAddEditLabelWidth, Utility.daysToRememberAddEditLabelHeight));

        RelativeLayout.LayoutParams daysToRememberAddEditDetailsParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDetailsHeight);
        daysToRememberAddEditDetailsParams.setMargins(Utility.daysToRememberAddEditTornPaperLeft, Utility.daysToRememberAddEditDetailsTop, 0, 0);
        daysToRememberAddEditDetails.setPadding(Utility.daysToRememberAddEditTornPaperPaddingSide, 0, Utility.daysToRememberAddEditTornPaperPaddingSide, 0);
        daysToRememberAddEditDetails.setTextSize(Utility.fontSize);
        daysToRememberAddEditDetails.setLayoutParams(daysToRememberAddEditDetailsParams);

        daysToRememberAddEditDetails.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.daysToRememberAddEditTornPaperWidth, Utility.daysToRememberAddEditDetailsHeight)));
    }

    //----------------------------------------------------------------------------------------------------------------------------

    private void setAddDynamicLayout()
    {
        RelativeLayout.LayoutParams daysToRememberAddEditSaveReturnButtonParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight);
        daysToRememberAddEditSaveReturnButtonParams.setMargins(Utility.daysToRememberAddEditSaveButtonLeft, Utility.daysToRememberAddEditButtonTop, 0, 100);
        daysToRememberAddEditSaveReturnButton.setLayoutParams(daysToRememberAddEditSaveReturnButtonParams);

        daysToRememberAddEditSaveReturnButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_return, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        daysToRememberAddEditSaveReturnButton.setActivated(true);
        daysToRememberAddEditSaveReturnButton.setVisibility(View.VISIBLE);

        daysToRememberAddEditSaveButton.setActivated(false);
        daysToRememberAddEditSaveButton.setVisibility(View.INVISIBLE);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams daysToRememberAddEditSaveMoreButtonParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight);
        daysToRememberAddEditSaveMoreButtonParams.setMargins(Utility.daysToRememberAddEditDeleteButtonLeft, Utility.daysToRememberAddEditButtonTop, 0, 100);
        daysToRememberAddEditSaveMoreButton.setLayoutParams(daysToRememberAddEditSaveMoreButtonParams);

        daysToRememberAddEditSaveMoreButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save_more, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        daysToRememberAddEditSaveMoreButton.setActivated(true);
        daysToRememberAddEditSaveMoreButton.setVisibility(View.VISIBLE);

        daysToRememberAddEditDeleteButton.setActivated(false);
        daysToRememberAddEditDeleteButton.setVisibility(View.INVISIBLE);
    }

    private void setEditDynamicLayout(Bundle extras)
    {
        daysToRememberAddEditName.setText(extras.getString("event_name"));
        daysToRememberAddEditDate.setText(extras.getString("event_date"));
        daysToRememberAddEditCategory.setText(daysToRememberAddEditCategoryPopupListViewArray.get(extras.getInt("event_category")));
        daysToRememberAddEditDetails.setText(extras.getString("event_details"));
        daysToRememberAddEditEventEditID = extras.getInt("event_id");

        RelativeLayout.LayoutParams daysToRememberAddEditSaveButtonParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight);
        daysToRememberAddEditSaveButtonParams.setMargins(Utility.daysToRememberAddEditSaveButtonLeft, Utility.daysToRememberAddEditButtonTop, 0, 100);
        daysToRememberAddEditSaveButton.setLayoutParams(daysToRememberAddEditSaveButtonParams);

        daysToRememberAddEditSaveButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_save, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        daysToRememberAddEditSaveButton.setActivated(true);
        daysToRememberAddEditSaveButton.setVisibility(View.VISIBLE);

        daysToRememberAddEditSaveReturnButton.setActivated(false);
        daysToRememberAddEditSaveReturnButton.setVisibility(View.INVISIBLE);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        RelativeLayout.LayoutParams daysToRememberAddEditDeleteButtonParams = new RelativeLayout.LayoutParams(Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight);
        daysToRememberAddEditDeleteButtonParams.setMargins(Utility.daysToRememberAddEditDeleteButtonLeft, Utility.daysToRememberAddEditButtonTop, 0, 100);
        daysToRememberAddEditDeleteButton.setLayoutParams(daysToRememberAddEditDeleteButtonParams);

        daysToRememberAddEditDeleteButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_addedit_delete, Utility.daysToRememberAddEditButtonWidth, Utility.daysToRememberAddEditButtonHeight));

        daysToRememberAddEditDeleteButton.setActivated(true);
        daysToRememberAddEditDeleteButton.setVisibility(View.VISIBLE);

        daysToRememberAddEditSaveMoreButton.setActivated(false);
        daysToRememberAddEditSaveMoreButton.setVisibility(View.INVISIBLE);
    }

    //----------------------------------------------------------------------------------------------------------------------------

    private void initCategoryPopupWindow()
    {
        int[] loc_int = new int[2];
        Rect location = new Rect();

        if(daysToRememberAddEditCategory != null)
        {
            daysToRememberAddEditCategory.getLocationOnScreen(loc_int);

            location.left = loc_int[0];
            location.top = loc_int[1];
            location.right = loc_int[0] + daysToRememberAddEditCategory.getWidth();
            location.bottom = loc_int[1] + daysToRememberAddEditCategory.getLayoutParams().height;
        }

        LayoutInflater inflater = (LayoutInflater)DaysToRememberAddEdit.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.days_to_remember_event_category_popup, (ViewGroup)findViewById(R.id.eventCategoryPopupElement));

        daysToRememberAddEditCategoryPopupListView = (ListView)popupView.findViewById(R.id.daysToRememberAddEditCategoryPopupListView);

        daysToRememberAddEditCategoryPopupListViewAdapter = new ArrayAdapter(DaysToRememberAddEdit.this, R.layout.days_to_remember_event_category_popup_list_item, daysToRememberAddEditCategoryPopupListViewArray);
        daysToRememberAddEditCategoryPopupListView.setAdapter(daysToRememberAddEditCategoryPopupListViewAdapter);

        daysToRememberAddEditCategoryPopupWindow = new PopupWindow(popupView, Utility.daysToRememberAddEditTornPaperWidth, 2*Utility.daysToRememberAddEditCategoryHeight, true);
        daysToRememberAddEditCategoryPopupWindow.showAtLocation(popupView, Gravity.TOP | Gravity.LEFT, location.left, location.bottom);

        daysToRememberAddEditCategoryPopupListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                daysToRememberAddEditCategory.setText(daysToRememberAddEditCategoryPopupListViewAdapter.getItem(position));
                daysToRememberAddEditCategoryPopupWindow.dismiss();
            }
        });
    }

    //----------------------------------------------------------------------------------------------------------------------------

    private boolean addDaysToRemember()
    {
        if(daysToRememberAddEditName.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Event Name cannot be Empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(daysToRememberAddEditDate.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Event Date cannot be Empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(daysToRememberAddEditCategory.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Event Category cannot be Empty", Toast.LENGTH_LONG).show();
            return false;
        }

        int id = new DBModule(getApplicationContext()).addDaysToRemember(daysToRememberAddEditName.getText().toString(), daysToRememberAddEditDate.getText().toString(), daysToRememberAddEditCategoryPopupListViewArray.indexOf(daysToRememberAddEditCategory.getText().toString()), daysToRememberAddEditDetails.getText().toString());

        Intent intent = new Intent(DaysToRememberAddEdit.this,notification_activity.class);

        intent.putExtra("id",id);
        intent.putExtra("event_name", daysToRememberAddEditName.getText().toString());
        intent.putExtra("event_date",daysToRememberAddEditDate.getText().toString());
        intent.putExtra("event_type",daysToRememberAddEditCategory.getText().toString());
        intent.putExtra("event_details", daysToRememberAddEditDetails.getText().toString());

        startActivity(intent);

        DaysToRemember.addToAdapter(id, daysToRememberAddEditName.getText().toString(), daysToRememberAddEditCategoryPopupListViewArray.indexOf(daysToRememberAddEditCategory.getText().toString()), daysToRememberAddEditDate.getText().toString(), daysToRememberAddEditDetails.getText().toString());

        return true;
    }

    private boolean editDaysToRemember()
    {
        if(daysToRememberAddEditName.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Event Name cannot be Empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(daysToRememberAddEditDate.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Event Date cannot be Empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(daysToRememberAddEditCategory.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Event Category cannot be Empty", Toast.LENGTH_LONG).show();
            return false;
        }

        new DBModule(getApplicationContext()).editDaysToRemember(daysToRememberAddEditEventEditID, daysToRememberAddEditName.getText().toString(), daysToRememberAddEditDate.getText().toString(), daysToRememberAddEditCategoryPopupListViewArray.indexOf(daysToRememberAddEditCategory.getText().toString()), daysToRememberAddEditDetails.getText().toString());

        DaysToRemember.editInAdapter(daysToRememberAddEditEventEditID, daysToRememberAddEditName.getText().toString(), daysToRememberAddEditCategoryPopupListViewArray.indexOf(daysToRememberAddEditCategory.getText().toString()), daysToRememberAddEditDate.getText().toString(), daysToRememberAddEditDetails.getText().toString());

        return true;
    }

    private void removeDaysToRemember()
    {
        new DBModule(getApplicationContext()).removeItem(DBMeta.DaysToRemember.table_name, DBMeta.DaysToRemember.id, daysToRememberAddEditEventEditID);

        DaysToRemember.removeFromAdapter(daysToRememberAddEditEventEditID);
    }

    //----------------------------------------------------------------------------------------------------------------------------
}
