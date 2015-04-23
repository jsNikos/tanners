require.config({
    baseUrl: '.',
    paths: {
    	'jquery':'../libs/jquery/dist/jquery',
    	'bootstrap':'../libs/bootstrap/dist/js/bootstrap',
    	'bootstrapCss': '../libs/bootstrap/dist/css/bootstrapSlate',
    	'backbone': '../libs/backbone/backbone',
    	'underscore': '../libs/underscore/underscore',
    	'alfnavigator': '../libs/alfnavigator/target/alfnavigator',
    		'async': '../libs/lib/async',
    	    'animojs': '../libs/animo.js/animo',
    	    'animate': '../libs/animate.css/animate',
    	    'parsequery': '../libs/parsequery/index',
    	   'async':'../libs/async/lib/async',
    	   'css': '../libs/require-css/css',
    	   'text': '../libs/text/text',
         'q': '../libs/q/q',
         'fuelux': '../libs/fuelux/dist/js/fuelux',
         'fueluxCss': '../libs/fuelux/dist/css/fuelux',
         'moment': '../libs/moment/moment',
         'fileupload': '../libs/blueimp-file-upload/js/jquery.fileupload',
         'jquery.ui.widget': '../libs/blueimp-file-upload/js/vendor/jquery.ui.widget',
         'iframeTransport': '../libs/blueimp-file-upload/js/jquery.iframe-transport',
         'sockjs': '../libs/sockjs-client/dist/sockjs',
         'stomp': '../libs/stomp-websocket/lib/stomp.min',
         'chartjs': '../libs/Chart.js/Chart'
    },
    shim:{
    	'bootstrap':{
    		deps: ['jquery']
    	},
    	'backbone':{
    		deps: ['jquery','underscore']
    	},
    	'parsequery':{
    		deps: ['jquery']
    	},
    	'animojs':{
    		deps: ['jquery', 'css!animate']
    	},
    	'fuelux':{
    		deps:['jquery', 'bootstrap', 'moment', 'css!bootstrapCss', 'css!fueluxCss']
    	},
    	'fileupload':{
    		deps:['iframeTransport']
    	}
    },
    map: {
    	  '*': {
    	    'css': 'css',
    	    'text': 'text'
    	  }
    	}
  });
