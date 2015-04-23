import {MiningDataView} from 'miningData/MiningDataView';
import * as alfnavigator from 'alfnavigator';
import {BaseController} from 'BaseController';
import {global} from 'global';
import 'jquery';
import 'fileupload';
import 'underscore';
import 'moment';

export class MiningDataController extends BaseController{
	constructor(){
		super();
	}

	init(callback){
		// model
		this.bulkUploads = undefined;
		
		this.view = new MiningDataView({controller: this});	
		this.initUpload();
		this.initFormerUploads();		
		this.initMiningDataFlow();
		callback();
	} 
	
	initMiningDataFlow(){
		this.findMiningChartData().then((miningChartDatas) => {
			var data = {
				    labels: _.chain(miningChartDatas).pluck('created').map((created) => { return moment(created).format('D/M/YY'); }).value(),
				    datasets: [
				        {
				            label: "Mining Data Flow",
				            fillColor: "rgba(220,220,220,0.2)",
				            strokeColor: "rgba(220,220,220,1)",
				            pointColor: "rgba(220,220,220,1)",
				            pointStrokeColor: "#fff",
				            pointHighlightFill: "#fff",
				            pointHighlightStroke: "rgba(220,220,220,1)",
				            data: _.pluck(miningChartDatas, 'numberRecords')
				        }
				    ]
				};		
			
			setTimeout(() => this.view.renderChart(data), 0);
		});	
	}
	
	initFormerUploads(){
		this.findBulkUploads().then((bulkUploads) => {
			this.bulkUploads = bulkUploads;
			this.view.renderFormerUploads();
		});
	} 

	
	initUpload(){		
		this.view.findFileUpload().fileupload({
			url: global.AppUrl+'/uploadMiningData',
	        dataType: 'json',	       
	        fail: (err) => this.handleUploadFail(err),
	        progress: (e, data) => this.handleUploadProgress(e, data),	       
	        dropZone: this.view.findFileDropZone(),
	        always: () => this.handleUploadFinished()
	    });		
	}
		
	handleUploadFinished(){
		this.view.hideUploadProgress();
	}
		
	handleRemoveAllData(event){
		var $context = jQuery(event.target).closest('.panel-body');
		this.view.showLoadingState();				 
		this.removeAllMiningData()
			.then(() => { alfnavigator.navigate('miningData'); },
				  (err) => { this.view.removeLoadingState();
				  			 this.handleError(err, $context); });		
	}
	
	handleRemoveBulkUpload(bulkUploadId){
		this.removeMiningData({id: bulkUploadId})
			.then(() => {
				var idx = _.findIndex(this.bulkUploads, (bulkUpload) => { return bulkUpload.id === bulkUploadId });
				this.bulkUploads.splice(idx, 1);
				this.view.removeBulkUpload(bulkUploadId);
			}) 
			.fail((err) => this.view.showAsError(JSON.parse(err.responseText)));		
	}
	
	removeMiningData(bulkUpload){
		return jQuery.ajax({
			url: global.AppUrl+'/'+'removeMiningData',			
			type: 'POST',
			data: JSON.stringify(bulkUpload),
			contentType: 'application/json'
		});
	}	
	
	removeAllMiningData(){
		return jQuery.ajax({
			url: global.AppUrl+'/'+'removeAllMiningData',			
			type: 'POST'			
		});
	}	
	
	findBulkUploads(){
		return jQuery.ajax({
			url: global.AppUrl+'/'+'findBulkUploads',			
			type: 'GET'			
		});
	}
	
	findMiningChartData(){
		return jQuery.ajax({
			url: global.AppUrl+'/'+'findMiningChartData',			
			type: 'GET'			
		});
	}
	
	/**
	 * Handling errors appearing during pure data-transfer.
	 */
	handleUploadFail(err){		
		this.view.showAsError({msg: 'Some error happened during uploading the data.'});			
	}
	
	handleUploadProgress(e, data){
		this.view.showUploadProgress(data.loaded / data.total);		
	}	
	
}
