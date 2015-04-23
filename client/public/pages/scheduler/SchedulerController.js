import {SchedulerView} from 'scheduler/SchedulerView';
import * as alfnavigator from 'alfnavigator';
import {BaseController} from 'BaseController';

export class SchedulerController extends BaseController{
	constructor(){
		super();
	}

	init(callback){
		this.model = {name: 'User'};
		this.view = new SchedulerView({controller: this});
		callback();
	}

	testme(){
		return 'tested';
	}
}
