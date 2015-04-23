package server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {
	public static int port = 8080; // the server port 
	public static int dbPort = 27017; // db-port
	public static String dbHost = "localhost"; // db-host 
	public static String dbName = "test"; // db-name
	public static String dbUsername = ""; // db-username 
	public static String dbPassword = ""; // db-password 

	/**
	 * Applies defaults:
	 * server-port: 8080
	 * db-port: 27017
	 * db-host: localhost
	 * db-name: test
	 * db-username:
	 * db-password:
	 */
	public static void start(){
		run();
	}

	/**
	 * @param port : the server-port
	 */
	public static void start(int port){		
		Application.port = port;		
		start();
	}

	public static void start(int port, int dbPort, String dbHost, String dbName){
		Application.dbPort = dbPort;
		Application.dbHost = dbHost;
		Application.dbName = dbName;
		start(port);
	}
	
	public static void start(int port, int dbPort, String dbHost, String dbName, String dbUsername, String dbPassword){
		Application.dbUsername = dbUsername;
		Application.dbPassword = dbPassword;
		start(port, dbPort, dbHost, dbName);
	}

	public static void main(String[] args) {
		start();
	} 

	private static void run(){
		SpringApplication.run(Application.class, new String[0]);
		
	}



}