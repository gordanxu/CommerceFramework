package com.gordan.framework.net;

import com.gordan.framework.http.BaseErrorResult;

/**
 * 
 * 类: OnSoapImplListener
 * 描述: 基本异步任务回调类
 * 作者: dongyukun
 * 日期: 2014-11-26
 * onImplCompletionListener - complete callback</br>
 * onImplCancelledListener - cancel callback</br>
 * onImplErrorListener - error callback
 */
public interface OnBaseSoapImplListener<Params, Progress, Result> {
	public void onImplCompletionListener(Result result);
	public void onImplCancelledListener();
	public void onImplErrorListener(BaseErrorResult<Result> result);
}
