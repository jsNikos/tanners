import * as alfnavigator from 'alfnavigator';

export class BaseController extends alfnavigator.BaseContentController{
	constructor(args){
		super(args);		
	}
	
	beforeNavigate(){
		this.view.showLoadingState();
	}
	
	/**
	 * All errors which came as server's response are delegated to here.
	 * This distinguishes between validation errors or unexpected.
	 * @param error : {responseText: string not empty, classType: string}
	 * @param $context : if given than validation-errors are shown relative this container, otherwise
	 *  the view's $el is taken.
	 */
	handleError(error, $context){
		var err = JSON.parse(error.responseText);
		if(_.contains(['ValidationException', 'ConfigException'], err.classType)){
			this.view.showAsValidation(err, $context);
		} else{
			this.view.showAsError(err);
		}
	}
	
}