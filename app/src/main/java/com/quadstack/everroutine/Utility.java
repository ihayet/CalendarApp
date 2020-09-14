package com.quadstack.everroutine;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by ISHRAK on 11/30/2016.
 */
public class Utility
{
    //Constants ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public final static int official_task = 0;
    public final static int academic_task = 1;
    public final static int miscellaneous_task = 2;
    public final static int combined_task = 3;

    public static int am = 0;
    public static int pm = 1;

    public static int start_time = 0;
    public static int stop_time = 1;

    public static String[] daysOfWeek;

    public static int non_recursive = -1;

    public static int welcomeScreenDisplayTime = 2000;
    //Constants ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Screen Dimensions ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int screenWidth, screenHeight;
    public static int actionBarHeight;
    //Screen Dimensions ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Common Title - Dynamic ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int titleWidth, titleHeight;
    //Common Title - Dynamic ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Common Font - Dynamic ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int fontSize;
    //Common Font - Dynamic ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Home Page - Dynamic ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int homeButtonWidth, homeButtonHeight, homeButtonLeftColumnLeft, homeButtonRightColumnLeft, homeButtonTopRowTop, homeButtonMiddleRowTop, homeButtonBottomRowTop;
    public static int homeButtonHorizontalGap, homeButtonVerticalGap, homeButtonLabelHeight, homeButtonLabelTopPadding;
    public static int centerTextViewWidth, centerTextViewHeight;
    //Home Page - Dynamic ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Double Drawer - Dynamic ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int drawerWidth, drawerHeight, drawerTop;
    //Double Drawer - Dynamic ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Double Drawer Row - Dynamic ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int shelfWidth, shelfHeight, drawerButtonWidth, drawerButtonHeight;
    public static int shelfLeft, shelfTop, drawerButtonLeft, drawerButtonTop;
    //Double Drawer Row - Dynamic ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Task View - Dynamic ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int taskTabBarWidth, taskTabBarHeight;
    public static int taskDateButtonTop, taskDateButtonLeft, taskDateButtonWidth, taskDateButtonHeight, taskArrowLeft, taskArrowWidth, taskArrowHeight;
    public static int taskListViewTop, taskListViewBottom, taskListViewHeight;
    public static int taskRowHeight, taskNameHeight, taskPriorityWidth;
    public static int taskButtonDiameter, taskAddLeft, taskAddTop, taskDeleteLeft, taskDeleteAllLeft;
    public static int taskButtonLabelWidth, taskButtonLabelHeight, taskAddLabelLeft, taskAddLabelTop, taskDeleteLabelLeft, taskDeleteAllLabelLeft;

