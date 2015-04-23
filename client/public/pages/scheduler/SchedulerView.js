import * as jQuery from 'jquery';
import {BaseView} from 'BaseView';
import * as alfnavigator from 'alfnavigator';
import 'css!scheduler/scheduler';
import schedulerHtml from 'text!scheduler/scheduler.html';
import 'underscore';

export class SchedulerView extends BaseView{
	constructor(args){
		super({html: _.template(schedulerHtml)(args.controller.model), controller: args.controller});		
		
	}
}