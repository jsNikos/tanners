package server.business;

import server.persistence.TaskEntity;

public abstract class MessageHolder<E extends TaskEntity> {
	private String progressStateMsg; // message to display for describing the current state of the task
	private String progressState; // the current state's name
	private Double progress; // progress for the current PROGRESS_STATE	
	private E taskEntity; // the db-taskEntity backing the task
	private boolean terminated; 
		
	public MessageHolder(){		
	}
	
	public MessageHolder(String progressStateMsg, String progressState, E entity, Double progress) {
		super();
		this.progressState = progressState;
		this.progressStateMsg = progressStateMsg;
		this.progress = progress;
		this.taskEntity = entity;
		this.terminated = entity != null && entity.hasTerminated();
	}
	
	public MessageHolder(String progressStateMsg, String progressState, E entity) {
		super();
		this.progressState = progressState;
		this.progressStateMsg = progressStateMsg;
		this.taskEntity = entity;
		this.terminated = entity != null && entity.hasTerminated();
	}
	
	public String getProgressStateMsg() {
		return progressStateMsg;
	}

	public Double getProgress() {
		return progress;
	}
	
	public String getProgressState() {
		return progressState;
	}

	public E getTaskEntity() {
		return taskEntity;
	}

	public boolean isTerminated() {
		return terminated;
	}
}
