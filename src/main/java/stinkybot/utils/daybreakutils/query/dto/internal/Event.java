package stinkybot.utils.daybreakutils.query.dto.internal;

import stinkybot.utils.daybreakutils.anatomy.Collection;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import stinkybot.utils.daybreakutils.query.dto.CensusCollectionImpl;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event extends CensusCollectionImpl {

	@JsonIgnore
	private Map<String, Object> properties = new HashMap<>();
	//As there are way too many different events, I decided to be lazy and just lump them all together.
	
	/**
	 * To retrieve data of this collection you need to specify the arguments:
	 * type [MetagameEvent, Death, ...]
	 * 
	 */
	public Event() {
		super(Collection.EVENT);
	}

	@JsonAnyGetter
	public Map<String, Object> getProperties() {
		return properties;
	}

	@JsonAnySetter
	public void setProperties(String key, Object value) {
		this.properties.put(key, value);
	}

	@Override
	public String toString() {
		return "Event [properties=" + properties + ", nestedCollections=" + nestedCollections + ", collection="
				+ collection + "]";
	}

	
}
