import * as jQuery from 'jquery';
import {BaseView} from 'BaseView';
import * as alfnavigator from 'alfnavigator';
import 'css!model/model';
import modelHtml from 'text!model/model.html';
import 'underscore';

export class ModelView extends BaseView{
	constructor(args){
		super({html: _.template(modelHtml)(args.controller.model), controller: args.controller});		
		
	}
}