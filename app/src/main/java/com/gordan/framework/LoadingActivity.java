package com.gordan.framework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gordan.framework.db.dao.NewsDao;
import com.gordan.framework.db.model.TB_News;
import com.gordan.framework.http.NetDataManager;
import com.gordan.framework.http.NetDataParams;
import com.gordan.framework.net.OnSoapImplListener;
import com.gordan.framework.net.manager.AddressManager;
import com.gordan.framework.net.model.AddressBean;
import com.gordan.framework.util.LogUtil;

import java.util.List;

public class LoadingActivity extends BaseActivity implements OnSoapImplListener
{
    private final static String tag="LoadingActivity";

    private Button btn;

    private AddressManager addressManager=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        btn=(Button)this.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent tent=new Intent(LoadingActivity.this,XListviewActivity.class);
                LoadingActivity.this.startActivity(tent);
            }
        });

        addressManager=AddressManager.getInstance(this);
        NewsDao dao=NewsDao.getInstance(this);
        TB_News myNews=null;
        for(int i=0;i<100;i++)
        {
            myNews=new TB_News();
            myNews.setCategory(i / 10);
            myNews.setCreatetime(System.currentTimeMillis());
            myNews.setTitle("猴年春晚节目单独家曝光("+i+")");
            myNews.setContent("TFBOYS和胡歌等将参加春晚，靖王王凯将不参加春晚！");
            dao.insertNews(myNews);
        }





//        addressManager.getProvinces();
//        LogUtil.i(tag, "开始获取省份");
//
//        addressManager.getCities(null,11);
//        LogUtil.i(tag, "开始获取城市");



    }

    @Override
    public void onImplCompletionListener(Class<?> cls, NetDataParams params)
    {
        if(params.httpUrl.equals(addressManager.provinceUrl))
        {
            List<AddressBean> provinces= NetDataManager.getInstance().getArrayData(addressManager.provinceUrl);
            LogUtil.i(tag,"成功获取省份:"+provinces.size()+"个");
        }
        else if(params.httpUrl.equals(addressManager.cityUrl))
        {
            List<AddressBean> cities= NetDataManager.getInstance().getArrayData(addressManager.cityUrl);
            LogUtil.i(tag,"成功获取城市:"+cities.size()+"个");
        }

    }

    @Override
    public void onImplCancelledListener(NetDataParams params,String url, String keys)
    {

    }

    @Override
    public void onImplErrorListener(NetDataParams params,String url,String keys) {

    }
}
