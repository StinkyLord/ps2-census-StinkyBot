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
import stinkybot.apiQuery.DaybreakApiQuery;
import stinkybot.utils.daybreakutils.anatomy.commands.IvIModel;
import stinkybot.utils.daybreakutils.anatomy.event.GenericCharacter;
import stinkybot.utils.daybreakutils.event.dto.parsers.DeathOrVehiclePayload;
import stinkybot.utils.daybreakutils.event.dto.parsers.GainExperiencePayload;
import stinkybot.utils.daybreakutils.event.dto.parsers.LogInOrLogOutPayload;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;
import stinkybot.utils.daybreakutils.query.dto.internal.Character;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStat;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStatByFaction;
import stinkybot.utils.daybreakutils.query.dto.internal.Item;
import sun.awt.image.ImageWatched;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        List<GainExperiencePayload> gainExperiencePayloads = parseJsonFile(gainExp, GainExperiencePayload.class);
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
//        DaybreakApiEvents.asyncPrintAll();

        Map<String, IvIModel> models = new HashMap<>();
        String id = DaybreakApiQuery.getPlayerIdByName("Sometimesitsjustthewayshegoes");
        List<CharactersWeaponStatByFaction> headshotRateRes = DaybreakApiQuery.getWeaponsHeadshotRateByChar(id);
        List<CharactersWeaponStat> accuracyRes = DaybreakApiQuery.getWeaponsAccuracyByChar(id);

        if (headshotRateRes == null) {
            return;
        }
        for (CharactersWeaponStatByFaction charStat : headshotRateRes) {

            Item item = (Item) charStat.getNested().get(0);
            String catId = item.getItem_category_id();
            if(catId ==null || catId.equals("2") || catId.equals("3") || catId.equals("4") || catId.equals("11")
            || catId.equals("24") || catId.equals("20") || catId.equals("23") || catId.equals("13")){
                continue;
            }
            String itemId = charStat.getItem_id();
            models.putIfAbsent(itemId, new IvIModel(itemId));
            IvIModel iviModel = models.get(itemId);


            String stat_name = charStat.getStat_name();
            if (stat_name.equals("weapon_headshots")) {
                float headshots = Float.parseFloat(charStat.getValue_vs())
                        + Float.parseFloat(charStat.getValue_nc())
                        + Float.parseFloat(charStat.getValue_tr());
                iviModel.setHeadshots(headshots);
            }
            if (stat_name.equals("weapon_kills")) {
                float kills = Float.parseFloat(charStat.getValue_vs())
                        + Float.parseFloat(charStat.getValue_nc())
                        + Float.parseFloat(charStat.getValue_tr());
                iviModel.setKills(kills);
            }
            if (iviModel.getItem() == null) {
//                Item item = (Item) charStat.getNested().get(0);
                String eName = item.getName().getEn();
                iviModel.setWeaponName(eName);
                String eDesc = item.getDescription().getEn();
                iviModel.setWeaponDesc(eDesc);
                iviModel.setItem(item);
            }
        }

        for (CharactersWeaponStat charStat : accuracyRes) {
            String itemId = charStat.getItem_id();
            if (!models.containsKey(itemId)) {
                continue;
            }
            IvIModel iviModel = models.get(itemId);

            String stat_name = charStat.getStat_name();
            if (stat_name.equals("weapon_fire_count")) {
                float fireCount = Float.parseFloat(charStat.getValue());
                iviModel.setFireCount(fireCount);
            }
            if (stat_name.equals("weapon_hit_count")) {
                float hitCount = Float.parseFloat(charStat.getValue());
                iviModel.setHitCount(hitCount);
            }
        }
        models.entrySet().removeIf(entry -> entry.getValue().getFireCount() == 0);
        for (IvIModel model : models.values()) {
            if (model.getFireCount() == 0 || model.getKills() < 200) {
                model.setIvIScore(0);
                continue;
            }

            float accuracy =  model.getHitCount() / model.getFireCount()* 100;
            float headshotRate = model.getHeadshots() / model.getKills() * 100 ;
            float iviScore = accuracy * headshotRate;
            model.setIvIScore(iviScore);
        }
        List<IvIModel> collect = models.values().stream().sorted().collect(Collectors.toList());
        collect.toString();

    }
}
