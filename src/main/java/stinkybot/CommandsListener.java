package stinkybot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;
import stinkybot.commandlisteners.CommandInterface;
import stinkybot.utils.CommandAnnotation;

import java.lang.reflect.Constructor;
import java.util.Set;

public class CommandsListener extends ListenerAdapter {

    private static final String EMPTY_STRING = "";
    public static final String PREFIX = "~";

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args.length > 2 || !args[0].startsWith(PREFIX)) {
            return;
        }
        String commandAbstractFullName = CommandInterface.class.getPackage().getName();
        Set<Class<?>> commandClasses = new Reflections(commandAbstractFullName).getTypesAnnotatedWith(CommandAnnotation.class, true);

        for (Class<?> commandClass : commandClasses) {
            try {
                Constructor<?> commandConstructor = commandClass.getConstructor();
                CommandInterface commandInstance = (CommandInterface)commandConstructor.newInstance();
                if (args[0].equalsIgnoreCase(PREFIX + commandInstance.getName())) {
                    commandInstance.run(event, args);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
