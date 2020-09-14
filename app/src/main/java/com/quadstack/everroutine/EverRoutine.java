package com.quadstack.everroutine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.days_to_remember.DaysToRemember;
import com.quadstack.everroutine.double_drawer.DoubleDrawerActivity;
import com.quadstack.everroutine.notification_manager.notification_activity;
import com.quadstack.everroutine.tasks.TabActivity;
import com.quadstack.everroutine.tasks_pool.TasksPool;

public class EverRoutine extends DoubleDrawerActivity
{
    private RelativeLayout homePageLayout = null;
    private View homePageViewFromDrawer = null;
    private DBModule dbModule = null;

    private ImageView title = null;
    private ImageView officialButton = null, officialLabel = null;
    private ImageView academicButton = null, academicLabel = null;
    private ImageView miscellaneousButton = null, miscellaneousLabel = null;
    private ImageView combinedButton = null, combinedLabel = null;
    private ImageView daysToRememberButton = null, daysToRememberLabel = null;
    private ImageView tasksListButton = null, tasksListLabel = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        homePageViewFromDrawer = super.setContentLayout(R.layout.activity_ever_routine, R.layout.custom_actionbar_layout);

        homePageLayout = (RelativeLayout)homePageViewFromDrawer.findViewById(R.id.homePageLayout);
        title = (ImageView)homePageViewFromDrawer.findViewById(R.id.titleView);
        officialButton = (ImageView)homePageViewFromDrawer.findViewById(R.id.officialButton);
        officialLabel = (ImageView)homePageViewFromDrawer.findViewById(R.id.officialLabel);
        academicButton = (ImageView)homePageViewFromDrawer.findViewById(R.id.academicButton);
        academicLabel = (ImageView)homePageViewFromDrawer.findViewById(R.id.academicLabel);
        miscellaneousButton = (ImageView)homePageViewFromDrawer.findViewById(R.id.miscellaneousButton);
        miscellaneousLabel = (ImageView)homePageViewFromDrawer.findViewById(R.id.miscellaneousLabel);
        combinedButton = (ImageView)homePageViewFromDrawer.findViewById(R.id.combinedButton);
        combinedLabel = (ImageView)homePageViewFromDrawer.findViewById(R.id.combinedLabel);
        daysToRememberButton = (ImageView)homePageViewFromDrawer.findViewById(R.id.daysToRememberButton);
        daysToRememberLabel = (ImageView)homePageViewFromDrawer.findViewById(R.id.daysToRememberLabel);
        tasksListButton = (ImageView)homePageViewFromDrawer.findViewById(R.id.tasksListButton);
        tasksListLabel = (ImageView)homePageViewFromDrawer.findViewById(R.id.tasksListLabel);

        setDynamicLayout();
        addListeners();

        dbModule = new DBModule(getApplicationContext());

