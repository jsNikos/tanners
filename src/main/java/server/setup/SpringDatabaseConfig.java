package server.setup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import server.Application;

import com.mongodb.MongoClient;

@Configuration
public class SpringDatabaseConfig {

	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		UserCredentials userCredentials = new UserCredentials(Application.dbUsername, Application.dbPassword);
		MongoClient mongo = new MongoClient(Application.dbHost, Application.dbPort);
		return new SimpleMongoDbFactory(mongo, Application.dbName, userCredentials);
	}

	public @Bean MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory());
	}

}
