package com.quadstack.everroutine.tasks;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.days_to_remember_tasks_pool.DaysToRemember_TasksPool_DataModel;
import com.quadstack.everroutine.double_drawer.DoubleDrawerActivity;
import com.quadstack.everroutine.tasks.fragments_tab.calendar_fragment.CalendarFragment;
import com.quadstack.everroutine.tasks.fragments_tab.daily_fragment.DailyFragment;
import com.quadstack.everroutine.tasks.fragments_tab.TaskFragmentPagerAdapter;
import com.quadstack.everroutine.tasks.fragments_tab.daily_fragment.DataModel;
import com.quadstack.everroutine.tasks.fragments_tab.weekly_fragment.WeeklyFragment;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends DoubleDrawerActivity implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener
{
    View TaskView = null;

    ImageView titleView = null;

    ViewPager viewPager;
    TabHost tabHost;

    TextView dailyTabBar, weeklyTabBar, calendarTabBar;

    public static int taskViewType = 0;
    public static int titleResource = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskView = setContentLayout(R.layout.activity_tab, R.layout.custom_actionbar_layout);

        titleView = (ImageView)TaskView.findViewById(R.id.tabTitle);

        taskViewType = (getIntent().getExtras()).getInt("TaskViewType");

        initViewPager();
        initTabHost();

        setDynamicLayout();
        addListeners();
    }

    private void initTabHost()
    {
        tabHost = (TabHost)TaskView.findViewById(R.id.tabHost);

        tabHost.setup();

        String[] tabNames = {"Daily", "Weekly" , "Calendar"};

        for(int i=0;i<tabNames.length;i++)
        {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(getApplicationContext()));
            tabHost.addTab(tabSpec);

            tabHost.getTabWidget().getChildAt(i).setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.taskTabBarWidth, Utility.taskTabBarHeight)));
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().width = Utility.taskTabBarWidth;
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = Utility.taskTabBarHeight;
        }

        tabHost.setOnTabChangedListener(this);

        dailyTabBar = (TextView)tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        weeklyTabBar = (TextView)tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        calendarTabBar = (TextView)tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);

        dailyTabBar.setTextSize(22);
        weeklyTabBar.setTextSize(15);
        calendarTabBar.setTextSize(15);
    }

    @Override
    public void onBackPressed()
    {
        if(DailyFragment.longPressSelectMode == false)
        {
            super.onBackPressed();
        }
        else
        {
            for(Object i : DailyFragment.selectedIDList)
            {
                ((DataModel)DailyFragment.listAdapter.getItem((int)i)).setTickImgResource(0);
            }

            DailyFragment.selectedIDList.removeAll(DailyFragment.selectedIDList);

            DailyFragment.listAdapter.notifyDataSetChanged();

            DailyFragment.longPressSelectMode = false;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int selectedItem) {
        tabHost.setCurrentTab(selectedItem);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }

    @Override
    public void onTabChanged(String tabId)
    {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);

        switch(selectedItem)
        {
            case 0:
                dailyTabBar.setTextSize(22);
                weeklyTabBar.setTextSize(15);
                calendarTabBar.setTextSize(15);
                break;
            case 1:
                dailyTabBar.setTextSize(15);
                weeklyTabBar.setTextSize(22);
                calendarTabBar.setTextSize(15);
                break;
            case 2:
                dailyTabBar.setTextSize(15);
                weeklyTabBar.setTextSize(15);
                calendarTabBar.setTextSize(22);
                break;
            default:
                break;
        }

        HorizontalScrollView hScrollView = (HorizontalScrollView)TaskView.findViewById(R.id.h_scroll_view);
        View tabView = tabHost.getCurrentTabView();
        int scrollPos = tabView.getLeft() - (hScrollView.getWidth() - tabView.getWidth()) / 2;
        hScrollView.smoothScrollTo(scrollPos, 0);
    }

    public class FakeContent implements TabHost.TabContentFactory {
        Context context;

        public FakeContent(Context mcontext){
            context = mcontext;
        }
        @Override
        public View createTabContent(String tag){
            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
    }

    private void initViewPager()
    {
        viewPager = (ViewPager)TaskView.findViewById(R.id.view_pager);

        List<Fragment> listfragments = new ArrayList<Fragment>();

        DailyFragment dailyFragment = new DailyFragment();
        WeeklyFragment weeklyFragment = new WeeklyFragment();
        CalendarFragment calendarFragment = new CalendarFragment();

        dailyFragment.initFragment(TabActivity.this, taskViewType);
        weeklyFragment.initFragment(TabActivity.this, taskViewType);
        calendarFragment.initFragment(TabActivity.this, taskViewType);

        listfragments.add(dailyFragment);
        listfragments.add(weeklyFragment);
        listfragments.add(calendarFragment);

        TaskFragmentPagerAdapter TaskFragmentPagerAdapter = new TaskFragmentPagerAdapter(
                getSupportFragmentManager(), listfragments);

        viewPager.setAdapter(TaskFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void setDynamicLayout()
    {
        titleView.getLayoutParams().width = Utility.titleWidth;
        titleView.getLayoutParams().height = Utility.titleHeight;
        ((RelativeLayout.LayoutParams)titleView.getLayoutParams()).setMargins(0, 0, 0, 0);

        switch(taskViewType)
        {
            case Utility.official_task:
                titleResource = R.drawable.official_task_title;
                break;
            case Utility.academic_task:
                titleResource = R.drawable.academic_task_title;
                break;
            case Utility.miscellaneous_task:
                titleResource = R.drawable.miscellaneous_task_title;
                break;
            case Utility.combined_task:
                titleResource = R.drawable.combined_task_title;
                break;
            default:
                break;
        }
        titleView.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), titleResource, Utility.titleWidth, Utility.titleHeight));
    }

    @Override
    protected void addListeners()
    {

    }
}
