package com.gordan.framework;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gordan.framework.util.LogUtil;

public class LoadingFragment extends Fragment
{
    private static final String tag="LoadingFragment";

    String title;
    int resId;

    public static LoadingFragment newInstance(String picTitle,int resId)
    {
        LogUtil.i(tag,"newInstance()方法");

        LoadingFragment instance=new LoadingFragment();
        Bundle param=new Bundle();
        param.putString("picTitle",picTitle);
        param.putInt("resId", resId);
        instance.setArguments(param);
        return instance;
    }

    public LoadingFragment()
    {  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(tag, "onCreate()方法");

        Bundle param=getArguments();
        this.title= param.getString("picTitle");
        this.resId= param.getInt("resId");
    }


    DisplayMetrics metrics=new DisplayMetrics();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        LogUtil.i(tag, "onCreateView()方法");
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        View root=inflater.inflate(R.layout.fragment_loading, null);
        ImageView ivPhoto=(ImageView)root.findViewById(R.id.iv_loading_photo);
        TextView tvTitle=(TextView)root.findViewById(R.id.tv_loading_title);

      ivPhoto.setImageResource(this.resId);

//        ivPhoto.setImageBitmap(showPartPicture(this.resId));

        tvTitle.setText(this.title);

        return root;
    }


    private Bitmap showPartPicture(int resId)
    {
        Bitmap map=null;

        Bitmap orignal= BitmapFactory.decodeResource(this.getActivity().getResources(),resId);

        LogUtil.i(tag,"图片大小为："+orignal.getByteCount()+"");

        int width=metrics.widthPixels;
        int height=metrics.heightPixels;

        LogUtil.i(tag,"屏幕宽度："+width);
        LogUtil.i(tag,"屏幕高度："+height);

        float scaleWidth = ((float)width)/orignal.getWidth();  //宽度不变
        float scaleHeight = ((float)height)/orignal.getHeight(); //高度缩小

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

        // 缩放图片动作
        matrix.postScale(scaleWidth,scaleHeight);
        // 新得到的图片是原图片经过变换填充到整个屏幕的图片

        map=Bitmap.createBitmap(orignal,0,0,width,height,null,true);

        return map;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        LogUtil.i(tag, "onAttach()方法");

    }

    @Override
    public void onDetach() {
        super.onDetach();

        LogUtil.i(tag, "onDetach()方法");


    }


}
