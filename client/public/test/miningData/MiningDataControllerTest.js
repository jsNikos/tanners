var expect = chai.expect;

describe("MiningController", function() {
  var miningController = undefined;
  before(function(done){
    // ensure the app and request navigation to obtain
    // reference to test-target
    require(['app'], function(app){
      app.app.navigator.navigate('miningData')
        .then(function(contentController){
        	miningController = contentController;
          done();
        });
    });
  });

  describe("#notifyBulkUploadProgress", function() {
    it("should resolve to a valid server request", function(done) {
    	miningController.notifyBulkUploadProgress({progress: 1.0/2.0})
    					.then(function(){ done(); }, done);      
    });    

  });

 
});
