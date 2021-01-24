package stinkybot;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import stinkybot.commandlisteners.utilities.CommandsListener;
import stinkybot.utils.discordutils.JDAConnector;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) {
        JDA jda = JDAConnector.getInstance().getJda();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.addEventListener(new CommandsListener());
    }
}
