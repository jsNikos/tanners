var expect = chai.expect;

describe("WelcomeController", function() {
  var welcomeController = undefined;
  before(function(done){
    // ensure the app and request navigation to obtain
    // reference to test-target
    require(['app'], function(app){
      app.app.navigator.navigate('welcome')
        .then(function(contentController){
          welcomeController = contentController;
          done();
        });
    });
  });

  describe("#testme", function() {
    it("should return 'tested'", function() {    	
      expect(welcomeController.testme()).to.equal("tested");
    });

    it("should not return 'nichts'", function() {
      expect(welcomeController.testme()).to.not.equal("nichts");
    });

  });

  // describe("#greets", function() {
  //   it("should throw if no target is passed in", function() {
  //     expect(function() {
  //       (new Cow()).greets();
  //     }).to.throw(Error);
  //   });
  //
  //   it("should greet passed target", function() {
  //     var greetings = (new Cow("Kate")).greets("Baby");
  //     expect(greetings).to.equal("Kate greets Baby");
  //   });
  // });
});
