package com.gordan.framework.net.manager;

import com.gordan.framework.http.NetDataManager;
import com.gordan.framework.http.NetDataParams;
import com.gordan.framework.net.OnSoapImplListener;
import com.gordan.framework.net.model.AddressBean;

import java.util.HashMap;

/**
 * Created by Gordan on 2015/11/6.
 *
 * AddressManager此类一个Activity只允许声明一个
 *
 * 如何让Manager的实例去和Activity绑定？？？？？
 *
 * URL地址不会重复，调用方法时也会传递参数，总是获取的最新参数
 *
 */
public class AddressManager
{
    private static AddressManager instance;

    private OnSoapImplListener listener;

    public String provinceUrl=WebURL.URL_BASE+WebURL.URL_PROVINCES_GET;

    public String cityUrl=WebURL.URL_BASE+WebURL.URL_CITES_GET;

    public String areaUrl=WebURL.URL_BASE+WebURL.URL_SUBAREAS_GET;

    private AddressManager()
    {    }

    public static AddressManager getInstance(OnSoapImplListener listener)
    {
        if(instance == null)
        {
            synchronized (AddressManager.class)
            {
                instance=new AddressManager();
            }
        }
        instance.listener=listener;
        return  instance;
    }


    public void getProvinces()
    {
        NetDataParams params = new NetDataParams(provinceUrl, NetDataParams.METHOD_GET, NetDataParams.PARAMS_TYPE_JSON,null);
        params.mListener = listener;
        params.resultType = NetDataParams.RESULT_TYPE_ARRAY;
        NetDataManager.getInstance().setData(AddressBean.class, params);
    }


    public void getCities(HashMap<String,Object> map,int provinceId)
    {
        cityUrl=String.format(cityUrl,provinceId);
        NetDataParams params = new NetDataParams(cityUrl, NetDataParams.METHOD_GET, NetDataParams.PARAMS_TYPE_JSON,null);
        params.mListener = listener;
        params.initParams(map);
        params.resultType = NetDataParams.RESULT_TYPE_ARRAY;
        NetDataManager.getInstance().setData(AddressBean.class,params);
    }


}
