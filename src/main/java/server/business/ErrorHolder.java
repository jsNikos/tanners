package server.business;

import utils.JSON;

public class ErrorHolder{
	private String msg;
	private String reason;
	private String classType;		
		
	public ErrorHolder() {
		super();
	}

	public ErrorHolder(BusinessException e){
		this(e.getClass(), e.getReason().getMessage(), e.getReason().getName());
	}
	
	public ErrorHolder(Exception e){
		this(e.getClass(), "Sorry. Some unexpected error happened.", "");
	}
	
	private ErrorHolder(Class<? extends Exception> classType, String msg, String reason) {
		super();
		this.msg = msg;
		this.reason = reason;			
		this.classType = classType.getSimpleName();
	}
	
	public String toJSON(){
		return JSON.stringify(this);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}
	
	
}
