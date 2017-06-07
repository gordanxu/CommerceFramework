package com.gordan.framework.http;

import android.os.Message;
import android.text.TextUtils;

import com.gordan.framework.util.LogUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientConnector {
	private static final String Tag = "HttpClientConnector";
	// 设置请求超时3秒钟
	private static final int REQUEST_TIMEOUT = 30 * 1000;
	// 设置等待数据超时时间3秒钟
	private static final int SO_TIMEOUT = 20 * 1000;

	public final static String HTTP_TIMEOUT_JSON= "{\"code\":1001,\"message\":\"请求超时\"}";

	public final static int HTTP_TIMEOUT_CODE= 1001;

	/***
	 *
	 * 执行HTTP请求的主方法(主要用于非查询类操作 如 更新 插入 删除等)
	 * 目前近支持GET和POST方式的请求
	 *
	 * Author：Gordan
	 *
	 * @param params
	 * @return 返回值包括响应码以及响应的正文(status状态码) 响应码在 what中 正文在 obj 中
	 */
	public static Message httpMethod2(BaseNetParams params)
	{
		Message msg = null;
		int code = -1;
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
		HttpResponse httpResponse = null;
		LogUtil.i("uri:" + params.httpUrl);
		LogUtil.i("params:" + params.params);
		try {
			if (BaseNetParams.METHOD_POST.equals(params.method))
			{
				HttpPost httpRequest = new HttpPost(params.httpUrl);
				httpRequest.setHeaders(params.headers);
				if (!TextUtils.isEmpty(params.params)) {
					StringEntity sEntity = new StringEntity(params.params, HTTP.UTF_8);
					httpRequest.setEntity(sEntity);
				}
				httpRequest.setHeader("Content-Type", "application/json;charset=utf-8");
				// 取得HttpResponse
				httpResponse = httpclient.execute(httpRequest);
			}
			else
			{
				HttpGet request = new HttpGet(params.httpUrl);
				request.setHeaders(params.headers);
				httpResponse = httpclient.execute(request);
			}
			code = httpResponse.getStatusLine().getStatusCode();
			LogUtil.i("code:" + code);
			HttpEntity r = httpResponse.getEntity();
			if (r != null) {
				result = EntityUtils.toString(r, "utf-8");
				LogUtil.i("result:" + result);
			}
			msg = new Message();
			msg.what = code;
			msg.obj = result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LogUtil.e(Tag + "-" + e.getMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return msg;
	}


	/***
	 *
	 * 执行HTTP请求的主方法（主要用于查询类请求）
	 * 支持的请求方式包括GET POST PUT DELETE等
	 *
	 *	那些放到表单里面的参数 是如何 提交的？？
	 *
	 * @param params
	 * @return 返回请求后的响应正文
	 */
	public static String httpMethod(BaseNetParams params) {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
		HttpRequest httpRequest;
		HttpResponse httpResponse=null;
		String result = null;
		int code=-1;
		LogUtil.i(Tag,"uri=" + params.httpUrl);
		LogUtil.i(Tag,"params:" + params.params);
		LogUtil.i(Tag,"headers:"+ params.headers);
		try {
			if (BaseNetParams.METHOD_POST.equals(params.method))
			{
				httpRequest = new HttpPost(params.httpUrl);
				httpRequest.setHeaders(params.headers);
				if(params.paramsType == BaseNetParams.PARAMS_TYPE_JSON)
				{
					//表单提交的数据类型是JSON
					if(params.params!=null && !TextUtils.isEmpty(params.params))
					{
						//application/x-www-form-urlencoded;charset=UTF-8
						httpRequest.setHeader("Content-Type", "application/json;charset=utf-8");
						((HttpPost)httpRequest).setEntity(new StringEntity(params.params, HTTP.UTF_8));
					}
				}
				else if(params.paramsType == BaseNetParams.PARAMS_TYPE_URLCODE)
				{
					//表单提交的数据类型是经过URL加密
					((HttpPost)httpRequest).setEntity(new UrlEncodedFormEntity(params.Listparams, HTTP.UTF_8));
				}
				httpResponse = httpclient.execute((HttpPost)httpRequest);
			}
			else if (BaseNetParams.METHOD_GET.equals(params.method))
			{
				httpRequest = new HttpGet(params.httpUrl);
				httpRequest.setHeaders(params.headers);
				httpResponse = httpclient.execute((HttpGet)httpRequest);
			}
			else if (BaseNetParams.METHOD_Delete.equals(params.method))
			{
				httpRequest = new HttpDelete(params.httpUrl);
				httpRequest.setHeaders(params.headers);
				httpResponse = httpclient.execute((HttpDelete)httpRequest);
			}
			else if (BaseNetParams.METHOD_Put.equals(params.method))
			{
				httpRequest = new HttpPut(params.httpUrl);
				httpRequest.setHeaders(params.headers);
				if(params.paramsType == BaseNetParams.PARAMS_TYPE_URLCODE)
				{
					((HttpPut)httpRequest).setEntity(new UrlEncodedFormEntity(params.Listparams, HTTP.UTF_8));
				}
				else if(params.paramsType == BaseNetParams.PARAMS_TYPE_JSON)
				{
					if(params.params!=null && !TextUtils.isEmpty(params.params))
					{
						((HttpPut)httpRequest).setEntity(new StringEntity(params.params, HTTP.UTF_8));
					}
				}
				httpResponse = httpclient.execute((HttpPut)httpRequest);
				//某些服务端会对客户端的请求进行Cookie或者Session的验证
				//获取服务端的验证信息 一般 Cookie的key为Set-Cookie Session的key为JSESSIONID
				//方式1：List<Cookie> cookies=((DefaultHttpClient)httpclient).getCookieStore().getCookies();
				//方式2：httpResponse.getAllHeaders()
			}
			//此处处理结果集
			if(httpResponse!=null)
			{
				code = httpResponse.getStatusLine().getStatusCode();
				LogUtil.i(Tag,"code:" + code);
				if (code == 200) {
					HttpEntity r = httpResponse.getEntity();
					if (r != null) {
						result = EntityUtils.toString(r);
						LogUtil.i(Tag,"result:" + result);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			httpclient.getConnectionManager().shutdown();
		}
		//若本次请求是 需要重试的
		if (result == null && params.isRetry && params.retryCount > 0)
		{
			params.retryCount--;
			return httpMethod(params);
		}
		return result;
	}

}
