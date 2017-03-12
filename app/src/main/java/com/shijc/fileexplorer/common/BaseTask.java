package com.shijc.fileexplorer.common;

import android.content.Context;
import android.os.AsyncTask;

import com.shijc.fileexplorer.AppContext;
import com.shijc.fileexplorer.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * 若要实现对后台任务结果的监听，必须实现TaskListener接口
 * 另外若需要进度提示界面，则需实现FeedListener
 * @author wangwei
 * @created 2014/4/25 
 */
public abstract class BaseTask<Input, Result> extends
		AsyncTask<Input, Object, Result> {
	protected Context mContext;
	private TaskListener<Result> taskListener;
	public static final String RESULT_OK = "-1";
	public static final Executor THREAD_POOL_EXECUTOR = Executors.newCachedThreadPool();
	private int status;



	public TaskListener<Result> getTaskListener() {
		return taskListener;
	}

	public void setTaskListener(TaskListener<Result> listener) {
		this.taskListener = listener;
	}

	private boolean isCancelable = true;
	
	/**
	 * 场景变量建议用AppContext
	 * @param mContext
	 */
	@Deprecated
	public BaseTask(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public BaseTask() {
		super();
		this.mContext = AppContext.getInstance();
	}
	
	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onPostExecute(Result result) {
		if (taskListener == null) 
			return;
		if (result != null) {
			if (taskListener != null) 
				taskListener.onSucess(result);
		}else {
			if (taskListener != null && !RESULT_OK.equals(status)) 
				taskListener.onFailed(new HttpConnectException(mContext.getString(R.string.warn_service_repose_isnull)));
		}
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		this.cancel(true);

		if (taskListener != null) {
			if (values[0] instanceof HttpConnectException) {
				HttpConnectException e = (HttpConnectException) values[0];
				if(e.getCause()==null){
					HttpConnectException albumConnectException=new HttpConnectException(e.getMessage(), e);
					taskListener.onFailed(albumConnectException);
				}else{
					taskListener.onFailed(e);
				}
			}else if (values.length > 1) {
				status = (Integer) values[1];
				onPostExecute((Result)values[0]);
			}
			
		}
	}
	@Override
	protected abstract Result doInBackground(Input... params);

	public interface TaskListener<Result> {
		void onSucess(Result result);
		void onFailed(HttpConnectException exception);
	}

	
	 public final AsyncTask<Input, Object, Result> asynExecute(Input... params) {
	     return executeOnExecutor(THREAD_POOL_EXECUTOR, params);
	 }
	
}
