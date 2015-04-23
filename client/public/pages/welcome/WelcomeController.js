import {WelcomeView} from 'welcome/WelcomeView';
import * as alfnavigator from 'alfnavigator';
import {BaseController} from 'BaseController';

export class WelcomeController extends BaseController{
	constructor(){
		super();
	}

	init(callback){
		this.model = {name: 'User'};
		this.view = new WelcomeView({controller: this});
		callback();
	}

	testme(){
		return 'tested';
	}
}
