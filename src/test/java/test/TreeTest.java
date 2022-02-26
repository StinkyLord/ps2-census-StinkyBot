package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import stinkybot.utils.daybreakutils.exception.CensusException;
import stinkybot.utils.daybreakutils.Query;
import stinkybot.utils.daybreakutils.Tree;
import stinkybot.utils.daybreakutils.anatomy.Collection;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;

class TreeTest {

	@Test
	void testToString() {
		String compare1 = "field:type_id^prefix:type_^list:1";
		String compare2 = "field:facility_type^list:1^start:regions";
		
		Tree tree1 = new Tree("type_id")
				.prefix("type_")
				.list(1);
		Tree tree2 = new Tree("facility_type")
				.start("regions")
				.list(1);
		assertEquals(compare1, tree1.toString());
		assertEquals(compare2, tree2.toString());
	}
	
	@Test
	void testCensusTreeComplex() throws CensusException {
		String compare = "http://census.daybreakgames.com/get/ps2:v2/vehicle?c:limit=500&c:tree=type_id^prefix:type_^list:1&c:lang=en";
		Query c = new Query(Collection.VEHICLE)
				.limit(500)
				.tree(new Tree("type_id")
						.prefix("type_")
						.list(1))
				.lang("en");
		try {
			System.out.println(c.get());
		} catch (CensusInvalidSearchTermException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
