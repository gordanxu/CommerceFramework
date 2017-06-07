package com.gordan.framework.net.model;

import java.io.Serializable;

/**
 *  获取商品详情解析类
 */
public class GoodsInfoBean implements Serializable
{
	public int ID; //id标识
	public int StoreID; //商户ID
	public String StoreName; //商户名字
	public String Name; //商品名字
	public double Price; //商品价格
	public String SupplyTime; //供货时间
	public boolean IsBuy;
	public boolean Collection; //是否被收藏
	public int PurchaseCount; //销售量
	public float Credit; //评分
	public int status; //结果状态错误码
	public String msg; //结果状态错误值
}
