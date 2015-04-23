package server.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
public class BroadcasterService {
	public final static String bulkUploadChannel = "/topic/bulkUpload";
	public final static String modelCreationChannel = "/topic/modelCreation";
	
	@Autowired(required=true)
	private SimpMessagingTemplate simpMessagingTemplate;	
	
	/**
	 * Broadcasts to channel /topic/bulkUpload
	 */
	public void sendBulkUploadMsg(BulkUploadMsg bulkUploadMsg){
		try{			
			simpMessagingTemplate.convertAndSend(bulkUploadChannel, bulkUploadMsg);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Broadcasts to channel /topic/modelCreation
	 */
	public void sendModelCreateMsg(ModelCreateMsg modelCreateMsg){
		try{			
			simpMessagingTemplate.convertAndSend(modelCreationChannel, modelCreateMsg);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
