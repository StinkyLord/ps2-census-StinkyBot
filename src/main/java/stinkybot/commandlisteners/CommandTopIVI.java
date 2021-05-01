package stinkybot.commandlisteners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stinkybot.apiQuery.DaybreakApiQuery;
import stinkybot.commandlisteners.utilities.CommandAnnotation;
import stinkybot.commandlisteners.utilities.CommandInterface;
import stinkybot.utils.daybreakutils.anatomy.commands.IvIModel;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStat;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStatByFaction;
import stinkybot.utils.daybreakutils.query.dto.internal.Item;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CommandAnnotation
public class CommandTopIVI implements CommandInterface {
    private static final Logger logger = LoggerFactory.getLogger(CommandTopWeapon.class);

    @Override
    public String getName() {
        return "topivi";
    }

    @Override
    public String getDescription() {
        return "topivi [PlayerName] case insensitive, will show 5 top weapons ivi score of the player \n " +
                "Weapons with less than 500 kills are not accounted for.";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        try {
            if (args.length < 2) {
                event.getChannel().sendMessage("topivi requires player name as second argument").queue();
                return;
            }
            EmbedBuilder ebInfo2 = getDaybreakInfo(args[1]);
            if (ebInfo2 != null) {
                event.getChannel().sendMessage(ebInfo2.build()).queue();
            }
        } catch (Exception e) {
            logger.warn("CommandTopWeapon - ", e);
        }
    }

    private EmbedBuilder getDaybreakInfo(String name) throws IOException, CensusInvalidSearchTermException {
        Map<String, IvIModel> models = new HashMap<>();
        String id = DaybreakApiQuery.getPlayerIdByName(name);
        List<CharactersWeaponStatByFaction> headshotRateRes = DaybreakApiQuery.getWeaponsHeadshotRateByChar(id);
        List<CharactersWeaponStat> accuracyRes = DaybreakApiQuery.getWeaponsAccuracyByChar(id);

        if (headshotRateRes == null) {
            return null;
        }
        for (CharactersWeaponStatByFaction charStat : headshotRateRes) {
            Item item = (Item) charStat.getNested().get(0);
            String catId = item.getItem_category_id();
            if(catId.equals("2") || catId.equals("3") || catId.equals("4") || catId.equals("11")
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
            if (model.getFireCount() == 0 || model.getKills() < 500) {
                model.setIvIScore(0);
                continue;
            }

            float accuracy = model.getHitCount() / model.getFireCount() * 100;
            float headshotRate = model.getHeadshots() / model.getKills() * 100;
            float iviScore = accuracy * headshotRate;
            model.setIvIScore(iviScore);
        }
        List<IvIModel> sortedIvI = models.values().stream().sorted().limit(5).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb.append("#) name - iviScore").append("\n");
        int count = 1;
        for (IvIModel iviModel : sortedIvI) {
            if (iviModel != null && iviModel.getIvIScore() > 0) {
                float iviScore = iviModel.getIvIScore();
                Item item = iviModel.getItem();
                String en = item.getName().getEn();
                sb.append(count).append(") ").append(en).append(" - ").append(iviScore).append("\n");
                count++;
            }
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0xff3923);
        eb.setTitle("Top Infantry vs Infantry of player: " + name);
        eb.setDescription(sb.toString());
        return eb;
    }
}
