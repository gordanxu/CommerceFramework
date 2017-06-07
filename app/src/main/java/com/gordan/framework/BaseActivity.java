package com.gordan.framework;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by Gordan on 2015/10/22.
 */
public class BaseActivity extends Activity
{
    public BaseApplication instance=null;

    protected DisplayMetrics mDisplayMetrics=new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        instance=(BaseApplication)this.getApplication();
        instance.pushActiviy(this);
        this.getWindowManager().getDefaultDisplay().getRealMetrics(mDisplayMetrics);

        int width = mDisplayMetrics.widthPixels;  // 屏幕宽度（像素）
        int height = mDisplayMetrics.heightPixels;  // 屏幕高度（像素）
        float density = mDisplayMetrics.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = mDisplayMetrics.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        double diagonalPixels = Math.sqrt(Math.pow(width, 2)+Math.pow(height, 2)) ;
        double screenSize = diagonalPixels/(160*density);
    }


    protected void showMessage(String text)
    {
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
