package com.gordan.framework.http;


import com.gordan.framework.util.LogUtil;

/**
 * 
 * 类: BaseClassFactory
 * 描述: 基本类型创建类,创建相应的数据对象
 * 作者: dongyukun
 * 日期: 2014-11-27
 * @param <T>
 */
public class BaseClassFactory<T> {
	private static final String Tag = "BaseClassFactory";
	
	@SuppressWarnings("unchecked")
	public T getInstance(String ClassName) {
		try {
			return (T)Class.forName(ClassName).newInstance();
		} catch (IllegalAccessException e) {
			LogUtil.e(Tag + "-" + e.getMessage());
		} catch (InstantiationException e) {
			LogUtil.e(Tag+"-"+e.getMessage());
		} catch (ClassNotFoundException e) {
			LogUtil.e(Tag+"-"+e.getMessage());
		} catch (Exception e) {
			LogUtil.e(Tag+"-"+e.getMessage());
		}
		return null;
	}

}
