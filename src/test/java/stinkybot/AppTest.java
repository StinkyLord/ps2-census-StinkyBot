package stinkybot;

import net.dv8tion.jda.api.EmbedBuilder;
import org.junit.Ignore;
import org.junit.Test;
import stinkybot.apiQuery.DaybreakApiQuery;
import stinkybot.commandlisteners.CommandDirectiveScore;
import stinkybot.commandlisteners.CommandStats;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppTest 
{
    @Ignore
    @Test
    public void shouldAnswerWithTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandStats instance = new CommandStats();
        Method method = CommandStats.class.getDeclaredMethod("getDaybreakInfo", String.class);
        method.setAccessible(true);
        EmbedBuilder eb = (EmbedBuilder)method.invoke(instance,"STINKYBULLET");
        eb.build();
    }
}
