import * as jQuery from 'jquery';
import * as alfnavigator from 'alfnavigator';
import {BaseView} from 'BaseView';
import 'css!algorithm/algorithm';
import algorithmHtml from 'text!algorithm/algorithm.html';
import 'underscore';

export class AlgorithmView extends BaseView{
	constructor(args){
		super({html: _.template(algorithmHtml)(args.controller.model), controller: args.controller});		
	
	}
}