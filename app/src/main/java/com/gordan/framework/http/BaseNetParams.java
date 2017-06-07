package com.gordan.framework.http;

import org.apache.http.Header;
import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 
 * 类: BaseNetParams
 * 描述: 基本的网络请求参数类
 * 包含网络请求的地址 请求方式 参数类型 表单提交的参数 响应结果类型
 *
 * 作者: Gordan
 * 日期: 2015-11-01
 */
public class BaseNetParams {

	//HTTP请求传递过来的参数类型
	public static final int PARAMS_TYPE_JSON = 0;
	public static final int PARAMS_TYPE_URLCODE = 1;

	//HTTP请求时 响应正文的数据类型  对象的数组 还是 对象
	public static final int RESULT_TYPE_OBJECT = 0;
	public static final int RESULT_TYPE_ARRAY = 1;
	public static final int RESULT_TYPE_INT=2;
	public static final int RESULT_TYPE_STRING=3;

	public static final String METHOD_GET = "get";
	public static final String METHOD_POST = "post";
	public static final String METHOD_Delete = "delete";
	public static final String METHOD_Put = "put";

	public List<NameValuePair> Listparams=null;//可选参数 可能表单提交的参数需要进行URL加密

	public String params; //需要提交到表单中的参数 格式化为 JSON的数组
	public String httpUrl;
	public String method = METHOD_GET;//默认的请求方式是Get
	public int paramsType = PARAMS_TYPE_JSON;//默认的参数类型是 JSON字符串
	public int resultType = RESULT_TYPE_OBJECT;	//默认的数据处理结果是 对象
	public boolean isRetry = false;//是否需要重新请求
	public Header[] headers= new Header[]{};//盒子的应用 执行HTTP请求时服务器会验证 头部信息
	public int retryCount = 0;//重新请求的次数

	/**
	 * 将需要提交到表单中的参数 格式化为一个JSON字符串
	 *
	 *
	 * @param map
	 */
	public void initParams(HashMap<String,Object> map)
	{
		if(map == null || map.size() <= 0)
		{
			return;
		}
		Set<String> keys= map.keySet();
		Iterator<String> key= keys.iterator();
		params="{";
		while (key.hasNext())
		{
			Object obj=map.get(key);
			if(obj instanceof String)
			{
				params+=("\""+ key+"\""+":"+"\""+obj.toString()+"\"");
			}
			else if(obj instanceof Boolean)
			{
				params+=("\""+ key+"\""+":" +(boolean)obj);
			}
			else
			{
				params+=("\""+ key+"\""+":"+(int)obj);
			}
			params+=",";
		}
		params=params.substring(0,params.length()-1);
		params+="}";
	}


}
