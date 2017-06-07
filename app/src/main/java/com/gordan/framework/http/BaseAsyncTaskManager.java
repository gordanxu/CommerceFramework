package com.gordan.framework.http;

import com.gordan.framework.util.LogUtil;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * 
 * 类: BaseAsyncTaskManager
 * 描述: 基本异步任务管理类，用于管理异步任务
 * 作者:	 Gordan
 * 日期:	 2015-11-01
 * executeTask - 添加任务到队列</br>
 * initQueue - 清空等待队列（线程池队列与外部等待队列）</br>
 * clearQueue - 清空队列（清空等待队列及取消执行中的任务）</br>
 */
public class BaseAsyncTaskManager{
	private static final String Tag = "BaseNetTaskManager";
	private static final int CORE_POOL_SIZE = 15;  		//核心线程数
	private static LinkedBlockingQueue<Runnable> poolQueue = new LinkedBlockingQueue<Runnable>();
	private static LinkedBlockingQueue<BaseAsyncTask> waitingQueue = new LinkedBlockingQueue<BaseAsyncTask>();
	private static LinkedBlockingQueue<BaseAsyncTask> runningQueue = new LinkedBlockingQueue<BaseAsyncTask>(CORE_POOL_SIZE);
	private static ThreadPoolExecutor sExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,200,10,TimeUnit.SECONDS,poolQueue);
	private static BaseAsyncTaskManager instance = null;	
	public static BaseAsyncTaskManager getInstance(){
		if(instance == null)
		{
			synchronized (BaseAsyncTaskManager.class)
			{
				instance = new BaseAsyncTaskManager();
			}
		}
		return instance;
	}

	/**
	 * 在线程池中执行任务
	 *
	 * @param task
	 */
	protected void executeTaskOnExecutor(BaseAsyncTask task)
	{
		LogUtil.i(Tag,"executeTaskOnExecutor()");
		if(runningQueue.size() == CORE_POOL_SIZE)
		{
			//若正在执行的线程数量达到最大线程数量的限制 则将此次任务追加到等待队列中
			waitingQueue.offer(task);
		}
		else
		{
			runningQueue.offer(task);
			task.executeOnExecutor(sExecutor,task.mParams);
		}
	}

	/***
	 * 直接执行任务
	 *
	 * @param task
	 */
	protected void executeTask(BaseAsyncTask task)
	{
		LogUtil.i(Tag, "executeTask()");
		if(runningQueue.size() == CORE_POOL_SIZE)
		{
			//若正在执行的线程数量达到最大线程数量的限制 则将此次任务追加到等待队列中
			waitingQueue.offer(task);
		}
		else
		{
			//将该任务追加到执行任务的队列中
			runningQueue.offer(task);
			if(sExecutor != null)
			{
				//若线程池不为空 则将该任务放到线程池中执行
				task.executeOnExecutor(sExecutor,task.mParams);
			}
			else
			{
				task.execute(task.mParams);
			}			
		}
	}
	/***
	 * 初始化任务线程池，清空等待队列中任务，取消正在执行的任务
	 * */
	public static void clearQueue(){
		if(poolQueue != null){
			poolQueue.clear();
		}		
		waitingQueue.clear();
		Iterator<BaseAsyncTask> it = runningQueue.iterator();
		while (it.hasNext()) {
			BaseAsyncTask task = it.next();
			task.cancel(true);
		}
	}
	
	/***
	 * 初始化任务线程池，清空等待队列中任务
	 * */
	public static void initQueue(){
		if(poolQueue != null){
			poolQueue.clear();
		}		
		waitingQueue.clear();
	}

	/**
	 * 当一个任务执行完成以后 马上从 队列中移除 该任务  并从等待队列中获取一个新任务 执行下去
	 *
	 * @param task
	 */
	protected void onTaskComplete(BaseAsyncTask task){
		LogUtil.i(Tag, "onTaskComplete()");
		if(runningQueue.contains(task)) {
			runningQueue.remove(task);
		}
		BaseAsyncTask temp = waitingQueue.poll();
		if(temp != null){			
			instance.executeTask(temp);
		}

	}
}
