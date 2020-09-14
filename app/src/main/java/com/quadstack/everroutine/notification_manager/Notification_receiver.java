package com.quadstack.everroutine.notification_manager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.quadstack.everroutine.R;

/**
 * Created by Sakib on 2/11/2017.
 */
public class Notification_receiver extends BroadcastReceiver {
    int id,month;

    String name,date,type,details;

    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle extras = intent.getExtras();
        if(extras != null) {
            id = extras.getInt("id");
            name = extras.getString("event_name");
            date = extras.getString("event_date");
            type = extras.getString("event_type");
            details = extras.getString("event_details");
        }

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, Loading_Notifications.class);

        notificationIntent.putExtra("event_name", name);
        notificationIntent.putExtra("event_date", date);
        notificationIntent.putExtra("event_type", type);
        notificationIntent.putExtra("event_details", details);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bitmap_image;
        String contentText ="SWIPE BELOW TO EXPAND";

        /*if (type == "Birthday" )
        {
            bitmap_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.days_to_remember);
        }
        else if (type == "Weeding" ) {
            bitmap_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.days_to_remember_add);
        }
        else
        {
             bitmap_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.days_to_remember_addedit_save);
        }*/

        bitmap_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ever_routine_notification_icon);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
        style.setSummaryText(details);

        android.support.v4.app.NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ever_routine_notification_icon)
                .setContentTitle(name)
                .setContentText(contentText)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setTicker("EVER ROUTINE");

        mNotifyBuilder.setStyle(style);
        /*notificationManager.notify( Integer.parseInt(id), mNotifyBuilder.build());*/
        notificationManager.notify( id, mNotifyBuilder.build());


    }


}