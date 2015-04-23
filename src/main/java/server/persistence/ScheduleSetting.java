package server.persistence;

import org.springframework.data.annotation.Id;

import server.business.ErrorHolder;

/**
 * Entity which intends to persist settings for the model-scheduler.
 *
 */
public class ScheduleSetting {
	@Id
	private String id;
	private boolean isBlocked; // when true the scheduler will not trigger when
								// conditions are met
	private ScheduleByTime scheduleByTime;
	private ScheduleByTraffic scheduleByTraffic;
	private ErrorHolder error; // error, if any happened

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public ScheduleByTime getScheduleByTime() {
		return scheduleByTime;
	}

	public void setScheduleByTime(ScheduleByTime scheduleByTime) {
		this.scheduleByTime = scheduleByTime;
	}

	public ScheduleByTraffic getScheduleByTraffic() {
		return scheduleByTraffic;
	}

	public void setScheduleByTraffic(ScheduleByTraffic scheduleByTraffic) {
		this.scheduleByTraffic = scheduleByTraffic;
	}

	public ErrorHolder getError() {
		return error;
	}

	public void setError(ErrorHolder error) {
		this.error = error;
	}

}
