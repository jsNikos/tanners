package server.business.mining;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.business.ConfigException;
import server.business.MiningDataService;
import server.persistence.mining.FrequentItemSet;
import server.persistence.mining.FrequentItemSetRepository;
import server.business.ValidationService;
import server.business.ValidationException;
import java.util.List;
import java.util.Map;

/**
 * This service includes all activities around frequent item sets.
 * Association rule is the knowledge that has extracted from the Mining Data
 * and has to do with the associations between Mining Data within a set.
 * A recommendation is an abstraction of the association rule that is given to end user.
 */

@Service
public class FrequentItemSetService {

    @Autowired(required=true)
    private FrequentItemSetRepository frequentItemSetRepository;
    @Autowired(required=true)
    private ValidationService validationService;
    @Autowired(required=true)
    private MiningDataService miningDataService;

    public List<FrequentItemSet> findFrequentItemSets(List<Map<String, Object>> items) throws ConfigException, ValidationException {
        validationService.validateAgainstItemDefs(items, miningDataService.findItemDefinition());
        // TODO SB
        // 1. this must be ensured to work on our items (Map<String, Object>)
        // 2. the result (recommendation) as well, must be list of our items (List<Map<String, Object>>) - not the Weka.Item
        // 3. moreover the items as given as arguments must be validated, I have done this above already.
        return frequentItemSetRepository.findAll();
    }

}

