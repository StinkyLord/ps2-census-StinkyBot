package stinkybot.commandlisteners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stinkybot.apiQuery.DaybreakApiQuery;
import stinkybot.commandlisteners.utilities.CommandAnnotation;
import stinkybot.commandlisteners.utilities.CommandInterface;
import stinkybot.utils.JoinImage;
import stinkybot.utils.QueryConstants;
import stinkybot.utils.daybreakutils.anatomy.Constants;
import stinkybot.utils.daybreakutils.exception.CensusException;
import stinkybot.utils.daybreakutils.query.dto.ICensusCollection;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStat;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStatByFaction;
import stinkybot.utils.daybreakutils.query.dto.internal.Item;
import stinkybot.utils.daybreakutils.query.dto.internal.Vehicle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CommandAnnotation
public class CommandTopVehicleWeapon implements CommandInterface {
    private static final Object LOCK = new Object();


    // creates one single instance of the class


    private static final Logger logger = LoggerFactory.getLogger(CommandTopVehicleWeapon.class);
    File file;

    @Override
    public String getName() {
        return "topv";
    }

    @Override
    public String getDescription() {
        return "topv [PlayerName] case insensitive, will show top weapon score of player with its K/D ratio";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        try {
            if (args.length < 2) {
                event.getChannel().sendMessage("topv requires player name as second argument").queue();
                return;
            }

            synchronized (LOCK) {
                EmbedBuilder ebInfo2 = getDaybreakInfo(args);
                if (ebInfo2 != null && file != null) {
                    event.getChannel().sendMessage(ebInfo2.build()).queue();
                    event.getChannel().sendFile(file).queue();
                    Files.deleteIfExists(file.toPath());
                }
            }
        } catch (Exception e) {
            logger.warn("CommandTopVehicleWeapon - ", e);
        }
    }


    public EmbedBuilder getDaybreakInfo(String[] args) throws IOException, CensusException {
        String playerName2 = args[1];

        CharactersWeaponStat weaponScore = DaybreakApiQuery.getPlayerTopVehicleWeaponKillsByName(playerName2);
        if (weaponScore == null) {
            return null;
        }
        Vehicle vehicle = (Vehicle) weaponScore.getNested().get(0);
        Item item = (Item) weaponScore.getNested().get(1);
        String weaponImage = Constants.CENSUS_ENDPOINT.toString() + item.getImage_path();
        String vehicleImage = Constants.CENSUS_ENDPOINT.toString() + vehicle.getImage_path();
        String vehicleName = vehicle.getName().getEn();
        String weaponName = item.getName().getEn();
        String weaponDesc = item.getDescription().getEn();
        file = JoinImage.joinImages(vehicleImage, weaponImage);
        List<ICensusCollection> weaponInfo = DaybreakApiQuery.getPLayerVehicleWeaponStats(
                weaponScore.getCharacter_id(), item.getItem_id(), vehicle.getVehicle_id());
        if (weaponInfo == null) {
            throw new NullPointerException();
        }

        Map<String, CharactersWeaponStatByFaction> stateNameToWeaponInfo = weaponInfo.stream()
                .collect(Collectors.toMap(
                        key -> ((CharactersWeaponStatByFaction) key).getStat_name(),
                        value -> ((CharactersWeaponStatByFaction) value)));
        CharactersWeaponStatByFaction weaponKills = stateNameToWeaponInfo.get(QueryConstants.WEAPON_KILLS);
        float killCount = Float.parseFloat(weaponKills.getValue_vs())
                + Float.parseFloat(weaponKills.getValue_nc())
                + Float.parseFloat(weaponKills.getValue_tr());
        CharactersWeaponStatByFaction weaponVehicleKills = stateNameToWeaponInfo.get("weapon_vehicle_kills");
        float killVehicleCount = Float.parseFloat(weaponVehicleKills.getValue_vs())
                + Float.parseFloat(weaponVehicleKills.getValue_nc())
                + Float.parseFloat(weaponVehicleKills.getValue_tr());
        float totalKillCount = killVehicleCount + killCount;
        CharactersWeaponStat playerVehicleDeaths = DaybreakApiQuery.getPLayerVehicleWeaponDeaths(
                weaponScore.getCharacter_id(), item.getItem_id(), vehicle.getVehicle_id());
        float deathCount = 1;

        if (playerVehicleDeaths != null) {
            deathCount = Float.parseFloat(playerVehicleDeaths.getValue());
        }

        float kd = totalKillCount / deathCount;
        if (deathCount == 1) {
            kd = 1;
        }

        EmbedBuilder ebInfo2 = new EmbedBuilder();
        ebInfo2.setColor(0xff3923);
        ebInfo2.setTitle("Top Vehicle Weapon of Player: " + playerName2);
        ebInfo2.setDescription(vehicleName + " - " + weaponName +
                "\n" + weaponDesc +
                "\n" + "kills: " + killCount +
                "\n" + "vehicle kills: " + killVehicleCount +
                "\n" + "deaths: " + deathCount +
                "\n" + "KD: " + String.format("%.02f", kd));
        return ebInfo2;
    }
}

