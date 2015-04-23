import * as jQuery from 'jquery';
import * as alfnavigator from 'alfnavigator';
import {BaseView} from 'BaseView';
import 'css!miningData/miningData';
import miningDataHtml from 'text!miningData/miningData.html';
import 'underscore';
import 'fuelux';
import 'animojs';
import 'chartjs';

export class MiningDataView extends BaseView{
	constructor(args){
		super({html: _.template(miningDataHtml)(args.controller.model), controller: args.controller});		
		this.registerEvents();
		
		this.formerUploadColumns = [{
			label: 'Created',
			property: 'created',
			sortable: true
		},{
			label: 'Canceled',
			property: 'canceled',
			sortable: true
		},{
			label: 'Finished',
			property: 'finished',
			sortable: true
		},{
			label: 'Error',
			property: 'error',
			sortable: true
		},{
			label: '# Records',
			property: 'numberRecords',
			sortable: true
		}];		
	}
	
	
	renderChart(data){
		var ctx = this.$el.find('[data-role="chart"]').get(0).getContext("2d");		
		new Chart(ctx).Line(data, {bezierCurve : false, datasetFill:false, responsive:true});
	}
	
	renderFormerUploads(){		
		this.$el.find('[data-role="former-uploads"]').repeater({dataSource: (options, callback) => {			
			var items = this.controller.bulkUploads.slice(0);			
			if(options.filter.value === 'errors'){
				items = _.filter(items, (bulkUpload) => {return bulkUpload.error;} );
			} 

			var firstItem = options.pageIndex * (options.pageSize || 50);
			var lastItem = firstItem + (options.pageSize || 50);
			lastItem = (lastItem <= items.length) ? lastItem : items.length;			

			if(options.sortProperty){
				items =	_.sortBy(items, options.sortProperty);
			}
			if (options.sortDirection === 'desc') {
				items.reverse();
			}

			callback({items: this.formattedItems(items.slice(firstItem, lastItem)),
				columns: this.formerUploadColumns,
				count: items.length,
				page: options.pageIndex,
				pages: Math.ceil(items.length / (options.pageSize || 50)),
				start: firstItem + 1,
				end: lastItem});
		}
		}); 
	}
	
	/**
	 * Removes row from former-uploads which corresponds to given bulkUpload.
	 */
	removeBulkUpload(bulkUploadId){
		var $tr = this.$el.find('[data-role="former-uploads"]')
						  .find('[data-id="'+bulkUploadId+'"]')
						  .closest('tr');
		$tr.animo({
			animation: 'flipOutX',
			duration: 0.5
		}, function(){
			$tr.remove();
		});
	}
	
	formattedItems(bulkUploads){
		var errorTmpl =  _.template('<span class="alert-danger"><%- error.msg %></span>');
		var numberRecordsTmpl = _.template('<%- numberRecords  %>'+
				'<span class="glyphicon glyphicon-trash remove-records" '+
				'		data-role="remove-records" data-id="<%- id %>" aria-hidden="true" '+
				'       title="click to remove this data-upload and its records from system"></span>');
		
		var items = [];
		bulkUploads.forEach((bulkUpload) => {
			var item = _.clone(bulkUpload);
			item.created = moment(bulkUpload.created).format('DD/MM/YY HH:mm:ss');
			item.canceled = bulkUpload.canceled &&  moment(bulkUpload.canceled).format('HH:mm:ss');
			item.finished = bulkUpload.finished && moment(bulkUpload.finished).format('HH:mm:ss');
			item.error = bulkUpload.error && errorTmpl(bulkUpload);			 
			item.numberRecords = numberRecordsTmpl(bulkUpload);							
			items.push(item);
		});
		return items;
	}
	
	findFileUpload(){
		return this.$el.find('[data-role="fileupload"]');
	}
	
	findFileDropZone(){
		return this.$el.find('[data-role="file-upload-drop-zone"]');
	}
	
	registerEvents(){			
		this.$el.on('click', '[data-role="remove-data"]', event => this.handleRemoveAllData(event))
				.on('click', '[data-role="remove-records"]', event => this.handleRemoveBulkUpload(event));				
	}
	
	handleRemoveBulkUpload(event){
		var bulkUploadId = jQuery(event.target).attr('data-id');
		this.showConfirm('Do you really want to remove the selected mining-data?')
		    .then(() => this.controller.handleRemoveBulkUpload(bulkUploadId));
	}
	
	handleRemoveAllData(event){
		var $context = jQuery(event.target).closest('.panel-body');
		this.removeValidation($context);
		this.showConfirm('Do you really want to remove all stored mining data on the system?')
			.then(() => this.controller.handleRemoveAllData(event));
	}
	
	/**
	 * Intended to show progress of pure data-transfer during file upload.
	 */
	showUploadProgress(progress){
		var displayProgress = progress * 100;
		this.$el.find('.progress.data-transfer').show()
				.find('.progress-bar')
				.width(displayProgress+'%');	
		
	}	
	
	hideUploadProgress(){
		this.$el.find('.progress.data-transfer').hide();
	}
	
}