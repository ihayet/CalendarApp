package com.quadstack.everroutine.tasks.fragments_tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sakib on 10/4/2016.
 */

public class TaskFragmentPagerAdapter extends FragmentPagerAdapter
{
    List<Fragment> listFragments;

    public TaskFragmentPagerAdapter(FragmentManager fm, List<Fragment> listFragments)
    {
        super(fm);
        this.listFragments = listFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }
}
