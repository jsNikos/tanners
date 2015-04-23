import * as jQuery from 'jquery';
import {BaseView} from 'BaseView';
import 'fuelux';
import 'underscore';

import 'css!dataDefinition/dataDefinition';
import dataDefinitionHtml from 'text!dataDefinition/dataDefinition.html';
import keyPropertyHtml from 'text!dataDefinition/keyProperty.html'


export class DataDefinitionView extends BaseView{
	constructor(args){
		super({html: _.template(dataDefinitionHtml)(), controller: args.controller});
		
		this.$publishButton = this.$el.find('[data-role="publish"]');
		this.$cancelButton = this.$el.find('[data-role="cancel"]');
		
		this.$keyProperties = this.$el.find('[data-role="keyProperties"]');
		this.keyPropertiesTmpl = _.template(keyPropertyHtml);
		this.registerEvents();
		this.$el.find('.overlay .loader').loader();
	}
	
	registerEvents(){			
		this.$el.on('click', '[data-role="removeKey"]', event => this.removeKey(event))
				.on('click', '[data-role="addKey"]', event => this.handleAddKey(event))
				.on('click', '[data-role="publish"]', event => this.controller.handlePublish(event))
				.on('click', '[data-role="cancel"]', event => this.controller.handleCancel(event));
	}
	
	removeKey(event){
		jQuery(event.target).closest('[data-role="keyProperty"]')
						    .remove();
	}
	
	disableButton($target){
		$target.attr('disabled', 'disabled');
		return this;
	}
	
	enableButton($target){
		$target.removeAttr('disabled');
		return this;
	}
	
	disableAllButtons(){
		this.disableButton(this.$publishButton)
			.disableButton(this.$cancelButton);
		return this;
	}
	
	enableAllButtons(){
		this.enableButton(this.$publishButton)
			.enableButton(this.$cancelButton);
		return this;
	}	
	
	/**
	 * @return ItemDefinition from user-inputs
	 */
	graspItemDefinition(){
		var raw = this.$el.find('[data-role="displayNameProp"], [data-role="keyAttribute"]')
					  	  .serializeArray();
		var result = {keyAttributes: []};
		for(let prop of raw){
			if(prop.name === 'keyAttribute'){
				result.keyAttributes.push(prop.value);
			} else{
				result[prop.name] = prop.value;
			}
		}
		return result;
	}	
	
	/**
	 * Renders model from scratch.
	 */
	render(){
		if(_.isEmpty(this.controller.itemDefinition)){
			jQuery('[data-role="warn-missing-config"]', this.$el).removeClass('hide');
			this.$keyProperties.append(this.keyPropertiesTmpl({keyAttribute: ''}));
			return;
		} 
		
		jQuery('[data-role="warn-changing"]', this.$el).removeClass('hide');
		jQuery('[data-role="displayNameProp"]', this.$el).val(this.controller.itemDefinition.displayNameProp);
		for(let keyAttribute of this.controller.itemDefinition.keyAttributes){
			this.$keyProperties.append(this.keyPropertiesTmpl({keyAttribute: keyAttribute}));
		}		
	}
	
	handleAddKey(event){
		jQuery(event.target)
			.closest('[data-role="keyProperty"]')
			.after(this.keyPropertiesTmpl({keyAttribute: ''}));
	}
}