package com.gordan.framework.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * SharedPreferences工具操作类
 *
 * 
 */
public class SharedPreferencesUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String sharedPreferencesInfo = "gordan_framework";
	private Context mContext;
	private static SharedPreferences mSharedPreferences;
	private static Editor mEditor;
	private static SharedPreferencesUtil sharedPreferencesUtil = null;

	private SharedPreferencesUtil()
	{}

	public static SharedPreferencesUtil getInstance(Context context) {
		if (sharedPreferencesUtil == null)
		{
			synchronized (SharedPreferencesUtil.class)
			{
				sharedPreferencesUtil=new SharedPreferencesUtil();
				mSharedPreferences = context.getSharedPreferences(sharedPreferencesInfo, Context.MODE_PRIVATE);
				mEditor = mSharedPreferences.edit();
			}
		}
		sharedPreferencesUtil.mContext=context;
		return sharedPreferencesUtil;
	}

	public boolean isContainKey(String key) {
		return mSharedPreferences.contains(key);
	}

	public String getString(String key) {
		return mSharedPreferences.getString(key, "");
	}

	public String getString(String key, String defaultValue) {
		return mSharedPreferences.getString(key, defaultValue);
	}

	public HashMap<String, String> getAll() {
		return (HashMap<String, String>) mSharedPreferences.getAll();
	}

	public boolean setString(String key, String value) {
		if (mSharedPreferences.contains(key)) {
			mEditor.remove(key);
		}
		mEditor.putString(key, value);
		return mEditor.commit();
	}


	/****
	 * 相应的将存储加密好的对象进行Base64的解密
	 *
	 * @param key
	 * @return
	 */
	public Object getObjectDecode(String key)
	{
		Object object = null;
		ByteArrayInputStream bais=null;
		ObjectInputStream ois=null;
		try
		{
			String objectBase64 = mSharedPreferences.getString(key, "");
			byte[] base64Bytes = Base64.decode(objectBase64.getBytes(), Base64.DEFAULT);
			bais = new ByteArrayInputStream(base64Bytes);
			ois = new ObjectInputStream(bais);
			object = ois.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				ois.close();
				bais.close();
			}
			catch (Exception e1)
			{e1.printStackTrace();}
		}
		return object;
	}


	/***
	 * 将内容以Base64加密的形式保存起来
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setObjectEncode(String key,Object value)
	{
		if (mSharedPreferences.contains(key))
		{
			mEditor.remove(key);
		}
		boolean flag=false;
		String objectBase64 = "";
		ByteArrayOutputStream baos=null;
		ObjectOutputStream oos=null;
		try
		{
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(value);
			objectBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
			mEditor.putString(key,objectBase64);
			flag =  mEditor.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {

			try {
				oos.close();
				baos.close();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();

			}
		}

		return flag;
	}

	public boolean clearItem(String key) {
		mEditor.remove(key);
		return mEditor.commit();
	}

}
