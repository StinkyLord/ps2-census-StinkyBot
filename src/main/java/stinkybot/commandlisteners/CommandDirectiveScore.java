package stinkybot.commandlisteners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stinkybot.apiQuery.DaybreakApiQuery;
import stinkybot.commandlisteners.utilities.CommandAnnotation;
import stinkybot.commandlisteners.utilities.CommandInterface;
import stinkybot.utils.daybreakutils.anatomy.Constants;
import stinkybot.utils.daybreakutils.exception.CensusException;
import stinkybot.utils.daybreakutils.query.dto.ICensusCollection;
import stinkybot.utils.daybreakutils.query.dto.internal.*;

import java.io.IOException;
import java.util.*;

@CommandAnnotation
public class CommandDirectiveScore implements CommandInterface {

    private static final Logger logger = LoggerFactory.getLogger(CommandDirectiveScore.class);

    @Override
    public String getName() {
        return "directive";
    }

    @Override
    public String getDescription() {
        return "directive [PlayerName] case insensitive, will show total directive score of player";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        try {
            if (args.length < 2) {
                event.getChannel().sendMessage("directive requires player name as second argument").queue();
                return;
            }
            EmbedBuilder ebInfo = getDaybreakInfo(args[1]);
            if (ebInfo != null) {
                event.getChannel().sendMessage(ebInfo.build()).queue();
            }
        } catch (Exception e) {
            logger.warn("CommandDirectiveScore - ", e);
        }
    }

    private EmbedBuilder getDaybreakInfo(String playerName) throws IOException, CensusException {
        List<CharactersDirectiveTree> charDirectiveTree = DaybreakApiQuery.getDirectiveTreeByCharacterName(playerName);
        if (charDirectiveTree == null) {
            return null;
        }
        int sum = 0;
        for(CharactersDirectiveTree tree : charDirectiveTree) {
            for (int x = 1; x < Integer.parseInt(tree.getCurrent_directive_tier_id()); x++) {
                List<ICensusCollection> nested = tree.getNested();
                for (ICensusCollection tierTemp : nested) {
                    DirectiveTier tier = (DirectiveTier)tierTemp;
                    if (tier.getDirective_tree_id().equals(tree.getDirective_tree_id())
                            && tier.getDirective_tier_id().equals(String.valueOf(x))) {
                        sum += Integer.parseInt(tier.getDirective_points());
                    }
                }
            }
        }

        EmbedBuilder ebInfo2 = new EmbedBuilder();
        ebInfo2.setColor(0xff3923);
        ebInfo2.setTitle("Directive Score of Player: " + playerName);
        ebInfo2.setImage(Constants.CENSUS_ENDPOINT.toString() + "/images/icon_directive.png");
        ebInfo2.setDescription("Directive Score: " + sum);
        return ebInfo2;
    }
}

