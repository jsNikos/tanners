import * as jQuery from 'jquery';
import {BaseView} from 'BaseView';
import * as alfnavigator from 'alfnavigator';
import 'css!welcome/welcome';
import welcomeHtml from 'text!welcome/welcome.html';
import 'underscore';

export class WelcomeView extends BaseView{
	constructor(args){
		super({html: _.template(welcomeHtml)(args.controller.model), controller: args.controller});		
		var test = 	jQuery('.checkbox', this.$el).checkbox('uncheck');		
	}
}