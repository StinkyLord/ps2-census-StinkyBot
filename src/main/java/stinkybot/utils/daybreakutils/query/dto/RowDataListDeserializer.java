package stinkybot.utils.daybreakutils.query.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import stinkybot.utils.daybreakutils.query.dto.util.RowData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RowDataListDeserializer extends JsonDeserializer<List<RowData>> {

	@Override
	public List<RowData> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = p.readValueAsTree();
		List<RowData> list = new ArrayList<>();
		node.forEach(n -> list.add(new RowData(n.path("RowData").path("RegionId").asText(), n.path("RowData").path("FactionId").asText())));
		return list;
	}

}
