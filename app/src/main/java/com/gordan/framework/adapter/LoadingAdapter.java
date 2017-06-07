package com.gordan.framework.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gordan.framework.util.LogUtil;

import java.util.List;

/**
 * Created by Gordan on 2015/11/10.
 */
public class LoadingAdapter extends FragmentPagerAdapter
{
    private final static String tag="LoadingAdapter";

    List<Fragment> lists;

    @Override
    public int getCount()
    {
        return lists.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Fragment getItem(int position)
    {
        LogUtil.i(tag, "getItem()");

        return lists.get(position);
    }

    public LoadingAdapter(FragmentManager fm,List<Fragment> data)
    {
        super(fm);
        LogUtil.i(tag, "LoadingAdapter()");
        lists=data;
    }

}
