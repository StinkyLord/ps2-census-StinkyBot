package stinkybot.commandlisteners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stinkybot.apiQuery.DaybreakApiQuery;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersDirectiveTree;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStat;

import java.io.IOException;
import java.util.List;

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
        CharactersWeaponStat test = DaybreakApiQuery.test(playerName);
        if (test == null) {
            return null;
        }
        return new EmbedBuilder();
    }

}





