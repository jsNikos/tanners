package server.setup;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

import server.Application;

@Component
public class SpringContainerCustomizer implements EmbeddedServletContainerCustomizer {

	 @Override
	    public void customize(ConfigurableEmbeddedServletContainer container) {
	        container.setPort(Application.port);
	    }

}
