import {ModelView} from 'model/ModelView';
import * as alfnavigator from 'alfnavigator';
import {BaseController} from 'BaseController';

export class ModelController extends BaseController{
	constructor(){
		super();
	}

	init(callback){
		this.model = {name: 'User'};
		this.view = new ModelView({controller: this});
		callback();
	}

	testme(){
		return 'tested';
	}
}
