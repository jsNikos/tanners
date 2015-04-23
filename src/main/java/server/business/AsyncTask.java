package server.business;

import java.util.concurrent.Future;

public abstract class AsyncTask implements Runnable{
	private Future<?> future;		
	private Runnable onCancel;
	private Runnable onTerminate;		
	
	@Override
	public void run() {			
		try{
			doIt();
		}finally{
			if(onTerminate != null){
				onTerminate.run();
			}
		}				
	}	
	
	/**
	 * Register external canceling handler.
	 * @param func
	 * @return
	 */
	public AsyncTask onCancel(Runnable func){
		onCancel = func;
		return this;
	}
	
	/**
	 * Register external termination handler.
	 * @param func
	 * @return
	 */
	public AsyncTask onTerminated(Runnable func){
		onTerminate = func;
		return this;
	}	
	
	/**
	 * Triggered along with run. But run is wrapped in a final block to ensure some
	 * callbacks after termination.
	 * Here go the task's core logic.
	 */
	protected abstract void doIt();
	
	public void cancel(){
		future.cancel(true);
		if(onCancel != null){
			onCancel.run();
		}	
		handleCancel();
	}	
	
	/**
	 * Internal handling of a cancel event.
	 */
	protected abstract void handleCancel();
	
	protected void setFuture(Future<?> future) {
		this.future = future;
	}	
}
