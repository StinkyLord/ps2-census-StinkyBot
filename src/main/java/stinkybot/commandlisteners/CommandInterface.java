package stinkybot.commandlisteners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface CommandInterface {
    String getName();
    String getDescription();
    void run(GuildMessageReceivedEvent event, String[] args);
}
