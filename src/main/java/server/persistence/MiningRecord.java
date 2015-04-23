package server.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MiningRecord {
	@Id
	@JsonIgnore
    private String id;
	private Date created; // timestamp when record was created
	private String bulkUploadId; // pointing to bulkUpload in case inserted by such one
	private List<Map<String, Object>> items = new ArrayList<>(); // user-defined

	public MiningRecord(){}

	public MiningRecord(List<Map<String, Object>> items, Date created) {
		super();
		this.items = items;
		this.created = created;
	}

	public List<Map<String, Object>> getItems() {
		return items;
	}

	public void setItems(List<Map<String, Object>> items) {
		this.items = items;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getBulkUploadId() {
		return bulkUploadId;
	}

	public void setBulkUploadId(String bulkUploadId) {
		this.bulkUploadId = bulkUploadId;
	}

	
}
