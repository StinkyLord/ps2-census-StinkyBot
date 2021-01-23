package stinkybot.commandlisteners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stinkybot.apiQuery.DaybreakApiQuery;
import stinkybot.utils.QueryConstants;
import stinkybot.utils.daybreakutils.anatomy.Constants;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;
import stinkybot.utils.daybreakutils.query.dto.ICensusCollection;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStat;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStatByFaction;
import stinkybot.utils.daybreakutils.query.dto.internal.Item;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CommandAnnotation
public class CommandTopWeapon implements CommandInterface {
    private static final Logger logger = LoggerFactory.getLogger(CommandTopWeapon.class);

    @Override
    public String getName() {
        return "topw";
    }

    @Override
    public String getDescription() {
        return "topw [PlayerName] case insensitive, will show top weapon score of player with its K/D ratio";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        try {
            if (args.length < 2) {
                event.getChannel().sendMessage("topw requires player name as second argument").queue();
                return;
            }
            EmbedBuilder ebInfo2 = getDaybreakInfo(args);
            if (ebInfo2 != null) {
                event.getChannel().sendMessage(ebInfo2.build()).queue();
            }
        } catch (Exception e) {
            logger.warn("CommandTopWeapon - ", e);
        }
    }

    private EmbedBuilder getDaybreakInfo(String[] args) throws IOException, CensusInvalidSearchTermException {
        String playerName2 = args[1];

        CharactersWeaponStat weaponScore = DaybreakApiQuery.getPlayerTopWeaponKillsByName(playerName2);
        if (weaponScore == null) {
            return null;
        }
        Item item = (Item) weaponScore.getNested().get(0);
        String weaponImage = Constants.CENSUS_ENDPOINT.toString() + item.getImage_path();
        String weaponName = item.getName().getEn();
        String weaponDesc = item.getDescription().getEn();
        List<ICensusCollection> weaponInfo = DaybreakApiQuery.getPLayerWeaponStats(weaponScore.getCharacter_id(), item.getItem_id());
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
        CharactersWeaponStat pLayerWeaponDeaths = DaybreakApiQuery.getPLayerWeaponDeaths(weaponScore.getCharacter_id(), item.getItem_id());
        if (pLayerWeaponDeaths == null) {
            throw new NullPointerException();
        }
        float deathCount = Float.parseFloat(pLayerWeaponDeaths.getValue());
        float kd = killCount / deathCount;
        EmbedBuilder ebInfo2 = new EmbedBuilder();
        ebInfo2.setColor(0xff3923);
        ebInfo2.setTitle("Top Weapon of Player: " + playerName2);
        ebInfo2.setImage(weaponImage);
        ebInfo2.setDescription(weaponName +
                "\n" + weaponDesc
                + "\n" + "kills: " + killCount +
                "\n" + "deaths: " + deathCount +
                "\n" + "KD: " + String.format("%.02f", kd));
        return ebInfo2;
    }
}

