package server.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * ItemDefinition is intended to describe the items used as mining-data.
 * Inflowing items are validated against this definition. 
 *
 */
public class ItemDefinition {
	@Id
	@JsonIgnore
    private String id;
	private String displayNameProp; // property-name which refers to display-name of item
	private List<String> keyAttributes = new ArrayList<>(); // the attribute-names which define the key
	
	public ItemDefinition() {
		super();
	}

	public ItemDefinition(List<String> keyAttributes, String displayNameProp) {
		super();
		this.keyAttributes = keyAttributes;
		this.displayNameProp = displayNameProp;
	}

	public List<String> getKeyAttributes() {
		return keyAttributes;
	}

	public void setKeyAttributes(List<String> keyAttributes) {
		this.keyAttributes = keyAttributes;
	}

	public String getDisplayNameProp() {
		return displayNameProp;
	}

	public void setDisplayNameProp(String displayNameProp) {
		this.displayNameProp = displayNameProp;
	}
	
	
}
