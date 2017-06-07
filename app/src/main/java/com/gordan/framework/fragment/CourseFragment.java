package com.gordan.framework.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.gordan.framework.R;
import com.gordan.framework.util.LogUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {

    RelativeLayout rlAllCategory;

    EditText etKeyword;

    PopupWindow window;

    ArrayList<String> category=new ArrayList<String>();

    ArrayList<String> subCategory=new ArrayList<String>();

    public CourseFragment() {  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View root=inflater.inflate(R.layout.fragment_course, container, false);

        rlAllCategory=(RelativeLayout)root.findViewById(R.id.rl_course_all_category);

        etKeyword=(EditText)root.findViewById(R.id.tv_keyword);

        etKeyword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                {
                    etKeyword.setSelection(etKeyword.getText().length());
                }
            }
        });

        rlAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showPopWindows(v);
            }
        });

        return root;
    }


    private void showPopWindows(View parent)
    {
        category.add("篮球");
        category.add("足球");
        category.add("排球");
        category.add("羽毛球");
        category.add("网球");
        category.add("棒球");
        category.add("游泳");
        category.add("瑜伽");

        subCategory.add("子类运动");
        subCategory.add("子类运动");
        subCategory.add("子类运动");
        subCategory.add("子类运动");
        subCategory.add("子类运动");
        subCategory.add("子类运动");

        View root=View.inflate(this.getActivity(),R.layout.filter,null);

        ListView lvLeft=(ListView)root.findViewById(R.id.lv_category_left);

        ListView lvRight=(ListView)root.findViewById(R.id.lv_category_right);

        int resId=android.R.layout.simple_spinner_item;
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this.getActivity(),resId,category);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        lvLeft.setAdapter(adapter);

        ArrayAdapter<String> subAdapter=new ArrayAdapter<String>(this.getActivity(),resId,subCategory);
        lvRight.setAdapter(subAdapter);

        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                LogUtil.i("XPZ", "当前的位置：" + position);
            }
        });

        window=new PopupWindow(root,600,600);//windows显示的空间大小
        window.setBackgroundDrawable(getDrawable(this.getActivity()));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setFocusable(true);
        window.update();
        window.showAtLocation(parent, Gravity.CENTER_VERTICAL,-50,-50);// window显示时 在父View上的位置


    }

    /**
     * 给PopWindows设置一个透明的背景 点击外部消失
     *
     * @param cxt
     * @return
     */
    private Drawable getDrawable(Context cxt)
    {
        ShapeDrawable drawable =new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(cxt.getResources().getColor(android.R.color.transparent));
        return drawable;
    }


}
