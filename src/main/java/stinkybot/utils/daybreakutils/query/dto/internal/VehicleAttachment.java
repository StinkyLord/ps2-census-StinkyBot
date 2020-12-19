package stinkybot.utils.daybreakutils.query.dto.internal;

import stinkybot.utils.daybreakutils.anatomy.Collection;
import stinkybot.utils.daybreakutils.query.dto.CensusCollectionImpl;

public class VehicleAttachment extends CensusCollectionImpl {

	private String item_id;
	private String vehicle_id;
	private String faction_id;
	private String description;
	private String slot_id;
	
	public VehicleAttachment() {
		super(Collection.VEHICLE_ATTACHMENT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "VehicleAttachment [item_id=" + item_id + ", vehicle_id=" + vehicle_id + ", faction_id=" + faction_id
				+ ", description=" + description + ", slot_id=" + slot_id + ", nestedCollections=" + nestedCollections
				+ ", collection=" + collection + "]";
	}


}
