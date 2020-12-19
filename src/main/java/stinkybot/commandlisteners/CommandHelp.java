package stinkybot.commandlisteners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.reflections.Reflections;
import stinkybot.CommandsListener;
import stinkybot.utils.CommandAnnotation;

import java.lang.reflect.Constructor;
import java.util.Set;

@CommandAnnotation
public class CommandHelp implements CommandInterface {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "The help command will show usage of this bot";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        String commandAbstractFullName = CommandInterface.class.getPackage().getName();
        Set<Class<?>> commandClasses = new Reflections(commandAbstractFullName).getTypesAnnotatedWith(CommandAnnotation.class, true);
        StringBuilder sb = new StringBuilder();
        for (Class<?> commandClass : commandClasses) {
            try {
                Constructor<?> commandConstructor = commandClass.getConstructor();
                CommandInterface commandInstance = (CommandInterface)commandConstructor.newInstance();
                sb.append(CommandsListener.PREFIX)
                        .append(commandInstance.getName())
                        .append(" - ")
                        .append(commandInstance.getDescription())
                        .append("\n");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        EmbedBuilder ebHelp = new EmbedBuilder();
        ebHelp.setColor(0xff3923);
        ebHelp.setTitle("Help Panel");
        ebHelp.setDescription("Usage:\n  ~[command]\n\n" +
                "Available Commands:\n" + sb.toString());
        event.getChannel().sendMessage(ebHelp.build()).queue();

    }

}
