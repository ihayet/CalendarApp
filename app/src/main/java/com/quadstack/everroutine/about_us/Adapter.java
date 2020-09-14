package com.quadstack.everroutine.about_us;

/**
 * Created by User on 13-Jan-17.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quadstack.everroutine.R;

public class Adapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    //private final String[] description;
    private final String[] email;
    private final Integer[] imgid;

    public Adapter(Activity context, String[] itemname, String[] email, Integer[] imgid) {
        super(context, R.layout.about_us_listview, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        //this.description=description;
        this.email = email;
        this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.about_us_listview, null,true);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y/4-200;

        rowView.setMinimumHeight(height);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extraTextView = (TextView) rowView.findViewById(R.id.textView1);
        TextView emailTextView = (TextView)rowView.findViewById(R.id.textView2);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        extraTextView.setText("IUT CSE'13");
        emailTextView.setText(email[position]);
        return rowView;

    };
}
