package server.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import server.business.BulkUploadMsg.PROGRESS_STATE;
import server.persistence.BulkUpload;
import server.persistence.MiningRecord;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BulkUploaderTask extends AsyncTask{
	@Autowired(required=true)
	private MiningDataService miningDataService;
	@Autowired(required=true)
	private BroadcasterService broadcasterService;	
	
	private MultipartFile file;
	private String bulkUploadId;	
	
	public BulkUploaderTask init(MultipartFile file, String bulkUploadId){	
		this.file = file;
		this.bulkUploadId = bulkUploadId;
		setFuture(Executors.newSingleThreadExecutor().submit(new FutureTask<>(this, null)));	
		return this;
	}	

	@Override	
	protected void doIt(){
		try{
			if(file.isEmpty()){
				throw new ValidationException(ValidationException.Reason.NO_MINING_DATA_IN_FILE);
			}	
			List<MiningRecord> records = miningDataService.addBulkData(addBulkUploadId(extractRecords(file)), bulkUploadId);				
			saveFinished(records.size());	
			broadcasterService.sendBulkUploadMsg(new BulkUploadMsg(PROGRESS_STATE.FINISHED, miningDataService.findBulkUpload(bulkUploadId)));				
		}catch(BusinessException e){
			saveError(e);
			broadcasterService.sendBulkUploadMsg(new BulkUploadMsg(PROGRESS_STATE.ERROR, miningDataService.findBulkUpload(bulkUploadId)));
		}catch(Exception e){
			saveError(e);
			broadcasterService.sendBulkUploadMsg(new BulkUploadMsg(PROGRESS_STATE.ERROR, miningDataService.findBulkUpload(bulkUploadId)));
		}
	}	
	
	@Override
	protected void handleCancel(){
		saveCanceled();
		broadcasterService.sendBulkUploadMsg(new BulkUploadMsg(PROGRESS_STATE.CANCELED, miningDataService.findBulkUpload(bulkUploadId)));
	}
	
	private BulkUpload saveError(Exception e){
		e.printStackTrace();
		BulkUpload bulkUpload = miningDataService.findBulkUpload(bulkUploadId);			
		bulkUpload.setError(new ErrorHolder(e));
		return miningDataService.save(bulkUpload);
	} 
	
	private BulkUpload saveError(BusinessException e){
		BulkUpload bulkUpload =	miningDataService.findBulkUpload(bulkUploadId);			
		bulkUpload.setError(new ErrorHolder(e));
		return miningDataService.save(bulkUpload);
	} 
	
	private BulkUpload saveCanceled(){
		BulkUpload bulkUpload =	miningDataService.findBulkUpload(bulkUploadId);
		bulkUpload.setCanceled(new Date());		
		return miningDataService.save(bulkUpload);
	} 
	
	private BulkUpload saveFinished(int numberRecords){
		BulkUpload bulkUpload =	miningDataService.findBulkUpload(bulkUploadId);
		bulkUpload.setFinished(new Date());
		bulkUpload.setNumberRecords(numberRecords);
		return miningDataService.save(bulkUpload);
	}
	
	private List<MiningRecord> extractRecords(MultipartFile file) throws ValidationException{
		List<MiningRecord> records = new ArrayList<>();
		broadcasterService.sendBulkUploadMsg(new BulkUploadMsg(PROGRESS_STATE.EXTRACT_RECORDS, miningDataService.findBulkUpload(bulkUploadId)));
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			records = objectMapper.readValue(file.getBytes(), new TypeReference<List<MiningRecord>>(){});				
		} catch (JsonParseException e) {
			throw new ValidationException(ValidationException.Reason.NOT_VALID_JSON_STRING);
		} catch (JsonMappingException e) {
			throw new ValidationException(ValidationException.Reason.DATA_NOT_PARSEABLE_AS_MINING_RECORD);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return records;
	}
	
	private List<MiningRecord> addBulkUploadId(List<MiningRecord> records){
		records.forEach((record) -> record.setBulkUploadId(bulkUploadId));
		return records;
	}
	
}
