package stinkybot.commandlisteners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stinkybot.apiQuery.DaybreakApiQuery;
import stinkybot.commandlisteners.utilities.CommandAnnotation;
import stinkybot.commandlisteners.utilities.CC;
import stinkybot.commandlisteners.utilities.CommandInterface;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;
import stinkybot.utils.daybreakutils.query.dto.internal.Character;
import stinkybot.utils.daybreakutils.query.dto.util.BattleRank;
import stinkybot.utils.daybreakutils.query.dto.util.Times;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommandAnnotation
public class CommandStats implements CommandInterface {

    private static final Logger logger = LoggerFactory.getLogger(CommandStats.class);

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "stats [PlayerName] case insensitive, will show statistics of player";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        try {
            if (args.length < 2) {
                event.getChannel().sendMessage("stats requires player name as second argument").queue();
                return;
            }
            EmbedBuilder ebInfo = getDaybreakInfo(args[1]);
            if (ebInfo != null) {
                event.getChannel().sendMessage(ebInfo.build()).queue();
            }
        } catch (Exception e) {
            logger.warn("CommandStats - ", e);
        }
    }

    private EmbedBuilder getDaybreakInfo(String playerName) throws IOException, CensusInvalidSearchTermException {
        Character character = DaybreakApiQuery.getCharacterStatistics(playerName);
        if (character == null) {
            return null;
        }
        Map<String, Object> properties = character.getProperties();
        List<Map<String, String>> stat = (List<Map<String, String>>) properties.get(CC.STAT);
        List<Map<String, String>> statByFaction = (List<Map<String, String>>) properties.get(CC.STAT_BY_FACTION);
        List<Map<String, Object>> directiveTree = (List<Map<String, Object>>) properties.get(CC.DIRECTIVE_TREE);

        int directiveScore = 0;
        for (Map<String, Object> tree : directiveTree) {
            for (int i = 1; i < Integer.parseInt((String) tree.get(CC.CURRENT_DIRECTIVE_TIER_ID)); i++) {
                List<Map<String, String>> tier = (List<Map<String, String>>) tree.get(CC.TIER);
                for (Map<String, String> tierTemp : tier) {
                    if (tierTemp.get(CC.DIRECTIVE_TIER_ID).equals(String.valueOf(i))) {
                        directiveScore += Integer.parseInt(tierTemp.get(CC.DIRECTIVE_POINTS));
                    }
                }
            }
        }

        Map<String, Double> statToValueMap = getStatToValueForever(stat);
        Map<String, Double> statByFactionToValueMap = getStatByFactionToValueMap(statByFaction);
        statToValueMap.putAll(statByFactionToValueMap);
        Times times = character.getTimes();
        BattleRank battleRank = character.getBattle_rank();


        String br = battleRank.getValue();
        String lastLoginDate = times.getLast_login_date();
        String creationDate = times.getCreation_date();
        double accuracy = statToValueMap.get("weapon_hit_count") / statToValueMap.get("weapon_fire_count") * 100;
        double headshotRatio = statToValueMap.get("weapon_headshots") / statToValueMap.get("weapon_kills") * 100;
        double kills = statToValueMap.get("weapon_kills");
        double deaths = statToValueMap.get("weapon_deaths");
        double killDeathRatio =  kills/deaths ;
        double killsPerMin = statToValueMap.get("weapon_kills") / (statToValueMap.get("play_time") / 60);
        double scorePerMinute = statToValueMap.get("weapon_score") / (statToValueMap.get("play_time") / 60);
        double siegeRating = statToValueMap.get("facility_capture_count") / statToValueMap.get("facility_defended_count") * 100;
        double inGameTime = statToValueMap.get("weapon_play_time") / 86400;
        String[] split = String.valueOf(inGameTime).split("\\.");
        String daysString = split[0];
        String remainder = "0." + split[1];
        double hoursInGame = Double.parseDouble(remainder)*24;
        double totalPlayTime = statToValueMap.get("play_time") / 86400;
        String[] split2 = String.valueOf(totalPlayTime).split("\\.");
        String daysTotal = split2[0];
        String remainderTotal = "0." + split[1];
        double hoursTotal = Double.parseDouble(remainderTotal)*24;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0xff3923);
        eb.setTitle("Character Statistics Of Player: " + playerName);
        eb.setDescription("Battle Rank - " + br +
                "\n" + "Directive Score - " + directiveScore +
                "\n" + "Accuracy - " + String.format("%.02f", accuracy) + "%"+
                "\n" + "Headshot Ratio - " + String.format("%.02f", headshotRatio) + "%" +
                "\n" + "Kills - " + String.format("%.0f", kills) +
                "\n" + "Deaths - " + String.format("%.0f", deaths) +
                "\n" + "KDR - " + String.format("%.02f", killDeathRatio) +
                "\n" + "KPM - " + String.format("%.02f", killsPerMin) +
                "\n" + "SPM - " + String.format("%.02f", scorePerMinute) +
                "\n" + "Siege - " + String.format("%.02f", siegeRating) + "%" +
                "\n" + "Play Time - " + daysString + " Days " + String.format("%.0f", hoursInGame) + " Hours" +
                "\n" + "Total Time - " + daysTotal + " Days " + String.format("%.0f", hoursTotal) + " Hours" +
                "\n" + "Started Playing Since - " + creationDate +
                "\n" + "Last Login Date - " + lastLoginDate);
        return eb;
    }



    @NotNull
    private Map<String, Double> getStatByFactionToValueMapWeapons(List<Map<String, String>> statByFaction) {
        Map<String, Double> statByFactionToValueMap = new HashMap<>();
        for (Map<String, String> statToValue : statByFaction) {
            String statName = statToValue.get(CC.STAT_NAME);
            double valueForeverNc = Double.parseDouble(statToValue.get(CC.VALUE_NC));
            double valueForeverVs = Double.parseDouble(statToValue.get(CC.VALUE_VS));
            double valueForeverTr = Double.parseDouble(statToValue.get(CC.VALUE_TR));
            double total = valueForeverTr + valueForeverVs + valueForeverNc;
            statByFactionToValueMap.compute(statName, (k, v) -> ((v == null) ? 0 : v) + total);
        }
        return statByFactionToValueMap;
    }

    @NotNull
    private Map<String, Double> getStatByFactionToValueMap(List<Map<String, String>> statByFaction) {
        Map<String, Double> statByFactionToValueMap = new HashMap<>();
        for (Map<String, String> statToValue : statByFaction) {
            String statName = statToValue.get(CC.STAT_NAME);
            double valueForeverNc = Double.parseDouble(statToValue.get(CC.VALUE_FOREVER_NC));
            double valueForeverVs = Double.parseDouble(statToValue.get(CC.VALUE_FOREVER_VS));
            double valueForeverTr = Double.parseDouble(statToValue.get(CC.VALUE_FOREVER_TR));
            double total = valueForeverTr + valueForeverVs + valueForeverNc;
            statByFactionToValueMap.compute(statName, (k, v) -> ((v == null) ? 0 : v) + total);
        }
        return statByFactionToValueMap;
    }

    private Map<String, Double> getStatToValueForever(List<Map<String, String>> stat) {
        Map<String, Double> resultMap = new HashMap<>();
        for (Map<String, String> statToValue : stat) {
            String statName = statToValue.get(CC.STAT_NAME);
            double valueForever = Double.parseDouble(statToValue.get(CC.VALUE_FOREVER));
            resultMap.compute(statName, (k, v) -> ((v == null) ? 0 : v) + valueForever);
        }
        return resultMap;
    }

    private Map<String, Double> getStatToValueWeapon(List<Map<String, String>> stat) {
        Map<String, Double> resultMap = new HashMap<>();
        for (Map<String, String> statToValue : stat) {
            String statName = statToValue.get(CC.STAT_NAME);
            double valueForever = Double.parseDouble(statToValue.get(CC.VALUE));
            resultMap.compute(statName, (k, v) -> ((v == null) ? 0 : v) + valueForever);
        }
        return resultMap;
    }

}





