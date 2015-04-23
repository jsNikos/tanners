import 'underscore';
import modalHtml from 'text!../templates/modal.html';


class ViewUtils{
	constructor(args){				
		this.modalTmpl = _.template(modalHtml);		
	}
	
	/**
	 * Not for validations.
	 */
	showAsError(err, $el){
		var $modal = this.createModal('Error!',
				_.template('<div class="alert alert-danger" role="alert"><%- msg %></div>')(err),
				'<button type="button" class="btn btn-primary" data-role="confirm">Ok</button>',
				$el)		
				.on('click', () => $modal.modal('hide'));	
	}
	
	/**
	 * Adds the modal to $el and registered closing-listener which removes from dom
	 * on closing.
	 *  
	 * @param title: string
	 * @param body: html
	 * @param footer: html
	 * @returns $modal
	 */
	createModal(title, body, footer, $el){
		var html = this.modalTmpl({title, body, footer});
		var $modal = jQuery(html).modal('show').appendTo($el);
		$modal.on('hidden.bs.modal', () => $modal.remove());
		return $modal;
	}
	
	/**
	 * Shows confirm message as pop-up.
	 * @returns: Promise: is fulfilled if clicked ok, otherwise rejected (if clicked cancel).
	 */
	showConfirm(msg, $el){
		var $modal =
			this.createModal('Confirm!',
							_.template('<div class="alert alert-info" role="alert"><%- msg %></div>')({msg}),
							'<button type="button" class="btn btn-primary" data-role="confirm">Ok</button>'+
							'<button type="button" class="btn btn-primary" data-role="cancel">Cancel</button>',
							$el);		
					 	
		return new Promise((resolve, reject) => {
			$modal
			.on('click', '[data-role="confirm"]', () => {
				$modal.modal('hide');
				resolve();
			})
			.on('click', '[data-role="cancel"]', () => {
				$modal.modal('hide');
				reject();
			});			
		});
	}
}

export var viewUtils = new ViewUtils();