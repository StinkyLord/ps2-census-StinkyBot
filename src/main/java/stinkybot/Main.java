package stinkybot;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import stinkybot.utils.daybreakutils.anatomy.Constants;
import stinkybot.utils.daybreakutils.apiQuery.DaybreakApiQuery;
import stinkybot.utils.daybreakutils.query.dto.ICensusCollection;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStat;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStatByFaction;
import stinkybot.utils.daybreakutils.query.dto.internal.Item;
import stinkybot.utils.discordutils.JDAConnector;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) {
        JDA jda = JDAConnector.getInstance().getJda();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.addEventListener(new CommandsListener());
//        try {
//            CharactersWeaponStat weaponScore = DaybreakApiQuery.getPlayerTopWeaponKillsByName("xStarmanx");
//            if (weaponScore == null) {
//                throw new NullPointerException();
//            }
//            Item item = (Item) weaponScore.getNested().get(0);
//            String weaponImage = Constants.CENSUS_ENDPOINT.toString() + item.getImage_path();
//            String weaponName = item.getName().getEn();
//            String weaponDesc = item.getDescription().getEn();
//            List<ICensusCollection> weaponInfo = DaybreakApiQuery.getPLayerWeaponStats(weaponScore.getCharacter_id(), item.getItem_id());
//            if (weaponInfo == null) {
//                throw new NullPointerException();
//            }
//            Map<String, CharactersWeaponStatByFaction> stateNameToWeaponInfo = weaponInfo.stream()
//                    .collect(Collectors.toMap(
//                            key -> ((CharactersWeaponStatByFaction) key).getStat_name(),
//                            value -> ((CharactersWeaponStatByFaction) value)));
//            CharactersWeaponStatByFaction weaponHeadshots = stateNameToWeaponInfo.get("weapon_headshots");
//            int headshotsCount = Integer.parseInt(weaponHeadshots.getValue_vs())
//                    +Integer.parseInt(weaponHeadshots.getValue_nc())
//                    + Integer.parseInt(weaponHeadshots.getValue_nc());
//            CharactersWeaponStatByFaction weaponKills = stateNameToWeaponInfo.get("weapon_kills");
//            int killCount = Integer.parseInt(weaponKills.getValue_vs())
//                    +Integer.parseInt(weaponKills.getValue_nc())
//                    + Integer.parseInt(weaponKills.getValue_nc());
//            CharactersWeaponStatByFaction weaponDeaths = stateNameToWeaponInfo.get("weapon_killed_by");
//            int deathCount = Integer.parseInt(weaponDeaths.getValue_vs())
//                    +Integer.parseInt(weaponDeaths.getValue_nc())
//                    + Integer.parseInt(weaponDeaths.getValue_nc());
////            String weaponDeathCount = weaponDeaths.getValue();
//            String weaponKillsCount = weaponScore.getValue();
////            int killDeathRatio = Integer.parseInt(weaponKillsCount) / Integer.parseInt(weaponDeathCount);
////            String kd = String.valueOf(killDeathRatio);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
