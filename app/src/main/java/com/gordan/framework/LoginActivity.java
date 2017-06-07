package com.gordan.framework;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.gordan.framework.adapter.LoadingAdapter;
import com.gordan.framework.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends FragmentActivity {

    private ViewPager vpLoading;

    private LoadingAdapter adapter;

    List<Fragment> data=new ArrayList<Fragment>();

    ArrayList<String> arraysTitle=new ArrayList<String>();

    int[] picResources={R.drawable.loading_1,R.drawable.loading_2,R.drawable.loading_3};

//    int[] picResources={R.drawable.land_1,R.drawable.land_2,R.drawable.land_3};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        LogUtil.i("XPZ","Loading界面！！");

        vpLoading=(ViewPager)this.findViewById(R.id.vp_loading);

        arraysTitle.add("高端豪宅，高端豪宅，高端豪宅");
        arraysTitle.add("漂亮的房子，漂亮的房子，漂亮的房子");
        arraysTitle.add("专业的顾问，丽兹行，丽兹行");

        for (int i=0;i<picResources.length;i++)
        {
            LoadingFragment fragment=LoadingFragment.
                    newInstance(arraysTitle.get(i),picResources[i]);
            data.add(fragment);
        }

        adapter=new LoadingAdapter(this.getSupportFragmentManager(),data);
        vpLoading.setAdapter(adapter);
        vpLoading.setCurrentItem(0);

        vpLoading.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                vpLoading.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
