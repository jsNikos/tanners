package server.business;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import server.business.ValidationException.Reason;
import server.persistence.ItemDefinition;
import server.persistence.MiningRecord;

@Service
public class ValidationService {

	/**
	 * Validates the itemDefinition to be sufficient.
	 * @param itemDefinition
	 * @throws ValidationException
	 */
	public void validateItemDefinition(ItemDefinition itemDefinition) throws ValidationException{
		if(itemDefinition.getKeyAttributes() == null || itemDefinition.getKeyAttributes().isEmpty()){
			throw new ValidationException(Reason.NO_KEY_ATTRIBUTES_IN_ITEM_DEFINITIONS);
		}
		for(String keyAttr : itemDefinition.getKeyAttributes()){
			if(StringUtils.isEmpty(keyAttr)){
				throw new ValidationException(Reason.HAS_EMPTY_KEY_ATTRIBUTE_IN_ITEM_DEFINITIONS);
			};
		}
		if(StringUtils.isEmpty(itemDefinition.getDisplayNameProp())){
			throw new ValidationException(Reason.NO_DISPLAY_PROP_IN_ITEM_DEFINITIONS);
		}
	}
	
	/**
	 * Validates given record against item-definition.
	 * @param record
	 * @param itemDefinition
	 * @throws ConfigException
	 * @throws ValidationException
	 */
	public void validateAgainstItemDefs(MiningRecord record, ItemDefinition itemDefinition) throws ConfigException, ValidationException{		
		List<Map<String, Object>> items = record.getItems();
		if(items == null){
			throw new ValidationException(Reason.NO_ITEMS_IN_RECORD);
		}
		validateAgainstItemDefs(items, itemDefinition);
	}
	
	/**
	 * Validates given items against given itemDefinition.
	 * @param items
	 * @param itemDefinition
	 * @throws ConfigException
	 * @throws ValidationException
	 */
	public void validateAgainstItemDefs(List<Map<String, Object>> items, ItemDefinition itemDefinition) throws ConfigException, ValidationException{
		for(Map<String, Object> item : items){
			for(String keyAttr : itemDefinition.getKeyAttributes())
			if(!item.containsKey(keyAttr)){
				throw new ValidationException(Reason.MISSING_KEY_ATTRIBUTE_FOR_ITEM);
			}
			if(!item.containsKey(itemDefinition.getDisplayNameProp())){
				throw new ValidationException(Reason.MISSING_DISPLAY_NAME_FOR_ITEM);
			}
			if(StringUtils.isEmpty(item.get(itemDefinition.getDisplayNameProp()))){
				throw new ValidationException(Reason.EMPTY_DISPLAY_NAME_FOR_ITEM);
			}
		}
	}
	
}
