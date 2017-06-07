package com.gordan.framework.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;

public class JsonUtil
{
	/*将JSON字符串转换为 实体类
	 *
	 * @param json
	 * @param c
	 * @param <T>
	 * @return
	 */
	public static <T> T toObject(String json,Class<T> c)
	{
		T p = null;
		try {
			p = new Gson().fromJson(json, c);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return p;
	}

	/**
	 * 将JSON字符串转换为 对象的list集合
	 *
	 * @param json
	 * @param type
	 * @param <T>
	 * @return
	 */
	public static <T> T toList(String json, Type type){
		T p = null;
		try {
			//Type listType = new TypeToken<T>(){}.getType();
			p = new Gson().fromJson(json, type);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return p;
	}

	/**
	 * 将JSON字符串转换为 对象的Linklist集合
	 *
	 * @param json
	 * @param <T>
	 * @return
	 */
	public static <T> LinkedList<T> toLinkList(String json){
		LinkedList<T> p = null;
		try {
			Type listType = new TypeToken<LinkedList<T>>(){}.getType();
			p = new Gson().fromJson(json, listType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return p;
	}


	/*
	 *将对象转换为JSON字符串
	 * @param src
	 * @param type
	 * @return
	 */
	public static String toJson(Object src, Type type){
		String p = null;
		try {
			//Type listType = new TypeToken<T>(){}.getType();
			p = new Gson().toJson(src, type);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return p;
	}
}
