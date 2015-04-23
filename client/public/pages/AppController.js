import {AppView} from 'AppView';
import * as alfnavigator from 'alfnavigator';
import {MessageController} from 'broadcast/MessageController';
import {global} from 'global';
import 'sockjs';
import 'stomp';

export class AppController {	
	constructor() {
		// the view must be before the navigator
		this.view = new AppView({controller: this});
		this.bulkUploadMsgController = undefined;
		this.modelCreateMsgController = undefined;	
		
		alfnavigator({contentRegister:
									{welcome: 'welcome/WelcomeController',
									algorithm: 'algorithm/AlgorithmController',
									dataDefinition: 'dataDefinition/DataDefinitionController',
									miningData: 'miningData/MiningDataController',
									model: 'model/ModelController',
									scheduler: 'scheduler/SchedulerController'},
			          defaultContent: 'welcome',
			          targetContent:'.content-container',
			          animate: true,
			          onContentInitError : (err) => this.handleError(err)
			        });
		this.navigator = alfnavigator;
		this.subscribeForMessages();
	}
	
	/**
	 * Initializes ws-connection and subscribes for channels. 
	 */
	subscribeForMessages(){
		 var socket = new SockJS('/broadcaster');		 
         var stompClient = Stomp.over(socket);
         stompClient.connect({}, (frame) => {    
        	 this.bulkUploadMsgController = this.bulkUploadMsgController ||
             new MessageController({
            	 channel: '/topic/bulkUpload',
            	 controller: this,
            	 taskName: 'Mining Data Upload',
            	 cancelUrl: global.AppUrl+'/'+'cancelBulkUpload'});       
        	 subscribeMessageController(this.bulkUploadMsgController);        	
        	 
        	 this.modelCreateMsgController = this.modelCreateMsgController || 
             new MessageController({
            	 channel: '/topic/modelCreation',
            	 stompClient,
            	 controller: this,
            	 taskName: 'Model Creation',
            	 cancelUrl: global.AppUrl+'/'+'cancelModelCreate'});
        	 subscribeMessageController(this.modelCreateMsgController); 
        	 
         }, (error) => this.tryWsReconnect());
         
         function subscribeMessageController(messageController){
        	 stompClient.subscribe(messageController.channel, (msg) => messageController.handleMessage(JSON.parse(msg.body)));
         }
	}	
	
	/**
	 * Calls subscribeForMessages in 5sec.
	 */
	tryWsReconnect(){
		setTimeout(() => this.subscribeForMessages(), 5000);
	}	
	
	/**
	 * Triggers to show errors in pop-up.
	 */
	handleError(error){
		var err = JSON.parse(error.responseText);
		this.view.showAsError(err);		
	}

	/**
	 * Triggers navigation.
	 */
	handleMenuItemClicked(content){
		alfnavigator.navigate(content).done(null, function(args){
			window.console && console.log(args.err);
			if(args.currentContentController && args.currentContentController.view){
				args.currentContentController.view.removeLoadingState();
			}			
		});
	}	
	
}
