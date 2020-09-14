package com.quadstack.everroutine.about_us;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.double_drawer.DoubleDrawerActivity;

public class AboutUsActivity extends DoubleDrawerActivity
{
    View aboutUsView;
    ImageView title;
    ListView list;
    String[] itemname ={
            "Ishrak Hayet Ratul",
            "Saadman Farhad",
            "Tanveer Fahad Haq",
            "Sakibul Islam"
    };

    Integer[] imgid={
            R.drawable.ratul,
            R.drawable.saadman,
            R.drawable.tanveer,
            R.drawable.about,
    };

    String [] email ={
            "ishrak_6@htomail.com",
            "saadmanheavy@gmail.com",
            "tfh_09@yahoo.com",
            "sakibulislamiutcse@gmail.com"
    };

    ImageView eatlLabel, eatlLogo, quadstack, attributionLabel, socialLabel, socialLogo;
    TextView eatlDescription, attributionDescription, socialText;
    RelativeLayout eatlLayout, socialLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutUsView = super.setContentLayout(R.layout.activity_about_us, R.layout.custom_actionbar_layout);

        Adapter adapter=new Adapter(this, itemname,email, imgid);
        list=(ListView)aboutUsView.findViewById(R.id.listView);
        list.setAdapter(adapter);

        eatlLabel = (ImageView)aboutUsView.findViewById(R.id.eatlLabel);
        eatlLogo = (ImageView)aboutUsView.findViewById(R.id.eatlLogo);
        quadstack = (ImageView)aboutUsView.findViewById(R.id.quadstack);
        attributionLabel = (ImageView)aboutUsView.findViewById(R.id.attributionLabel);
        socialLabel = (ImageView)aboutUsView.findViewById(R.id.socialLabel);
        socialLogo = (ImageView)aboutUsView.findViewById(R.id.facebook);
        eatlDescription = (TextView)aboutUsView.findViewById(R.id.eatlDescription);
        attributionDescription = (TextView)aboutUsView.findViewById(R.id.attributionDescription);
        socialText = (TextView)aboutUsView.findViewById(R.id.socialText);
        eatlLayout = (RelativeLayout)aboutUsView.findViewById(R.id.eatlDetails);
        socialLayout = (RelativeLayout)aboutUsView.findViewById(R.id.socialLayout);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

        title = (ImageView)aboutUsView.findViewById(R.id.aboutUsTitle);

        setDynamicLayout();
        addListeners();
    }

    @Override
    protected void setDynamicLayout()
    {
        title.getLayoutParams().width = Utility.titleWidth;
        title.getLayoutParams().height = Utility.titleHeight;
        ((RelativeLayout.LayoutParams)title.getLayoutParams()).setMargins(0, 0, 0, 0);
        title.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.about_title, Utility.titleWidth, Utility.titleHeight));

        //-------------------------------------------------------------------------------------------------------------------------------------------

        ((RelativeLayout.LayoutParams)eatlLayout.getLayoutParams()).setMargins(0, -(Utility.daysToRememberAddEditLabelHeight/2), 0, 0);

        eatlLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        eatlLabel.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)eatlLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, 30, 0, 0);

        ((RelativeLayout.LayoutParams)eatlDescription.getLayoutParams()).width = Utility.daysToRememberAddEditTornPaperWidth;
        ((RelativeLayout.LayoutParams)eatlDescription.getLayoutParams()).height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)eatlDescription.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, -(Utility.daysToRememberAddEditLabelHeight/2), 0, 0);
        eatlDescription.setPadding(5, 0, 15, 0);

        //-------------------------------------------------------------------------------------------------------------------------------------------

        quadstack.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        quadstack.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)quadstack.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, 30, 0, 0);

        //-------------------------------------------------------------------------------------------------------------------------------------------

        attributionLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        attributionLabel.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)attributionLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, 30, 0, 0);

        attributionDescription.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        attributionDescription.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)attributionDescription.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, -(Utility.daysToRememberAddEditLabelHeight/2), 0, 0);
        attributionDescription.setPadding(55, 55, 15, 0);

        //-------------------------------------------------------------------------------------------------------------------------------------------

        ((RelativeLayout.LayoutParams)socialLayout.getLayoutParams()).setMargins(0, -(Utility.daysToRememberAddEditLabelHeight/2), 0, 0);

        socialLabel.getLayoutParams().width = Utility.daysToRememberAddEditLabelWidth;
        socialLabel.getLayoutParams().height = Utility.daysToRememberAddEditLabelHeight;
        ((RelativeLayout.LayoutParams)socialLabel.getLayoutParams()).setMargins(Utility.daysToRememberAddEditNameLabelLeft, 30, 0, 0);

        //-------------------------------------------------------------------------------------------------------------------------------------------

        socialLogo.setPadding(100, 55, 0, 0);

        socialText.getLayoutParams().width = Utility.daysToRememberAddEditTornPaperWidth;
        socialText.getLayoutParams().height = Utility.daysToRememberAddEditNameHeight;
        ((RelativeLayout.LayoutParams)socialText.getLayoutParams()).setMargins(Utility.daysToRememberAddEditTornPaperLeft, -(Utility.daysToRememberAddEditLabelHeight/2), 0, 0);
        socialText.setPadding(0, 100, 15, 0);

        //-------------------------------------------------------------------------------------------------------------------------------------------
    }

    @Override
    protected void addListeners()
    {
        socialLabel.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    startActivity(newFacebookIntent(getApplicationContext().getPackageManager(), "https://www.facebook.com/EverRoutine"));
                }
                return true;
            }
        });

        socialLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    startActivity(newFacebookIntent(getApplicationContext().getPackageManager(), "https://www.facebook.com/EverRoutine"));
                }
                return true;
            }
        });

        socialLogo.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    startActivity(newFacebookIntent(getApplicationContext().getPackageManager(), "https://www.facebook.com/EverRoutine"));
                }
                return true;
            }
        });

        socialText.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    startActivity(newFacebookIntent(getApplicationContext().getPackageManager(), "https://www.facebook.com/EverRoutine"));
                }
                return true;
            }
        });
    }

    public static Intent newFacebookIntent(PackageManager pm, String url)
    {
        Uri uri = Uri.parse(url);

        try
        {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);

            if(applicationInfo.enabled)
            {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        }
        catch(PackageManager.NameNotFoundException e)
        {

        }

        return new Intent(Intent.ACTION_VIEW, uri);
    }
}
