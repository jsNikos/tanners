package server.persistence;

import org.springframework.data.annotation.Id;

/**
 * Intended for scheduling whenever the system detects new records since the
 * last run according to numberNewRecors.  
 *
 */
public class ScheduleByTraffic {
	@Id
	private String id;
	private long numberNewRecords;

	public long getNumberNewRecords() {
		return numberNewRecords;
	}

	public void setNumberNewRecords(long numberNewRecords) {
		this.numberNewRecords = numberNewRecords;
	}
}
