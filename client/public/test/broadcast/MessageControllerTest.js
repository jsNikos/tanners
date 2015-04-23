var expect = chai.expect;

describe("MessageController", function() {
  var messageController = undefined;
  before(function(done){
	  // ensure the app and request navigation to obtain
	  // reference to test-target
	  require(['app', 'broadcast/MessageController', 'global'], function(app, MessageController, global){
		  messageController = new (MessageController.MessageController)({
         	 channel: '/topic/bulkUpload',
        	 controller: app.app,
        	 taskName: 'Mining Data Upload',
        	 cancelUrl: global.global.AppUrl+'/'+'cancelBulkUpload'}); 
		  done();
	  });
  });

  describe("#handleMessage", function() {
    it("should display messageHolder in view", function() {
    	var messageHolder = {progress: null,
    					progressState: "VALIDATE_RECORDS",
    					progressStateMsg:"Validating records.",
    					taskEntity: {canceled:1427707214869,
    					created:1427707214869,
    					error: {msg: 'This is an error'},
    					finished:1427707214869,
    					id:"5519154e4e231dc57d757ca8",
    					},
    					terminated:true};    	
    	
    	messageController.handleMessage(messageHolder);    					      
    });    

  });

 
});
