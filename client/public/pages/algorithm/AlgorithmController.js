import {AlgorithmView} from 'algorithm/AlgorithmView';
import * as alfnavigator from 'alfnavigator';
import {BaseController} from 'BaseController';

export class AlgorithmController extends BaseController{
	constructor(){
		super();
	}

	init(callback){
		this.model = {name: 'User'};
		this.view = new AlgorithmView({controller: this});
		callback();
	}

	testme(){
		return 'tested';
	}
}
