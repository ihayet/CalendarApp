package com.quadstack.everroutine.notification_manager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.quadstack.everroutine.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class notification_activity extends Activity {

    int id,formattedmonth,formattedate;
    String name,date,type,details;
    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_main);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getInt("id");
            name = extras.getString("event_name");
            date = extras.getString("event_date");
            type = extras.getString("event_type");
            details = extras.getString("event_details");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date fdate = sdf.parse(date);
            formattedmonth = fdate.getMonth();
            formattedate = fdate.getDate();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, formattedmonth);
        calendar.set(Calendar.DAY_OF_MONTH, formattedate);
        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);

        Intent intent1 = new Intent(notification_activity.this, Notification_receiver.class);
        intent1.putExtra("id",id);
        intent1.putExtra("month",formattedmonth);
        intent1.putExtra("event_name", name);
        intent1.putExtra("event_date", date);
        intent1.putExtra("event_type", type);
        intent1.putExtra("event_details", details);

       /* startActivity(intent1);*/


        PendingIntent pendingIntent = PendingIntent.getBroadcast(notification_activity.this, id,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) notification_activity.this.getSystemService(notification_activity.this.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


