package stinkybot.utils.daybreakutils.query.dto.util;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import stinkybot.utils.daybreakutils.query.dto.RowDataListDeserializer;

import java.util.ArrayList;
import java.util.List;

public class Regions {
	private String isList;
	
	@JsonDeserialize(using = RowDataListDeserializer.class)
	private List<RowData> row = new ArrayList<>();

	public String getIsList() {
		return isList;
	}
	public void setIsList(String isList) {
		this.isList = isList;
	}
	public List<RowData> getRow() {
		return row;
	}
	public void setRow(List<RowData> row) {
		this.row = row;
	}
	@Override
	public String toString() {
		return "Regions [IsList=" + isList + ", Row=" + row + "]";
	}
	
	
}
