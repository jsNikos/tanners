import 'underscore';
import * as alfnavigator from 'alfnavigator';
import {viewUtils} from 'viewUtils';


export class BaseView extends alfnavigator.BaseContentView{
	constructor(args){
		super(args);
		this.addLoader();
	}
	
	addLoader(){
		this.$el.prepend(
				'<div class="overlay">'+
				  '<div class="loader"></div>'+
				'</div>');
	}
	
	/**
	 * Creates html-snippet to show validation issues around given msg.
	 */
	createValidationMsg(err){
		var tmpl = '<div class="alert alert-danger" role="alert" class="validation"><%- msg %></div>';
		return _.template(tmpl)(err);
	}
	
	/**
	 * Removes validations by emptying the 'validation-msg' container.
	 * @param: $context, when given this validation containers relative to this
	 * context are regarded, otherwise the $el.
	 */
	removeValidation($context){
		$context = $context || this.$el;
		$context.find('[data-role="validation-msg"]')
		   	    .empty();
		return this;
	}
	
	/**
	 * This intends to show given error as validation-issue.
	 * Specific views want to implement this because it require the existence
	 * and location of a 'validation-msg'-container.
	 * @param $context : if given than validation-errors are shown relative this container
	 */
	showAsValidation(err, $context){
		$context = $context || this.$el;
		$context.find('[data-role="validation-msg"]')
			    .empty()
		        .append(this.createValidationMsg(err));
		return this;
	}	
	
	/**
	 * Shows loading wheel on top of current content
	 */
	showLoadingState(){
		this.$el.find('.overlay').show();
		return this;
	}
	
	removeLoadingState(){
		this.$el.find('.overlay').hide();
		return this;
	}
	
	/**
	 * Shows success message as pop-up.
	 */
	showSuccess(msg){
		var $modal =
			viewUtils.createModal('Success!',
							_.template('<div class="alert alert-success" role="alert"><%- msg %></div>')({msg}),
							'<button type="button" class="btn btn-primary" data-role="confirm">Ok</button>',
							this.$el)		
					 .on('click', '[data-role="confirm"]', () => $modal.modal('hide'));		
	}
	
	showConfirm(msg){
		return viewUtils.showConfirm(msg, this.$el);
	}
	
	/**
	 * Not for validations.	  
	 */
	showAsError(err){
		viewUtils.showAsError(err, this.$el);
	}	
	
}