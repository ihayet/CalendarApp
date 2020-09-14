package com.quadstack.everroutine.notification_manager;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.quadstack.everroutine.R;

/**
 * Created by Sakib on 2/11/2017.
 */
public class Loading_Notifications extends Activity{

    private TextView idText;
    private TextView mText;
    private TextView nameText;
    private TextView dateText;
    private TextView typeText;
    private TextView detailsText;

    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_main2);

       /* idText = (TextView) findViewById(R.id.editText);
        mText = (TextView) findViewById(R.id.editText6);*/
       nameText = (TextView) findViewById(R.id.editText2);
        dateText = (TextView) findViewById(R.id.editText3);
        typeText = (TextView) findViewById(R.id.editText4);
        detailsText = (TextView) findViewById(R.id.editText5);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
           /* int id = extras.getInt("id");
            int m = extras.getInt("month");*/
            String name = extras.getString("event_name");
            String date = extras.getString("event_date");
            String type = extras.getString("event_type");
            String details = extras.getString("event_details");
           /* String ids = "" + id;
            String ms = "" + m;*/
           /* idText.setText(ids);*/
            nameText.setText(name);
            dateText.setText(date);
            typeText.setText(type);
            detailsText.setText(details);
           /* mText.setText(ms);*/

        }


    }

}
