package server.rest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import server.business.ConfigException;
import server.business.MiningDataService;
import server.business.ModelService;
import server.business.ValidationException;
import server.persistence.BulkUpload;
import server.persistence.ItemDefinition;
import utils.JSON;

/**
 * Router intended for web end-points. 
 *
 */
@RestController
@RequestMapping(value="webrouter")
public class WebRouter {
	@Autowired(required=true)
	private MiningDataService miningDataService;
	@Autowired(required=true)
	private ModelService modelService;
	
	/**
	 * Request to add ItemDefinition.  	  
	 * @param records [MiningRecord]  
	 * @return
	 */
	@RequestMapping(value="/addItemDefinition", method = RequestMethod.POST, consumes="application/json")
	public void addItemDefinition(@RequestBody String itemDefinitionJSON) throws ValidationException{
		miningDataService.addItemDefinition(JSON.parse(itemDefinitionJSON, ItemDefinition.class));
	}	
	
	/**
	 * Returns itemDefinition from db.	  
	 * @return
	 */
	@RequestMapping(value="/findItemDefinition", method = RequestMethod.GET, produces="application/json")
	public String findItemDefinition() throws ValidationException, ConfigException{		
		return JSON.stringify(miningDataService.findItemDefinition());
	}	
	
	/**
	 * Data to display in chart about mining-data.
	 * @return [miningChartData]
	 */
	@RequestMapping(value="/findMiningChartData", method = RequestMethod.GET, produces="application/json")
	public String findMiningChartData(){
		return JSON.stringify(miningDataService.findMiningChartData());
	}
	
	/**
	 * Removes data from given bulkUpload
	 * @param id
	 * @return
	 * @throws ValidationException 
	 */
	@RequestMapping(value="/removeMiningData", method=RequestMethod.POST, consumes="application/json")
	@ResponseStatus(value=HttpStatus.OK)
	public void removeMiningData(@RequestBody String bulkUploadJSON) throws ValidationException{	
		miningDataService.removeData(JSON.parse(bulkUploadJSON, BulkUpload.class).getId());  
	}
	
	/**
	 * Removes all mining-data ever stored.
	 * @return
	 * @throws ValidationException 
	 */
	@RequestMapping(value="/removeAllMiningData", method=RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public void removeAllMiningData() throws ValidationException{
		miningDataService.removeData();
	}
	
	/**
	 * Fetches all records from bulkUploads table.
	 * @return
	 */
	@RequestMapping(value="/findBulkUploads", method=RequestMethod.GET, produces="application/json")
	public String findBulkUploads(){
		return JSON.stringify(miningDataService.findBulkUploads());
	}	
	
	/**
	 * Requests the current Bulk-upload process to cancel.
	 * @return
	 */
	@RequestMapping(value="/cancelBulkUpload", method=RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public void cancelBulkUpload(){
		miningDataService.cancelBulkUpload();		
	}
	
	/**
	 * Triggers to insert mining-data of uploaded file into db.
	 * This will be processed asynchronous and broadcaster will publish on channel 'Bulk-upload'.
	 * @param name
	 * @param file
	 * @return BulkUpload 
	 * @throws ValidationException 
	 */
	@RequestMapping(value="/uploadMiningData", method=RequestMethod.POST, produces="application/json")
    public String uploadMiningData(@RequestParam("file") MultipartFile file) throws ValidationException{
		return JSON.stringify(miningDataService.createBulkUploaderTask(file));		
    }
	
	/**
	 * Requests the current mode-create process to cancel.
	 * @return
	 */
	@RequestMapping(value="/cancelModelCreate", method=RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public void cancelModelCreate(){
		modelService.cancelModelCreate();
	}
	
	/**
	 * Requests a mode-create process to start.
	 * @return
	 * @throws ValidationException 
	 */
	@RequestMapping(value="/triggerModelCreate", method=RequestMethod.POST, produces="application/json")
	@ResponseStatus(value=HttpStatus.OK)
	public String triggerModelCreate() throws ValidationException{
		return JSON.stringify(modelService.triggerModelCreateTask());
	}

	
}
