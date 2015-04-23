package server.business;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import server.persistence.AlgorithmSetting;
import server.persistence.ModelCreate;
import server.persistence.ModelRepository;

/**
 * Provides service around the frequent-item model. 
 * 
 */
@Service
public class ModelService {
	private AsyncTask modelCreateTask; // current running instance
	
	@Autowired(required=true)
	private ModelSchedulerService modelSchedulerService;
	@Autowired(required=true)
	private ModelRepository modelRepository;
	@Autowired(required=true)
	private WebApplicationContext application;

	/**
	 * Triggers the scheduler to run a modelCreateTask now.
	 */
	public synchronized ModelCreate triggerModelCreateTask() throws ValidationException{
		if(isModelCreateRunning()){
			throw new ValidationException(ValidationException.Reason.ONLY_ONE_MODEL_CREATE_AT_A_TIME);
		}		
		ModelCreate modelCreate = modelRepository.insert(new ModelCreate(new Date())); 
		modelCreateTask = application.getBean(ModelCreateTask.class)
			.init(modelCreate.getId())
			.onCancel(()-> modelCreateTask = null)
			.onTerminated(()-> modelCreateTask = null);		
		return modelCreate;
	}	
	
	public synchronized void cancelModelCreate(){
		if(isModelCreateRunning()){
			modelCreateTask.cancel();
		}
	}	
	
	public boolean isModelCreateRunning(){
		return modelCreateTask != null;
	}
	
	/**
	 * Every created model is based on MiningRecords. These contain a 'created' property.
	 * This method returns the highest 'created'-date of the latest successfully built model.	  
	 * @return can be null, if no run exists
	 */
	public Date findLastDateOfLatestRun(){
		//TODO
		return null;
	}
	
	/**
	 * Returns the algorithmSetting as a merged result of hardcoded defaults with those
	 * which are persisted in db.
	 * @return
	 */
	public AlgorithmSetting findSetting(){
		//TODO
		return null;
	}
	
	/**
	 * Removes the current peristed algorithmSetting from db and returns result
	 * of call to findSetting (includes the defaults only).
	 * @return
	 */
	public AlgorithmSetting restoreDefaultSetting(){
		//TODO
		return null;
	}
	
	public ModelCreate findModelCreate(String modelCreateId){
		return modelRepository.findOne(modelCreateId);
	}
	
	public ModelCreate save(ModelCreate modelCreate){		
		return modelRepository.save(modelCreate);
	}	
	
}
