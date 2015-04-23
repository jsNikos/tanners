var gulp = require('gulp'),
watch = require('gulp-watch'),
less = require('gulp-less'),
browserSync = require('browser-sync'),
runSequence = require('run-sequence'),
babel = require('gulp-babel'),
mainBowerFiles = require('main-bower-files'),
rename = require('gulp-rename'),
argv = require('yargs').argv,
gulpif = require('gulp-if'),
uglify = require('gulp-uglify'),
minifyCSS = require('gulp-minify-css'),
gulpFilter = require('gulp-filter'),
rimraf = require('rimraf');


function handleError(err){
	console.error(err);
}

var isProd = (argv.mode === 'production');

gulp.task('js', function () {
	gulp.src('public/pages/**/*.js')
	.pipe(babel({modules: 'amd'}))	
	.pipe(gulpif(isProd, uglify()))	
	.pipe(gulp.dest('build/pages'))
	.on('error', handleError);
});

gulp.task('test-src', function () {
	gulp.src('public/test/**/*.js')
	.pipe(gulp.dest('build/test'));
});

var jsFilter = gulpFilter('**/*.js');
var cssFilter = gulpFilter('**/*.css');
gulp.task('libs', function(){
	gulp.src(mainBowerFiles({paths: {
        bowerDirectory: 'public/libs',
        bowerrc: '.bowerrc',
        bowerJson: 'bower.json'
    }}), { base: 'public/libs' })
    .pipe(jsFilter)
    .pipe(gulpif(isProd, uglify()))    
    .pipe(jsFilter.restore())
    .pipe(cssFilter)
    .pipe(gulpif(isProd, minifyCSS()))    
    .pipe(cssFilter.restore())    
    .pipe(gulp.dest("build/libs"));

	gulp.src('public/libs/**/animo.js')
	.pipe(gulpif(isProd, uglify()))
	.pipe(gulp.dest('build/libs'));

	gulp.src('public/libs/sinon-1.12.2/index.js')
	.pipe(gulpif(isProd, uglify()))
	.pipe(gulp.dest('build/libs'));
	
	gulp.src('public/libs/bootstrapSlate/index.css')
	.pipe(rename('bootstrapSlate.css'))
	.pipe(gulpif(isProd, minifyCSS()))
	.pipe(gulp.dest('build/libs/bootstrap/dist/css'));
});

gulp.task('build', function(callback){
	console.log('running build');	
	return runSequence(
			 'clean',
			'js',
			'libs',
			'html',
			'icons',
			'less',
			'test-src',	
			callback			
	);
});

gulp.task('clean', function(callback) {
	rimraf('./build', callback);
});

gulp.task('less', function(){
	gulp.src('public/pages/**/*.less')
	.pipe(less())
	.pipe(gulpif(isProd, minifyCSS()))
	.pipe(gulp.dest('build/pages'));
});

gulp.task('icons', function(){
	gulp.src('public/icons/**')
	.pipe(gulp.dest('build/icons'));
});

gulp.task('html', function(){
   gulp.src('public/pages/**/*.html')
	.pipe(gulp.dest('build/pages'));
   
   gulp.src('public/templates/**')
	.pipe(gulp.dest('build/templates'));
});

gulp.task('serve', function(done) {
	var spawn = require('child_process').spawn,
		server = spawn('node', ['app.js'], { stdio: 'inherit' });

//	browserSync({
//		open: false,
//		port: 3000,
//		server: {
//			baseDir: ['./build'],
//			middleware: function (req, res, next) {
//				res.setHeader('Access-Control-Allow-Origin', '*');
//				next();
//			}
//		}
//	}, done);
});

gulp.task('default', ['serve', 'build'], function() {	
	// gulp.watch('public/**', ['build']);
//	gulp.watch('public/**', ['build', browserSync.reload]);
});
