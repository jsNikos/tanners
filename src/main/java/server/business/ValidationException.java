package server.business;

@SuppressWarnings("serial")
public class ValidationException extends BusinessException {
	
	public static enum Reason implements ExceptionReason{		
		NO_KEY_ATTRIBUTES_IN_ITEM_DEFINITIONS("Item Definition must contain at least one property which names the item's key property."),
		NO_DISPLAY_PROP_IN_ITEM_DEFINITIONS("Item Definition must contain a property which names the item's property which is used to display the item."),
		HAS_EMPTY_KEY_ATTRIBUTE_IN_ITEM_DEFINITIONS("Some key property is empty."),
		NO_ITEMS_IN_RECORD("The record doesn't contain items."),
		MISSING_KEY_ATTRIBUTE_FOR_ITEM("The item's key property is missing."),
		MISSING_DISPLAY_NAME_FOR_ITEM("The item's property for display is missing."),
		EMPTY_DISPLAY_NAME_FOR_ITEM("The item's property for display is empty. "),
		WRONG_CREATED_TIMESTAMP("The record doesn't contain a valid created timestamp."),
		NOT_VALID_JSON_STRING("The json-string provided is not valid."),
		NO_MINING_DATA_IN_FILE("No data contained in file."),
		ONLY_ONE_BULK_UPLOAD_AT_A_TIME("The already runs a file-upload. Wait until finished or interrupt."),
		DATA_NOT_PARSEABLE_AS_MINING_RECORD("The data provided are not parseable as mining-record."),
		BULK_UPLOAD_IS_RUNNING("You cannot change mining-data while a bulk-upload is running. Wait to finish or cancel the upload."),
		ONLY_ONE_MODEL_CREATE_AT_A_TIME("Only one model creation is permitted at a time.");
		
		
		private Reason(String msg){
			this.msg = msg;
		}
		
		private String msg;	
		
		@Override
		public String getMessage(){
			return msg;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}
	
	public ValidationException(Reason type){
		super(type);
	}	
	
	
}
