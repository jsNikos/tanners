package server.persistence;

import java.util.Date;

import org.springframework.data.annotation.Id;

import server.business.ErrorHolder;

public abstract class TaskEntity {
	@Id	
	private String id;
	private Date created; // the time when upload-process started
	private Date canceled; // the time when canceled, if at all
	private Date finished; // when finished normally
	private ErrorHolder error; // error, if any happened
	
	public TaskEntity(){
	}
	
	public TaskEntity(Date created) {
		super();
		this.created = created;
	}
	
	public boolean hasTerminated(){
		return canceled != null || finished != null || error != null;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public ErrorHolder getError() {
		return error;
	}

	public void setError(ErrorHolder error) {
		this.error = error;
	}

	public Date getCanceled() {
		return canceled;
	}

	public void setCanceled(Date canceled) {
		this.canceled = canceled;
	}

	public String getId() {
		return id;
	}

	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}

	public void setId(String id) {
		this.id = id;
	}
}
