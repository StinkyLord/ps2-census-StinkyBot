package test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

class CensusTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    void test() {
        String mem_list = "{\"outfit_list\":[{\"outfit_id\":\"37511617366493874\",\"name\":\"Broken Arrow Company\",\"name_lower\":\"broken arrow company\",\"alias\":\"BAWC\",\"alias_lower\":\"bawc\",\"time_created\":\"1362064687\",\"time_created_date\":\"2013-02-28 15:18:07.0\",\"leader_character_id\":\"5428011263345071089\",\"member_count\":\"169\",\"members\":[{\"character_id\":\"5428016813477836529\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"SpunkMagnet\",\"first_lower\":\"spunkmagnet\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"5428690458375680721\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"C120P\",\"first_lower\":\"c120p\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"5428861139969151729\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"Terrarista\",\"first_lower\":\"terrarista\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"5428883591442192401\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"IvanEnchev2\",\"first_lower\":\"ivanenchev2\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"5428962001684326689\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"Focuslight2\",\"first_lower\":\"focuslight2\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"5428990295216862321\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"Droperix2137\",\"first_lower\":\"droperix2137\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"5428990295221766225\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"NARFMAN\",\"first_lower\":\"narfman\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"5428998138732138801\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"TiefenwaechterTR\",\"first_lower\":\"tiefenwaechtertr\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"5428998138866702993\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"K2azEU\",\"first_lower\":\"k2azeu\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"5429010062859842689\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"ZangiefTRE\",\"first_lower\":\"zangieftre\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"5429012964657314865\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"AimStArTR\",\"first_lower\":\"aimstartr\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"8274010520671372833\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"Brosef317\",\"first_lower\":\"brosef317\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"8287959095898599185\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"Fairee\",\"first_lower\":\"fairee\"},\"onlineStatus\":{\"online_status\":\"0\"}}},{\"character_id\":\"8289857657759645153\",\"rank\":\"Trial\",\"rank_ordinal\":\"8\",\"character\":{\"name\":{\"first\":\"Rainbows37\",\"first_lower\":\"rainbows37\"},\"onlineStatus\":{\"online_status\":\"0\"}}}]}],\"returned\":1}";
        try {
            JsonNode node = new ObjectMapper().readTree(mem_list);
            // Iterator<Map.Entry<String, JsonNode>> fields =
            // node.path("outfit_member_extended_list").fields();

            for (JsonNode json : node.path("outfit_list")) {
                for (Iterator<Map.Entry<String, JsonNode>> it = json.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> next = it.next();
                    System.out.println("[" + next.getKey() + "] " + next.getValue());
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void tmp(JsonNode node, java.util.Collection col) {
        System.out.println("[" + col + "]");
        for (JsonNode json : node.path(col.toString().toLowerCase() + "_list")) {
            for (Iterator<Map.Entry<String, JsonNode>> it = json.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> next = it.next();

                System.out.println("private String " + next.getKey() + ";");

                if (next.getValue().isContainerNode()) {
                    for (Iterator<Map.Entry<String, JsonNode>> it_inner = next.getValue().fields(); it_inner
                            .hasNext(); ) {
                        Map.Entry<String, JsonNode> next_inner = it_inner.next();
                        System.out.println("__private String " + next_inner.getKey() + ";");
                    }
                }

            }
        }
    }
}
