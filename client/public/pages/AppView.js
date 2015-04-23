import * as jQuery from 'jquery';
import * as bootstrap from 'bootstrap';
import * as alfnavigator from 'alfnavigator';
import * as fuelux from 'fuelux';
import 'css!main.css';
import 'css!bootstrapCss';
import mainHtml from 'text!main.html';
import {viewUtils} from 'viewUtils';

export class AppView{
	/**
	 * @param args: {controller: AppController}
	 */
	constructor(args){
		this.controller = args.controller;
		this.$el = jQuery('body');
		this.initNavBar();
	}

	initNavBar(){
		this.$el.append(mainHtml);		
		this.$el.on('click', '.navbar a[data-content]', event => {
			var $target = jQuery(event.target);
			this.controller.handleMenuItemClicked($target.attr('data-content'));
			event.preventDefault();
		});
	}
	
	/**
	 * Adds the given view's el to the broadcasts-container.
	 */
	addMessageView(messageView){
		this.$el.find('.broadcasts .panel-body').append(messageView.$el);
	}
	
	/**
	 * Not for validations.
	 */
	showAsError(err){
		viewUtils.showAsError(err, this.$el);
	}
}