        dateRefCacheInit();
    }

    @Override
    protected void onResume()
    {
        title.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.title, Utility.titleWidth, Utility.titleHeight));
        officialButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.official, Utility.homeButtonWidth, Utility.homeButtonHeight));
        officialLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.official_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));
        academicButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.academic, Utility.homeButtonWidth, Utility.homeButtonHeight));
        academicLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.academic_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));
        miscellaneousButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.miscellaneous, Utility.homeButtonWidth, Utility.homeButtonHeight));
        miscellaneousLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.miscellaneous_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));
        combinedButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.combined, Utility.homeButtonWidth, Utility.homeButtonHeight));
        combinedLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.combined_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));
        daysToRememberButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember, Utility.homeButtonWidth, Utility.homeButtonHeight));
        daysToRememberLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));
        tasksListButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks, Utility.homeButtonWidth, Utility.homeButtonHeight));
        tasksListLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));

        dbModule = new DBModule(getApplicationContext());

        dateRefCacheInit();

        super.onResume();
    }

    @Override
    protected void onStop()
    {
        (((BitmapDrawable)title.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)officialButton.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)officialLabel.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)academicButton.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)academicLabel.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)miscellaneousButton.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)miscellaneousLabel.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)combinedButton.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)combinedLabel.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)daysToRememberButton.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)daysToRememberLabel.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)tasksListButton.getDrawable()).getBitmap()).recycle();
        (((BitmapDrawable)tasksListLabel.getDrawable()).getBitmap()).recycle();

        dbModule = null;

        super.onStop();
    }

    @Override
    protected void setDynamicLayout()
    {
        homePageLayout.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.background, Utility.screenWidth, Utility.screenHeight)));

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        title.getLayoutParams().width = Utility.titleWidth;
        title.getLayoutParams().height = Utility.titleHeight;
        ((RelativeLayout.LayoutParams)title.getLayoutParams()).setMargins(0, 0, 0, 0);
        title.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.title, Utility.titleWidth, Utility.titleHeight));

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        officialButton.getLayoutParams().width = Utility.homeButtonWidth;
        officialButton.getLayoutParams().height = Utility.homeButtonHeight;
        ((RelativeLayout.LayoutParams)officialButton.getLayoutParams()).setMargins(Utility.homeButtonLeftColumnLeft, Utility.homeButtonTopRowTop, 0, 0);
        officialButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.official, Utility.homeButtonWidth, Utility.homeButtonHeight));

        officialLabel.getLayoutParams().width = Utility.homeButtonWidth;
        officialLabel.getLayoutParams().height = Utility.homeButtonLabelHeight + Utility.homeButtonLabelTopPadding; // Utility.homeButtonLabelTopPadding is added to preserve bottom padding
        officialLabel.setPadding(-Utility.homeButtonLabelTopPadding, Utility.homeButtonLabelTopPadding, -Utility.homeButtonLabelTopPadding, 0);
        officialLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.official_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        academicButton.getLayoutParams().width = Utility.homeButtonWidth;
        academicButton.getLayoutParams().height = Utility.homeButtonHeight;
        ((RelativeLayout.LayoutParams)academicButton.getLayoutParams()).setMargins(Utility.homeButtonRightColumnLeft, Utility.homeButtonTopRowTop, 0, 0);
        academicButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.academic, Utility.homeButtonWidth, Utility.homeButtonHeight));

        academicLabel.getLayoutParams().width = Utility.homeButtonWidth;
        academicLabel.getLayoutParams().height = Utility.homeButtonLabelHeight + Utility.homeButtonLabelTopPadding; // Utility.homeButtonLabelTopPadding is added to preserve bottom padding
        academicLabel.setPadding(-Utility.homeButtonLabelTopPadding, Utility.homeButtonLabelTopPadding, -Utility.homeButtonLabelTopPadding, 0);
        academicLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.academic_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        miscellaneousButton.getLayoutParams().width = Utility.homeButtonWidth;
        miscellaneousButton.getLayoutParams().height = Utility.homeButtonHeight ;
        ((RelativeLayout.LayoutParams)miscellaneousButton.getLayoutParams()).setMargins(Utility.homeButtonLeftColumnLeft, Utility.homeButtonMiddleRowTop, 0, 0);
        miscellaneousButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.miscellaneous, Utility.homeButtonWidth, Utility.homeButtonHeight));

        miscellaneousLabel.getLayoutParams().width = Utility.homeButtonWidth;
        miscellaneousLabel.getLayoutParams().height = Utility.homeButtonLabelHeight + Utility.homeButtonLabelTopPadding; // Utility.homeButtonLabelTopPadding is added to preserve bottom padding
        miscellaneousLabel.setPadding(-Utility.homeButtonLabelTopPadding, Utility.homeButtonLabelTopPadding, -Utility.homeButtonLabelTopPadding, 0);
        miscellaneousLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.miscellaneous_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        combinedButton.getLayoutParams().width = Utility.homeButtonWidth;
        combinedButton.getLayoutParams().height = Utility.homeButtonHeight;
        ((RelativeLayout.LayoutParams)combinedButton.getLayoutParams()).setMargins(Utility.homeButtonRightColumnLeft, Utility.homeButtonMiddleRowTop, 0, 0);
        combinedButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.combined, Utility.homeButtonWidth, Utility.homeButtonHeight));

        combinedLabel.getLayoutParams().width = Utility.homeButtonWidth;
        combinedLabel.getLayoutParams().height = Utility.homeButtonLabelHeight + Utility.homeButtonLabelTopPadding; // Utility.homeButtonLabelTopPadding is added to preserve bottom padding
        combinedLabel.setPadding(-Utility.homeButtonLabelTopPadding, Utility.homeButtonLabelTopPadding, -Utility.homeButtonLabelTopPadding, 0);
        combinedLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.combined_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        daysToRememberButton.getLayoutParams().width = Utility.homeButtonWidth;
        daysToRememberButton.getLayoutParams().height = Utility.homeButtonHeight;
        ((RelativeLayout.LayoutParams)daysToRememberButton.getLayoutParams()).setMargins(Utility.homeButtonLeftColumnLeft, Utility.homeButtonBottomRowTop, 0, 0);
        daysToRememberButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember, Utility.homeButtonWidth, Utility.homeButtonHeight));

        daysToRememberLabel.getLayoutParams().width = Utility.homeButtonWidth;
        daysToRememberLabel.getLayoutParams().height = Utility.homeButtonLabelHeight + Utility.homeButtonLabelTopPadding; // Utility.homeButtonLabelTopPadding is added to preserve bottom padding
        daysToRememberLabel.setPadding(-Utility.homeButtonLabelTopPadding, Utility.homeButtonLabelTopPadding, -Utility.homeButtonLabelTopPadding, 0);
        daysToRememberLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        tasksListButton.getLayoutParams().width = Utility.homeButtonWidth;
        tasksListButton.getLayoutParams().height = Utility.homeButtonHeight;
        ((RelativeLayout.LayoutParams)tasksListButton.getLayoutParams()).setMargins(Utility.homeButtonRightColumnLeft, Utility.homeButtonBottomRowTop, 0, 0);
        tasksListButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks, Utility.homeButtonWidth, Utility.homeButtonHeight));

        tasksListLabel.getLayoutParams().width = Utility.homeButtonWidth;
        tasksListLabel.getLayoutParams().height = Utility.homeButtonLabelHeight + Utility.homeButtonLabelTopPadding; // Utility.homeButtonLabelTopPadding is added to preserve bottom padding
        tasksListLabel.setPadding(-Utility.homeButtonLabelTopPadding, Utility.homeButtonLabelTopPadding, -Utility.homeButtonLabelTopPadding, 0);
        tasksListLabel.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_pool_label, Utility.homeButtonWidth, Utility.homeButtonLabelHeight));
    }

    @Override
    protected void addListeners()
    {
        officialButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    officialButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.official_selected, Utility.homeButtonWidth, Utility.homeButtonHeight));
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    officialButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.official, Utility.homeButtonWidth, Utility.homeButtonHeight));

                    Intent intent = new Intent(EverRoutine.this, TabActivity.class);
                    intent.putExtra("TaskViewType", Utility.official_task);
                    startActivity(intent);
                }

                return true;
            }
        });

        academicButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    academicButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.academic_selected, Utility.homeButtonWidth, Utility.homeButtonHeight));
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    academicButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.academic, Utility.homeButtonWidth, Utility.homeButtonHeight));

                    Intent intent = new Intent(EverRoutine.this, TabActivity.class);
                    intent.putExtra("TaskViewType", Utility.academic_task);
                    startActivity(intent);
                }

                return true;
            }
        });

        miscellaneousButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    miscellaneousButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.miscellaneous_selected, Utility.homeButtonWidth, Utility.homeButtonHeight));
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    miscellaneousButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.miscellaneous, Utility.homeButtonWidth, Utility.homeButtonHeight));

                    Intent intent = new Intent(EverRoutine.this, TabActivity.class);
                    intent.putExtra("TaskViewType", Utility.miscellaneous_task);
                    startActivity(intent);
                }

                return true;
            }
        });

        combinedButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    combinedButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.combined_selected, Utility.homeButtonWidth, Utility.homeButtonHeight));

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    combinedButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.combined, Utility.homeButtonWidth, Utility.homeButtonHeight));

                    Intent intent = new Intent(EverRoutine.this, TabActivity.class);
                    intent.putExtra("TaskViewType", Utility.combined_task);
                    startActivity(intent);
                }

                return true;
            }
        });

        daysToRememberButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    daysToRememberButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember_selected, Utility.homeButtonWidth, Utility.homeButtonHeight));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    daysToRememberButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.days_to_remember, Utility.homeButtonWidth, Utility.homeButtonHeight));

                    Intent daysToRememberIntent = new Intent(EverRoutine.this, DaysToRemember.class);
                    startActivity(daysToRememberIntent);
                }

                return true;
            }
        });

        tasksListButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tasksListButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks_selected, Utility.homeButtonWidth, Utility.homeButtonHeight));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    tasksListButton.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.tasks, Utility.homeButtonWidth, Utility.homeButtonHeight));

                    Intent tasksPoolIntent = new Intent(EverRoutine.this, TasksPool.class);
                    startActivity(tasksPoolIntent);
                }

                return true;
            }
        });
    }

    private void dateRefCacheInit()
    {
        int id;
        String date;

        Cursor cursor = dbModule.getDateRef();

        if(cursor.moveToFirst())
        {
            do
            {
                id = cursor.getInt(cursor.getColumnIndex(DBMeta.DateRef.id));
                date = cursor.getString(cursor.getColumnIndex(DBMeta.DateRef.date));

                Utility.dateRefCache.put(id, date);
            }
            while(cursor.moveToNext());
        }

        dbModule.showDateRefCache();
    }
}
