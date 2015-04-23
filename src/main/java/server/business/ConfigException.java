package server.business;


@SuppressWarnings("serial")
public class ConfigException extends BusinessException {
	
	public static enum Reason implements ExceptionReason{		
		ITEM_DEFINITIONS_MISSING("There is no Item Definition given to the system. You can provide this in Settings > Data Definition.");
		
		private Reason(String msg){
			this.msg = msg;
		}
		private String msg;	
		
		public String getMessage(){
			return msg;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}
	
	public ConfigException(Reason type){
		super(type);
	}

	
	
	
}
