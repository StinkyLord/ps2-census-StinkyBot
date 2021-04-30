package stinkybot;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import stinkybot.apiQuery.DaybreakApiEvents;
import stinkybot.utils.daybreakutils.anatomy.event.GenericCharacter;
import stinkybot.utils.daybreakutils.event.dto.parsers.DeathOrVehiclePayload;
import stinkybot.utils.daybreakutils.event.dto.parsers.GainExperiencePayload;
import stinkybot.utils.daybreakutils.event.dto.parsers.LogInOrLogOutPayload;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppTest {
    @Ignore
    @Test
    public void shouldAnswerWithTrue() throws IOException, CensusInvalidSearchTermException {

//        List<String> members = DaybreakApiQuery.getCharacterIdsFromOutfitTag("BRTD");
        String session = "philipTest";
        String killDeath = session + "_" + RandomStringUtils.random(6, 'A', 'Z' + 1, false, false);
        String gainExp = session + "_" + RandomStringUtils.random(6, 'A', 'Z' + 1, false, false);
        String playerLogger = session + "_" + RandomStringUtils.random(6, 'A', 'Z' + 1, false, false);
        try (FileOutputStream fos = new FileOutputStream(killDeath);
             OutputStreamWriter streamWriter = new OutputStreamWriter(fos);
             BufferedWriter bw = new BufferedWriter(streamWriter);
             FileOutputStream fos2 = new FileOutputStream(gainExp);
             OutputStreamWriter streamWriter2 = new OutputStreamWriter(fos2);
             BufferedWriter bw2 = new BufferedWriter(streamWriter2);
             FileOutputStream fos3 = new FileOutputStream(playerLogger);
             OutputStreamWriter streamWriter3 = new OutputStreamWriter(fos3);
             BufferedWriter bw3 = new BufferedWriter(streamWriter3)
        ) {
            DaybreakApiEvents.streamAllEventsForStinkyBot(bw, bw2, bw3, 0, 5, new String[]{GenericCharacter.ALL.toString()});
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<DeathOrVehiclePayload> deathOrVehiclePayloads = parseJsonFile(killDeath, DeathOrVehiclePayload.class);
        List<GainExperiencePayload> gainExperiencePayloads =  parseJsonFile(gainExp, GainExperiencePayload.class);
        List<LogInOrLogOutPayload> logInOrLogOutPayloads = parseJsonFile(playerLogger, LogInOrLogOutPayload.class);
        logInOrLogOutPayloads.toString();

    }

    private <T> List<T> parseJsonFile(String filename, Class<T> classType) throws IOException {
        List<T> result = new LinkedList<>();
        ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonFactory factory = new JsonFactory(om);
        File file = new File(filename);
        if (file.exists()) {
            try (JsonParser parser = factory.createParser(file)) {
                Iterator<T> itr = parser.readValuesAs(classType);
                while (itr.hasNext()) {
                    T info = itr.next();
                    result.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                FileUtils.forceDelete(file);
            }
        }
        return result;
    }

//        DaybreakApiEvents.asyncPrintAll();
//        CommandStats instance = new CommandStats();
//        Method method = CommandStats.class.getDeclaredMethod("getDaybreakInfo", String.class);
//        method.setAccessible(true);
//        EmbedBuilder eb = (EmbedBuilder)method.invoke(instance,"STINKYBULLET");
//        eb.build();
    @Ignore
    @Test
    public void mema() throws IOException, CensusInvalidSearchTermException {
//        DaybreakApiEvents.syncReconnect();
        DaybreakApiEvents.asyncPrintAll();
    }
}
