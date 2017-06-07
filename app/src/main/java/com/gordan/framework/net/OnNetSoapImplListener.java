package com.gordan.framework.net;


/***
 *
 * HTTP请求 传递过程中的 接口
 *
 * 2
 *
 */
public interface OnNetSoapImplListener{
	public <T> void onImplCompletionListener(Class<T> cls,String url,String result,String... key);
	public void onImplCancelledListener(Class<?> cls,String url, String... key);
	public void onImplErrorListener(Class<?> cls,String url, String errorCode, String errorDes, String... key);
}
