package server.business;

@SuppressWarnings("serial")
public abstract class BusinessException extends Exception{
	
	public static interface ExceptionReason{
		public String getMessage();
		public String getName();
	}
	
	private ExceptionReason reason;	
	
	public BusinessException(ExceptionReason reason){
		super(reason.getName());
		this.reason = reason;
	}	

	public ExceptionReason getReason() {
		return reason;
	}
	
	
	
}
