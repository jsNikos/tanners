package server.business;

import java.util.Date;

public class MiningChartData {
	private Integer day;
	private Integer month;
	private Integer year;
	private Date created;
	private Integer numberRecords;

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getNumberRecords() {
		return numberRecords;
	}

	public void setNumberRecords(Integer numberRecords) {
		this.numberRecords = numberRecords;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
