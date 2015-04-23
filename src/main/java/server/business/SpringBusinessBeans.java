package server.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import server.business.mining.associators.AssociatorFactory;

@Configuration
public class SpringBusinessBeans {

	@Bean
	public AssociatorFactory associatorFactory(){
		return new AssociatorFactory();
	}
	
	@Bean
	@Scope("prototype")
	public BulkUploaderTask bulkUploaderTask(){
		return new BulkUploaderTask();
	}
	
	@Bean
	@Scope("prototype")
	public ModelCreateTask modelCreateTask(){
		return new ModelCreateTask();
	}
	
}
