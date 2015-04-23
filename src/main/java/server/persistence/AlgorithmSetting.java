package server.persistence;

import org.springframework.data.annotation.Id;

/**
 * This intends to persist settings for the algorithm.  
 *
 */
public class AlgorithmSetting {
	@Id	
	private String id;	
}
