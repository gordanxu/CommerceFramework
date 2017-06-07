package com.gordan.framework.net.manager;

import com.gordan.framework.http.NetDataManager;
import com.gordan.framework.http.NetDataParams;
import com.gordan.framework.net.OnSoapImplListener;
import com.gordan.framework.net.model.GoodsInfoBean;

import org.apache.http.Header;

import java.util.HashMap;


/**
 *  获取商品相关信息Manager
 */
public class GoodsInfoManager
{
	//执行HTTP请求的 回调函数 由Activity传入
	private OnSoapImplListener listener=null;

	//执行HTTP请求的地址（执行方法后是唯一） 在Activity中 以此URL地址 作为 MAP数据源中的Key
	public String getGoodsLevelListUrl=WebURL.URL_BASE + WebURL.URL_SUBAREAS_GET;

	public String getGoodsSubClassificationUrl=WebURL.URL_BASE + WebURL.URL_SUBAREAS_GET;

	private static GoodsInfoManager instance=null;

	private GoodsInfoManager(){}
	
	public static GoodsInfoManager getInstance(OnSoapImplListener listener)
	{
		if(instance != null)
		{
			synchronized (GoodsInfoManager.class)
			{
				instance=new GoodsInfoManager();
			}
		}
		instance.listener=listener;
		return instance;
	}
	
	/**
	 *  获取商品一级分类
	 */
	public void getGoodsLevelList(Header[] headers)
	{
		getGoodsLevelListUrl =String.format(getGoodsLevelListUrl, 1, "12", "13");
		NetDataParams params = new NetDataParams(getGoodsLevelListUrl, NetDataParams.METHOD_GET, NetDataParams.PARAMS_TYPE_JSON, null);
		params.mListener = listener;
		params.resultType = NetDataParams.RESULT_TYPE_ARRAY;
		NetDataManager.getInstance().setData(GoodsInfoBean.class, params);
	}
	
	/**
	 *  获取商品一级分类的子分类
	 */
	public void getGoodsSubClassification(int goodsLevelId,Header[] headers)
	{
		getGoodsSubClassificationUrl =String.format(getGoodsSubClassificationUrl, goodsLevelId, "100", "200");
		NetDataParams params = new NetDataParams(getGoodsSubClassificationUrl, NetDataParams.METHOD_GET, NetDataParams.PARAMS_TYPE_JSON,null);
		params.mListener = listener;
		params.resultType = NetDataParams.RESULT_TYPE_ARRAY;
		NetDataManager.getInstance().setData(GoodsInfoBean.class, params);
	}


	/**
	 * 此方法 可以用于执行非查询类的操作
	 *
	 *
	 * @param goods
	 * @param headers
	 */
	public void insertGoods(int goods,HashMap<String,Object> map,Header[] headers)
	{
		getGoodsSubClassificationUrl =String.format(getGoodsSubClassificationUrl, goods, "100", "200");
		NetDataParams params = new NetDataParams(getGoodsSubClassificationUrl, NetDataParams.METHOD_GET, NetDataParams.PARAMS_TYPE_JSON,null);
		params.mListener = listener;
		//此处表示提交到表单的数据
		params.initParams(map);
		//注意此处给的结果类型
		params.resultType = NetDataParams.RESULT_TYPE_INT;
		NetDataManager.getInstance().setData(params);
	}


}
