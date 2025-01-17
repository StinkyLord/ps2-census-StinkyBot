package stinkybot.apiQuery;


import stinkybot.utils.daybreakutils.EventMessageBuilder;
import stinkybot.utils.daybreakutils.EventStreamClient;
import stinkybot.utils.daybreakutils.anatomy.event.*;
import stinkybot.utils.daybreakutils.event.listener.EventStreamListener;
import stinkybot.utils.daybreakutils.event.listener.GenericEventPrinter;
import stinkybot.utils.daybreakutils.event.listener.WriteToFileEvent;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author LuiZiffer
 * <p>
 * The examples here make use of the {@link Closeable} Interface.
 * This may cause an issue where the onClosing and onClosed events of the {@link EventStreamListener} are not called.
 * <br> To demonstrate this, I have added a 2 second delay after the {@link EventStreamClient#close()} is called in EventStreamExample#syncHelpRequest(). While the others do not have said delay.
 * <br> This delay solves the issue, as there is enough time before termination to call the respective Eventhandlers.
 */
public class DaybreakApiEvents {

    /**
     * Asynchronously connects to the API, subscribes to all events, prints for 2 seconds and then disconnects and terminates
     */
    public static void asyncPrintAll() {
        try (EventStreamClient client = EventStreamClient.getInstance()) {
            GenericEventPrinter printer = new GenericEventPrinter();
            client.addEventListeners(printer);
            client.connect();

            client.sendMessage(new EventMessageBuilder(EventStreamAction.SUBSCRIBE)
                    .worlds(EventStreamWorld.MILLER)
                    .events(WorldEvent.CONTINENT_UNLOCK)
                    .build());


            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.interrupted();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public static void streamAllEventsForStinkyBot(BufferedWriter bw, BufferedWriter bw2,BufferedWriter bw3,
                                                   long sleep, long recordMin, String[] chars) {
        if (sleep > 0){
            try {
                TimeUnit.MINUTES.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        WriteToFileEvent fileWriter = new WriteToFileEvent(bw, bw2, bw3);
        try (EventStreamClient client = EventStreamClient.getInstance()) {
            client.addEventListeners(fileWriter);
            client.connect();

            client.sendMessage(new EventMessageBuilder(EventStreamAction.SUBSCRIBE)
                    .worlds(EventStreamWorld.MILLER)
                    .chars(chars)
                    .events(CharacterEvent.DEATH, CharacterEvent.VEHICLE_DESTROY, CharacterEvent.GAIN_EXPERIENCE,
                            CharacterEvent.PLAYER_LOGIN, CharacterEvent.PLAYER_LOGOUT)
                    .build());
            try {
                TimeUnit.MINUTES.sleep(recordMin);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.interrupted();
            } finally {
                client.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * Synchronously connects to the API, subscribes to FacilityControl events and all events on Cobalt and Miller.
     * <br> NOTE: WorldEvent.ALL has the same effect as CharacterEvent.ALL -&gt; enables all available events.
     * <br> While it is possible to separately subscribe to "FacilityControl" and "all" events, it has the same effect as FacilityControl is already included in all.
     * <br> Important here is that "all" and "FacilityControl" will show up as separate events in the {@link EventStreamListener#onSubscriptionResponse(com.fasterxml.jackson.databind.JsonNode)} method.
     * <p>
     * After 10 seconds the connection is closed and shortly after resumed.
     * <br> After another 10 seconds the connection closed and terminated.
     */
    public static void syncReconnect() {
        try (EventStreamClient client = EventStreamClient.getInstance()) {
            GenericEventPrinter printer = new GenericEventPrinter();
            client.addEventListeners(printer);
            client.awaitConnection();

            client.subscribe(EventStreamWorld.MILLER, WorldEvent.ALL);
            client.subscribe(GenericCharacter.ALL.toString(), CharacterEvent.GAIN_EXPERIENCE);



            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.interrupted();
            }

            try {
                client.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.interrupted();
            }
            client.resume();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.interrupted();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}