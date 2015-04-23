package server.persistence;

import java.util.Date;

public class BulkUpload extends TaskEntity {	
	private Integer numberRecords; // number records which were inserted
	

	public BulkUpload() {
		super();
	}

	public BulkUpload(Date created) {
		super(created);		
	}

	public Integer getNumberRecords() {
		return numberRecords;
	}

	public void setNumberRecords(Integer numberRecords) {
		this.numberRecords = numberRecords;
	}

	

	

}
