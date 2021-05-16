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
import stinkybot.utils.daybreakutils.anatomy.commands.DeathEvent;
import stinkybot.utils.daybreakutils.anatomy.commands.DeathVehicleKillMapper;
import stinkybot.utils.daybreakutils.anatomy.commands.IvIModel;
import stinkybot.utils.daybreakutils.anatomy.event.GenericCharacter;
import stinkybot.utils.daybreakutils.event.dto.parsers.DeathOrVehicleDestroy;
import stinkybot.utils.daybreakutils.event.dto.parsers.DeathOrVehiclePayload;
import stinkybot.utils.daybreakutils.event.dto.parsers.GainExperiencePayload;
import stinkybot.utils.daybreakutils.event.dto.parsers.LogInOrLogOutPayload;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;
import stinkybot.utils.daybreakutils.query.dto.internal.*;
import stinkybot.utils.daybreakutils.query.dto.internal.Character;
import stinkybot.utils.daybreakutils.enums.Faction;
import stinkybot.utils.daybreakutils.query.dto.util.LanguageObject;
import stinkybot.utils.daybreakutils.enums.Zone;
import stinkybot.utils.daybreakutils.enums.World;

import java.io.*;
import java.util.*;
import java.util.Map;
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
            DaybreakApiEvents.streamAllEventsForStinkyBot(
                    bw, bw2, bw3, 0, 5, new String[]{GenericCharacter.ALL.toString()});
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<DeathOrVehiclePayload> deathOrVehiclePayloads = parseJsonFile(killDeath, DeathOrVehiclePayload.class);
        List<GainExperiencePayload> gainExperiencePayloads = parseJsonFile(gainExp, GainExperiencePayload.class);
        List<LogInOrLogOutPayload> logInOrLogOutPayloads = parseJsonFile(playerLogger, LogInOrLogOutPayload.class);

        DeathVehicleKillMapper deathMapper = DaybreakApiQuery.getDeathVehicleDestroyEventInfo(deathOrVehiclePayloads);
        if (deathMapper == null) {
            return;
        }
        List<DeathEvent> deathEvents = convertInfoDeathEvents(deathOrVehiclePayloads, deathMapper);
        insertDeathEventsIntoDatabase(deathEvents, session);

    }

    private void insertDeathEventsIntoDatabase(List<DeathEvent> deathEvents, String session) {

    }

    private List<DeathEvent> convertInfoDeathEvents(List<DeathOrVehiclePayload> events, DeathVehicleKillMapper mapper) {
        Map<String, Character> characters = mapper.getCharacters();
        Map<String, Loadout> loadOuts = mapper.getLoadOuts();
        Map<String, Vehicle> vehicles = mapper.getVehicles();
        Map<String, String> weapons = mapper.getWeapons();
        List<DeathEvent> sqlInput = new LinkedList<>();
        for (DeathOrVehiclePayload event : events) {
            DeathEvent sqlEvent = new DeathEvent();
            DeathOrVehicleDestroy payload = event.getPayload();
            sqlEvent.setEventName(payload.getEvent_name());
            String attackerId = payload.getAttacker_character_id();
            if (attackerId != null) {
                Character ch = characters.get(attackerId);
                if (ch != null) {
                    sqlEvent.setAttackerName(ch.getName().getFirst());
                    if (ch.getFaction_id() != null) {
                        Faction faction = Faction.findFaction(Integer.parseInt(ch.getFaction_id()));
                        if (faction != null) {
                            sqlEvent.setAttackerFaction(faction.getAcronym());
                        }
                    }
                    Object outfit = ch.getProperties().get("outfit");
                    if (outfit != null) {
                        String outfitAlias = ((Map<String, String>) outfit).get("alias");
                        sqlEvent.setAttackerOutfit(outfitAlias);
                    } else {
                        sqlEvent.setAttackerOutfit("N/A");
                    }
                }
            }
            String victimId = payload.getCharacter_id();
            if (victimId != null) {
                Character ch = characters.get(victimId);
                if (ch != null) {
                    sqlEvent.setVictimName(ch.getName().getFirst());
                    if (ch.getFaction_id() != null) {
                        Faction faction = Faction.findFaction(Integer.parseInt(ch.getFaction_id()));
                        if (faction != null) {
                            sqlEvent.setVictimFaction(faction.getAcronym());
                        }
                    }
                    Object outfit = ch.getProperties().get("outfit");
                    if (outfit != null) {
                        String outfitAlias = ((Map<String, String>) outfit).get("alias");
                        sqlEvent.setVictimOutfit(outfitAlias);
                    } else {
                        sqlEvent.setVictimOutfit("N/A");
                    }
                }
            }

            String chClassId = payload.getCharacter_loadout_id();
            if (chClassId != null) {
                Loadout loadout = loadOuts.get(chClassId);
                if (loadout != null) {
                    String codeName = loadout.getCode_name();
                    if (codeName != null) {
                        sqlEvent.setVictimClass(codeName);
                    }
                } else {
                    sqlEvent.setVictimClass("N/A");
                }
            } else {
                sqlEvent.setVictimClass("N/A");
            }
            String attackerClassId = payload.getAttacker_loadout_id();
            if (attackerClassId != null) {
                Loadout loadout = loadOuts.get(attackerClassId);
                if (loadout != null) {
                    String codeName = loadout.getCode_name();
                    if (codeName != null) {
                        sqlEvent.setAttackerClass(codeName);
                    }
                }
            }
            String vehicleId = payload.getVehicle_id();
            if (vehicleId != null) {
                Vehicle vehicle = vehicles.get(vehicleId);
                if (vehicle != null) {
                    LanguageObject name = vehicle.getName();
                    if (name != null) {
                        sqlEvent.setVehicleName(name.getEn());
                    }
                } else {
                    sqlEvent.setVehicleName("N/A");
                }
            } else {
                sqlEvent.setVehicleName("N/A");
            }
            String attackerVehicle = payload.getAttacker_vehicle_id();
            if (attackerVehicle != null) {
                Vehicle vehicle = vehicles.get(attackerVehicle);
                if (vehicle != null) {
                    LanguageObject name = vehicle.getName();
                    if (name != null) {
                        sqlEvent.setAttackerVehicle(name.getEn());
                    }
                } else {
                    sqlEvent.setAttackerVehicle("N/A");
                }
            } else {
                sqlEvent.setAttackerVehicle("N/A");
            }

            String weaponId = payload.getAttacker_weapon_id();
            if (weaponId != null) {
                String weaponName = weapons.get(weaponId);
                if (weaponName == null) {
                    weaponName = "N/A";
                }
                sqlEvent.setAttackerWeapon(weaponName);
            } else {
                sqlEvent.setAttackerWeapon("N/A");
            }

            boolean isHeadshot = payload.getIs_headshot();
            sqlEvent.setHeadshot(isHeadshot);

            sqlEvent.setTimestamp(payload.getTimestamp());

            int zoneId = Integer.parseInt(payload.getZone_id());
            Zone zone = Zone.findZone(zoneId);
            if (zone != null) {
                sqlEvent.setZoneName(zone.getName());
            }
            int worldId = Integer.parseInt(payload.getWorld_id());
            World world = World.findWorld(worldId);
            if (world != null) {
                sqlEvent.setWorldName(world.getName());
            }
            sqlInput.add(sqlEvent);
        }
        return sqlInput;
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
    public void iviTest() throws IOException, CensusInvalidSearchTermException {
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
            if (catId == null || catId.equals("2") || catId.equals("3") || catId.equals("4") || catId.equals("11")
                    || catId.equals("24") || catId.equals("20") || catId.equals("23") || catId.equals("13")) {
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

            float accuracy = model.getHitCount() / model.getFireCount() * 100;
            float headshotRate = model.getHeadshots() / model.getKills() * 100;
            float iviScore = accuracy * headshotRate;
            model.setIvIScore(iviScore);
        }
        List<IvIModel> collect = models.values().stream().sorted().collect(Collectors.toList());
        collect.toString();
    }

    @Test
    public void check() throws IOException, CensusInvalidSearchTermException {
        DaybreakApiQuery.getPlayerByName("stinkybullet");
    }
}
