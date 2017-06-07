package com.gordan.framework.fragment;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gordan.framework.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    int index=1;

    android.os.Handler handler=null;

    ViewPager viewPager;

    LinearLayout llIndicator;

    MyVPAdapter adapter;

    List<View> lists=new ArrayList<View>();

    int[] adResources={R.drawable.banner1,R.drawable.banner2,R.drawable.banner3,
            R.drawable.banner4,R.drawable.banner5};

    public HomeFragment()
    {
        
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View root=inflater.inflate(R.layout.fragment_home, container, false);
        viewPager=(ViewPager)root.findViewById(R.id.vp_ad);
        llIndicator=(LinearLayout)root.findViewById(R.id.ll_indicate);

        for (int i=0;i<adResources.length;i++)
        {
            ImageView view=new ImageView(this.getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
            view.setImageResource(adResources[i]);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            lists.add(view);
        }

        adapter=new MyVPAdapter(lists);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        setIndicatorStyle(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                viewPager.setCurrentItem(position);
                setIndicatorStyle(position);

                index=position;


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        handler=new Handler();

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                viewPager.setCurrentItem(index);
                setIndicatorStyle(index);

                index++;
                handler.postDelayed(this,3000);
            }
        },3000);



        return root;
    }

    private void setIndicatorStyle(int position)
    {
        int count=llIndicator.getChildCount();

        for(int i=0;i<count;i++)
        {
            TextView tv=(TextView)llIndicator.getChildAt(i);
            if((position%count) == i)
            {
                tv.setTextColor(Color.RED);
            }
            else
            {
                tv.setTextColor(Color.WHITE);
            }
        }
    }


    class MyVPAdapter extends PagerAdapter
    {

        //注意：此时所有的View 都需要装到List集合中 在后面的销毁Item中需要用到！
        private List<View> views;

        public MyVPAdapter(List<View> views)
        {
            super();
            this.views=views;
        }

        //返回Item的总数量 这里可以使得 ViewPager的首项和未项连贯起来
        //注意获取当前Item索引的写法是 position 除以size取余
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }


        //初始化 Item 视图
        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            try
            {
                container.addView(this.views.get((position%this.views.size())));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return this.views.get((position%this.views.size()));
        }

        //销毁 Item的视图
        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView(this.views.get((position%this.views.size())));
        }

        //必须要 重写此方法
        @Override
        public boolean isViewFromObject(View view, Object object)
        {

            return view == object;
        }
    }


}
