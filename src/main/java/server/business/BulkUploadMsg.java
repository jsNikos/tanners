package server.business;

import server.persistence.BulkUpload;

/**
 * Message used to broadcast during bulkUpload.
 * This will be json-ized! 
 *
 */
public class BulkUploadMsg extends MessageHolder<BulkUpload> {		
	public BulkUploadMsg(){
		super();
	}
	
	public BulkUploadMsg(PROGRESS_STATE info, double uploadProgress) {
		super(info.getMsg(), info.name(), null, uploadProgress);		
	}

	public BulkUploadMsg(PROGRESS_STATE info, BulkUpload bulkUpload) {
		super(info.getMsg(), info.name(), bulkUpload);		
	}	
	
	public static enum PROGRESS_STATE{
		EXTRACT_RECORDS("Extracting records."),
		VALIDATE_RECORDS("Validating records."),
		INSERT_RECORDS("Writing records to database."),
		CANCELED("Canceling the upload."),
		FINISHED("Upload finished successful."),
		ERROR("An error happened during upload.");
		
		private String msg;
		private PROGRESS_STATE(String msg){
			this.msg = msg;
		}
		
		public String getMsg() {
			return msg;
		}		
	}
	
}
