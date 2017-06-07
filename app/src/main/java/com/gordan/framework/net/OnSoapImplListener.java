package com.gordan.framework.net;

import com.gordan.framework.http.NetDataParams;


/****
 *
 * HTTP请求 之前 参数传递的 接口 同时也是 执行完HTTP后返回结果的接口
 *
 * 1 和  3
 *
 */
public interface OnSoapImplListener
{
	public void onImplCompletionListener(Class<?> cls,NetDataParams params);
	public void onImplCancelledListener(NetDataParams params,String url,String keys);
	public void onImplErrorListener(NetDataParams params,String url,String keys);
}
