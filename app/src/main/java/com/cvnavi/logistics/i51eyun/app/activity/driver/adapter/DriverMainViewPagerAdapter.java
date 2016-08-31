package com.cvnavi.logistics.i51eyun.app.activity.driver.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/6/17.
 */
public class DriverMainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> list = null;


    public DriverMainViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }


    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
