package com.gordan.framework.http;

import com.gordan.framework.net.OnNetSoapImplListener;
import com.gordan.framework.util.LogUtil;

/**
 * 
 * 类: NetDataLoadTask
 * 描述: 接口类-网络下载任务类
 * 作者: Gordan
 * 日期: 2015-11-01
 */
public class NetDataLoadTask extends BaseAsyncTask<NetDataParams, Void, String> {

	private final String TAG="NetDataLoadTask";
	private OnNetSoapImplListener mListener;
	private Class<?> mCls;
	private String[] keys;
	private String url;

	public NetDataLoadTask(Class<?> cls,String url, OnNetSoapImplListener l, String... key)
	{
		mCls = cls;
		this.url= url;
		mListener = l;
		keys = key;
	}

	@Override
	protected String doInBackground(NetDataParams... params)
	{
		LogUtil.i(TAG,"doInBackground()");
		String result = HttpClientConnector.httpMethod(params[0]);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		LogUtil.i(TAG, "onPostExecute()");
		if (mListener != null)
		{
			if (result != null)
			{
				mListener.onImplCompletionListener(mCls, url, result);
			}
			else
			{
				mListener.onImplErrorListener(mCls,url,"","",result);
			}
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		LogUtil.i(TAG, "onCancelled()");
		if (mListener != null) {
			mListener.onImplCancelledListener(mCls,url,keys);
		}
	}
}
