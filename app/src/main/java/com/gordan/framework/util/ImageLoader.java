package com.gordan.framework.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.gordan.framework.http.BaseAsyncTask;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 类：ImageLoader
 * 基本描述：异步加载网络图片类
 * 作者：Gordan
 * 日期：2015-11-01
 *
 */
public class ImageLoader {

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	ExecutorService executorService;
	private ImageLoader(Context context) {
		fileCache = new FileCache(context);
		//executorService = Executors.newFixedThreadPool(5);
		executorService = Executors.newFixedThreadPool(5);
		//Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
	}
	private static ImageLoader instance;
	
	private Context mContext;
	final Handler handler = new Handler();
	public static ImageLoader getInstance(Context context){
		if(instance == null){
			synchronized (ImageLoader.class) 
			{				
				instance = new ImageLoader(context);
			}						
		}
		instance.mContext=context;
		return instance;
	}

	public void preDownloaded(final String url){
		File f = fileCache.getFile(url);

		// 先从文件缓存中查找是否有
		Bitmap b = decodeFile(f);
		if (b != null)
			return ;
		executorService.submit(new Runnable() {
			
			@Override
			public void run() {
				File f = fileCache.getFile(url);
				// 最后从指定的url中下载图片
				try {
					URL imageUrl = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) imageUrl
							.openConnection();
					conn.setConnectTimeout(30000);
					conn.setReadTimeout(30000);
					conn.setInstanceFollowRedirects(true);
					InputStream is = conn.getInputStream();
					OutputStream os = new FileOutputStream(f);
					CopyStream(is, os);
					os.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
		});
	}
	
	int stub_id; //图片未加载完成时需要显示的图片的资源ID
	int error_id;//图片加载出错时需要显示的图片资源ID


	/****
	 * 加载图片方法1
	 *
	 * @param url
	 * @param imageView
	 */
	public void displayImage(String url, ImageView imageView) {
		this.displayImage(url,imageView,0,0);
	}


	/***
	 * 下载图片的核心方法
	 *
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void displayImage(String url, ImageView imageView,int width,int height) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找

		Bitmap bitmap = memoryCache.get(url);
//		imageView.setImageResource(stub_id);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			// 若没有的话则开启新线程加载图片
			DisplayDrawable drawable = new DisplayDrawable(imageView);
			drawable.executeTask(url);
			imageView.setImageResource(stub_id);
		}
	}

	/***
	 * 加载图片方法2
	 * @param url
	 * @param imageView
	 * @param defaultID 图片未加载完成时需要显示的图片的资源ID
	 */
	public void displayImage(String url, ImageView imageView ,int defaultID) {
		stub_id = defaultID;
		displayImage(url,imageView);
	}

	/***
	 * 加载图片方法3
	 * @param url
	 * @param imageView
	 * @param defaultID 图片未加载完成时需要显示的图片的资源ID
	 * @param erroorDefault 图片加载出错时需要显示的图片资源ID
	 * @param isShow
	 */
	public void displayImage(String url, ImageView imageView ,int defaultID, int erroorDefault, boolean isShow) {
		stub_id = defaultID;
		error_id = erroorDefault;
		displayImage(url,imageView);
	}

	/**
	 * 通过 GET方式获取 服务端的 图片（主要是指验证码）
	 * Author:XPZ
	 * 
	 * @param httpUrl
	 * @return
	 */
	public Bitmap getHttpBimapByGet(String httpUrl)
	{
		HttpClient client = null;
		Bitmap map = null;
		try {			
			Log.i("XPZ", "验证码请求的地址:" + httpUrl);
			HttpGet request = new HttpGet(httpUrl);
			request.addHeader(new BasicHeader("Content-Type",
					"application/json"));
			request.addHeader(new BasicHeader("Accept",
					"application/vnd.aios.v1+json"));
			client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(request);			
			Header header= httpResponse.getLastHeader("capture");
			Log.i("XPZ", "Header中获取图片验证码的值为："+header.getValue());
//			if(header!=null)
//			{
//				CacheManager.instance(mContext).setCapture(header.getValue());
//			}
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code == 200) {
				HttpEntity r = httpResponse.getEntity();
				InputStream is=r.getContent();
				if (r != null && is!=null) {			
					map = BitmapFactory.decodeStream(is);				
					is.close();
					is=null;					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			client.getConnectionManager().shutdown();
		}
		return map;	
	}
		
	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// 先从文件缓存中查找是否有
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// 最后从指定的url中下载图片
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close(); 
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	
	public class DisplayDrawable extends BaseAsyncTask<String, Void, Bitmap> {
		public ImageView imageView;
		private String url;
		public DisplayDrawable(ImageView i){
			imageView = i;
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			if (imageViewReused(imageView,url))
				return null;
			Bitmap bmp = getBitmap(params[0]);
			memoryCache.put(params[0], bmp);
			if (imageViewReused(imageView,url))
				return null;
			return bmp;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (imageViewReused(imageView,url))
				return;
			if (result != null)
				imageView.setImageBitmap(result);
			else
				imageView.setImageResource(error_id);
		}		
	}


	/**
	 * 防止图片错位
	 *
	 * @param imageView
	 * @param url
	 * @return
	 */
	boolean imageViewReused(ImageView imageView,String url) {
		String tag = imageViews.get(imageView);
		if (tag == null || !tag.equals(url))
			return true;
		return false;
	}
}
