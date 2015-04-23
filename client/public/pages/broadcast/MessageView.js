import 'jquery';
import 'underscore';
import broadcastMessageHtml from 'text!broadcast/broadcastMessage.html';
import statusMessageHtml from 'text!broadcast/statusMessage.html';
import 'css!broadcast/message.css';
import {viewUtils} from 'viewUtils';

/**
 * Supports rendering of broadcasted messages.  
 */
export class MessageView{
	constructor(args){
		this.controller = args.controller;		
		this.taskEntityId = undefined; // view-key
		this.$el = jQuery(_.template(broadcastMessageHtml)(args));
		this.statusMessageTmpl = _.template(statusMessageHtml);
		this.registerEvents();
	}
	
	registerEvents(){			
		this.$el.on('click', '[data-role="close"]', () => this.controller.handleClose())
			    .on('click', '[data-role="cancel"]', () => this.handleCancel());				
	}
	
	handleCancel(){
		viewUtils.showConfirm('Do you really want to cancel this task?', this.controller.parentView.$el)
		.then(() => this.controller.handleCancel());
	}
	
	/**
	 * Adds the given message.
	 */
	renderMessage(messageHolder){
		this.taskEntityId = messageHolder.entity != undefined ? messageHolder.entity.id : undefined;
		if(messageHolder.terminated){
			this.hideCancel().showClose();
		}else{
			this.showCancel();
		}		
		var data = _.extend({
			formatTime(time){return moment(time).format('HH:mm:ss');},
			displayProgress : messageHolder.progress != undefined ? messageHolder.progress*100 : 100
			}, messageHolder);		
		this.$el.find('.panel-body').empty().append(this.statusMessageTmpl(data));		
	}	
	
	showCancel(){
		this.$el.find('[data-role="cancel"]').removeClass('hide');
		return this;
	}
	
	hideCancel(){
		this.$el.find('[data-role="cancel"]').addClass('hide');
		return this;
	}
	
	showClose(){
		this.$el.find('[data-role="close"]').removeClass('hide');
		return this;
	}
	
	
}