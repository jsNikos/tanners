package server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import server.business.ConfigException;
import server.business.mining.FrequentItemSetService;
import server.business.MiningDataService;
import server.business.ValidationException;
import server.persistence.MiningRecord;

import java.util.List;
import java.util.Map;

/**
 * This router is intended for application internal end-points.
 * For web, use WebRouter. 
 */
@RestController
@RequestMapping(value="router")
public class Router {
	@Autowired(required=true)
	private MiningDataService miningDataService;

	@Autowired(required=true)
	private FrequentItemSetService frequentItemSetService;
	
	/**
	 * Request to add new training data.	  	  
	 * @param records [MiningRecord]  
	 * @return
	 */
	@RequestMapping(value="/addMiningData", method = RequestMethod.POST, consumes="application/json")
	public void addMiningData(@RequestBody List<MiningRecord> records) throws ValidationException, ConfigException{
		miningDataService.addData(records);
	}

	/**
	 * Based on given list of items, this method returns a list of most probably associated items
	 * which the current model proposes.
	 * @param List<Map<String, Object>> items
	 * @return List<Map<String, Object>> 
	 */
	@RequestMapping(value="/findFrequentItemSets", method = RequestMethod.GET, produces="application/json", consumes="application/json")
	public void findRecommendations(@RequestBody List<Map<String, Object>> items) throws ValidationException, ConfigException {	
		//TODO nikos toJson on result (List<Map<String, Object>>) when method is finished
        frequentItemSetService.findFrequentItemSets(items);
	}
}
