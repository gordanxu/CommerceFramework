package com.gordan.framework.http;

import android.os.Handler;

import com.google.gson.reflect.TypeToken;
import com.gordan.framework.net.INetDataManager;
import com.gordan.framework.net.OnNetSoapImplListener;
import com.gordan.framework.util.JsonUtil;
import com.gordan.framework.util.LogUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 类: NetDataManager
 * 描述: 网络数据管理类,主要包含三部分功能
 * 1,将HTTP请求的任务加入到请求队列中
 * 2,对HTTP请求的结果进行解析，并且存入HashMap中
 * 3,通知上层的ManagerHTTP请求的结果
 * 作者: Gordan
 * 日期: 2015-11-01
 */
public class NetDataManager implements INetDataManager,OnNetSoapImplListener {
	private static final String Tag = "NetDataManager";
	private static NetDataManager mManager;
	private static HashMap<String, Object> mCacheObject;	//临时数据，根据生存时间保存。
	private static HashMap<String, Object> mObject;			//持久数据，一直保存到程序退出位置
	private static HashMap<String, Integer> mLiveTime;		//数据的生存时间
	private static HashMap<String, Integer> mRsTime;		//数据的刷新时间
	private static HashMap<String, Runnable> mRsTask;   	//刷新任务列表
	private static HashMap<String, NetDataParams> mCacheParams;//临时数据，根据生存时间保存。
	private NetDataManager(){
		if(mCacheObject == null){
			mCacheObject = new HashMap<String, Object>();
		}
		if(mObject == null){
			mObject = new HashMap<String, Object>();
		}
		if(mLiveTime == null){
			mLiveTime = new HashMap<String, Integer>();
		}
		if(mRsTime == null){
			mRsTime = new HashMap<String, Integer>();
		}
		if(mRsTask == null){
			mRsTask = new HashMap<String, Runnable>();
		}
		if(mCacheParams == null){
			mCacheParams = new HashMap<String, NetDataParams>();
		}
	}
	public static NetDataManager getInstance(){
		if(mManager == null) {
			mManager = new NetDataManager();
		}
		return mManager;
	}
	
	@Override
	public int getDataCount()
	{
		return mObject.size()+mCacheObject.size();
	}


	@Override
	public Object getObjectData(String url)
	{
		Object obj=mObject.get(url);
		if(obj == null)
		{
			obj=mCacheObject.get(url);
		}
		return obj;
	}

	@Override
	public <T> T getData(String url) {

		T tmp = (T) mObject.get(url);
		if(tmp == null){//not find data from long map,try to find in cache map
			tmp = (T) mCacheObject.get(url);
		}
		return tmp;			
	}

	@Override
	public <T> List<T> getArrayData(String url)
	{
		List<T> tmp = (List<T>) mObject.get(url);
		if(tmp == null)
		{
			tmp=(List<T>) mCacheObject.get(url);
		}
		return tmp;
	}

	@Override
	public void removeData(Class<?> cls) {
		mCacheObject.remove(cls.getName());
		mRsTime.remove(cls.getName());
		mLiveTime.remove(cls.getName());
		mObject.remove(cls.getName());
		mRsTask.remove(cls.getName());
		mCacheParams.remove(cls.getName());
	}
	
	public void release(){
		mCacheObject.clear();
		mRsTime.clear();
		mLiveTime.clear();
		mObject.clear();
		mRsTask.clear();
		mCacheParams.clear();
		
		mCacheObject = null;
		mRsTime = null;
		mLiveTime = null;
		mObject = null;
		mRsTask = null;
		mCacheParams = null;
	}
	
	//private Handler mHandler;
	private String mActivity;
	@Override
	public void startAutoRefresh(Handler handler,String activity) {
		//mHandler = handler;
		if(mActivity != null && !mActivity.equals(activity)){
			closeAutoRefresh();
			mActivity = activity;
		}		
	}

	@Override
	public void closeAutoRefresh() {
		
	}

	@Override
	public void addRefreshData() {
		
	}

	@Override
	public void removeRefreshData()
	{	}

	@Override
	public void setData(NetDataParams params)
	{
		mCacheParams.put(params.httpUrl, params);
		NetDataLoadTask task = new NetDataLoadTask(null,params.httpUrl,this);
		task.executeTask(params);
	}

	@Override
	public void setData(Class<?> cls,NetDataParams params,String... key) {
		mCacheParams.put(params.httpUrl, params);
		NetDataLoadTask task = new NetDataLoadTask(cls,params.httpUrl,this,key);
		task.executeTask(params);		
	}

	@Override
	public void setArrayData(Class<?> cls,NetDataParams params,String... key) {
		mCacheParams.put(params.httpUrl, params);
		NetDataLoadTask task = new NetDataLoadTask(cls,params.httpUrl,this,key);
		task.executeTask(params);	
	}


	public synchronized <T> void onImplCompletionListener(Class<T> cls, String url,String result,String... keys) {
		LogUtil.i(Tag,"onImplCompletionListener()");
		try
		{
			NetDataParams params = mCacheParams.get(url);
			if(params != null && result!=null)
			{
				if(params.resultType == NetDataParams.RESULT_TYPE_ARRAY)
				{
					//返回的结果是一个数组类型
					List<T>	arrays=JsonUtil.toList(result, new TypeToken<List<T>>() {}.getType());

					if(params.isCacheData)
					{
						mCacheObject.put(url,arrays);
					}else{
						mObject.put(url,arrays);
					}
				}
				else if(params.resultType == NetDataParams.RESULT_TYPE_OBJECT)
				{
					//返回的结果是 对象类型
					T p= JsonUtil.toObject(result,cls);
					if(params.isCacheData){
						mCacheObject.put(url,p);
					}else{
						mObject.put(url,p);
					}
				}
				else if(params.resultType == NetDataParams.RESULT_TYPE_INT || params.resultType == NetDataParams.RESULT_TYPE_STRING )
				{
					//此处非查询类的结果还要依据 服务器端返回的结果进行确定
					JSONObject obj=new JSONObject(result);
					int msg= obj.getInt("result");
					if(params.isCacheData){
						mCacheObject.put(params.httpUrl,msg);
					}else{
						mObject.put(params.httpUrl,msg);
					}
				}
				else
				{

				}
				params.mListener.onImplCompletionListener(cls,params);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	@Override
	public synchronized void onImplCancelledListener(Class<?> cls, String url,String... key)
	{
		LogUtil.i(Tag,"onImplCancelledListener()");
		NetDataParams params = mCacheParams.get(url);
		params.mListener.onImplCancelledListener(params,url,key[0]);
	}

	@Override
	public synchronized void onImplErrorListener(Class<?> cls, String errorCode,String url,String errorDes, String... key)
	{
		LogUtil.i(Tag,"onImplErrorListener()");
		NetDataParams params = mCacheParams.get(url);
		params.mListener.onImplErrorListener(params,url,errorCode);
	}

}