    public static int calendarWidth, weeklyListWidth, weeklyListLabelWidth, weeklyListLabelHeight;
    //Task View - Dynamic ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Task AddEdit - Dynamic ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int taskAddEditUnitHeightLabel, taskAddEditUnitHeight;
    public static int taskAddEditRecursiveLabelTop, taskAddEditDayDateLabelTop, taskAddEditStartTimeLabelTop, taskAddEditStopTimeLabelTop;
    public static int taskAddEditRecursiveTop, taskAddEditDayDateTop, taskAddEditStartTimeTop, taskAddEditStopTimeTop, taskAddEditButtonTop;
    //Task AddEdit - Dynamic ------------------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Days To Remember - Dynamic ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int daysToRememberListViewTop, daysToRememberListViewBottom, daysToRememberListViewHeight;
    public static int daysToRememberButtonDiameter, daysToRememberAddLeft, daysToRememberAddTop, daysToRememberDeleteLeft, daysToRememberDeleteAllLeft;
    public static int daysToRememberButtonLabelWidth, daysToRememberButtonLabelHeight, daysToRememberAddLabelLeft, daysToRememberAddLabelTop, daysToRememberDeleteLabelLeft, daysToRememberDeleteAllLabelLeft;
    //Days To Remember - Dynamic ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Days To Remember Row - Dynamic ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int rowWidth, rowHeight, rowLeft, rowTop, rowRight;
    public static int tickImageWidth, tickImageHeight, tickImageLeft, tickImageTop;
    public static int eventImageWidth, eventImageHeight, eventImageLeft, eventImageTop;
    public static int eventNameWidth, eventNameHeight, eventNameLeft, eventNameTop;
    public static int eventDateWidth, eventDateHeight, eventDateLeft, eventDateTop;
    public static int eventDetailsWidth, eventDetailsHeight, eventDetailsLeft, eventDetailsTop;
    //Days To Remember Row - Dynamic ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Days To Remember AddEdit - Dynamic ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static int daysToRememberAddEditLabelWidth, daysToRememberAddEditLabelHeight;
    public static int daysToRememberAddEditNameLabelLeft, daysToRememberAddEditNameLabelTop, daysToRememberAddEditDateLabelTop, daysToRememberAddEditCategoryLabelTop, daysToRememberAddEditDetailsLabelTop;
    public static int daysToRememberAddEditTornPaperWidth, daysToRememberAddEditTornPaperLeft, daysToRememberAddEditTornPaperPaddingSide;
    public static int daysToRememberAddEditNameHeight, daysToRememberAddEditNameTop, daysToRememberAddEditDateHeight, daysToRememberAddEditDateTop, daysToRememberAddEditCategoryHeight, daysToRememberAddEditCategoryTop, daysToRememberAddEditDetailsHeight, daysToRememberAddEditDetailsTop;
    public static int daysToRememberAddEditButtonWidth, daysToRememberAddEditButtonHeight, daysToRememberAddEditSaveButtonLeft, daysToRememberAddEditDeleteButtonLeft, daysToRememberAddEditButtonTop;
    public static int tasksPoolAddEditButtonTop;
    //Days To Remember AddEdit - Dynamic ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    //Date ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
    public static HashMap dateRefCache = null;                             //key: dateRefID    value: date
    public static SimpleDateFormat dateFormat = null, timeFormat = null;
    public static Calendar calendar = null;
    //Date ---------------------------#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-##-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

    static
    {
        dateRefCache = new HashMap();
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        timeFormat = new SimpleDateFormat("hh:mm a");
        calendar = new GregorianCalendar();

        daysOfWeek = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    }

    public static class Time
    {
        public int hour, minute, amPm;

