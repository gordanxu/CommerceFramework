package com.gordan.framework.http;

import com.gordan.framework.net.OnSoapImplListener;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;

/**
 * 
 * 类: NetDataParams
 * 描述: 网络数据参数类,用于保存网络数据参数，具备基本网络请求参数类的功能-{@link BaseNetParams}
 * 作者: Gordan
 * 日期:	 2015-11-01
 */
public class NetDataParams extends BaseNetParams{
	public boolean isCacheData = false;	//是否为暂存数据
	public boolean sqliteflag = false;	//是否保存到数据库中
	public Runnable rsTask = null;		//数据刷新任务
	public OnSoapImplListener mListener;
	public NetDataParams(){}
	
	public NetDataParams(String httpUrl,String method,int paramsType,ArrayList<Header> headers){
		this.httpUrl = httpUrl;
		this.method = method;
		this.paramsType = paramsType;
		if(headers!=null && headers.size()>0)
		{
			//此处可以处理某些服务端会对客户端的请求进行Cookie的验证
			//可以带上上次从服务端获取的Cookie信息
			this.headers=new Header[headers.size()+1];
			for (int i=0;i<headers.size();i++)
			{
				this.headers[i]=headers.get(i);
			}
			this.headers[headers.size()]=new BasicHeader("Cookie","");
		}
	}


}
