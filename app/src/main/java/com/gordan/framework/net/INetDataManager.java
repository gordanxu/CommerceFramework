package com.gordan.framework.net;

import android.os.Handler;

import com.gordan.framework.http.NetDataParams;

import java.util.List;

/**
 * 
 * 类: INetDataManager
 * 描述: 接口类-网络数据管理类
 * 作者:
 * 日期:
 */
public interface INetDataManager {
	/***
	 * 获取DataManager中数据的数量
	 * */
	public int getDataCount();


	/**
	 * 获取非查询类的执行结果
	 * @param url 本次请求的URL地址
	 * @return
	 */
	public Object getObjectData(String url);

	/**
	 * 获取DataManager中  单一 对象的数据
	 *
	 * @param url 执行HTTP请求的URL地址
	 * @param <T> 对象类
	 * @return
	 */
	public  <T> T getData(String url);

	/***
	 * 获取DataManager中  对象的集合 的数据
	 *
	 * @param url
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getArrayData(String url);


	/***
	 * 准备执行非查询类的HTTP请求 并将结果存入DataManager中
	 *
	 * @param params
	 */
	public void setData(NetDataParams params);

	/***
	 * 从网络获取数据并放入DataManager中
	 * @param cls 获取的数据类型
	 * @param params 获取数据的参数
	 * */
	public void setData(Class<?> cls, NetDataParams params, String... key);

	/***
	 * 从网络获取数据组并放入DataManager中
	 * @param cls 获取的数据类型
	 *
	 * */
	public void setArrayData(Class<?> cls, NetDataParams params, String... key);
	/***
	 * 从DataManager中，移除数据
	 * @param cls 移除的数据类型
	 * */
	public void removeData(Class<?> cls);
	/***
	 * 开启刷新
	 * */
	public void startAutoRefresh(Handler handler, String act);
	/***
	 * 关闭刷新
	 * */
	public void closeAutoRefresh();
	/***
	 * 添加要刷新的数据-根据setData时的设置rstime
	 * */
	public void addRefreshData();
	/***
	 * 移除要刷新的数据-根据setData时的设置rstime
	 * */
	public void removeRefreshData();
}
