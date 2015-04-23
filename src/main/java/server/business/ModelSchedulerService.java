package server.business;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.persistence.ScheduleByTime;
import server.persistence.ScheduleByTraffic;
import server.persistence.ScheduleSetting;
import server.persistence.ScheduleSettingRepository;

/**
 * Intends to define scheduling for a model.
 * Triggers model creation when schedule-event is produced. 
 *
 */
@Service
public class ModelSchedulerService {
	@Autowired(required=true)
	private ModelService modelService;
	@Autowired(required=true)
	private MiningDataService miningDataService;
	@Autowired(required=true)
	private ScheduleSettingRepository scheduleSettingRepository;
	
	private Timer timer; // the timer instance which schedules the next task	
	
	@PostConstruct
	public void init(){	
		ScheduleSetting scheduleSetting = findScheduleSetting();
		if(scheduleSetting.getScheduleByTime() != null){
			initScheduleByTime(scheduleSetting.getScheduleByTime());
		} else if(scheduleSetting.getScheduleByTraffic() != null){
			initScheduleByTraffic(scheduleSetting.getScheduleByTraffic());
		}		
	}
	
	/**
	 * Schedules the timer to trigger mode-create at the specific time.
	 * When triggered, initializes the timer with the next date.
	 * @param scheduleByTime
	 */
	private void initScheduleByTime(ScheduleByTime scheduleByTime){		
		Calendar calendar =	new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, scheduleByTime.getHour());
		calendar.set(Calendar.MINUTE, scheduleByTime.getMinute());
		if(calendar.getTime().before(new Date())){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		timer = new Timer();		
		timer.schedule(new TimerTask() {			
			@Override
			public void run() {
				try {
					Thread.sleep(1000); // ensure the next schedule 1 day later
					triggerModelCreate();
				} catch(BusinessException e){					
					handleError(e);					
				} catch (Exception e) {					
					handleError(e);
				}	
				ScheduleByTime scheduleByTime = findScheduleSetting().getScheduleByTime();
				if(scheduleByTime != null){
					initScheduleByTime(scheduleByTime);
				}
			}
		}, calendar.getTime());
		
		ScheduleSetting scheduleSetting = findScheduleSetting();
		scheduleSetting.getScheduleByTime().setNextRun(calendar.getTime());
		scheduleSettingRepository.save(scheduleSetting);		
	}
	
	/**
	 * Checks every minute if since last run new data have been added which triggers the model-create
	 * in case amount is large enough.
	 * @param scheduleByTraffic
	 */
	private void initScheduleByTraffic(ScheduleByTraffic scheduleByTraffic){
		timer = new Timer();		
		timer.schedule(new TimerTask() {			
			@Override
			public void run() {
				try {
					if(!modelService.isModelCreateRunning() && checkTraffic(scheduleByTraffic)){
						triggerModelCreate();
					}
				} catch(BusinessException e){					
					handleError(e);					
				} catch (Exception e) {					
					handleError(e);
				}				
			}
		}, new Date(), 60000);		
	}
	
	private boolean checkTraffic(ScheduleByTraffic scheduleByTraffic){
		return scheduleByTraffic.getNumberNewRecords() >= miningDataService.countRecordsSince(modelService.findLastDateOfLatestRun());
	}
	
	private void handleError(BusinessException e){
		e.printStackTrace();
		ScheduleSetting scheduleSetting = findScheduleSetting();
		scheduleSetting.setError(new ErrorHolder(e));
		scheduleSettingRepository.save(scheduleSetting);
	}

	private void handleError(Exception e){
		e.printStackTrace();
		ScheduleSetting scheduleSetting = findScheduleSetting();
		scheduleSetting.setError(new ErrorHolder(e));
		scheduleSettingRepository.save(scheduleSetting);
	}
	
	private void triggerModelCreate() throws ValidationException{
		ScheduleSetting scheduleSetting = findScheduleSetting();		
		if(!scheduleSetting.isBlocked()){
			scheduleSetting.setError(null);
			scheduleSettingRepository.save(scheduleSetting);			
			modelService.triggerModelCreateTask();
		}
	}
	
	private ScheduleSetting findScheduleSetting(){
		List<ScheduleSetting> entitys = scheduleSettingRepository.findAll();
		if(entitys.isEmpty()){
			return	scheduleSettingRepository.insert(new ScheduleSetting());			
		} else{
			return entitys.get(0);
		}
	}
	
	public synchronized void blockScheduler(){
		ScheduleSetting scheduleSetting = findScheduleSetting();
		scheduleSetting.setBlocked(true);
		scheduleSettingRepository.save(scheduleSetting);		
	}
	
	/**
	 * Sets and persists scheduling by time.
	 * Erases other schedule settings.
	 */
	public synchronized void scheduleByTime(ScheduleByTime scheduleByTime){
		ScheduleSetting scheduleSetting = findScheduleSetting();
		resetSchedule(scheduleSetting);	
		
		scheduleSetting.setScheduleByTime(scheduleByTime);
		scheduleSettingRepository.save(scheduleSetting);
		initScheduleByTime(scheduleByTime);
	}
	
	/**
	 * Sets and persists scheduling by traffic.
	 * Erases other schedule settings.
	 */
	public synchronized void scheduleByRecords(ScheduleByTraffic scheduleByTraffic){
		ScheduleSetting scheduleSetting = findScheduleSetting();
		resetSchedule(scheduleSetting);	
		
		scheduleSetting.setScheduleByTraffic(scheduleByTraffic);
		scheduleSettingRepository.save(scheduleSetting);		
		initScheduleByTraffic(scheduleByTraffic);
	}
	
	/**
	 * Clears the scheduling of model-create.
	 */
	public synchronized void clearSchedule(){
		ScheduleSetting scheduleSetting = findScheduleSetting();
		resetSchedule(scheduleSetting);
		scheduleSettingRepository.save(scheduleSetting);
	}
	
	/**
	 * Resets the scheduler by stopping all related tasks and clearing
	 * all scheduled task in timers.
	 */
	private void resetSchedule(ScheduleSetting scheduleSetting){
		scheduleSetting.setScheduleByTime(null);
		scheduleSetting.setScheduleByTraffic(null);
		scheduleSetting.setError(null);
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}
}
