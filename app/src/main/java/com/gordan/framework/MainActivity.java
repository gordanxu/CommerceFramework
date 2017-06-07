package com.gordan.framework;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gordan.framework.fragment.CircleFragment;
import com.gordan.framework.fragment.CourseFragment;
import com.gordan.framework.fragment.HomeFragment;
import com.gordan.framework.fragment.MemberFragment;
import com.gordan.framework.http.HttpClientConnector;
import com.gordan.framework.http.NetDataParams;

public class MainActivity extends BaseActivity implements View.OnClickListener
{
    HomeFragment homeFragment;
    CourseFragment courseFragment;
    CircleFragment circleFragment;
    MemberFragment memberFragment;

    FragmentManager fm=null;

    RadioGroup rgMenu;

    RadioButton radHome;
    RadioButton radCourse;
    RadioButton radCircle;
    RadioButton radMember;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rgMenu=(RadioGroup)findViewById(R.id.rg_menu);
        fm=this.getFragmentManager();


        new Thread(new Runnable() {
            @Override
            public void run()
            {

                NetDataParams params=new NetDataParams();
                params.httpUrl="http://10.101.111.186/csm_app/api/stores/21/goods/7?access_token=thtf-dfe516a6f7a74c928190d9f73543cb57812889be115e4536926a624f286535e2&t=collect";
                params.method=NetDataParams.METHOD_POST;
                HttpClientConnector.httpMethod(params);


                params.httpUrl="http://10.101.111.186/csm_app/api/stores/21/goods/7?access_token=thtf-dfe516a6f7a74c928190d9f73543cb57812889be115e4536926a624f286535e2&t=uncollect";
                params.method=NetDataParams.METHOD_Delete;
                HttpClientConnector.httpMethod(params);


            }
        }).start();

        radHome=(RadioButton)this.findViewById(R.id.rad_home);
        radCourse=(RadioButton)this.findViewById(R.id.rad_course);
        radCircle=(RadioButton)this.findViewById(R.id.rad_circle);
        radMember=(RadioButton)this.findViewById(R.id.rad_member);

        homeFragment=new HomeFragment();
        FragmentTransaction transaction= fm.beginTransaction();
        transaction.replace(R.id.fragment, homeFragment);
        transaction.commit();

        rgMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                FragmentTransaction transaction= fm.beginTransaction();
                switch (checkedId)
                {
                   case  R.id.rad_home:

                       radHome.setTextColor(Color.parseColor("#E1660A"));
                       radCourse.setTextColor(Color.BLACK);
                       radCircle.setTextColor(Color.BLACK);
                       radMember.setTextColor(Color.BLACK);

                       homeFragment=new HomeFragment();

                    transaction.replace(R.id.fragment, homeFragment);

                       break;

                  case  R.id.rad_course:

                      radCourse.setTextColor(Color.parseColor("#E1660A"));
                      radHome.setTextColor(Color.BLACK);
                      radCircle.setTextColor(Color.BLACK);
                      radMember.setTextColor(Color.BLACK);

                      courseFragment=new CourseFragment();
                      transaction.replace(R.id.fragment, courseFragment);


                     break;

                    case R.id.rad_circle:

                        radCircle.setTextColor(Color.parseColor("#E1660A"));
                        radHome.setTextColor(Color.BLACK);
                        radCourse.setTextColor(Color.BLACK);
                        radMember.setTextColor(Color.BLACK);

                        circleFragment=new CircleFragment();

                        transaction.replace(R.id.fragment,circleFragment);


                        break;

                    case R.id.rad_member:

                        radMember.setTextColor(Color.parseColor("#E1660A"));
                        radHome.setTextColor(Color.BLACK);
                        radCourse.setTextColor(Color.BLACK);
                        radCircle.setTextColor(Color.BLACK);

                        memberFragment=new MemberFragment();
                        transaction.replace(R.id.fragment, memberFragment);

//                        transaction.add(R.id.fragment,memberFragment);

                        break;
                }

                transaction.commit();

            }
        });














    }




    @Override
    public void onClick(View v)
    {



    }
}
