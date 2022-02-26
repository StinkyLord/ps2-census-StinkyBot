package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import stinkybot.utils.daybreakutils.EventMessageBuilder;
import stinkybot.utils.daybreakutils.EventStreamClient;
import stinkybot.utils.daybreakutils.Join;
import stinkybot.utils.daybreakutils.Query;
import stinkybot.utils.daybreakutils.anatomy.Collection;
import stinkybot.utils.daybreakutils.anatomy.Verb;
import stinkybot.utils.daybreakutils.anatomy.event.EventStreamAction;
import stinkybot.utils.daybreakutils.anatomy.event.EventStreamWorld;
import stinkybot.utils.daybreakutils.anatomy.event.WorldEvent;
import stinkybot.utils.daybreakutils.event.dto.FacilityControl;
import stinkybot.utils.daybreakutils.event.dto.GainExperience;
import stinkybot.utils.daybreakutils.event.listener.EventStreamListener;
import stinkybot.utils.daybreakutils.exception.CensusException;
import stinkybot.utils.daybreakutils.query.dto.ICensusCollection;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersEvent;
import stinkybot.utils.daybreakutils.tree.Pair;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class EventStreamClientTest {

    @Test
    void test() {
        String input = "{\"amount\":\"96\",\"character_id\":\"5429018053084408945\",\"event_name\":\"GainExperience\"," +
                "\"experience_id\":\"2\",\"loadout_id\":\"1\",\"other_id\":\"5428510351673779425\",\"timestamp\":" +
                "\"1593765908\",\"world_id\":\"1\",\"zone_id\":\"2\"}";
        try {
            GainExperience event = new ObjectMapper().readValue(input, GainExperience.class);
            System.out.println(event);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //EventStreamExample.syncReconnect();
        //EventStreamExample.asyncPrintAll();

        //EventStreamExample.syncHelpRequest();
    }

    @Test
    void testFacilityControlAndCaptures() {
        EventStreamClient client = EventStreamClient.getInstance();
        client.addEventListeners(new EventStreamListener() {

            private final Join characters_event = new Join(Collection.CHARACTERS_EVENT)
                    .on("character_id")
                    .terms(new Pair<>("type", "FACILITY_CHARACTER"));

            private final Join outfit_members = new Join(Collection.OUTFIT_MEMBER)
                    .on("outfit_id")
                    .list(1);

            private final Query query = new Query(Collection.OUTFIT)
                    .join(outfit_members);

            @Override
            public void onFacilityControl(FacilityControl event) {
                System.out.println(event);
                if (!event.getOutfit_id().equals("0")) {
                    //Join j = new Join(characters_event)
                    //	.terms(new Pair<>("facility_id", event.getFacility_id()));

                    Query q = new Query(query)
                            .filter("outfit_id", event.getOutfit_id())
                            .join(new Join(outfit_members)
                                    .join(new Join(characters_event)));
                    System.out.println(q.url(Verb.GET));

                    try {
                        List<ICensusCollection> list = q.getAndParse();
                        if (list == null) return;
                        List<ICensusCollection> filtered = list.get(0).getNested().stream().filter(p -> {
                            if (!p.getNested().isEmpty()) {
                                CharactersEvent e = (CharactersEvent) p.getNested().get(0);
                                long event_time = Long.parseLong(e.getTimestamp());
                                long compare_time = Long.parseLong(event.getTimestamp());
                                return e.getProperties().get("facility_id").equals(event.getFacility_id())
                                        && event_time >= compare_time;
                            }
                            return false;
                        }).collect(Collectors.toList());

                        System.out.println(filtered);
                    } catch (CensusException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        EventMessageBuilder builder = new EventMessageBuilder(EventStreamAction.SUBSCRIBE);
        builder.chars("all")
                .worlds(EventStreamWorld.ALL)
                //.events(CharacterEvent.PLAYER_FACILITY_DEFEND)
                .events(WorldEvent.FACILITY_CONTROL)
                .logicalAndCharactersWithWorlds(true);
        client.connect();
        try {
            client.sendMessage(builder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
