import {DataDefinitionView} from 'dataDefinition/DataDefinitionView';
import * as alfnavigator from 'alfnavigator';
import * as jQuery from 'jquery';
import {global} from 'global';
import {BaseController} from 'BaseController';
import * as _ from 'underscore';

export class DataDefinitionController extends BaseController{
	
	constructor(){
		super();
		this.itemDefinition = {}; 
	}

	init(callback){
		this.findItemDefinition()
		    .then(result => this.updateModel(result))
		    .then(() => {
		    	this.view = new DataDefinitionView({controller: this});
		    	this.view.render();
		    	})
		    .fail(callback)
		    .done(callback);		
	}
	
	updateModel(result){
		_.extend(this.itemDefinition, result);		
	}	
	
	handlePublish(event){
		var successMsg = 'The changes have been applied successfully.'
		var $button = jQuery(event.target);
		var view = this.view;
		view.disableAllButtons()
			.removeValidation()
		    .showLoadingState();
		this.addItemDefinition(this.view.graspItemDefinition())
			.then(() => {genericHandle(); this.view.showSuccess(successMsg)},
				  (err) => {genericHandle(); this.handleError(err);});  
		
		function genericHandle(){
			view.enableAllButtons() 
	    	 	.removeLoadingState();
		}		
	}
	
	/**
	 * Restores default, by navigating to itself.
	 */
	handleCancel(event){
		alfnavigator.navigate('dataDefinition');
	}
		
	findItemDefinition(){
		return jQuery.ajax({
			url: global.AppUrl+'/'+'findItemDefinition'			
		});
	}
	
	addItemDefinition(itemDefinition){
		return jQuery.ajax({
			url: global.AppUrl+'/'+'addItemDefinition',
			data: JSON.stringify(itemDefinition),
			type: 'POST',
			contentType: 'application/json'
		});
	}

	
}
