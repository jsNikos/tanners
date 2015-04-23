Restbi is an easy to use web platform that introduces automation in data mining.
Just define the items that are going to be used and feed the restbi with data. 
Your data is just sets of your defined items. 

## Running the service from your app
* just add the jar to your classpath and invoke Application.start(port, ...).
  This will run the server on given port. 
* Example:

  Application.start(9090, 7070, "localhost", "mydb", "admin", "admin");
  
  This will run the server on port 9090, and uses a mongo-db which is setup on port 7070, host localhost,
  name mydb, username admin and password admin.
  
  
  
## Feeding model with new data
* just call ... .addData(records)

## Using the model
* just call ... .applyModel(List<Item>)

## Basic Setup
* this project requires a mongo-db running
* default port: 27017
* default host: localhost
* default db-name: test
* default db-user: 
* default db-pwd:
In the Application.start(port, ...) you can specify these parameter if they are different.



## Installation Guide (for Dev)

* project created with spring-boot (see here http://spring.io/guides/gs/spring-boot/)
* run to build project  >gradle build
* to set-up eclipse classpath, run:  >gradle eclipse
* install mongoDb: http://docs.mongodb.org/manual/installation/
* add mongoHome/bin to path
* to start mongo run  >mongod
* monjaDB is good for eclipse
* adding spring-mongo dependency (see here http://spring.io/guides/gs/accessing-data-mongodb/)
* public resources goes into folder /public
* note: the app is 3-tired, keep front-end, business-logic and persistence layer separated, see the folder-structure
* added integration tests based on spring, run tests with   >gradle test

## Running project (for Dev)
>gradle run
or run Application.java

## Coding concepts (for Dev)

* this is a TDD project: means no submit if no test, best - always implement against a test
* Exception-handling:
  GlobalExceptionHandler distinguishes between business and application exception.
  Business-exception are supposed to show to user at front-end some message, like 'you must config something'
  Application exceptions will log into the app and only tell to front-end, 500
  Business Exception defs should be added to business-folder and created in manner like found there already.
  All other exceptions can just throw runtimeException (these are the application errors) 