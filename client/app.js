var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var routes = require('./routes');
var users = require('./routes/users');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

//uncomment after placing your favicon in /public
//app.use(favicon(__dirname + '/public/favicon.ico'));
app.use(logger('dev'));
//app.use(bodyParser.json());  otherwise proxy don't work
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'build')));

app.use('/', routes);
app.use('/users', users);

app.get('/', function (req, res) {
	res.send('Hello World!');
});

// proxy requests to backend
var proxy = require('http-proxy').createProxyServer({
	target: {
		host: 'localhost',
		port: 8080
	},
	ws: true})
	.on('error', console.error);


app.all('/webrouter/*', function(req, res){
	proxy.web(req, res);
});

app.all('/broadcaster/*', function(req, res){
	proxy.web(req, res);
});

//catch 404 and forward to error handler
app.use(function(req, res, next) {
	var err = new Error('Not Found');
	err.status = 404;
	next(err);
});

//error handlers

//development error handler
//will print stacktrace
if (app.get('env') === 'development') {
	app.use(function(err, req, res, next) {
		res.status(err.status || 500);
		res.render('error', {
			message: err.message,
			error: err
		});
	});
}

//production error handler
//no stacktraces leaked to user
app.use(function(err, req, res, next) {
	res.status(err.status || 500);
	res.render('error', {
		message: err.message,
		error: {}
	});
});

var server = app.listen(3000, function () {
	var host = server.address().address;
	var port = server.address().port;
	console.log('Example app listening at http://%s:%s', host, port);

});

// gulp tasks
require('./gulpfile');
var gulp = require('gulp');
gulp.watch('public/**', ['build']);


module.exports = app;
