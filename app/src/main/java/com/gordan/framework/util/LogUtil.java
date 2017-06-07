package com.gordan.framework.util;

import android.util.Log;

public class LogUtil{

	public static boolean BLOCK_ALL=true;

	private static String TAG="Framework";

	public static int LOG_LEVEL=Log.VERBOSE;

	public static void e(String msg) {
		e(TAG, msg);
	}
	
	public static void e(String tag, String msg) {
		if(BLOCK_ALL && LOG_LEVEL <= Log.ERROR) {
			Log.e(tag, msg);
		}
	}
	
	public static void w(String msg) {
		w(TAG, msg);
	}
	
	public static void w(String tag, String msg) {
		if(BLOCK_ALL && LOG_LEVEL <= Log.WARN) {
			Log.w(tag, msg);
		}
	}
	
	public static void d(String msg) {
		d(TAG, msg);
	}
	
	public static void d(String tag, String msg) {
		if(BLOCK_ALL && LOG_LEVEL <= Log.DEBUG) {
			Log.d(tag, msg);
		}
	}
	
	public static void i(String msg) {
		i(TAG, msg);
	}
	
	public static void i(String tag, String msg) {
		if(BLOCK_ALL && LOG_LEVEL <= Log.INFO) {
			Log.i(tag, msg);
		}
	}
	
	public static void v(String msg) {
		v(TAG, msg);
	}
	
	public static void v(String tag, String msg) {
		if(BLOCK_ALL && LOG_LEVEL <= Log.VERBOSE) {
			Log.v(tag, msg);
		}
	}
}