package stinkybot.utils.daybreakutils.event.listener;

import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.Response;
import okhttp3.ResponseBody;


import java.io.*;

public class WriteToFileEvent extends EventStreamListener {


    private final BufferedWriter bwKillDeath;
    private final BufferedWriter bwGainExp;
    private final BufferedWriter bwPlayerLogger;


    public WriteToFileEvent(BufferedWriter bwKillDeath, BufferedWriter bwGainExp, BufferedWriter bwPlayerLogger) {
        this.bwPlayerLogger = bwPlayerLogger;
        this.bwKillDeath = bwKillDeath;
        this.bwGainExp = bwGainExp;
    }

    private void write(String str) {
        try {
            if (str.startsWith("{\"payload\"")) {
                if (str.startsWith("{\"payload\":{\"amount")) {
                    bwGainExp.write(str);
                } else if (str.contains("\"event_name\":\"PlayerLogout\"") || str.contains("\"event_name\":\"PlayerLogin\"")) {
                    bwPlayerLogger.write(str);
                } else {
                    //VehicleDestroy || Death events
                    bwKillDeath.write(str);
                }
            } else {
                System.out.println("Received: " + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(JsonNode node) {
        write(node.toString() + "\n");
    }

    @Override
    public void onSubscriptionResponse(JsonNode node) {
        System.out.println("Sub Response: " + node.toString());
    }

    @Override
    public void onRecentCharIdListOrCount(JsonNode node) {
        System.out.println("CharIdListOrCount: " + node.toString());
    }

    @Override
    public void onClosed(int code, String reason) {
        System.out.println("Closed: [" + code + "] " + reason);
    }

    @Override
    public void onClosing(int code, String reason) {
        System.out.println("Closing: [" + code + "] " + reason);
    }

    @Override
    public void onOpen(Response response) {
        try {
            ResponseBody body = response.body();
            if (body != null) {
                System.out.println(body.string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Throwable t, Response r) {
        System.out.println("Failure: " + r);
        t.printStackTrace();
    }

    @Override
    public void onException(Throwable t) {
        System.out.println("Exception: " + t.getMessage());
        t.printStackTrace();
    }
}