        public void setTime(int h, int m, int a)
        {
            hour = h;
            minute = m;
            amPm = a;
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void setScreenSize(Context context)
    {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth =  size.x;
        screenHeight = size.y;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        fontSize = screenWidth/40;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~
    }

    public static void setTitleDimensions()
    {
        titleWidth = screenWidth;
        titleHeight = (int)(titleWidth/3.8);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        //Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth)
        {
            final int halfHeight = height/2;
            final int halfWidth = width/2;

            //Calculate the largest inSampleSize value
            //that is a power of 2 and keeps both height
            //and width larger than the requested height and width

            while((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
    {
        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        //Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        options.inPreferredConfig = Bitmap.Config.RGB_565;                        //memory optimization
        options.inDither = true;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateDateHash(String date)
    {
        int dateHash = 0;

        try
        {
            Date theDate = Utility.dateFormat.parse(date);
            Utility.calendar.setTime(theDate);

            dateHash = Utility.calendar.get(Calendar.YEAR)*10000 + (Utility.calendar.get(Calendar.MONTH) + 1)*100 + Utility.calendar.get(Calendar.DAY_OF_MONTH);
        }
        catch(Exception ex)
        {
            Log.e("Exception: ", ex.getMessage());
        }

        return dateHash;
    }

    public static int getImgResource(int taskCategory)
    {
        switch(taskCategory)
        {
            case 0:
                return R.drawable.official;
            case 1:
                return R.drawable.academic;
            case 2:
                return R.drawable.miscellaneous;
            default:
                return R.drawable.combined;
        }
    }

    public static int getEventImgResource(int eventCategory)
    {
        int imgResource;

        switch(eventCategory)
        {
            case 0:
                imgResource = R.drawable.birthday;
                break;
            case 1:
                imgResource = R.drawable.anniversary;
                break;
            case 2:
                imgResource = R.drawable.national_holiday;
                break;
            default:
                imgResource = 0;
                break;
        }

        return imgResource;
    }
    //--------------------------------------------------------------------------------------------------------------------------



    //--------------------------------------------------------------------------------------------------------------------------
    public static void doubleDrawerDynamicLayout(Context context)
    {
        setScreenSize(context);

        drawerWidth = shelfWidth;
        drawerHeight = (int)(0.5*screenHeight) + (shelfHeight + drawerButtonHeight) + 30;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~
    }

    public static void setDrawerTop(Context context, int height)
    {
        actionBarHeight = height;
        drawerTop = (int)(0.5*(screenHeight-drawerHeight) - actionBarHeight + 30);
    }

    public static void doubleDrawerRowDynamicLayout(Context context)
    {
        setScreenSize(context);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        shelfWidth = (int)(0.3*screenWidth);
        shelfHeight = (int)(shelfWidth/6.1);
        drawerButtonWidth = (int)(0.6*shelfWidth);
        drawerButtonHeight = drawerButtonWidth;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        drawerButtonLeft = (int)(0.2*shelfWidth);
        drawerButtonTop = drawerButtonLeft;
        shelfLeft = 0;
        shelfTop = drawerButtonTop + drawerButtonHeight;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~
    }
    //--------------------------------------------------------------------------------------------------------------------------



    //--------------------------------------------------------------------------------------------------------------------------
    public static void taskDynamicLayout(Context context)
    {
        setScreenSize(context);
        setTitleDimensions();

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskTabBarWidth = (int)(0.33*screenWidth);
        taskTabBarHeight = (int)(0.25*taskTabBarWidth);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskListViewTop = (int)(0.12*screenHeight) - (titleHeight/4) + (int)(0.03*screenHeight);
        taskListViewBottom = 0;
        taskListViewHeight = (int)(0.52*screenHeight) - titleHeight;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskDateButtonWidth = (int)(0.3*screenWidth);
        taskDateButtonHeight = (int)(taskDateButtonWidth/2.2);
        taskDateButtonTop = taskListViewTop - taskDateButtonHeight - (int)(0.03*screenWidth);
        taskDateButtonLeft = ((screenWidth - taskDateButtonWidth)/2);

        taskArrowLeft = (int)(0.05*screenWidth);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskRowHeight = (int)(0.22*screenWidth);

        taskNameHeight = (int)(0.26*taskRowHeight);
        taskPriorityWidth = (int)(0.6*rowWidth);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskButtonDiameter = (int)(0.125*screenHeight);

        taskButtonLabelWidth = (int)(0.25*screenWidth);
        taskButtonLabelHeight = (int)(taskButtonLabelWidth/5.87);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskAddTop = taskListViewTop + taskListViewHeight + (int)(0.01*screenHeight);
        taskAddLeft = rowLeft;
        taskAddLabelLeft = taskAddLeft - ((taskButtonLabelWidth - taskButtonDiameter)/2);
        taskAddLabelTop = taskAddTop + taskButtonDiameter + 5;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskDeleteAllLeft = screenWidth - rowRight - taskButtonDiameter;
        taskDeleteAllLabelLeft = taskDeleteAllLeft - ((taskButtonLabelWidth - taskButtonDiameter)/2);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskDeleteLeft = rowLeft + (taskDeleteAllLeft - taskAddLeft)/2;
        taskDeleteLabelLeft = taskDeleteLeft - ((taskButtonLabelWidth - taskButtonDiameter)/2);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        calendarWidth = (int)(0.85*screenWidth);
        weeklyListWidth = (screenWidth/7);
        weeklyListLabelHeight = (int)(weeklyListWidth*0.6);
    }

    public static void taskAddEditDynamicLayout(Context context)
    {
        setScreenSize(context);
        setTitleDimensions();

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskAddEditUnitHeightLabel = (daysToRememberAddEditLabelHeight/2) + daysToRememberAddEditNameHeight + 100;
        taskAddEditUnitHeight = (daysToRememberAddEditLabelHeight/2);

        taskAddEditRecursiveLabelTop = daysToRememberAddEditDetailsTop + daysToRememberAddEditNameHeight + 100;
        taskAddEditDayDateLabelTop = taskAddEditRecursiveLabelTop + taskAddEditUnitHeightLabel;
        taskAddEditStartTimeLabelTop = taskAddEditDayDateLabelTop + taskAddEditUnitHeightLabel;
        taskAddEditStopTimeLabelTop = taskAddEditStartTimeLabelTop + taskAddEditUnitHeightLabel;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskAddEditRecursiveTop = taskAddEditRecursiveLabelTop + taskAddEditUnitHeight;
        taskAddEditDayDateTop = taskAddEditDayDateLabelTop + taskAddEditUnitHeight;
        taskAddEditStartTimeTop = taskAddEditStartTimeLabelTop + taskAddEditUnitHeight;
        taskAddEditStopTimeTop = taskAddEditStopTimeLabelTop + taskAddEditUnitHeight;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        taskAddEditButtonTop = taskAddEditStopTimeTop + daysToRememberAddEditNameHeight + 100;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~
    }
    //--------------------------------------------------------------------------------------------------------------------------



    //--------------------------------------------------------------------------------------------------------------------------
    public static void homePageDynamicLayout(Context context)
    {
        setScreenSize(context);
        setTitleDimensions();

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        homeButtonWidth = (int)(screenWidth*0.28);
        homeButtonHeight = homeButtonWidth;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        homeButtonHorizontalGap = (int)(screenWidth*0.1);
        homeButtonVerticalGap = (int)(screenHeight*0.07);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        homeButtonLeftColumnLeft = (int)(screenWidth*0.17);
        homeButtonRightColumnLeft = homeButtonLeftColumnLeft + homeButtonWidth + homeButtonHorizontalGap;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        homeButtonTopRowTop = (int)(screenHeight*0.15);
        homeButtonMiddleRowTop = homeButtonTopRowTop + homeButtonHeight + homeButtonVerticalGap;
        homeButtonBottomRowTop = homeButtonMiddleRowTop + homeButtonHeight + homeButtonVerticalGap;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        homeButtonLabelHeight = (int)(homeButtonWidth/5.95);
        homeButtonLabelTopPadding = homeButtonVerticalGap/6;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        centerTextViewWidth = (int)(screenWidth*0.6);
        centerTextViewHeight = (int)(screenHeight*0.03);
    }
    //--------------------------------------------------------------------------------------------------------------------------



    //--------------------------------------------------------------------------------------------------------------------------
    public static void daysToRememberDynamicLayout(Context context)
    {
        setScreenSize(context);
        setTitleDimensions();

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberListViewTop = (int)(0.16*screenHeight);
        daysToRememberListViewBottom = 0;
        daysToRememberListViewHeight = (int)(0.52*screenHeight);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberButtonDiameter = (int)(0.125*screenHeight);

        daysToRememberButtonLabelWidth = (int)(0.25*screenWidth);
        daysToRememberButtonLabelHeight = (int)(daysToRememberButtonLabelWidth/5.87);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddTop = daysToRememberListViewTop + daysToRememberListViewHeight + (int)(0.02*screenHeight);
        daysToRememberAddLeft = rowLeft;
        daysToRememberAddLabelLeft = daysToRememberAddLeft - ((daysToRememberButtonLabelWidth - daysToRememberButtonDiameter)/2);
        daysToRememberAddLabelTop = daysToRememberAddTop + daysToRememberButtonDiameter + 5;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberDeleteAllLeft = screenWidth - rowRight - daysToRememberButtonDiameter;
        daysToRememberDeleteAllLabelLeft = daysToRememberDeleteAllLeft - ((daysToRememberButtonLabelWidth - daysToRememberButtonDiameter)/2);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberDeleteLeft = rowLeft + (daysToRememberDeleteAllLeft - daysToRememberAddLeft)/2;
        daysToRememberDeleteLabelLeft = daysToRememberDeleteLeft - ((daysToRememberButtonLabelWidth - daysToRememberButtonDiameter)/2);
    }

    public static void daysToRememberRowDynamicLayout(Context context)
    {
        setScreenSize(context);

        rowWidth = (int)(0.84*screenWidth);
        rowHeight = (int)(0.3*screenWidth);

        rowLeft = (int)(0.08*screenWidth);
        rowTop = 0;
        rowRight = (int)(0.08*screenWidth);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        eventImageWidth = (int)(0.65*rowHeight);
        eventImageHeight = (int)(0.65*rowHeight);

        eventImageLeft = (int)(0.08*rowWidth);
        eventImageTop = (int)(0.15*rowHeight);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        tickImageWidth = 2*eventImageLeft;
        tickImageHeight = 2*eventImageLeft;
        tickImageLeft = rowLeft - (tickImageWidth/2);
        tickImageTop = (rowHeight/2) - (tickImageHeight/2);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        eventNameWidth = rowWidth - eventImageLeft - eventImageWidth - 32;
        eventNameHeight = (int)(0.4*eventImageHeight);

        eventNameLeft = eventImageLeft + eventImageWidth + 30;
        eventNameTop = eventImageTop;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        eventDateWidth = eventNameWidth;
        eventDateHeight = (int)(0.37*eventImageHeight);

        eventDateLeft = eventNameLeft;
        eventDateTop = eventNameTop + eventNameHeight + 1;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        eventDetailsWidth = eventNameWidth;
        eventDetailsHeight = (int)(0.22*eventImageHeight);

        eventDetailsLeft = eventNameLeft;
        eventDetailsTop = eventDateTop + eventDateHeight;
    }

    public static void daysToRememberAddEditDynamicLayout(Context context)
    {
        setScreenSize(context);
        setTitleDimensions();

        daysToRememberAddEditLabelWidth = (int)(0.4*screenWidth);
        daysToRememberAddEditLabelHeight = (int)(daysToRememberAddEditLabelWidth/3.79);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddEditTornPaperWidth = (int)(0.88*screenWidth);
        daysToRememberAddEditTornPaperLeft = (int)(0.06*screenWidth);
        daysToRememberAddEditTornPaperPaddingSide = (int)(0.085*screenWidth);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddEditNameLabelLeft = (int)(0.5*screenWidth - 0.5*daysToRememberAddEditLabelWidth);
        daysToRememberAddEditNameLabelTop = 0;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddEditNameHeight = (int)(0.3*screenWidth);   //Torn Paper Height for Event Name
        daysToRememberAddEditNameTop = daysToRememberAddEditNameLabelTop + (daysToRememberAddEditLabelHeight/2);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddEditDateLabelTop = daysToRememberAddEditNameTop + daysToRememberAddEditNameHeight + 100;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddEditDateHeight = daysToRememberAddEditNameHeight;
        daysToRememberAddEditDateTop = daysToRememberAddEditDateLabelTop + (daysToRememberAddEditLabelHeight/2);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddEditCategoryLabelTop = daysToRememberAddEditDateTop + daysToRememberAddEditDateHeight + 100;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddEditCategoryHeight = daysToRememberAddEditNameHeight;
        daysToRememberAddEditCategoryTop = daysToRememberAddEditCategoryLabelTop + (daysToRememberAddEditLabelHeight/2);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddEditDetailsLabelTop = daysToRememberAddEditCategoryTop + daysToRememberAddEditCategoryHeight + 100;

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddEditDetailsHeight = (int)(0.8*screenWidth);
        daysToRememberAddEditDetailsTop = daysToRememberAddEditDetailsLabelTop + (daysToRememberAddEditLabelHeight/2);

        //----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~----~~~~

        daysToRememberAddEditButtonWidth = (int)(0.45*daysToRememberAddEditTornPaperWidth);
        daysToRememberAddEditButtonHeight = (int)(daysToRememberAddEditButtonWidth/2.19);

        daysToRememberAddEditSaveButtonLeft = daysToRememberAddEditTornPaperLeft + (int)(0.02*daysToRememberAddEditTornPaperWidth);
        daysToRememberAddEditDeleteButtonLeft = daysToRememberAddEditSaveButtonLeft + daysToRememberAddEditButtonWidth + (int)(0.06*daysToRememberAddEditTornPaperWidth);
        daysToRememberAddEditButtonTop = daysToRememberAddEditDetailsTop + daysToRememberAddEditDetailsHeight + 100;

        tasksPoolAddEditButtonTop = daysToRememberAddEditDetailsTop + daysToRememberAddEditNameHeight + 100;
    }
    //--------------------------------------------------------------------------------------------------------------------------
}
