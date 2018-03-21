package com.chae_s_j.alarmapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.chae_s_j.alarmapp.Activity.MainActivityPager.GPSPager;
import com.chae_s_j.alarmapp.Activity.MainActivityPager.ListPager;
import com.chae_s_j.alarmapp.Activity.MainActivityPager.SettingPager;
import com.chae_s_j.alarmapp.Activity.MainActivityPager.TimerPager;

/**
 * Created by chae_s_j on 2018-01-03.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    int CountNumber;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        CountNumber = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            ListPager Tab = new ListPager();
            return Tab;
        }else if(position==1){
            GPSPager Tab = new GPSPager();
            return Tab;
        }else if(position==2){
            TimerPager Tab = new TimerPager();
            return Tab;
        }else if(position==3){
            SettingPager Tab = new SettingPager();
            return Tab;
        }else{
            return null;
        }
    }

    @Override
    public int getCount() {
        return CountNumber;
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}
