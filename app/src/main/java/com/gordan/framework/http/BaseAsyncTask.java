package com.gordan.framework.http;

import android.os.AsyncTask;

import com.gordan.framework.net.OnBaseSoapImplListener;
import com.gordan.framework.util.LogUtil;

import java.util.concurrent.Executor;


/**
 * 
 * 类: BaseAsyncTask
 * 描述: 基本异步任务类，用于处理异步任务
 * 作者: Gordan
 * 日期: 2015-11-01
 * execute - 使用AsyncTask默认方式执行任务</br>
 * executeOnExecutor - 使用AsyncTask自定义线程池方式执行任务</br>
 * executeTask - 使用BaseNetTaskManager管理并执行任务</br>
 * executeTaskOnExecutor - 使用BaseNetTaskManager管理并使用自定义线程池执行任务</br>
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{
	private static final String Tag = "BaseAsyncTask";
	public Params[] mParams;
	public OnBaseSoapImplListener<Params, Progress, Result> mListener;
	private BaseAsyncTaskManager manager = null;
	public BaseAsyncTask(){
		manager = BaseAsyncTaskManager.getInstance();
	}
	
	public BaseAsyncTask(Params[] params,OnBaseSoapImplListener<Params, Progress, Result> listener){
		mParams = params;
		mListener = listener;
		manager = BaseAsyncTaskManager.getInstance();
	}
	
	public void executeTaskOnExecutor(Executor exec,Params... params){
		LogUtil.i(Tag,"executeTaskOnExecutor()");
		mParams = params;
		manager.executeTaskOnExecutor(this);
	}
	
	public void executeTask(Params... params){
		LogUtil.i(Tag,"executeTask()");
		mParams = params;
		manager.executeTask(this);
	}
	
	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		LogUtil.i(Tag, "onPostExecute()");
		if(mListener != null){
			if(result != null) {
				mListener.onImplCompletionListener(result);
			}else{
				mListener.onImplErrorListener(new BaseErrorResult(result));
			}
		}
		manager.onTaskComplete(this);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		LogUtil.i(Tag, "onCancelled()");
		if(mListener != null){
			mListener.onImplCancelledListener();
		}
		manager.onTaskComplete(this);
	}
	
}
