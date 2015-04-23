import {MessageView} from 'broadcast/MessageView';

/**
 * Subscribing via ws for given channel and handling receiving messages.
 */
export class MessageController{
	constructor(args){
		this.messageView = undefined;
		this.channel = args.channel;
		this.taskName = args.taskName; 
		this.parentView = args.controller.view;
		this.cancelUrl = args.cancelUrl;		
	}	
	
	handleMessage(messageHolder){	
		this.ensureView(messageHolder)
		    .messageView.renderMessage(messageHolder);		
	}
	
	/**
	 * Ensures to have a view created corresponding to the underlying task.
	 */
	ensureView(messageHolder){
		if(!this.messageView ||
				messageHolder.entity && messageHolder.entity.id !== this.messageView.taskEntityId){
			this.messageView = new MessageView({taskName: this.taskName, controller: this});
			this.parentView.addMessageView(this.messageView);			
		}
		return this;
	}
	
	handleClose(){
		this.messageView.$el.remove();
		this.messageView = undefined;
	}
	
	handleCancel(){		
		return jQuery.ajax({
			url: this.cancelUrl,			
			type: 'POST'			
		});
	}
}