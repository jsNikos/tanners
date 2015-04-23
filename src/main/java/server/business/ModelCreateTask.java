package server.business;

import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is the task which manages to create new models.
 * It uses the current algorithm settings and calls algorithm-services in order
 * to create new models from mining-data.
 * Core stuff like algorithms, ..., are to be injected here.
 * 
 * It is registered with scope 'prototype'.  
 *
 */
public class ModelCreateTask extends AsyncTask {
	@Autowired(required=true)
	private BroadcasterService broadcasterService;
	@Autowired(required=true)
	private ModelService modelService;
	
	private String modelCreateId; // refer only by id to ensure to always fetch latest from db
	
	public ModelCreateTask init(String modelCreateId){		
		this.modelCreateId = modelCreateId;
		setFuture(Executors.newSingleThreadExecutor().submit(new FutureTask<>(this, null)));	
		return this;
	}	
	
	@Override
	protected void doIt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleCancel() {
		// TODO Auto-generated method stub
		
	}

}
