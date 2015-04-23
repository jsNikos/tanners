package server.business;



import static org.springframework.data.mongodb.core.mapreduce.GroupBy.keyFunction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import server.business.BulkUploadMsg.PROGRESS_STATE;
import server.business.ConfigException.Reason;
import server.persistence.BulkUpload;
import server.persistence.BulkUploadRepository;
import server.persistence.ItemDefinition;
import server.persistence.ItemDefinitionRepository;
import server.persistence.MiningRecord;
import server.persistence.MiningRecordRepository;


/**
 * This service comprises all activities around mining-data. Everything should pass through here.
 * Storing, fetching of data. Data definitions, ...
 *
 */
@Service
public class MiningDataService {
	private ItemDefinition itemDefinitionCash;	
	private AsyncTask bulkUploaderTask;
	
	@Autowired(required=true)
	private MiningRecordRepository miningRecordRepository;
	@Autowired(required=true)
	private ItemDefinitionRepository itemDefinitionRepository;
	@Autowired(required=true)
	private BulkUploadRepository bulkUploadRepository;
	@Autowired(required=true)
	private ValidationService validationService;
	@Autowired(required=true)
	private BroadcasterService broadcasterService;	
	@Autowired(required=true)
	private WebApplicationContext application;
	@Autowired(required=true)
	private MongoTemplate mongoTemplate;
	
	/**
	 * Adds the given training data.
	 * Validation will take place against user definition of items.
	 */
	public void addData(List<MiningRecord> records) throws ValidationException, ConfigException{
		validateRecords(records);
		miningRecordRepository.insert(records);	
	}
	
	protected List<MiningRecord> addBulkData(List<MiningRecord> records, String BulkUploadId) throws ValidationException, ConfigException{		 
		broadcasterService.sendBulkUploadMsg(new BulkUploadMsg(PROGRESS_STATE.VALIDATE_RECORDS, findBulkUpload(BulkUploadId)));		
		validateRecords(records);
		broadcasterService.sendBulkUploadMsg(new BulkUploadMsg(PROGRESS_STATE.INSERT_RECORDS, findBulkUpload(BulkUploadId)));
		return miningRecordRepository.insert(records);	
	}
	
	public void validateRecords(List<MiningRecord> records) throws ConfigException, ValidationException{
		for(MiningRecord record : records){
			validationService.validateAgainstItemDefs(record, findItemDefinition());
		}	
	}
	
	/**
	 * Fetches data for mining-data flow grouped by days.
	 * @return
	 */
	public List<MiningChartData> findMiningChartData(){
		List<MiningChartData> miningChartDatas = new ArrayList<>();		
		mongoTemplate.group("miningRecord",
				keyFunction("function(doc){ return {day: doc.created.getDay(), month: doc.created.getMonth(), year: doc.created.getFullYear()}; }")
				.initialDocument("{ numberRecords: 0 }")
				.reduceFunction("function(doc, prev) { prev.numberRecords += 1; }")
				.finalizeFunction("function(result){ result.created = new Date(result.year, result.month, result.day); }"), MiningChartData.class)
				.forEach((miningChartData) -> miningChartDatas.add(miningChartData));
		miningChartDatas.sort( (o1, o2) -> {return o1.getCreated().compareTo(o2.getCreated());});
		return miningChartDatas;
	}
	
	/**
	 * Counts mining-records which have created >= since.
	 * @param since
	 * @return
	 */
	public long countRecordsSince(Date since){
		Query query = new Query();
		if(since != null){
			query.addCriteria(Criteria.where("created").gte(since));
		}
		return mongoTemplate.count(query, MiningRecord.class);		
	}
	
	/**
	 * Finds all mining-records.
	 */
	public List<MiningRecord> findData() {
		return miningRecordRepository.findAll();
	}
	
	public void removeData() throws ValidationException{
		if(bulkUploaderTask != null){
			throw new ValidationException(ValidationException.Reason.BULK_UPLOAD_IS_RUNNING);
		}
		miningRecordRepository.deleteAll();
		bulkUploadRepository.deleteAll();
	}
	
	/**
	 * Deletes all mining-data which where inserted by the given BulkUpload.
	 * @param BulkUploadId
	 */
	public void removeData(String BulkUploadId) throws ValidationException{
		if(bulkUploaderTask != null){
			throw new ValidationException(ValidationException.Reason.BULK_UPLOAD_IS_RUNNING);
		}
		BulkUpload bugUpload = bulkUploadRepository.findOne(BulkUploadId);
		if(bugUpload == null){
			return;
		}
		miningRecordRepository.deleteByBulkUploadId(BulkUploadId);
		bulkUploadRepository.delete(BulkUploadId);
	}
	
	/**
	 * Removes former item-definitions and replaces by given one.
	 * @param itemDefinition
	 */
	public void addItemDefinition(ItemDefinition itemDefinition) throws ValidationException{
		validationService.validateItemDefinition(itemDefinition);
		itemDefinitionRepository.deleteAll();
		itemDefinitionRepository.save(itemDefinition);
		itemDefinitionCash = itemDefinition;
	}
	
	public ItemDefinition findItemDefinition() throws ConfigException{
		if(itemDefinitionCash != null){
			return itemDefinitionCash;
		}
		List<ItemDefinition> result = itemDefinitionRepository.findAll();
		if(result.isEmpty()){
			throw new ConfigException(Reason.ITEM_DEFINITIONS_MISSING);
		}
		return result.get(0);
	}	
	
	public List<BulkUpload> findBulkUploads(){
		return bulkUploadRepository.findAll();
	}
	
	public BulkUpload findBulkUpload(String BulkUploadId){
		return bulkUploadRepository.findOne(BulkUploadId);
	}
	
	public BulkUpload save(BulkUpload bulkUpload){		
		return bulkUploadRepository.save(bulkUpload);
	}	
	
	/**
	 * Creates a BulkUploaderTask and initializes with given file.
	 * @param file
	 */
	public synchronized BulkUpload createBulkUploaderTask(MultipartFile file) throws ValidationException{
		if(bulkUploaderTask != null){
			throw new ValidationException(ValidationException.Reason.ONLY_ONE_BULK_UPLOAD_AT_A_TIME);
		}		
		BulkUpload bulkUpload = bulkUploadRepository.insert(new BulkUpload(new Date())); 
		bulkUploaderTask = application.getBean(BulkUploaderTask.class)
			.init(file, bulkUpload.getId())
			.onCancel(()-> bulkUploaderTask = null)
			.onTerminated(()-> bulkUploaderTask = null);		
		return bulkUpload;
	}
	
	public synchronized void cancelBulkUpload(){
		if(bulkUploaderTask != null){
			bulkUploaderTask.cancel();
		}
	}	
	
	
}
