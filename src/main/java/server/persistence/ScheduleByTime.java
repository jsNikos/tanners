package server.persistence;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class ScheduleByTime {
	@Id
	private String id;
	private int hour;
	private int minute;
	private Date nextRun;

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public Date getNextRun() {
		return nextRun;
	}

	public void setNextRun(Date nextRun) {
		this.nextRun = nextRun;
	}
}
