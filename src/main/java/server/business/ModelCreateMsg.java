package server.business;

import server.persistence.ModelCreate;

/**
 * Message for used to broadcast during model-creation. 
 *
 */
public class ModelCreateMsg extends MessageHolder<ModelCreate> {
	public ModelCreateMsg(){
		super();
	}	
	// TODO implement analogos to BulkUploadMsg
}
